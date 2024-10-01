
package victor.training.java.optional;



import victor.training.java.optional.model.Customer;
import victor.training.java.optional.model.MemberCard;

import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class Optional_Intro {
  public static void main(String[] args) {
    // test with 10 points or no MemberCard
    System.out.println(getDiscountLine(new Customer(new MemberCard("bar", 60))));
  // NPE -   Of the most useful features of Java 17. In practice, the new pointers are very explicit
    System.out.println(getDiscountLine(new Customer(new MemberCard("bar", 10))));
  }

  public static String getDiscountLine(Customer customer) {
    //enjoy the new Java 17 feature, wonderful exception message of NPE
      return "You got a discount of %" + computeDiscount(customer.getMemberCard()).globalPercentage();
  }

  private static Discount computeDiscount(MemberCard card) {
    if (card.getFidelityPoints() >= 100) {
      return new Discount(5, Map.of());
    }
    if (card.getFidelityPoints() >= 50) {
      return new Discount(3, Map.of());
    }
    return null;
  }

  public record Discount(int globalPercentage, Map<String, Integer> categoryDiscounts) {
  }
}

