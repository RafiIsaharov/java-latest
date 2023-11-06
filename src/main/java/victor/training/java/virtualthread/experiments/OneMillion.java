package victor.training.java.virtualthread.experiments;

import victor.training.java.Util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class OneMillion {

  public static void main(String[] args) {
    var counter = new AtomicInteger(0);
    try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (int i = 0; i < 1_000_000; i++) {
        virtualExecutor.submit(() -> {
          counter.incrementAndGet();
          Util.sleepMillis(5000);
          counter.decrementAndGet();
        });
      }
      System.out.println("All tasks submitted");
      Util.sleepMillis(4000);
      System.out.println("Now running threads: " + counter.get());
    }
    System.out.println("All threads finished: " + counter.get());
  }
}
