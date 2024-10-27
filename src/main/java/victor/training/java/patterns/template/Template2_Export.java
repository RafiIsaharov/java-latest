package victor.training.java.patterns.template;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jooq.lambda.Unchecked;
import victor.training.java.patterns.template.support.Order;
import victor.training.java.patterns.template.support.OrderRepo;
import victor.training.java.patterns.template.support.Product;
import victor.training.java.patterns.template.support.ProductRepo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class Template2_Export {
    public static final File FOLDER = new File("export");
    private final FileExporter exporter;
//    private final OrderRepo orderRepo;
//    private final ProductRepo productRepo;
    private final ProductExporter productExporter;
    private final OrderExporter orderExporter;

//    @SneakyThrows // darkest lombok feature
    // it tricks the javac compiler to ignore checked exceptions (to think that this method throws Exception)
    //"checked" means that the compiler checks if you handle the exception
    // checked exceptions are a design mistake in Java
    // at runtime there's no difference between checked and unchecked exceptions -JVM doesn't care
    public void exportOrders() {
//        new OrderExporter(FOLDER,orderRepo).export("orders.csv");
        //there is a glitch because the IOException is not caught in the export method
        // Java 8 hate checked exceptions, the  options  @SneakyThrows (bug in Intellij ) doesn't compile: java: incompatible thrown types java.io
        // .IOException in functional expression
//        exporter.export("orders.csv", writer -> uncheck(()->orderExporter.writeContents(writer)));
        exporter.export("orders.csv", uncheck(orderExporter::writeContents));
        //    https://projectlombok.org/features/SneakyThrows
    }

    public void exportProducts() {
        // TODO 'the same way you did the export of orders'
        // RUN UNIT TESTS!
//        new ProductExporter(FOLDER, productRepo).export("products.csv");
        // but this hard-working solution is not good enough, we need to make it more generic. as it will be used in other places
        // we will use AOP to make it more generic, I would like a write a function to which i could pass a variable behavior as a parameter
        // f() -> variable behavior

//        exporter.export("products.csv", writer -> uncheck(()->productExporter.writeContents(writer)));
        exporter.export("products.csv", uncheck(productExporter::writeContents));
        // jool library - already constructed a function that does this
        exporter.export("products.csv", Unchecked.consumer(productExporter::writeContents));
    }
    // I wish I had a function that can take a ThrowingConsumer and convert it to a Consumer
    //If I had such utility, I could do exporter.export("products.csv", uncheck(productExporter::writeContents));
    // I want a function that transforms a function (ThrowingConsumer) into another function (Consumer)
    //PANIC MOMENT.
    // but I never returned the before a function from my function
    // FP means that function are first class citizens, they can be passed as parameters, returned from functions, stored in variables

    @FunctionalInterface
    interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }
// I want to return a consumer(function) that when called will call the 'throwingConsumer' parameter and if it throws an exception, it will wrap it in a RuntimeException
    private static <T> Consumer<T> uncheck(ThrowingConsumer<T> throwingConsumer) {
//        Consumer<String> f1 = t -> System.out.println(t);
//        Consumer<T> f2 = t -> System.out.println(t);
//        Consumer<T> tConsumer = t -> {
//            try {
//                throwingConsumer.accept(t);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        };
        return t -> {
            try {
                throwingConsumer.accept(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

//    private void uncheck(ThrowingRunnable r) {
//        try { // this is a same part in all exporters
//           r.run(); // variable behavior
//        } catch (Exception e) { // this is a same part in all exporters
//            throw new RuntimeException(e);
//        }
//    }
//    private void uncheck(Writer writer) {
//        try { // this is a same part in all exporters
//            productExporter.writeContents(writer); // variable behavior
//        } catch (IOException e) { // this is a same part in all exporters
//            throw new RuntimeException(e);
//        }
//    }
}


@RequiredArgsConstructor
class FileExporter {
    private final File exportFolder;

    public File export(String fileName, Consumer<Writer> writeContents) {
        File file = new File(exportFolder, fileName);
        long t0 = System.currentTimeMillis();
        try (Writer writer = new FileWriter(file)) { // java 7 try-with-resources
            System.out.println("Starting export to: " + file.getAbsolutePath());

            writeContents.accept(writer);

            System.out.println("File export completed: " + file.getAbsolutePath());
            return file;
        } catch (Exception e) {
            System.out.println("Pretend: Send Error Notification Email"); // TODO CR: only for export orders, not for products
            throw new RuntimeException("Error exporting data", e);
        } finally {
            long t1 = System.currentTimeMillis();
            System.out.println("Pretend: Metrics: Export finished in: " + (t1 - t0));
        }
    }

    // Java sucks because it allows you to override any public you inherited from your Super! types
    // other languages like C# or Kotlin don't allow this

//    protected abstract void writeContents(Writer writer) throws IOException;
    //in Java 8 this signature could be Consumer<Writer> writeContents;
    // We want to play functional programming on this and instead of relying on me being extended by others,
    // I'm going to instead take the missing behavior as a parameter

    // #1 reason to use Template Method: to allow the subclass to provide the missing behavior
    /** Override this method if you want to encrypt the exported file */
    protected void encryptFile(File file) { /*NOOP*/ } // the hook method

}
class OrderExporter extends FileExporter {
    private final OrderRepo orderRepo;

    public OrderExporter(File exportFolder, OrderRepo orderRepo) {
        super(exportFolder);
        this.orderRepo = orderRepo;
    }

//    @Override
//    protected void encryptFile(File file) {
//        // fun only here, for orders
//    }
//@SneakyThrows
    public void writeContents(Writer writer) throws IOException {
        writer.write("OrderID;CustomerId;Amount\n"); // header
        for (Order order : orderRepo.findByActiveTrue()) {// body
            String csv = order.id() + ";" + CSVUtil.escapeCell(order.customerId()) + ";" + order.amount() + "\n";
            writer.write(csv);
        }
    }

}

class ProductExporter extends FileExporter {
    private final ProductRepo productRepo;

    public ProductExporter(File exportFolder, ProductRepo productRepo) {
        super(exportFolder);
        this.productRepo = productRepo;
    }

    public void writeContents(Writer writer) throws IOException {
        writer.write("ProductID;Name;Price\n"); // header
        for (Product product : productRepo.findAll()) {
            String csv = product.id() + ";" + product.name() + ";" + product.price() + "\n";
            writer.write(csv);
        }
    }
}