package per.lvjc.concurrent.util;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    //小区门口一共 2 辆共享单车
    //private static Semaphore bicycles = new Semaphore(2);
    private static LvjcSemaphore bicycles = new LvjcSemaphore(2);

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            String name = Thread.currentThread().getName();
            System.out.println(name + ": 社畜的一天开始了");
            sleepMoment();
            boolean findBicycle = bicycles.tryAcquire();
            if (findBicycle) {
                System.out.println(name + ": 竟然还有一辆共享单车，骑车上班");
                sleepMoment();
                System.out.println(name + ": 下班还车");
                bicycles.release();
            } else {
                System.out.println(name + ": 没车了，走路上班吧");
            }
            System.out.println(name + ": 社畜的一天结束了");
        };
        for (int i = 1; i <= 6; i++) {
            System.out.println("------ 星期 " + i + " ------");
            new Thread(runnable, "Jack").start();
            new Thread(runnable, "Rose").start();
            new Thread(runnable, "Nobody").start();
            Thread.sleep(500);
        }
    }

    private static void sleepMoment() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
