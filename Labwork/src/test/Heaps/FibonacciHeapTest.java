package Heaps;

public class FibonacciHeapTest extends AbstractHeapTest {

    @Override
    public AbstractHeap<Integer, Integer> getHeap() {
        return new FibonacciHeap<>(Integer.MIN_VALUE);
    }
}