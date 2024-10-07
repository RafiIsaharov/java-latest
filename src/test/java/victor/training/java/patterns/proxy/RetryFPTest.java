package victor.training.java.patterns.proxy;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RetryFPTest {

  @Test
  void mainMethodTest() {
    // Mock RandomProvider to always return a value greater than 0.8
    RandomProvider mockRandomProvider = mock(RandomProvider.class);
    when(mockRandomProvider.getRandom()).thenReturn(0.9);
    RetryFP.setRandomProvider(mockRandomProvider);

    // Capture the console output
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    // Run the main method
    RetryFP.main(new String[]{});

    // Verify the output
    String output = outContent.toString();
    assertTrue(output.contains("SUCCESS"));
  }

  @Test
  void retrySucceedsOnFirstAttempt() {
    RetryFP.retry(3, () -> System.out.println("SUCCESS"));
  }

  @Test
  void retrySucceedsAfterRetries() {
    int[] attempts = {0};
    RetryFP.retry(3, () -> {
      if (attempts[0]++ < 2) {
        throw new RuntimeException("Network error");
      }
      System.out.println("SUCCESS");
    });
  }

  @Test
  void retryFailsAfterMaxRetries() {
    assertThrows(RuntimeException.class, () -> {
      RetryFP.retry(3, () -> {
        throw new RuntimeException("Network error");
      });
    });
  }

  @Test
  void riskyNetworkCallSucceeds() {
    RandomProvider mockRandomProvider = mock(RandomProvider.class);
    when(mockRandomProvider.getRandom()).thenReturn(0.9);
    RetryFP.setRandomProvider(mockRandomProvider);

    assertDoesNotThrow(RetryFP::riskyNetworkCall);
  }

  @Test
  void riskyNetworkCallFails() {
    RandomProvider mockRandomProvider = mock(RandomProvider.class);
    when(mockRandomProvider.getRandom()).thenReturn(0.7);
    RetryFP.setRandomProvider(mockRandomProvider);

    assertThrows(RuntimeException.class, RetryFP::riskyNetworkCall);
  }
}