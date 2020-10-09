package per.lvjc.concurrent.util.phaser;

import java.util.concurrent.Phaser;

public class PhaserTest2 {

    private static Phaser phaser = new Phaser(2) {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            System.out.println(Thread.currentThread().getName() + ":" + phase + "补魔...");
            //return super.onAdvance(phase, registeredParties);
            throw new RuntimeException("xxx");
        }
    };

    public static void main(String[] args) {
        Runnable runnable = () -> {
            String name = Thread.currentThread().getName();
            System.out.println(name + ": 准备到房间补魔...");
            System.out.println(name + ": 进入房间");
            phaser.arriveAndAwaitAdvance();
            System.out.println(name + ": 准备到客厅补魔...");
            System.out.println(name + ": 进入客厅");
            phaser.arriveAndAwaitAdvance();
            System.out.println(name + ": 准备到浴室补魔...");
            System.out.println(name + ": 进入浴室");
            phaser.arriveAndAwaitAdvance();
            System.out.println(name + ": 魔补满了真舒服");
        };
        new Thread(runnable, "士郎").start();
        new Thread(runnable, "凛").start();
    }
}
