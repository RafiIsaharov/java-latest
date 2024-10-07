package victor.training.java.records;

public class RecordsIntro {
  public static void main(String[] args) {
    Point point = new Point(1,1);
//    point.setX(1);
//    point.setY(1);
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

// => @ToString, @EqualsAndHashCode, @Getter, @Setter
class Point {
  private final int x;
  private final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof Point)) return false;
    final Point other = (Point) o;
    if (!other.canEqual((Object) this)) return false;
    if (this.getX() != other.getX()) return false;
    if (this.getY() != other.getY()) return false;
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof Point;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + this.getX();
    result = result * PRIME + this.getY();
    return result;
  }

  public String toString() {
    return "Point(x=" + this.getX() + ", y=" + this.getY() + ")";
  }
}

