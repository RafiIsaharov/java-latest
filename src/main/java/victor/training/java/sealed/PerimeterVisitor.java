package victor.training.java.sealed;


import victor.training.java.sealed.model.Circle;
import victor.training.java.sealed.model.Rectangle;
import victor.training.java.sealed.model.Square;

public class PerimeterVisitor implements ShapeVisitor {
	private double totalPerimeter;

	public void visit(Square square) {
		totalPerimeter += 4 * square.getEdge();
	}
	public void visit(Circle circle) {
		totalPerimeter += 2 * Math.PI * circle.getRadius();
	}
	public void visit(Rectangle rectangle) {
		totalPerimeter += (rectangle.getW() + rectangle.getH()) * 2;
	}
	public double getTotalPerimeter() {
		return totalPerimeter;
	}
}
