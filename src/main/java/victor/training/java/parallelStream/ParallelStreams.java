package victor.training.java.parallelStream;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static victor.training.java.Util.sleepMillis;

@Slf4j
public class ParallelStreams {//don't use parallel stream for IO bound work
  public static void main(String[] args) throws ExecutionException, InterruptedException {
     OnAServer.otherParallelRequestsAreRunning();
      // starve the shared commonPool din JVM
      // you met someone (another flow) that starves the commonPool even worse than you
        // you can't control the other flows

      List<Integer> list = IntStream.range(1, 100).boxed().toList();

    long t0 = System.currentTimeMillis();
      // 1. measure (JFR profiler) where you loose most time.
      // 2. if you loose time in a cpu work that does not hit network (Hint: most server apps are IO-bound)
      // => use parallelStream and measure the benefit. (JFR profiling in production)

      var result = list.parallelStream()
                //If you have a stream of elements and you want to filter them
                //And then you want to process them in parallel
                //You can use the parallelStream method
                //This will create a parallel stream that will process the elements in parallel
                //The parallel stream will use the common fork join pool
                //The common fork join pool is a shared resource pool that is used by all the parallel streams in the JVM
                //If you have other parallel requests running in the JVM
                //They will starve the common fork join pool
                //And your parallel stream will be slow
                //If you have a parallel stream that is slow
                //You can try to increase the size of the common fork join pool
                //You can do this by setting the system property java.util.concurrent.ForkJoinPool.common.parallelism
                //To the number of threads that you want to use in the common fork join pool
              // the default threads in JVM common pool is the number of cores in the machine - 1 (to leave one core for the main thread)
        .filter(i -> i % 2 == 0)
              //if the elements are independent, if the code are independent and they are
              //Processing element doesn't have anything to do with processing another element
                //Then you can parallelize the processing of these elements
        .map(id -> fetchProduct(id)).toList();
      // The profiler reported toList taking too long of your flow, you will try to optimize
      // One of the first things to try to parallelize the processing of these elements

//      increased speed by 10x
//      the improvement is amazing.
//      But: what kind of work do you want to parallelize ?
//      - ✅CPU bound work: eg: encryption, decryption, compression, decompression, image processing, video processing, XSLT
      // spread the work to All available CPU cores
//      - 🚫IO bound work: eg: network calls, database calls, file system calls, SOAP calls, REST calls
      //  > #1 issue: thread starvation: you will abuse the shared JVM commonPool
      //       see this video https://www.youtube.com/watch?v=0hQvWIdwnw4
      //       to see how to run your parallel stream in a custom pool

        //  > #2 issue: if you go over the network to the same server, you will not get any benefit
      // famous remote-call-in-a-loop anti-pattern
                    //GET products/1, GET products/2, GET products/3, GET products/4
      // It would be much smarter to get all the products in one go instead of making multiple requests one after the other
      // you would fetch them in pages (eg: 1000 products at a time)
      // example: GET products?ids=1,2,3,4
      long t1 = System.currentTimeMillis();
    log.info("Took {} ms to get: {}", t1 - t0, result);
  }

    private static int fetchProduct(Integer i) {
        log.info("Map " + i);
        sleepMillis(100); // network call (DB, REST, SOAP..) or CPU work
        return i * 2;
    }
}
