package victor.training.java.records;

import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;

import java.util.Optional;

public class RecordsIntro {
  public static void main(String[] args) {
    Point point = new Point("-1","1");

//1) manual validation:
    Validator validator = jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator();
    validator.validate(point).forEach(System.out::println);

//    2) in typical code, you would have a service method that receives a Point
//    and the method signature says @Validated/@Valid on the param service.f(point)
//    public void f(@Valid Point point) { // or @Validated Point point
//    via Method interceptor (AOP) throwing an error if the object is not valid,


    //What's wrong with setters?
//    Well, nothing tells me that this method inside of it somewhere darker than that. It changes the state of my argument.
    Point movedPoint = darkLogic(point);
    System.out.println(movedPoint);
//    why Immutability?
//    1)unexpected side effect to the state of the object (an argument)
//     Causing Temporal Coupling with next line
//     = you can grow afraid of passing the object to other methods
//    This is one of the most dramatic reasons for immutability in high complexity systems
//    2) race conditions multi-threading code
    System.out.println(point);
    System.out.println(point.x());// getter in record does not have the "get" prefix
  }

  private static Point darkLogic(Point point) {
    //race conditions multi-threading code can happen here
    // once we convert to immutable, we can't change the state of the object
    //point.setX(point.getX() + 1); // was quick fix TO DO remove on next sprint
    Point newPoint =new Point(point.x()+1, point.y());
    return newPoint;

  }

}


//@Data//🤬  => @ToString, @EqualsAndHashCode, @Getter, @Setter
//@Value//🥰 => @Data + all fields are final
//record also use Generics record Point<T> (T x, T y)
// in binary form, a record is a final class that extends java.lang.Record
record Point(
        @Min(2)
        int x, // record components are final
        int y) implements Comparable<Point> {

  //🥵 we should not change the state of the object
  //  @Override public int x() { return x * 2;}
  //🤬 can't change the getter return type or signature
  //@Override public Optional<Integer> x(){return Optional.of(1);}

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
record Transaction(
        String from,
        String to,
        int amount,
        Optional<Double> fee) {
//  @Override
//  you cannot change the signature of the getter instead pass Optional<Double> fee
//  public Optional<Double> fee() {
//    return Optional.ofNullable(fee);
//  }
}
// the fee will be NULL!!!!!!!! yeeeewwwwww

