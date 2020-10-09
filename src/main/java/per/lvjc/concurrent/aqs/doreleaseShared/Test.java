package per.lvjc.concurrent.aqs.doreleaseShared;

import java.util.concurrent.locks.Lock;

public class Test {

    private static MyReentrantReadWriteLock readWriteLock = new MyReentrantReadWriteLock();
    private static Lock readLock = readWriteLock.readLock();
    private static Lock writeLock = readWriteLock.writeLock();

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            runReadAndLock("thread" + i);
        }
    }

    private static void runReadAndLock(String threadName) {
        new Thread(() -> {
            System.out.println("read " + threadName + " begin");
            readLock.lock();
            System.out.println("read " + threadName + " acquired lock");
        }).start();
    }
}
