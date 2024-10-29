package victor.training.java.sealed.shapes;

public class AreaVisitor implements ShapeVisitor {
   private double totalArea;

   @Override
   public void visit(Circle circle) {
      totalArea += Math.PI * circle.radius() * circle.radius();
   }

   @Override
   public void visit(Rectangle rectangle) {
      totalArea += rectangle.width() * rectangle.height();
   }

   @Override
   public void visit(Square square) {
      totalArea += square.edge() * square.edge();
   }

   public double getTotalArea() {
      return totalArea;
   }
}
