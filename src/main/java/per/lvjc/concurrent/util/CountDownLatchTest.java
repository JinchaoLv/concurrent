package per.lvjc.concurrent.util;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class CountDownLatchTest {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("begin");
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("end");
        }).start();
        new Thread(() -> {
            try {
                Field sync = CountDownLatch.class.getDeclaredField("sync");
                sync.setAccessible(true);
                AbstractQueuedSynchronizer synchronizer = (AbstractQueuedSynchronizer) sync.get(countDownLatch);
                Field state = synchronizer.getClass().getSuperclass().getDeclaredField("state");
                state.setAccessible(true);
                state.set(synchronizer, 5);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            System.out.println(countDownLatch.getCount());
            countDownLatch.countDown();
        }).start();
    }
}
