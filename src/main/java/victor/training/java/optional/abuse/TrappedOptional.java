package victor.training.java.optional.abuse;

import java.util.Optional;

public class TrappedOptional {
  public void kafkaSend(String personName) {
  }
  static class MyDto {
    public String recipientPerson;
  }
  static class MyEntity {
    private String recipient;

    public String getRecipient() {
      return recipient;
    }

    public void setRecipient(String recipient) {
      this.recipient = recipient;
    }
  }

  public void trappedOptional(MyDto dto) {
    MyEntity entity = new MyEntity();
    //Abuse of Optional
//    Rule don't start ofNullable . . .  and finish with the Optional in the same method
    // with orElse/ifPresent/orElseThrow/orElseGet - it does not make sense to create mapped and destroy it

//    Optional.ofNullable(dto.recipientPerson)
//            .map(String::toUpperCase)
//            .ifPresent(name -> entity.setRecipient(name));
//    THSI IS SIMPLIFIED:
    //Instead use traditional if
    //Optional was added to Java to be return type of methods,
//    largely used in your code/public API
    if (dto.recipientPerson != null) {
      entity.setRecipient(dto.recipientPerson.toUpperCase());
    }

  }

  public void trappedOptionalWithExternalSideEffect(MyDto dto) {
    Optional.ofNullable(dto.recipientPerson)
            .map(String::toUpperCase)
            .ifPresent(name -> kafkaSend(name));
  }
}
