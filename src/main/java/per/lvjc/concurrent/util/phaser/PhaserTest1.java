package per.lvjc.concurrent.util.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class PhaserTest1 {

    private static final Phaser phaser = new Phaser(1);

    private static class LRunnable implements Runnable {

        private long time;
        public LRunnable(long time) {
            this.time = time;
        }

        @Override
        public void run() {
            try {
                String name = Thread.currentThread().getName();
                System.out.println(name + " : phase1 begin");
                if (time > 0) {
                    TimeUnit.SECONDS.sleep(time);
                }
                System.out.println(phaser.getArrivedParties());
                System.out.println(name + ": arrive, " + phaser.arriveAndAwaitAdvance());
                System.out.println(name + ": phase: " + phaser.getPhase());
                System.out.println(phaser.getArrivedParties());
                System.out.println(name + ": phase1 end");
                System.out.println("---------------------------");

                System.out.println(name + " : phase2 begin");
                if (time > 0) {
                    TimeUnit.SECONDS.sleep(time);
                }
                System.out.println(name + ": phase:" + phaser.getPhase());
                System.out.println(name + ": reg parties:" + phaser.getRegisteredParties());
                System.out.println(name + ": arrived parties: " + phaser.getArrivedParties());
                System.out.println(name + ": arrive, " + phaser.arrive());
                System.out.println(name + ": phase: " + phaser.getPhase());
                System.out.println(name + ": reg parties:" + phaser.getRegisteredParties());
                System.out.println(name + ": arrived parties: " + phaser.getArrivedParties());
                System.out.println(name + ": phase2 end");
                System.out.println("-----------------");
                System.out.println(phaser.arrive());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //new Thread(new LRunnable(0), "a").start();
        //new Thread(new LRunnable(1), "b").start();
        new Thread(() -> {
            int size = 10;
            for (int i = 0; i < size; i++) {
                System.out.println("------------- phase" + i + "-----------");
                System.out.println("arrive:" + phaser.arrive());
                System.out.println("phase:" + phaser.getPhase());
                System.out.println("parties:" + phaser.getRegisteredParties());
                System.out.println("arrived:" + phaser.getArrivedParties());
                System.out.println("unarrived:" + phaser.getUnarrivedParties());
            }
        }).start();
    }
}
