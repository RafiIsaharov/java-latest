package victor.training.java.optional.abuse;

import java.util.Optional;

public class OrElseGetTrap {
  private record Transfer(String from, String to, int amount) {
  }

  static Optional<Transfer> takeOwnMoney(String user) {
    System.out.println("Taking money from " + user);
    return Optional.of(new Transfer("Victor", "Vodafone", 100));
  }

  static Transfer takeCreditMoney(String user) {
    System.out.println("Borrowing⚠️ money for " + user);
    return new Transfer("Creditor", "Vodafone", 100);
  }

  public static void main(String[] args) {
//    Transfer transfer = takeOwnMoney("user")
       //BUG: .orElseGet(() -> takeCreditMoney("user")); // THIS IS CORRECT: this is executed only if the Optional is empty
//        .orElse(takeCreditMoney("user")); // THIS CALL THIS IS NOT CORRECT: this be executed even if the Optional is not empty
    //    System.out.println("Transfer: " + transfer);
// instead write: takeOwnMoney("user") in lambda expression
//RULE: don't call methods in orElse, use lambdas
    Transfer transfer2 = takeOwnMoney("user").orElseGet(() -> takeCreditMoney("user")); // this is the correct way to do it
    // in java arguments are passed by VALUE.
    //that is, before calling orElse JVM is invoking takeCreditMoney("user") and then passing the result to orElse
    //PROBLEM: calls takeCreditMoney even if the Optional is not empty because the method is called before the orElse mtethod

    System.out.println("Transfer: " + transfer2);
  }
}
