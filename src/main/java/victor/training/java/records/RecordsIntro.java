package victor.training.java.records;

import jakarta.validation.constraints.Min;
import lombok.Value;

public class RecordsIntro {
  public static void main(String[] args) {
    Point point = new Point("-1","1");

    //What's wrong with setters?
//    Well, nothing tells me that this method inside of it somewhere darker than that. It changes the state of my argument.
    darkLogic(point);
//    why Immutability?
//    1)unexpected side effect to the state of the object (an argument)
//     Causing Temporal Coupling with next line
//     = you can grow afraid of passing the object to other methods
//    This is one of the most dramatic reasons for immutability in high complexity systems
//    2) race conditions multi-threading code
    System.out.println(point);
    System.out.println(point.x());// getter in record does not have the "get" prefix
  }

  private static void darkLogic(Point point) {
    darkerPlace(point);

  }

  private static void darkerPlace(Point point) {
    //race conditions multi-threading code can happen here
    // once we convert to immutable, we can't change the state of the object
    //point.setX(point.getX() + 1); // was quick fix TO DO remove on next sprint
  }
}


//@Data//🤬  => @ToString, @EqualsAndHashCode, @Getter, @Setter
//@Value//🥰 => @Data + all fields are final
record Point(
        @Min(0)
        int x, // record components are final
        int y) implements Comparable<Point> {
// we should not change the state of the object
  //  @Override public int x() { return x * 2;}

Point {
//    if (x < 0 || y < 0) {
//      throw new IllegalArgumentException("Invalid point: " + this);
//    }

  if(x<0){x=-x;}//So I was able to change the X to flip it backwards if X is less than 0, then negate X
  //we are not re-assigned the field, we are re-assigning the argument,
  // but rather messing up with the parameters before they are assigned to the fields
}
//Overloaded constructor MUST call the canonical constructor on the first line
//canonical examples of such small immutable Value Objects:
// try to avoid overloaded constructors, and instead create static factory methods
Point (String x, String y) {
  this(Integer.parseInt(x), Integer.parseInt(y));
}

  @Override
  public int compareTo(Point o) {
    return Integer.compare(x, o.x);
  }

  public static Point of(String x, String y) {//🤩 static factory method
  int x1 = Integer.parseInt(x);
  int y1 = Integer.parseInt(y);
  return new Point(x1, y1);// call the canonical constructor
}

  public Point mirrorOx() {
    return new Point(x, -y);// produce changed copy(mutated copy)
  }
  public boolean isVisible() {
    return x > 0 && y > 0;
  }
}
//canonical examples of such small immutable Value Objects:
// Money{Currency:currency, BigDecimal:amount},
// Point,
// Color,
// Date,
// Range,
// Interval,
// Fee(TYPE, Money:money),
// etc


