package per.lvjc.concurrent.cas;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class CASAtomicityTest {

    private static int sum = 0;

    private static Unsafe unsafe;
    private static final long valueOffset;


    static {
        try {
//            Field thUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
//            thUnsafe.setAccessible(true);
//            unsafe = (Unsafe) thUnsafe.get(null);
            Constructor constructor = Unsafe.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            valueOffset = unsafe.staticFieldOffset
                    (CASAtomicityTest.class.getDeclaredField("sum"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    private static void atomicIncrement() {
        int oldValue, newValue;
        boolean success;
        do {
            oldValue = sum;
            newValue = oldValue + 1;
            success = unsafe.compareAndSwapInt(CASAtomicityTest.class, valueOffset, oldValue, newValue);
            if (!success) {
                System.out.println(success);
            }
        } while (!success);
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(System.getProperty("sun.boot.class.path"));
        Runnable runnable = () -> {
            for (int i = 0; i < 10000; i++) {
                atomicIncrement();
            }
        };
        int size = 10;
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
            threads[i] = thread;
        }
        for (int i = 0; i < size; i++) {
            threads[i].join();
        }
        System.out.println("sum = " + sum);
    }
}
