package per.lvjc.concurrent.threadlocal;

import java.lang.reflect.Field;

public class ThreadLocalTest {

    private static class IntThreadLocal extends ThreadLocal<Integer> {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    }

    private static final ThreadLocal<Integer> t = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    private static final ThreadLocal<Integer> th = ThreadLocal.withInitial(() -> 0);

    private static final IntThreadLocal threadLocal = new IntThreadLocal();

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Runnable runnable = () -> {
            int i = 0;
            while (i++ < 10000) {
                int value = th.get();
                value = value + 1;
                th.set(value);
            }
            System.out.println(Thread.currentThread().getName() + ":" + th.get());
        };
        //new Thread(runnable, "thread1").start();
        //new Thread(runnable, "thread2").start();
        System.out.println(threadLocal.get());
        threadLocal.set(null);
        System.out.println(threadLocal.get());
        clearThreadLocal();

    }

    private static void clearThreadLocal() throws NoSuchFieldException, IllegalAccessException {
        Thread currentThread = Thread.currentThread();
        Field threadLocalMap = Thread.class.getDeclaredField("threadLocals");
        threadLocalMap.setAccessible(true);
        threadLocalMap.set(currentThread, null);
    }
}
