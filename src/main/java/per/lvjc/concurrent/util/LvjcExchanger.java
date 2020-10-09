package per.lvjc.concurrent.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LvjcExchanger<V> {

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private Object data;
    private Thread thread;

    public V exchange(V value) throws InterruptedException {
        try {
            lock.lockInterruptibly();
            Thread currentThread = Thread.currentThread();
            // 其它线程还没有提交数据
            while (thread == null || thread == currentThread) {
                data = value;
                thread = currentThread;
                condition.await();
                if (thread != currentThread) {
                    Object o = data;
                    data = null;
                    thread = null;
                    return (V) o;
                }
            }
            Object o = data;
            data = value;
            thread = currentThread;
            condition.signal();
            return (V) o;
        } finally {
            lock.unlock();
        }
    }

    public V exchange(V value, long i, TimeUnit unit) throws InterruptedException, TimeoutException {
        long remaining = TimeUnit.MILLISECONDS.convert(i, unit);
        long deadline = System.currentTimeMillis() + remaining;
        try {
            lock.lockInterruptibly();
            Thread currentThread = Thread.currentThread();
            while (thread == null || thread == currentThread) {
                data = value;
                thread = currentThread;
                condition.await(remaining, TimeUnit.MILLISECONDS);
                if (thread != currentThread) {
                    Object o = data;
                    data = null;
                    thread = null;
                    return (V) o;
                } else {
                    remaining = deadline - System.currentTimeMillis();
                    if (remaining <= 0) {
                        throw new TimeoutException();
                    }
                }
            }
            Object o = data;
            data = value;
            thread = currentThread;
            condition.signal();
            return (V) o;
        } finally {
            lock.unlock();
        }
    }
}
