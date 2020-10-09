package per.lvjc.concurrent.waitnotify;

import java.util.concurrent.TimeUnit;

public class ThreadTerminatedTest {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("thread begin");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread end");
        });
        thread.start();
        synchronized (thread) {
            thread.wait();
        }
        System.out.println("main end");
    }
}
