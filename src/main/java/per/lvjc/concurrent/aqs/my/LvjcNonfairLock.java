package per.lvjc.concurrent.aqs.my;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 不可重入非公平锁
 */
public class LvjcNonfairLock implements Lock {

    private final Sync sync;

    public LvjcNonfairLock() {
        sync = new Sync();
    }

    @Override
    public void lock() {
        //sync.acquire(1);
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unlock() {
        //sync.release(1);
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return sync.new ConditionObject();
    }

    /**
     * 实现自己的同步器
     */
    static final class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int arg) {
            int state = getState();
            //state 初始值为 0，不等于 0 表示锁已经被某线程持有
            if (state != 0) {
                //不可重入，直接返回获取锁失败，当前线程会进入阻塞
                return false;
            }
            //state == 0，锁没有被持有，抢锁
            if (compareAndSetState(0, 1)) {
                //设置锁被当前线程持有，释放锁时会校验
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            //没抢到锁，还是返回 false
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                //锁没有被当前线程持有，报错
                throw new IllegalMonitorStateException();
            }
            //锁被当前线程持有，直接释放；
            //不需要 cas，因为独占锁这里不存在并发
            setState(0);
            //不可重入锁，不用计数，直接返回 true，让其它线程可以抢锁
            return true;
        }

        @Override
        protected int tryAcquireShared(int arg) {
            return 1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            return Thread.currentThread() == getExclusiveOwnerThread();
        }
    }

    private static final LvjcNonfairLock lock = new LvjcNonfairLock();

    public static void main(String[] args) throws InterruptedException {
        Condition condition = lock.newCondition();
        condition.await();


        TimeUnit.MILLISECONDS.sleep(10);
        Runnable runnable = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " begin");
            try {
                lock.lock();
                System.out.println(threadName + " acquired lock");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(threadName + " will unlock");
                lock.unlock();
            }
        };
        for (int i = 0; i < 5; i++) {
            new Thread(runnable, "thread-" + i).start();
        }
    }
}
