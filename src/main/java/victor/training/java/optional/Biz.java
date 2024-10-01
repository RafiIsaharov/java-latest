package victor.training.java.optional;

import lombok.RequiredArgsConstructor;
import victor.training.java.optional.model.Customer;
import victor.training.java.optional.model.MemberCard;
import victor.training.java.optional.model.Order;

@RequiredArgsConstructor
public class Biz {
   private final Config config;

   public void applyDiscount(Order order, Customer customer) {
      System.out.println("START");
      if (order.getOfferDate().before(config.getLastPromoDate())) { // TODO inside
         // broke compilation when discovered BUG this will return NPE so i should fix it because
//         int points = customer.getMemberCard().getFidelityPoints();
//         if the customer did not have a member card, the points default to 0
         int points = customer.getMemberCard()
                 .map(MemberCard::getFidelityPoints).orElse(0);
         order.setPrice(order.getPrice() * (100 - 2 * points) / 100);
         System.out.println("APPLIED DISCOUNT using " + customer.getMemberCard().getBarcode());
      } else {
         System.out.println("NO DISCOUNT");
      }
   }
}

