package per.lvjc.concurrent.cas;

import java.util.concurrent.atomic.AtomicReferenceArray;

public class AtomicReferenceArrayTest {

    private static String[] strArray = {"x", "y"};
    private static AtomicReferenceArray<String> atomicReferenceArray = new AtomicReferenceArray<>(strArray);

    public static void main(String[] args) {
        atomicReferenceArray.compareAndSet(1, "y", "z");
    }
}
