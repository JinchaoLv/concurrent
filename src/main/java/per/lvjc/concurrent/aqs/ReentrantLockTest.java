package per.lvjc.concurrent.aqs;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            System.out.println("thread2 begin");
            try {
                lock.lock();
                System.out.println("thread2 lock acquired");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("thread2 lock released");
                lock.unlock();
            }
            System.out.println("thread2 end");
        }).start();
        new Thread(() -> {
            System.out.println("thread1 begin");
            try {
                lock.lock();
                System.out.println("thread1 lock acquired");
            } finally {
                lock.unlock();
            }
            System.out.println("thread1 end");
        }).start();
    }
}
