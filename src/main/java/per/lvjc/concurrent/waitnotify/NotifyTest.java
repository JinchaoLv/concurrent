package per.lvjc.concurrent.waitnotify;

import java.util.concurrent.TimeUnit;

public class NotifyTest {

    public static void main(String[] args) throws InterruptedException {
        Object o = new Object();
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 begin");
            synchronized (o) {
                while (true) {
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("awaken");
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 begin");
            synchronized (o) {
                o.notify();
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //thread1.start();
        TimeUnit.MILLISECONDS.sleep(10);
        thread2.start();
    }
}
