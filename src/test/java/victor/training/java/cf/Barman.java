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
import static java.util.concurrent.CompletableFuture.supplyAsync;

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
    CompletableFuture<Beer> cfBeer = supplyAsync(() -> fetchBeer(beerType))
            .exceptionally(e-> new Beer("draught beer")); // take 0ms
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
//    try {// try catch solution to handle the exception Never do this, it's a bad practice
//      CompletableFuture<Void> cfVoid = CompletableFuture.runAsync(() -> auditTheDrink(dilly));

      //1) solution
//    cfVoid.join();// stupidly block the main thread until the audit is done,
//    }catch (Exception e) {
//      log.error("Audit failed", e); // never executes because the exception is thrown in the background thread
//    }
    //    2) solution - Handle errors
    //add try{}catch : inside the auditTheDrink task, but with this solution you lose the context of the parent thread
//  3) solution: fallbacks via exceptionally (everything related to computable future is callback based)
    // equivalent to the callback in the JavaScript of a catch with promises
    //Possible outcomes. A compatible future can give you the result, or it can give you the error
    //all this 3 solution lead to blocking the main thread we have 2 tread, the main thread and the worker thread 2*0.5 MB,
    // but imagine if we have 200 threads that will be 2000*0.5 MB =  1 GB of memory wasted
    // if you have a DB Oracle can lead to a battle neck
//    4) solution: add a Callback-base to the CompletableFuture (everything related to computable future is callback based)
    CompletableFuture<Void> cfVoid = CompletableFuture.runAsync(() -> auditTheDrink(dilly)).exceptionally(e -> {
      log.error("Failed to audit the drink, i was asked for beer type " + beerType, e);
      return null;
    });

    // Fire-and-forget - done
    // Handle errors - done
    //TODO Callback-based non-blocking concurrency

    log.info("HTTP thread blocked for {} durationMillis", currentTimeMillis() - t0);
    return dilly;
  }



  @GetMapping("/drink-non-blocking")
  //What can I return?  How? What can I give to my web framework back?
//You can take your promise of a dilly and give that promise - CompletableFuture<DillyDilly>
  //The web framework understands that you want it to wait for this dilly to be available
  //In other words, you want the framework to wait for this to actually execute in the future, and both are ready, and when it's done, serialize it as a Jason.
  //  What's happening in the browser is the exact same thing you're going to see.
  //  The browser is going to wait for the promise to be done, and when it's done, it's going to show you the result.

  //But what happens in the main thread end is that the main thread that enters this method exits the method instantaneously.
  // It does not ever have to wait anything. The HTTP thread blocked for 0 milliseconds.
  public CompletableFuture<DillyDilly> drinkNonBlocking() { // non-blocking, callback-based concurrency
    // no .get or .join allowed here
    String beerType = "IPA";
    long t0 = currentTimeMillis();

    var beer = supplyAsync(()->fetchBeer(beerType)).exceptionally(e -> new Beer("draught beer"));
    var vodka = supplyAsync(this::fetchVodka).exceptionally(e -> new Vodka("cheap vodka"));
    //When you combine 2 completable futures, 2 promises, I'm using the word promise repeatedly on purpose.
    //It's a promise that something is gonna be done.
    //I'm going to combine 2 promises, 2 completable futures, 2 deferreds, 2 tasks, 2 threads, 2 async operations
    //When the beer promise is gonna be done, then combine that promise with the vodka
    //It's a promise that some deal is gonna be ready in the future sometime
    //After both beer and vodka are done. I'm going to combine them into a DillyDilly
    //You combine the things which you don't have yet, and you get a third thing that of course you don't have yet.
    //That's why it's a compatible feature. It's a promise that something is gonna be done.
    //BiFunction<Beer, Vodka, DillyDilly> dillyDillyBiFunction = (b, v) -> new DillyDilly(b, v);
//    CompletableFuture<DillyDilly> dilly = beer.thenCombine(vodka, (b, v) -> new DillyDilly(b, v));
    CompletableFuture<DillyDilly> dilly = beer.thenCombine(vodka, DillyDilly::new)
            .exceptionally(e -> new DillyDilly(new Beer("draught beer"),
                    new Vodka("cheap vodka")));
//    var dilly = new DillyDilly(beer, vodka);
    log.info("HTTP thread blocked for {} millis", currentTimeMillis() - t0);
    return dilly;
  }

  @SneakyThrows
  // like a public void processUploadFile(File) This takes 5 minutes up to one hour, you want to run that in the background.
  //This is a traditional situation in which you want to start some process in the background.
  public void auditTheDrink(DillyDilly dilly) {
    //imagine: DB insert, kafka send, API call, takes time
//    2) solution
    //add try{} catch inside the task, but with this solution you lose the context of the parent thread
    // if I run 2 calls in parallel then I won't know which one through that error
//    try {
      log.info("Auditing the drink: {}", dilly);
      Thread.sleep(500);
      if (true) {
        throw new RuntimeException("DB is down");
      }
      log.info("Audit done");
//    } catch (Exception e) {
//    log.error("Audit failed from auditTheDrink", e); // never executes because the exception is thrown in the background thread
//  }
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
//    if(true) {
//      throw new RuntimeException("Beer is out of stock");
//    }
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