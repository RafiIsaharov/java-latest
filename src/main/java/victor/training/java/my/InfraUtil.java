package victor.training.java.my;

import java.util.function.Supplier;

public class InfraUtil {
    public static <T> Supplier<T> createSingletonSupplier(Supplier<T> supplier) {
        return new Supplier<>() {
            private T instance;

            @Override
            public synchronized T get() {
                if (instance == null) {
                    instance = supplier.get();
                }
                return instance;
            }
        };
    }
}
