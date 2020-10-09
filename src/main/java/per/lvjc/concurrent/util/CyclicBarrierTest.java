package per.lvjc.concurrent.util;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierTest {

    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
        System.out.println("补魔...");
        System.out.println(Thread.currentThread().getName() + ": barrier");
    });

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("a begin");
            cyclicBarrier.reset();
            try {
                //cyclicBarrier.await(1, TimeUnit.SECONDS);
                int i = cyclicBarrier.await();
                System.out.println("a:" + i);
            } catch (InterruptedException e) {
                System.out.println("a interrupt");
            } catch (BrokenBarrierException e) {
                System.out.println("a broken");
            }
            System.out.println("a end");
        }, "a").start();
        new Thread(() -> {
            System.out.println("b begin");
            try {
                //TimeUnit.SECONDS.sleep(2);
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                System.out.println("b" + " interrupt");
            } catch (BrokenBarrierException e) {
                System.out.println("b" + " broken");
            }
            System.out.println("b end");
        }, "b").start();
    }
}
