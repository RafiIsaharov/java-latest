package victor.training.java.optional.abuse;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OptionalCollections {
  private static class Product {}
  private static class Coupon {}
  private static class Customer {}
  private interface CustomerRepo {
    Optional<Customer> findById(Long id);
  }

  private CustomerRepo customerRepo;
// bad practice: returning Optional<List> instead of List, because the list is never null, has an empty state
  public List<Coupon> findApplicableCoupons(Long customerId, List<Product> products) {
    System.out.println("Retrieve customer coupons " + customerId);
    Optional<Customer> customerFound = customerRepo.findById(customerId);
    if (customerFound.isEmpty()) {
      return List.of();// Optional.empty() -> List.of()
    }
    List<Coupon> coupons = new ArrayList<>();
    System.out.println("Filter out not applicable coupons from " + coupons);
//    return Optional.of(coupons);
    return coupons;
  }

  public void caller(Long customerId) {
    List<Product> products = List.of(new Product(), new Product());
//    Optional<List<Coupon>> couponsOpt = findApplicableCoupons(customerId, products);
    List<Coupon> couponsOpt = findApplicableCoupons(customerId, products);

  //  if (couponsOpt.isPresent()) {
      for (Coupon coupon : couponsOpt /*couponsOpt.get()*/) {
        System.out.println("Apply coupon: " + coupon);
 //     }
    }
  }
}
