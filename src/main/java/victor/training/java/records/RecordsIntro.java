package victor.training.java.records;

import lombok.Data;

public class RecordsIntro {
  public static void main(String[] args) {
    Point point = new Point(1,1);

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

@Data// => @ToString, @EqualsAndHashCode, @Getter, @Setter
class Point {
  private final int x;
  private final int y;
}

