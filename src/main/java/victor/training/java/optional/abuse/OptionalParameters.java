package victor.training.java.optional.abuse;

import java.util.Optional;

public class OptionalParameters {

  public void callers() {
    // without
    sendMessage("jdoe", "message", null);
//    sendMessage("jdoe", "message", Optional.empty());

    // with
    sendMessage("jdoe", "message", "REGLISS");
//    sendMessage("jdoe", "message", Optional.of("REGLISS"));

  }

  // ⬇⬇⬇⬇⬇⬇ utility / library code ⬇⬇⬇⬇⬇⬇
  public void sendMessage(String recipient, String message) {
    System.out.println("Resolve phone number for " + recipient);
    System.out.println("Send message " + message);
  }

    public void sendMessage(String recipient, String message, String trackingRegistry) {
//  public void sendMessage(String recipient, String message, Optional<String> trackingRegistry) {
      sendMessage(recipient, message);
      System.out.println("Notify the tracking registry : " + trackingRegistry);
    }
}
