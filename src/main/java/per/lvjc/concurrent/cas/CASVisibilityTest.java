package per.lvjc.concurrent.cas;

import sun.misc.Unsafe;

public class CASVisibilityTest {

    private static volatile int value;

    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.staticFieldOffset
                    (CASVisibilityTest.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    private static void atomicIncrement() {
        int oldValue, newValue;
        boolean success;
        do {
            oldValue = value;
            newValue = oldValue + 1;
            success = unsafe.compareAndSwapInt(CASVisibilityTest.class, valueOffset, oldValue, newValue);
            if (!success) {
                System.out.println(success);
            }
        } while (!success);
    }

    public static void main(String[] args) throws InterruptedException {
        Thread read = new Thread(() -> {
            System.out.println("read begin");
            while (value < 5) {
                if (value != 0) {
                    System.out.println("read: value = " + value);
                }
            }
            System.out.println("read end");
        });
        Thread write = new Thread(() -> {
            System.out.println("write begin");
            while (value < 5) {
                atomicIncrement();
                System.out.println("write: value = " + value);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("write end");
        });
        read.start();
        Thread.sleep(100);
        write.start();
    }
}
