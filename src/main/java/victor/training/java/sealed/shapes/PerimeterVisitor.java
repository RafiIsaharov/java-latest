package victor.training.java.sealed.shapes;

public class PerimeterVisitor implements ShapeVisitor {
    private double totalPerimeter = 0;
   @Override
   public void visit(Circle circle) {
      totalPerimeter += 2 * Math.PI * circle.radius();
   }

   @Override
   public void visit(Rectangle rectangle) {
      totalPerimeter += 2 * (rectangle.height() + rectangle.width());
   }

   @Override
   public void visit(Square square) {
      totalPerimeter += 4 * square.edge();
   }

   public double getTotalPerimeter() {
      return totalPerimeter;
   }
}
