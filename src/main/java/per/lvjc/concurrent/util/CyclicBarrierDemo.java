package per.lvjc.concurrent.util;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {

    private static final CyclicBarrier barrier = new CyclicBarrier(2, () -> {
        System.out.println("补魔...");
    });

    public static void main(String[] args) {
        Runnable runnable = () -> {
            try {
                String name = Thread.currentThread().getName();
                System.out.println(name + ": 准备到房间补魔...");
                System.out.println(name + ": 进入房间");
                barrier.await();
                System.out.println(name + ": 准备到客厅补魔...");
                System.out.println(name + ": 进入客厅");
                barrier.await();
                System.out.println(name + ": 准备到浴室补魔...");
                System.out.println(name + ": 进入浴室");
                barrier.await();
                System.out.println(name + ": 魔补满了真舒服");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };
        new Thread(runnable, "士郎").start();
        new Thread(runnable, "凛").start();
    }
}
