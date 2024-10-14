package victor.training.java.patterns.template;

import lombok.RequiredArgsConstructor;
import victor.training.java.patterns.template.support.Order;
import victor.training.java.patterns.template.support.OrderRepo;
import victor.training.java.patterns.template.support.Product;
import victor.training.java.patterns.template.support.ProductRepo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

@RequiredArgsConstructor
public class Template2_Export {
    private final FileExporter exporter;

    public void exportOrders() throws Exception {
        exporter.exportOrders("orders.csv");
    }

    public void exportProducts() throws Exception {
        // TODO 'the same way you did the export of orders'
        // RUN UNIT TESTS!
        exporter.exportOrders("product.csv");
    }
}

@RequiredArgsConstructor
class FileExporter {
    private final OrderRepo orderRepo;
    private final File exportFolder;

    public File exportOrders(String fileName) {
        File file = new File(exportFolder, fileName);
        long t0 = System.currentTimeMillis();
        try (Writer writer = new FileWriter(file)) { // try-with-resources java 7
            System.out.println("Starting export to: " + file.getAbsolutePath());
//-----------------new logic for product should be changed --------------------------------
            // we can use passing lambda to the method as a behavior
            writeContents(writer);
//-------------------------------------------------
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
    // Java sucks because it allows you to override any public you inherited from your SuperClass!
    // we don't know if the subclass will call this method or not
    // other languages like C# or Kotlin have the 'final' keyword to prevent this
    // the code misslead the reader, because the method override the method from the superclass
    //but who knows if the subclass will call this method or not
    protected void writeContents(Writer writer) throws IOException {
        writer.write("OrderID;CustomerId;Amount\n");//header
        //body
        for (Order order : orderRepo.findByActiveTrue()) {
            String csv = order.id() + ";" + order.customerId() + ";" + order.amount() + "\n";
            writer.write(csv);
        }
    }

    public String escapeCell(Object cellValue) {
        if (cellValue instanceof String s) {
            if (!s.contains("\n")) return s;
            return "\"" + s.replace("\"", "\"\"") + "\"";
        } else {
            return Objects.toString(cellValue);
        }
    }
}

class ProdauctExporter extends FileExporter {
    private final ProductRepo productRepo;
    public ProdauctExporter(OrderRepo orderRepo, File exportFolder, ProductRepo productRepo) {
        super(orderRepo, exportFolder);
        this.productRepo = productRepo;
    }
    protected void writeContents(Writer writer) throws IOException {
        writer.write("ProductID;Name;Price\n");//header
        //body
        for (Product product : productRepo.findAll()) {
            String csv = product.id() + ";" + escapeCell(product.name()) + ";" + product.price() + "\n";
            writer.write(csv);
        }
    }

}
