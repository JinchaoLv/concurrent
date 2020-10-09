package per.lvjc.concurrent.util;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExchangerDemo {

    private static LvjcExchanger<String> exchanger = new LvjcExchanger<>();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + ": 发起交易，等待对方同意");
            try {
                // 这里仅仅用来阻塞，无所谓对方给什么数据，等待对方调用 exchange 方法
                exchanger.exchange("");
            } catch (InterruptedException e) {
                System.out.println(threadName + ": 交易失败");
                return;
            }
            // 对方也调用 exchange 方法提交数据之后，当前线程就会被唤醒走到这里
            System.out.println(threadName + ": 对方已同意，交易开始");
            String goods = null;
            try {
                //自己提交了 “屠龙宝刀 × 1”， goods 为对方提交的数据
                goods = exchanger.exchange("屠龙宝刀 * 1", 30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                // 打断走这里
                System.out.println(threadName + ": 交易失败");
            } catch (TimeoutException e) {
                // 超时走这里
                System.out.println(threadName + ": 交易超时，已取消");
            }
            System.out.println(threadName + ": 交易成功，获得 " + goods);
        }, "伊莉雅").start();
        Thread.sleep(1000);
        new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + ": 收到交易申请");
            try {
                // exchange 方法可以提交 null，对方也会得到 null
                exchanger.exchange(null);
            } catch (InterruptedException e) {
                System.out.println(threadName + ": 交易失败");
                return;
            }
            System.out.println(threadName + ": 已接受申请，交易开始");
            String goods = null;
            try {
                goods = exchanger.exchange("2000000 金币", 30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.out.println(threadName + ": 交易失败");
            } catch (TimeoutException e) {
                System.out.println(threadName + ": 交易超时，已取消");
            }
            System.out.println(threadName + ": 交易成功，获得 " + goods);
        }, "士郎").start();
    }
}
