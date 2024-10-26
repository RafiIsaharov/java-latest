package victor.training.java.patterns.template;

import java.util.Objects;

public class CSVUtil {
    // #2 reason to use Template Method: when superclass provides some tools(methods) that the subclass can use to get the job done
    public static String escapeCell(Object cellValue) {
        if (cellValue instanceof String s) {
            if (!s.contains("\n")) return s;
            return "\"" + s.replace("\"", "\"\"") + "\"";
        } else {
            return Objects.toString(cellValue);
        }
    }
}
