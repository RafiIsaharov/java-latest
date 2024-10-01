
package victor.training.java.optional;



import victor.training.java.optional.model.Customer;
import victor.training.java.optional.model.MemberCard;

import java.util.List;
import java.util.Map;

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

    Discount discount = computeDiscount(customer.getMemberCard());
    if (discount != null) {
      return "You got a discount of %" + discount.globalPercentage();
    }
    else {
      return "No discount for you";
    }
  }

  private static Discount computeDiscount(MemberCard card) {
    if (card.getFidelityPoints() >= 100) {
      return new Discount(5, Map.of());
    }
    if (card.getFidelityPoints() >= 50) {
      return new Discount(3, Map.of());
    }
    List<String> list = List.of(); //Null Object Pattern for collections = natural empty collection (null object)
    //NEVER EVER LEAVE A NULL IN A COLLECTION => but []
    // Null Object Pattern: a non-instance value that represents the absence of an object
    // authorizer.authorize(action);
    // Null object for authorizer: Authorizer.NULL_AUTHORIZER.authorize(action);
    // public static Authorizer NULL_AUTHORIZER = Authorize() {@ public void authorize(Action action) {/*nothing here*/} };
    //inside the #authorize method does NO CHECKS, {} => no-op
    //the caller might not be aware of this convention
    return Discount.NO_DISCOUNT;
  }

  public record Discount(int globalPercentage, Map<String, Integer> categoryDiscounts) {
    public static final Discount NO_DISCOUNT = new Discount(0, Map.of());
  }
}

