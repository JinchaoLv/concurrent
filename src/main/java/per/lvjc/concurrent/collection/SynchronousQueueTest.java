package per.lvjc.concurrent.collection;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class SynchronousQueueTest {

    public static void main(String[] args) throws InterruptedException {
        //SynchronousQueue<String> queue = new SynchronousQueue<>();
        SynchronousQueue<String> queue = new SynchronousQueue<>(true);

        for (int i = 0; i < 5; i++) {
            // new 5 个线程去 take，会阻塞直到有线程 put
            new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " begin...");
                try {
                    System.out.println(threadName + " take: " + queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(threadName + " end...");
            }, "thread-" + i).start();
            TimeUnit.MILLISECONDS.sleep(200);
        }

        // new 一个线程 put 5 个元素，take 的线程会被依次唤醒
        new Thread(() -> {
            System.out.println("put thread begin");
            for (int i = 0; i < 5; i++) {
                try {
                    queue.put("element" + i);
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("put thread end");
        }).start();
    }
}
