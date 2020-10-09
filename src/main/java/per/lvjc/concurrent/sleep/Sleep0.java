package per.lvjc.concurrent.sleep;

import static java.lang.Thread.sleep;
import static java.lang.Thread.yield;

public class Sleep0 {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("current time:" + System.currentTimeMillis());
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setPriority(1);
        thread.start();

    }
}
