package per.lvjc.concurrent.collection;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class LinkedTransferQueueTest {

    private static LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();

    public static void main(String[] args) throws InterruptedException {
        Thread take = new Thread(() -> {
            System.out.println("take thread begin");
            try {
                System.out.println(queue.take());
                System.out.println(queue.take());
                System.out.println(queue.take());
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("take thread end");
        }, "take");
        Thread put = new Thread(() -> {
            System.out.println("put thread begin");
            try {
                queue.put("put1");
                queue.put("put2");
                queue.put("put3");
                queue.transfer("transfer");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("put thread end");
        }, "put");
        put.start();
        TimeUnit.SECONDS.sleep(2);
        take.start();
    }
}
