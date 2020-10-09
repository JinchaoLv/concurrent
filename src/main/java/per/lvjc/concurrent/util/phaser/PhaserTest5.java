package per.lvjc.concurrent.util.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PhaserTest5 {

    private static Phaser phaser = new Phaser(3);

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("A begin");
            try {
                phaser.awaitAdvanceInterruptibly(phaser.arrive(), 2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            System.out.println("A end");
        }, "A").start();
        new Thread(() -> {
            System.out.println("B begin");
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phaser.arriveAndAwaitAdvance();
            System.out.println("B end");
        }, "B").start();
        new Thread(() -> {
            System.out.println("C begin");
            phaser.arriveAndAwaitAdvance();
            System.out.println("C end");
        }, "C").start();
    }
}
