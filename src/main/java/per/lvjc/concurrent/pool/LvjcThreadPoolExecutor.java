package per.lvjc.concurrent.pool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LvjcThreadPoolExecutor implements ExecutorService {

    //初始状态为 running，工作线程数量为 0
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    public static final int RUNNING    = -1 << COUNT_BITS;
    public static final int SHUTDOWN   =  0 << COUNT_BITS;
    public static final int STOP       =  1 << COUNT_BITS;
    public static final int TERMINATED =  3 << COUNT_BITS;

    // Packing and unpacking ctl
    public static int runStateOf(int c)     { return c & ~CAPACITY; }
    public static int workerCountOf(int c)  { return c & CAPACITY; }
    public static int ctlOf(int rs, int wc) { return rs | wc; }

    private volatile int corePoolSize;
    private volatile int maximumPoolSize;
    private volatile long keepAliveTime;
    private final BlockingQueue<Runnable> workQueue;
    private final ThreadFactory threadFactory;
    private final LvjcRejectedExecutionHandler rejectedExecutionHandler;
    private final HashSet<Worker> workers;

    private ReentrantLock lock = new ReentrantLock();
    private Condition terminated = lock.newCondition();

    public LvjcThreadPoolExecutor(int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue,
                                  ThreadFactory threadFactory,
                                  LvjcRejectedExecutionHandler handler) {
        this.corePoolSize = corePoolSize > 0 ? corePoolSize : 1;
        this.maximumPoolSize = Math.max(maximumPoolSize, corePoolSize);
        this.keepAliveTime = keepAliveTime <= 0 || unit == null
                ? TimeUnit.SECONDS.toNanos(60)
                : unit.toNanos(keepAliveTime);
        this.workQueue = workQueue == null ? new LinkedBlockingQueue<>() : workQueue;
        this.threadFactory = threadFactory == null ? new LvjcDefaultThreadFactory() : threadFactory;
        this.rejectedExecutionHandler = handler == null ? new DefaultRejectExecutionHandler() : handler;
        this.workers = new HashSet<>();
    }

    @Override
    public void shutdown() {
        try {
            lock.lock();
            //1.状态改为 shutdown
            advanceState(SHUTDOWN);
            //2.打断所有空闲线程
            interruptIdleWorkers(false);
        } finally {
            lock.unlock();
        }
        //3.try terminate
        tryTerminate();
    }

    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> waitingTasks;
        try {
            lock.lock();
            //1.状态改为 stop
            advanceState(STOP);
            //2.打断所有线程
            interruptWorkers();
            //3.清空工作队列，返回工作队列中的任务
            waitingTasks = drainQueue();
        } finally {
            lock.unlock();
        }
        //4.try terminate
        tryTerminate();
        return waitingTasks;
    }

    private void advanceState(int targetState) {
        for (;;) {
            int c = ctl.get();
            int state = runStateOf(c);
            if (state >= targetState) {
                return;
            }
            int workerCount = workerCountOf(c);
            ctl.compareAndSet(c, ctlOf(targetState, workerCount));
        }
    }

    private List<Runnable> drainQueue() {
        BlockingQueue<Runnable> q = workQueue;
        ArrayList<Runnable> taskList = new ArrayList<Runnable>();
        q.drainTo(taskList);
        if (!q.isEmpty()) {
            for (Runnable r : q.toArray(new Runnable[0])) {
                if (q.remove(r))
                    taskList.add(r);
            }
        }
        return taskList;
    }

    /**
     * 要注意，只要不是 running 状态，都算 shutdown
     * @return
     */
    @Override
    public boolean isShutdown() {
        return runStateOf(ctl.get()) >= SHUTDOWN;
    }

    @Override
    public boolean isTerminated() {
        return runStateOf(ctl.get()) >= TERMINATED;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long waitNanos = unit.toNanos(timeout);
        try {
            lock.lock();
            for (;;) {
                if (isTerminated()) {
                    return true;
                }
                if (waitNanos <= 0) {
                    return false;
                }
                waitNanos = terminated.awaitNanos(waitNanos);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        RunnableFuture<T> futureTask = new FutureTask<>(task);
        execute(futureTask);
        return futureTask;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        RunnableFuture<T> futureTask = new FutureTask<>(task, result);
        execute(futureTask);
        return futureTask;
    }

    @Override
    public Future<?> submit(Runnable task) {
        RunnableFuture<?> futureTask = new FutureTask<>(task, null);
        execute(futureTask);
        return futureTask;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException();
        }
        for (;;) {
            int c = ctl.get();
            //1.如果已经 shutdown/stop/terminated，拒绝接受新任务
            if (runStateOf(c) >= SHUTDOWN) {
                rejectedExecutionHandler.rejectedExecution(command, this);
                return;
            }
            //2.如果小于 corePoolSize，创建核心线程执行任务
            int workerCount = workerCountOf(c);
            if (workerCount < corePoolSize) {
                try {
                    lock.lock();
                    if (compareAndIncrementWorkerCount(c)) {
                        new Worker(this, threadFactory, command).start();
                        return;
                    }
                    //cas failed
                    continue;
                } finally {
                    lock.unlock();
                }
            }
            //3.如果大于 corePoolSize，队列未满，扔进队列
            if (workQueue.offer(command)) {
                return;
            }
            //4.如果大于 corePoolSize，队列已满，创建备用线程执行任务
            try {
                lock.lock();
                new Worker(this, threadFactory, command).start();
            } finally {
                lock.unlock();
            }
            return;
        }
    }

    public boolean compareAndIncrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect + 1);
    }

    public boolean compareAndDecrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect - 1);
    }

    public void decrementWorkerCount() {
        do {} while (! compareAndDecrementWorkerCount(ctl.get()));
    }

    public HashSet<Worker> getWorkers() {
        return workers;
    }

    public int getCtl() {
        return ctl.get();
    }

    public BlockingQueue<Runnable> getWorkQueue() {
        return workQueue;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    /**
     * 检查是否要进入 terminated 状态，
     * 线程池进入 terminated 状态的情况：
     * 1.shutdownNow 方法被调用，使线程进入了 stop 状态，并且工作线程数量为 0；
     * 2.shutdown 方法被调用，使线程进入 shutdown 状态，并且工作线程数量为 0，工作队列里没有任务
     * ps：这两种情况实际上是同一种情况：工作线程数量为 0，工作队列没有任务，线程池没有在 running
     */
    public void tryTerminate() {
        int c = ctl.get();
        int state = runStateOf(c);
        //1.如果线程池还在 running，不可能 terminated
        if (state == RUNNING) {
            return;
        }
        //2.如果工作队列还有任务没处理，也不可能 terminated
        if (!workQueue.isEmpty()) {
            return;
        }
        //3.如果还有工作线程，把空闲线程从等待任务中打断
        if (workerCountOf(c) != 0) {
            interruptIdleWorkers(true);
            return;
        }
        //4.非 running，工作队列没有任务，没有工作线程，进入 terminated 状态
        try {
            lock.lock();
            ctl.set(ctlOf(TERMINATED, 0));
            //5.唤醒在等待 terminate 的线程
            terminated.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 打断空闲线程，会被 shutdown 和 tryTerminate 方法调用，
     * 其中，shutdown 方法需要打断所有空闲线程；
     * tryTerminate 方法只需要打断任意一个即可，
     * 因为 tryTerminate 打断空闲线程发生在线程池已经不工作并且工作队列已空的情况，
     * 这种情况打断一个线程后，被打断的线程会跳出循环，结束工作，
     * 然后再次触发 tryTerminate，再打断一个线程，被打断的线程结束，又触发 tryTerminate...
     * @param onlyOne
     */
    private void interruptIdleWorkers(boolean onlyOne) {
        try {
            lock.lock();
            for (Worker worker : workers) {
                if (worker.isIdle()) {
                    worker.interruptIfStarted();
                }
                if (onlyOne) {
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 打断所有线程，包括正在执行任务的线程，
     * 只会被 shutdownNow 调用
     */
    private void interruptWorkers() {
        try {
            lock.lock();
            for (Worker worker : workers) {
                worker.interruptIfStarted();
            }
        } finally {
            lock.unlock();
        }
    }
}
