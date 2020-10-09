package per.lvjc.concurrent.threadlocal;

import java.lang.reflect.Field;

public class ClearThreadLocalTest {

    private static final ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        System.out.println(threadLocal.get());
        threadLocal.set(null);
        System.out.println(threadLocal.get());
        clearThreadLocal();
        System.out.println(threadLocal.get());
    }

    private static void clearThreadLocal() throws NoSuchFieldException, IllegalAccessException {
        Thread currentThread = Thread.currentThread();
        Field threadLocalMap = Thread.class.getDeclaredField("threadLocals");
        threadLocalMap.setAccessible(true);
        threadLocalMap.set(currentThread, null);
    }
}
