package per.lvjc.concurrent.collection;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueTest {

    // DelayQueue 中的元素必须实现 Delayed 接口
    private static class Ele implements Delayed {

        public Ele(long value) {
            this.value = value;
            this.deadline = System.currentTimeMillis() + value;
        }

        private long value;
        private long deadline;

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(deadline - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        //Delayed 接口继承了 Comparable 接口，这个接口方法是从 Comparable 那里继承来的
        @Override
        public int compareTo(Delayed o) {
            // 正确的实现应该是 Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS))
            // 这里故意反过来比较
            return Long.compare(o.getDelay(TimeUnit.MILLISECONDS), this.getDelay(TimeUnit.MILLISECONDS));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DelayQueue<Ele> delayQueue = new DelayQueue<>();
        //放入第一个元素， 10 秒后过期
        delayQueue.offer(new Ele(10000));
        //放入第二个元素， 0.5 秒后过期
        delayQueue.offer(new Ele(500));
        //放入第三个元素， 0.02 后过期
        delayQueue.offer(new Ele(20));
        //取出一个元素，并不会取出 20 的那个，而是会阻塞 10 秒，然后取出 10000 的那个
        System.out.println(delayQueue.take().value);
    }
}