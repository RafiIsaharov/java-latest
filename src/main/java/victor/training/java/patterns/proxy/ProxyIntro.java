package victor.training.java.patterns.proxy;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;

@SpringBootApplication
public class ProxyIntro {
    public static void main(String[] args) {
        // Play the role of Spring here (there's no framework)
        // TODO 1 : LOG the arguments of any invocation of a method in Maths w/ decorator
        // TODO 2 : without changing anything below the line (w/o any interface)
        // TODO 3 : so that any new methods in Maths are automatically logged [hard]
//        Maths realMathObject = new Maths();
//        Callback h = new MethodInterceptor() {
//            @Override
//            public Object intercept(Object obj, Method methodIntercepted, Object[] args, MethodProxy proxy) throws Throwable {
//                //this runs any method you call on the 'proxy' object (which is a Maths object) below
//                // the method called is 'methodIntercepted'
//                System.out.println("Intercepted method: " + methodIntercepted.getName() + " with args: " + Arrays.toString(args));
//                long t0 = System.currentTimeMillis();
//                try {
////                return proxy.invokeSuper(obj, args);
////                return methodIntercepted.invoke(realMathObject, args);
//                    return proxy.invoke(realMathObject, args);
//                } finally {
//                    System.out.println("Method " + methodIntercepted.getName() + " took " + (System.currentTimeMillis() - t0) + " ms");
//                }
//            }
//        };
//        // java calls the intercept a 'proxy'
//        Maths proxy = (Maths) Enhancer.create(Maths.class, h);//instance of a subclass of Maths generated at runtime
//        SecondGrade secondGrade = new SecondGrade(proxy);
        // if it's generated at runtime
//        Maths loggingDecorator = new LoggingDecorator(realMathObject);
//        Maths timingDecorator = new TimingDecorator(realMathObject);
//        SecondGrade secondGrade = new SecondGrade(realMathObject);
//        SecondGrade secondGrade = new SecondGrade(loggingDecorator);
//      SecondGrade secondGrade = new SecondGrade(new TimingDecorator(new LoggingDecorator(new Maths())));
//      SecondGrade secondGrade = new SecondGrade(new LoggingDecorator(new Maths()));
//      SecondGrade secondGrade = new SecondGrade(new TimingDecorator(new Maths()));
//        new ProxyIntro().run(secondGrade);
        // TODO 4 : let Spring do its job, and do the same with an Aspect
         SpringApplication.run(ProxyIntro.class, args);
    }
    // =============== THE LINE =================
    @Autowired
    public void run(SecondGrade secondGrade) {
        System.out.println("At runtime...");
        secondGrade.mathClass();
    }

}
@RequiredArgsConstructor
@Service
class SecondGrade {
    private final Maths maths;

    public void mathClass() {
        //all of these calls should be logged. this class is NOT aware that the sum/product methods
        //go to the LoggingDecorator instead of Maths class, thanks to Polymorphism

        // how Would I know if maths is a proxy or not? Spring inject for me a hack class that extends Maths: Maths$$SpringCGLIB$$0
        System.out.println("the class of maths is " + maths.getClass());//the class of maths is class victor.training.java.patterns.proxy.Maths$$SpringCGLIB$$0
        System.out.println("2+4=" + maths.sum(2, 4));
        System.out.println("1+5=" + maths.sum(1, 5));
        System.out.println("2x3=" + maths.product(2, 3));
    }
}
//REQUIREMENT: any method in Math class should be log its arguments
//without changing the Math class
//use OOP
//Generating a class that looks and smells and shows like your real class, but it's not.
//But it extends it so that it can be passed as a dependency instead of your expected object.
//It's a proxy. It's a decorator. It's a wrapper.
// frameworks can generate this class at runtime for us: example: CGLIB, ByteBuddy, Javassist, ASM, etc.
//Spring AOP uses CGLIB by default

//@RequiredArgsConstructor
//class LoggingDecorator extends Maths {
//    //favor composition over inheritance means that you should prefer to use composition instead of inheritance
//    private final Maths decorated;
//
//    @Override
//    public int sum(int a, int b) {
//        System.out.println("Summing " + a + " and " + b);
//        return decorated.sum(a, b);
//    }
//    @Override
//    public int product(int a, int b) {
//        System.out.println("Multiplying " + a + " and " + b);
//        return decorated.product(a, b);
//    }
//}
// Thoughts:
// - HOW about private methods? They are not inherited, so they are not intercepted
// what can you change in the Maths class so a method cannot be intercepted anymore?
// Hint: proxing is done by extending the class
//final class Maths { #3🥴 crashes CGLIB
@Service
class Maths {
    // @Secured("ROLE_ADMIN") // this does not work if you call methods from within the same class
    @LoggedMethod
    public int sum(int a, int b) {
        if (true) throw new RuntimeException("intentional");
        return privateMethod(a, b); // private methods are not intercepted since they are not inherited
    }
    //  public static int sum(int a, int b) { #4🥴 static methods (eg in Util/Helper) are not proxied because they are not inherited, you can't override them
//    #2🥴 final method no longer proxied (intercepted)
//    public final int sum(int a, int b) {
//        return privateMethod(a, b);
//    }
//#1🥴 private methods are not intercepted since they are not inherited
    private static int privateMethod(int a, int b) {
        return a + b;
    }

    // method interception is used by Spring( and other frameworks) to implement features like:
    // @Transactional, @Cacheable, @Async, @Retryable, @Scheduled, @Secured, @PreAuthorize, @PostAuthorize, @PostFilter, @PreFilter
//    public int product(int a, int b) {
//        return a * b;
//    }
    @LoggedMethod
    public int product(int a, int b) {
        int result = 0;
        for (int i = 0; i < b; i++) {
            result = sum(result, a); // #5 👑 calling my own local method from within the class
            // does not run the interceptors (according to @Transactional, @Cacheable, etc)
        }
        return result;
    }
}


// Key Points
// [2] Class Proxy using CGLIB (Enhancer) extending the proxied class
// [3] Spring Cache support [opt: redis]
// [4] Custom @Aspect, applied to methods in @Facade
// [6] Tips: self proxy, debugging, final
// [7] OPT: Manual proxying using BeanPostProcessor
