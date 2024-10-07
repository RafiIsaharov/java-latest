package victor.training.java.records;

import lombok.Data;

import java.util.Objects;

public class RecordsIntro {
  public static void main(String[] args) {
    Point point = new Point();
    point.setX(1);
    point.setY(1);
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
    point.setX(point.getX() + 1); // was quick fix TO DO remove on next sprint
  }
}
@Data // => @ToString, @EqualsAndHashCode, @Getter, @Setter
class Point {
  private int x;
  private int y;
}

