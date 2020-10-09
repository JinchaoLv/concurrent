package per.lvjc.concurrent.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantRWLockTest {

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static Lock readLock = readWriteLock.readLock();
    private static Lock writeLock = readWriteLock.writeLock();

    public static void main(String[] args) throws InterruptedException {
        runReadThreadAndLock("thread-read-1");
        Thread.sleep(100);
        runWriteThreadAndLock("thread-write-1");
        Thread.sleep(100);
        runReadThreadAndLock("thread-read-2");
    }

    private static void runWriteThreadAndLock(String threadName) {
        new Thread(() -> {
            System.out.println(threadName + " begin");
            writeLock.lock();
            System.out.println(threadName + " acquired write lock");
        }, threadName).start();
    }

    private static void runReadThreadAndLock(String threadName) {
        new Thread(() -> {
            System.out.println(threadName + " begin");
            readLock.lock();
            System.out.println(threadName + " acquired read lock");
        }, threadName).start();
    }
}
