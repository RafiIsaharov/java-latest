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

        //1. OOP💖 java 8
        // we changed the state of the object Square, Circle

//        double totalPerimeter = 0;
//        for(Shape shape: shapes){
//            totalPerimeter+= shape.perimeter();
//        }
//        double totalPerimeter = shapes.stream().mapToDouble(Shape::perimeter).sum(); // TASK : compute
        //2. instanceOf
        // what if we cannot add a method to the Shapes objects, because they are from a library in a jar or class is already humongous 300 lines of code
//        for(Shape shape: shapes){
//            if(shape instanceof Square square){
//                totalPerimeter+= 4 * square.edge();
//            } else if(shape instanceof Circle circle){
//                totalPerimeter+= 2 * Math.PI * circle.radius();
//            }
//            //what if tomorrow we add a new shape to the list,
//            // we need to change this code and nothing tells us here that we have a new shape in the
//            else{ // so we need add a some guard us against future types of shapes
//                // Because you want to have a clue that you didn't do something for in this case a rectangle.
//                throw new IllegalArgumentException("Unknown shape: " + shape);
//            }
//            //what if we have 100 shapes, we need to add 100 if else also problem
//        }

//        3. Visitor Pattern, became an anti-pattern in Java 21 because of sealed classes
        // we want to add a new behavior to the shapes, without changing the shapes
        //You might want to support different types of operations on the different types of shapes.
        //you can define the shape visitor interface which says visit a square, visit a circle, visit a rectangle
        // and then this is a very OO garbage that you can do, you are going to have in shape interface a method accept with a parameter of the visitor
        // and then all the implementations have to support this method
        //The circle is gonna say to the visitor hey visitor visit me, and the visitor is gonna say circle.visit(this)
        // in the shape implementation, you are going to call the visitor.visit(this)
        //every new type you create, you have to create a new method in the visitor 123 shapes classes 123 methods
        // So that you have by compilation time by polymorphism, you force yourself to have one method per concrete subtype
        //You have a hierarchy of classes. The shapeVisitor is a top level and then the square, circle, rectangle are the subtypes. You have a
        // method for each of them.
        // now create a perimeter visitor, and you have to implement the perimeter visitor for each of the shapes
//        double totalPerimeter = 0;
//        PerimeterVisitor perimeterVisitor = new PerimeterVisitor();
//        This syntax I would prefer to have a loop in this case because I'm not producing anything.
        //I am not producing anything. I am doing something.So if you are doing something, don't employ lambdas
//        for(Shape shape: shapes){
//            shape.accept(perimeterVisitor);
//        }
//        double totalPerimeter = perimeterVisitor.getTotalPerimeter();
//
//        AreaVisitor visitor = new AreaVisitor();
//        for (Shape shape : shapes) {
//            shape.accept(visitor);
//        }
//        double totalArea = visitor.getTotalArea();

        double totalPerimeter =0;
        double totalArea =0;
        System.out.println(totalPerimeter);
        System.out.println(totalArea);

    }
}


