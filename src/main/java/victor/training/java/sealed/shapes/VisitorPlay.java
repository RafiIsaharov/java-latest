package victor.training.java.sealed.shapes;



import java.util.List;

public class VisitorPlay {

    public static void main(String[] args) {
        // ## instanceOf
        // ## OOP (behavior next to state)
        // ## VISITOR 😱
        // ## switch+sealed
        List<Shape> shapes = List.of(
                new Square(10), // 4 * E
                new Circle(5), // 2 * PI * R
                new Square(5),
                new Rectangle(3, 4),
                new Square(1));
//        Task:
//        I want you to compute the total perimeter of all the shapes in this list.

        //1. OOP java 8
        // we changed the state of the object Square, Circle

        double totalPerimeter = 0;
//        for(Shape shape: shapes){
//            totalPerimeter+= shape.perimeter();
//        }
//        double totalPerimeter = shapes.stream().mapToDouble(Shape::perimeter).sum(); // TASK : compute
        //2. instanceOf
        // what if we cannot add a method to the Shapes objects, because they are from a library in a jar or class is already humongous 300 lines of code
        for(Shape shape: shapes){
            if(shape instanceof Square square){
                totalPerimeter+= 4 * square.edge();
            } else if(shape instanceof Circle circle){
                totalPerimeter+= 2 * Math.PI * circle.radius();
            }
            //what if tomorrow we add a new shape to the list,
            // we need to change this code and nothing tells us here that we have a new shape in the
            else{ // so we need add a some guard us against future types of shapes
                // Because you want to have a clue that you didn't do something for in this case a rectangle.
                throw new IllegalArgumentException("Unknown shape: " + shape);
            }
            //what if we have 100 shapes, we need to add 100 if else also problem
        }



        System.out.println(totalPerimeter);
    }
}


