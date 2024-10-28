package victor.training.java.sealed.shapes;



import java.util.List;

public class VisitorPlay {

    public static void main(String[] args) {
        List<Shape> shapes = List.of(
                new Square(10), // 4 * E
                new Circle(5), // 2 * PI * R
                new Square(5),
                new Square(1));

        //OOP java 8
        double totalPerimeter = shapes.stream().mapToDouble(Shape::perimeter)
                .sum(); // TASK : compute
//        double totalPerimeter = 0;
//        for(Shape shape: shapes){
//            totalPerimeter+= shape.perimeter();
//        }
//        Task:
//        I want you to compute the total perimeter of all the shapes in this list.
        // ## instanceOf
        // ## OOP (behavior next to state)
        // ## VISITOR 😱
        // ## switch+sealed

        System.out.println(totalPerimeter);
    }
}


