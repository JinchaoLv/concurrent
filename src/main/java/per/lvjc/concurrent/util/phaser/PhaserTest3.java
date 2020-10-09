package per.lvjc.concurrent.util.phaser;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Phaser;

public class PhaserTest3 {

    private static Phaser rootPhaser = new Phaser(1);
    private static Phaser childPhaser1 = new Phaser(3);
    private static Phaser childPhaser2 = new Phaser(rootPhaser, 3);

    private static class ArriveRunnable implements Runnable {

        private String name;
        private Phaser phaser;

        public ArriveRunnable(Phaser phaser, String name) {
            this.name = name;
            this.phaser = phaser;
        }

        @Override
        public void run() {
            System.out.println(name + " ready");
            players.add(name);
            phaser.arriveAndAwaitAdvance();
            System.out.println(name + ": " +phaser.getArrivedParties());
        }
    }

    static volatile String player1 = null;
    static volatile String player2 = null;
    static ConcurrentLinkedQueue<String> players = new ConcurrentLinkedQueue<>();


    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            rootPhaser.arriveAndAwaitAdvance();
            System.out.println("总决赛开始...");
            String winner = player1.compareTo(player2) > 0 ? player1 : player2;
            System.out.println("总决赛结束，冠军：" + winner);
        }).start();

        new Thread(() -> {
            System.out.println("淘汰赛1");
            childPhaser1.arriveAndAwaitAdvance();
            String p1 = players.poll();
            String p2 = players.poll();
            System.out.println("淘汰赛1：" + p1 + " Vs " + p2);
            String winner = p1.compareTo(p2) > 0 ? p1 : p2;
            player1 = winner;
            System.out.println("淘汰赛1，胜者：" + winner);
        }).start();

        new Thread(() -> {
            childPhaser2.arriveAndAwaitAdvance();
            String p3 = players.poll();
            String p4 = players.poll();
            System.out.println("淘汰赛2：" + p3 + " Vs " + p4);
            String winner = p3.compareTo(p4) > 0 ? p3 : p4;
            player2 = winner;
            System.out.println("淘汰赛2，胜者：" + winner);
        }).start();

        new Thread(new ArriveRunnable(childPhaser1, "Jack")).start();
        new Thread(new ArriveRunnable(childPhaser1, "Rose")).start();
        Thread.sleep(10000000000L);
        new Thread(new ArriveRunnable(childPhaser2, "Bob")).start();
        new Thread(new ArriveRunnable(childPhaser2, "Amy")).start();
    }
}
