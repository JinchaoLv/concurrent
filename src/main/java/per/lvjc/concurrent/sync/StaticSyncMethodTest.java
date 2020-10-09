package per.lvjc.concurrent.sync;

public class StaticSyncMethodTest {

    private static int count;

    public static synchronized void syncIncrease() {
        StaticSyncMethodTest.count++;
    }

    public static void increase() {
        StaticSyncMethodTest.count++;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            int i = 0;
            while (i++ < 10000) {
                StaticSyncMethodTest.syncIncrease();
            }
        });
        Thread thread2 = new Thread(() -> {
            int i = 0;
            while (i++ < 10000) {
                synchronized (StaticSyncMethodTest.class) {
                    StaticSyncMethodTest.increase();
                }
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(StaticSyncMethodTest.count);
    }
}
