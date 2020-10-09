package per.lvjc.concurrent.util.phaser;

import java.util.concurrent.Phaser;

public class PhaserTest4 {

    private static Phaser rootPhaser = new Phaser(2);
    private static Phaser phaser1 = new Phaser(rootPhaser, 1);
    private static Phaser phaser2 = new Phaser(phaser1, 1);
    private static Phaser phaser3 = new Phaser(phaser2, 1);

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            System.out.println("root begin");
            System.out.println("root parties = " + rootPhaser.getRegisteredParties());
            System.out.println("root arrived parties = " + rootPhaser.getArrivedParties());
            rootPhaser.arriveAndAwaitAdvance();
            System.out.println("root end");
        }, "root").start();
        Thread.sleep(1000);
        new Thread(() -> {
            System.out.println("1 begin");
            System.out.println("phaser1 parties = " + phaser1.getRegisteredParties());
            System.out.println("phaser1 arrived parties = " + phaser1.getArrivedParties());
            phaser1.arriveAndAwaitAdvance();
            System.out.println("1 end");
        },"1").start();
        Thread.sleep(1000);
        new Thread(() -> {
            System.out.println("2 begin");
            System.out.println("phaser2 parties = " + phaser2.getRegisteredParties());
            System.out.println("phaser2 arrived parties = " + phaser2.getArrivedParties());
            phaser2.arriveAndAwaitAdvance();
            System.out.println("2 end");
        }, "2").start();
        Thread.sleep(1000);
        new Thread(() -> {
            System.out.println("3 begin");
            System.out.println("phaser3 parties = " + phaser3.getRegisteredParties());
            int arrive = phaser3.arrive();
            System.out.println("phaser3 arrived parties = " + phaser3.getArrivedParties());
            phaser3.awaitAdvance(arrive);
            System.out.println("3 end");
        }, "3").start();
        rootPhaser.arrive();
    }
}
