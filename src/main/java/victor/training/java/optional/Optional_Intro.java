
package victor.training.java.optional;



import victor.training.java.optional.model.Customer;
import victor.training.java.optional.model.MemberCard;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("ConstantConditions")
public class Optional_Intro {

  public static void main(String[] args) {
    // test with 10 points or no MemberCard
    System.out.println(getDiscountLine(new Customer(new MemberCard("bar", 60))));
  // NPE -   Of the most useful features of Java 17. In practice, the new pointers are very explicit
//    Where this is exactly the biggest problem in the in the old Java.
//    That if someone else called the same function, they might not expect to see a new coming back from this function.
//    So they might get their own share of null point or exception because,
//    they might not be aware of the the No coming back.
    System.out.println(getDiscountLine(new Customer(new MemberCard("bar", 10))));
  }

  public static String getDiscountLine(Customer customer) {
    //enjoy the new Java 17 feature, wonderful exception message of NPE

    Optional<Discount> discount = computeDiscount(customer.getMemberCard());
//    How is this better?
//What's better is that the developers that call this method from discount had to type or orElseThrow.
//Had to assume the possibility of an empty box and an exception in here something that was previously promiscuous and very mysterious.
    //To explain the absence of something, the absence of a discount, the absence of a value.
    if(discount.isPresent()) {
      return "You got a discount of %" + discount.orElseThrow().globalPercentage();
    } else {
      return "Earn more points to get a discount";
    }
  }
// CTRL + SHIFT + ENTER => complete the statement with the missing parts
  private static Optional<Discount> computeDiscount(MemberCard card) {
    if (card.getFidelityPoints() >= 100) {
      return Optional.of(new Discount(5, Map.of()));
    }
    if (card.getFidelityPoints() >= 50) {
      //the of method is a factory method that creates an Optional object requireNonNull => null check
      return Optional.of(new Discount(3, Map.of())); // full box
    }
    //List<String> list = List.of(); //Null Object Pattern for collections = natural empty collection (null object)
    //NEVER EVER LEAVE A NULL IN A COLLECTION => but []
    // Null Object Pattern: a non-instance value that represents the absence of an object
    // authorizer.authorize(action);
    // Null object for authorizer: Authorizer.NULL_AUTHORIZER.authorize(action);
    // public static Authorizer NULL_AUTHORIZER = Authorize() {@ public void authorize(Action action) {/*nothing here*/} };
    //inside the #authorize method does NO CHECKS, {} => no-op
    //the caller might not be aware of this convention
    //    return Discount.NO_DISCOUNT;
    return Optional.empty(); // an empty box
  }

  public record Discount(int globalPercentage, Map<String, Integer> categoryDiscounts) {
    public static final Discount NO_DISCOUNT = new Discount(0, Map.of());
  }
}

