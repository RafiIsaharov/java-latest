package victor.training.java.records;

import lombok.Data;

import java.util.Objects;

public class RecordsIntro {
  public static void main(String[] args) {
    Point point = new Point();
    point.setX(1);
    point.setY(2);
    System.out.println(point);
  }
}
@Data // => @ToString, @EqualsAndHashCode, @Getter, @Setter
class Point {
  private int x;
  private int y;
}

