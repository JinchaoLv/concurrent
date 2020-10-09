package per.lvjc.concurrent.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * @author lvjc
 * @date 2020/9/7
 */
public class LvjcSemaphore {

    /**
     * cas 操作需要 Unsafe，当然也可以用 JDK 封装好的 AtomicInteger
     */
    private static final long permitOffset;
    private static final Unsafe unsafe;
    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
            permitOffset = unsafe.objectFieldOffset(LvjcSemaphore.class.getDeclaredField("permits"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }

    /**
     * volatile 保证并发可见性
     */
    private volatile int permits;
    /**
     * 一个并发非阻塞容器来保存处于等待中的 Thread
     */
    private ConcurrentLinkedQueue<Thread> queue;

    public LvjcSemaphore(int permits) {
        this.permits = permits;
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void acquire() throws InterruptedException {
        //自旋
        for (;;) {
            int available = permits;
            // permit 数量不足
            if (available < 1) {
                // 当前线程进入等待队列
                queue.offer(Thread.currentThread());
                // 阻塞当前线程，
                // 不必担心其它线程已经先从队列取出当前 thread 进行了 unpark，导致下面的 park 无限阻塞，
                // 如果 unpark 发生在 park 之前，被 unpark 的线程（如果已经 start）下一次 park 不会阻塞
                // 但还是要在 park 之前再检查一次，以免在当前线程放入队列之前，其它线程已经 release，错过唤醒导致永远阻塞
                if (permits < 1) {
                    LockSupport.park();
                }
                // 醒来之后
                if (Thread.interrupted()) {
                    // 如果是被 interrupt 叫醒的，抛出 InterruptedException
                    throw new InterruptedException();
                }
            }
            // cas 修改 permit 变量 -1
            else if (casPermit(available, available - 1)) {
                // 修改成功，退出
                break;
            }
        }
    }

    public void acquireUninterruptibly() {
        for (;;) {
            int available = permits;
            if (available < 1) {
                queue.offer(Thread.currentThread());
                if (permits < 1) {
                    LockSupport.park();
                }
                if (Thread.interrupted()) {
                    //相比 acquire 方法，只在这里有区别，这里不抛异常，仅设置 interrupt 状态
                    Thread.currentThread().interrupt();
                }
            } else if (casPermit(available, available - 1)) {
                break;
            }
        }
    }

    public boolean tryAcquire() {
        for(;;) {
            int available = permits;
            if (available < 1) {
                return false;
            } else if (casPermit(available, available - 1)) {
                // cas 失败要重新进入循环，不能直接 return false
                return true;
            }
        }
    }

    public void release() {
        // 自旋
        for (;;) {
            int available = permits;
            // cas 修改 permit 数量 +1
            if (casPermit(available, available + 1)) {
                // 修改成功，因为只放入 1 个 permit，所以只叫醒 1 个线程
                Thread waitingThread = queue.poll();
                if (waitingThread != null) {
                    // 如果有线程正在等待，将其唤醒
                    LockSupport.unpark(waitingThread);
                }
                break;
            }
        }
    }

    private boolean casPermit(int expected, int permit) {
        return unsafe.compareAndSwapInt(this, permitOffset, expected, permit);
    }
}
