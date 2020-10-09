package per.lvjc.concurrent.aqs;

import per.lvjc.concurrent.aqs.my.LvjcNonfairLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

    private static Lock lock = new LvjcNonfairLock();
    private static Condition condition1 = lock.newCondition();
    private static Condition condition2 = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            try {
                lock.lock();
                System.out.println("thread1 acquired lock");
                System.out.println("thread1 await on condition1");
                try {
                    condition1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread1 wake up");
            } finally {
                System.out.println("thread1 will release lock");
                lock.unlock();
            }
        }).start();
        new Thread(() -> {
            try {
                lock.lock();
                System.out.println("thread2 acquired lock");
                System.out.println("thread2 await on condition2");
                try {
                    condition2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread2 wake up");
            } finally {
                System.out.println("thread2 will release lock");
                lock.unlock();
            }
        }).start();
        Thread.sleep(10);
        new Thread(() -> {
            try {
                lock.lock();
                System.out.println("thread3 acquired lock");
                System.out.println("thread3 will signal condition1");
                condition1.signal();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("thread3 will release lock");
                lock.unlock();
            }
            try {
                lock.lock();
                System.out.println("thread3 acquired lock");
                System.out.println("thread3 will signal condition2");
                condition2.signal();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("thread3 will release lock");
                lock.unlock();
            }
        }).start();
    }
}
