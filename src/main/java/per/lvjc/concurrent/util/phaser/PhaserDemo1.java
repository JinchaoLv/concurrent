package per.lvjc.concurrent.util.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class PhaserDemo1 {

    private static Phaser phaser = new Phaser(3);

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("A begin");
            phaser.register();
            phaser.arriveAndAwaitAdvance();
            System.out.println("A end");
        }, "A").start();

        Runnable runnable = () -> {
            String name = Thread.currentThread().getName();
            System.out.println(name + " begin...");
            randomSleep();
            System.out.println(name + " end...");
            phaser.arrive();
        };

        new Thread(runnable, "C").start();

        new Thread(() -> {
            System.out.println("B begin");
            phaser.register();
            phaser.arriveAndAwaitAdvance();
            System.out.println("B end");
        }, "B").start();

        new Thread(runnable, "D").start();
        new Thread(runnable, "E").start();
    }

    private static void randomSleep() {
        Random random = new Random(System.currentTimeMillis());
        try {
            TimeUnit.MILLISECONDS.sleep(random.nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
