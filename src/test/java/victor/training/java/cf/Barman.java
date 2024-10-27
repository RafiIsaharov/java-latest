package victor.training.java.cf;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.System.currentTimeMillis;

@Slf4j
@RestController
public class Barman {
  @Autowired
  private RestTemplate rest;

  //  @FunctionalInterface
  interface BeerSupplier {
    Beer getBeer();
  }

  @GetMapping("/drink")
  public DillyDilly drink() {
    String beerType = "IPA";
    long t0 = currentTimeMillis();

    // Java's CompletableFuture === JavaScript/TypeScript promises Deferred/Promise, async/await
    CompletableFuture<Beer> cfBeer = CompletableFuture.supplyAsync(() -> fetchBeer(beerType)); // take 0ms
    CompletableFuture<Beer> cfWarmBeer = cfBeer.thenApply(b -> warmup(b)); // callback, when beer arrive to me from fetchBeer
    cfWarmBeer.thenAccept(b -> log.info("Drinking warm 🍺: {}", b)); // callback
    Vodka vodka = fetchVodka();// the initial thread handling the HTTP request (coming from Tomcat spring boot) is blocked until vodka is fetched
    // join() > throws any exception occurred during the execution of the future
    Beer beer = cfBeer.join(); // block current thread until beer is fetched //take 1 sec, in RAM memory, a thread takes 0.5 MB (Thread stack size)
    //the way Java evolves the biggest bottleneck,
    // the biggest challenge we are facing today with modern applications is reducing the memory consumption of our flows.
    //Right now this is a wasteful approach. we use here 3 threads, 1 for beer, 1 for vodka, 1 for the main thread
    // we can use 2 threads, the main thread and a worker thread that will do the work of the vodka and other new thread will do the work of the beer
    DillyDilly dilly = new DillyDilly(beer, vodka);
//    auditTheDrink(dilly);// do stuff and nothing is returned, you want to run it in the background
//    I wrap this in the Lambda.
    //Converting what's now a call into an object which will call the function and then.
//This right now gives me void if I want to put a functional type to that, I'm going to say runnable
//    Runnable r=()->auditTheDrink(dilly);// do stuff and nothing is returned, you want to run it in the background
    //And then I want to hand this runable to someone to execute, run a synch
//    CompletableFuture.runAsync(r);//Fire-and-forget

    //Fire-and-forget
    //runAsync is a new thread that run in the background, it's a fire and forget, we don't care about the result
    // the dark side here : if the audit fails, we don't know about it,It can be a problem in many cases.
    //How do fix this?
    CompletableFuture<Void> cfVoid = CompletableFuture.runAsync(() -> auditTheDrink(dilly));
    //1) solution
    cfVoid.join();// stupidly block the main thread until the audit is done,


    //TODO Fire-and-forget
    //TODO Handle errors
    //TODO Callback-based non-blocking concurrency

    log.info("HTTP thread blocked for {} durationMillis", currentTimeMillis() - t0);
    return dilly;
  }

  @SneakyThrows
  // like a public void processUploadFile(File) This takes 5 minutes up to one hour, you want to run that in the background.
  //This is a traditional situation in which you want to start some process in the background.
  public void auditTheDrink(DillyDilly dilly) {
    //imagine: DB insert, kafka send, API call, takes time
    log.info("Auditing the drink: {}", dilly);
    Thread.sleep(500);
//    if(true) {
//      throw new RuntimeException("DB is down");
//    }
    log.info("Audit done");
  }

  private static Beer warmup(Beer beer1) {
    log.info("Warmup the beer: {}", beer1);
    return beer1;
  }

  private Vodka fetchVodka() {
    return rest.getForObject("http://localhost:9999/vodka", Vodka.class);
  }

  private Beer fetchBeer(String beerType) {
    String type = beerType;
    if(true) {
      throw new RuntimeException("Beer is out of stock");
    }
    return rest.getForObject("http://localhost:9999/beer", Beer.class);
  }
}


// CACHING should **NEVER** be the first answer to "how can I improve performance".
// Try first:
// - call less network: bring more data in one call: /beverages?types=beer,vodka ===> [Beer, Vodka]
//     GET /products?ids=1,2,3,4,5,6,7,8,9,10 or [1,2,3,4,5,6,7,8,9,10] in the body
//     <== [Product1, Product2, Product3, Product4, Product5, Product6, Product7, Product8, Product9, Product10]
// - optimize the network calls
// - parallelize your work (CPU or network) after JFR profiling your flow

// There are only 2 hard things in Computer Science:
// cache invalidation and naming things.