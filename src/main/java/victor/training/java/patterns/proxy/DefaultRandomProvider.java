package victor.training.java.patterns.proxy;

public class DefaultRandomProvider implements RandomProvider {
    @Override
    public double getRandom() {
        return Math.random();
    }
}
