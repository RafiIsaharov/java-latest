package victor.training.java.patterns.proxy;

import lombok.SneakyThrows;
import org.apache.commons.lang3.ThreadUtils;

import java.util.function.Supplier;

import static java.time.Duration.ofSeconds;
//Disable/Enable copilot : Ctrl+Alt+Shift+O
public class ExecuteAroundPattern {
  public static void main(String[] args) {
//    long t0 = System.currentTimeMillis(); //# repeted code
//    int sum = sum(2, 3);
//    long t1 = System.currentTimeMillis();//# repeted code
//    System.out.println("Computed: " + sum + " in " + (t1 - t0) + "ms");//# repeted code
//
//    long t0b = System.currentTimeMillis();//# repeted code
//    int multiply = multiply(2, 3);
//    long t1b = System.currentTimeMillis();//# repeted code
//    System.out.println("Computed: " + sum + " in " + (t1b - t0b) + "ms");//# repeted code


    int result = measureTime(  ()   -> sum(2,3)     );
    System.out.println("Computed: " + result);

    int multiplyResult = measureTime(  ()   -> multiply(2,3)     );
    System.out.println("Computed: " + multiplyResult);

    // private final MeterRegistry meterRegistry; // micrometer = standard in Java to report metrics
    //
    //  public void method() {
    //    log.info("Doing something");
    //    meterRegistry.timer("my.timer").record(() -> {
    //      log.info("Doing something expensive");
    //    });
    //  }
  }
//passing arbitrary piece of logic Supplier<T> codeToMeasure to be executed
  public static <T> T measureTime(Supplier<T> codeToMeasure) {
    long t0 = System.currentTimeMillis(); // before
    T r = codeToMeasure.get();//execute the code to measure and get the result (the arbitrary piece of logic)
    long t1 = System.currentTimeMillis();// after
    System.out.println("Took " + (t1 - t0) + "ms");
    return r;
  }

  //Higher Order Function
//  public static Integer measureTime(Supplier<Integer> codeToMeasure) {
//    long t0 = System.currentTimeMillis();
//    Integer r = codeToMeasure.get();
//    long t1 = System.currentTimeMillis();
//    System.out.println("Took " + (t1 - t0) + "ms");
//    return r;
//  }


  //@SneakyThrows
  public static int sum(int a, int b) {
   // ThreadUtils.sleep(ofSeconds(1));
    return a + b;
  }
  public static int multiply(int a, int b) {
    return a * b;
  }
}
