package per.lvjc.concurrent.pool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认线程工厂，基本照搬 {@link java.util.concurrent.Executors.DefaultThreadFactory}
 */
public class LvjcDefaultThreadFactory implements ThreadFactory {

    private AtomicInteger threadNum;
    private String threadNamePrefix;
    private ThreadGroup threadGroup;

    public LvjcDefaultThreadFactory() {
        this.threadGroup = Thread.currentThread().getThreadGroup();
        this.threadNamePrefix = "lvjc-pool-thread-";
        this.threadNum = new AtomicInteger(1);
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(threadGroup, r, threadNamePrefix + threadNum.getAndIncrement());
    }
}
