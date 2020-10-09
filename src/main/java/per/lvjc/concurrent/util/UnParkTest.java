package per.lvjc.concurrent.util;

import java.util.concurrent.locks.LockSupport;

public class UnParkTest {

    public static void main(String[] args) throws InterruptedException {
        Thread park1 = new Thread(() -> {
            System.out.println("park1 begin");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LockSupport.park();
            System.out.println("park1 end");
        });
        Thread park2 = new Thread(() -> {
            System.out.println("park2 begin");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LockSupport.park();
            System.out.println("park2 end");
        });
        Thread unpark = new Thread(() -> {
            System.out.println("unpark begin");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LockSupport.unpark(park1);
            System.out.println("unpark end");
        });
        unpark.start();
        park1.start();
        park2.start();

    }
}
