package per.lvjc.concurrent.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 表示线程池里的一个工作线程
 */
public class Worker implements Runnable {

    private final LvjcThreadPoolExecutor executor;

    private final Thread thread;
    private Runnable firstTask;
    //线程状态：是否空闲（没有在处理任务）
    private volatile boolean idle = true;

    public Worker(LvjcThreadPoolExecutor executor, ThreadFactory threadFactory, Runnable firstTask) {
        this.executor = executor;
        this.thread = threadFactory.newThread(this);
        this.firstTask = firstTask;
        this.executor.getWorkers().add(this);
    }

    public void start() {
        if (thread == null || thread.isAlive()) {
            throw new IllegalThreadStateException();
        }
        thread.start();
    }

    public boolean isIdle() {
        return idle;
    }

    public void interruptIfStarted() {
        if (thread.isAlive() && !thread.isInterrupted()) {
            thread.interrupt();
        }
    }


    @Override
    public void run() {
        try {
            Runnable task = firstTask;
            firstTask = null;
            while (task != null || (task = getTaskFromWorkQueue()) != null) {
                //1.当前线程置为非空闲状态
                idle = false;
                //2.重置 interrupt 状态：
                //如果线程池已经 stop，则当前工作线程应该 interrupt，这样可以不用重复 interrupt；
                //如果线程池没有 stop，则当前工作线程必须没有 interrupt，否则可能导致工作线程不能正确响应 interrupt
                if (LvjcThreadPoolExecutor.runStateOf(executor.getCtl()) >= LvjcThreadPoolExecutor.STOP) {
                    if (!thread.isInterrupted()) {
                        thread.interrupt();
                    }
                } else {
                    Thread.interrupted();
                }
                //3.执行工作任务
                try {
                    task.run();
                } finally {
                    task = null;
                }
                //4.当前线程置为空闲状态
                idle = true;
            }
        } finally {
            //5.线程结束前的收尾工作
            //5.1.清除当前工作线程
            executor.getWorkers().remove(this);
            //5.2.工作线程数量 -1
            executor.decrementWorkerCount();
            //5.3.尝试使线程池进入 terminated 状态
            //因为 terminated 状态是线程池根据自身运行状况自动进入的，
            //所以需要有一些地方来触发线程池检查自身运行状况，看是否需要进入 terminated 状态，
            //这里是其中一个触发点
            executor.tryTerminate();
        }
    }

    private Runnable getTaskFromWorkQueue() {
        BlockingQueue<Runnable> workQueue = executor.getWorkQueue();
        int corePoolSize = executor.getCorePoolSize();
        int maxPoolSize = executor.getMaximumPoolSize();
        long keepAliveTime = executor.getKeepAliveTime();
        for (;;) {
            int ctl = executor.getCtl();
            int state = LvjcThreadPoolExecutor.runStateOf(ctl);
            //1.如果线程池已经 stop，return null，让当前线程结束
            if (state == LvjcThreadPoolExecutor.STOP) {
                return null;
            }
            //2.如果线程池 shutdown，并且工作队列已空，return null，让当前线程结束
            if (state == LvjcThreadPoolExecutor.SHUTDOWN && workQueue.isEmpty()) {
                return null;
            }
            int workerCount = LvjcThreadPoolExecutor.workerCountOf(ctl);
            //3.如果当前线程数超过最大线程数，return null 让当前线程结束
            if (workerCount > maxPoolSize) {
                return null;
            }
            //4.从工作队列获取任务，如果被 interrupt，可能是因为 shutdown，要继续循环从头开始判断线程池状态
            try {
                Runnable runnable;
                //4.1.如果当前工作线程数量大于核心线程数，等待一定时间拿不到任务，要让当前线程结束，使线程数逐渐缩减到核心线程数
                if (workerCount > corePoolSize) {
                    runnable = workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS);
                }
                //4.2.如果当前工作线程数量不大于核心线程数，持续等待任务，让核心线程保持存活
                else {
                    runnable = workQueue.take();
                }
                return runnable;
            } catch (InterruptedException e) {
                //吃掉打断异常，什么都不用做，重新循环即可
            }
        }
    }
}
