package per.lvjc.concurrent.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchDemo {

    private static LvjcCountDownLatch countDownLatch = new LvjcCountDownLatch(2);

    private static int price = 70;
    private static int money = 10000;

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("小明： 当前股价： " + price + " 元");
            System.out.println("小明： 可用金额： " + money + " 元");
            //可以换成 if， 因为 CountDownLatch 不能重用，只能阻塞一次
            while (price * 1000 > money) {
                try {
                    //阻塞等待条件达成
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("小明： 知道啦，买了 10 手");
        }).start();

        new Thread(() -> {
            System.out.println("小红： 股价跌了，小明你买了吗？");
            try {
                //阻塞在同一个 CountDownLatch 上面
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("小红： " + price + " 啦");
            System.out.println("小红： 哦，买了呀，小明好厉害！");
        }).start();

        new Thread(() -> {
            System.out.println("股价变动...");
            while (price > 65) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                    System.out.println("股价： " + price);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                --price;
            }
            //条件 1 达成
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            System.out.println("银证转账中...");
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            money += 60000;
            System.out.println("证券账户转入 60000 元");
            //条件 2 达成
            countDownLatch.countDown();
        }).start();
    }
}