package per.lvjc.concurrent.sync;

import java.util.concurrent.TimeUnit;

public class VisibilityTest {

    private static int i = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread write = new Thread(() -> {
            while (i < 5) {
                i++;
                System.out.println("write: i =" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread read = new Thread(() -> {
            int localI;
            while ((localI = i) < 5) {
                if (localI != 0) {
                    System.out.println("read: i = " + i);
                }
            }
        });
        read.start();
        TimeUnit.MILLISECONDS.sleep(100);
        write.start();
    }
}