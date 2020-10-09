package per.lvjc.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest2 {

    private static Lock lock = new ReentrantLock();

    private static int i = 0;

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                lock.lock();
                i = 5;
            } finally {
                lock.unlock();
            }
        }, "thread-write").start();
        new Thread(() -> {
            try {
                lock.lock();
                int v = i;
            } finally {
                lock.unlock();
            }
        }, "thread-read").start();
    }
}
