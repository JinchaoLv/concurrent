package per.lvjc.concurrent.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class LvjcCountDownLatch {

    /**
     * 不使用 Unsafe 了，直接用 JDK 封装好的 Atomic 类
     */
    private AtomicInteger atomicInteger;
    private ConcurrentLinkedQueue<Thread> queue;

    public LvjcCountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.atomicInteger = new AtomicInteger(count);
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void await() throws InterruptedException {
        while (atomicInteger.get() != 0) {
            queue.offer(Thread.currentThread());
            if (atomicInteger.get() != 0) {
                LockSupport.park();
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        }
    }

    public void countDown() {
        if (atomicInteger.get() == 0) {
            return;
        }
        if (atomicInteger.decrementAndGet() == 0) {
            Thread thread;
            while ((thread = queue.poll()) != null) {
                LockSupport.unpark(thread);
            }
        }
    }
}
