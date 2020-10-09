package per.lvjc.concurrent.sync;

public class NonStaticSyncMethodTest {

    private int count = 0;

    public synchronized void syncIncrease() {
        this.count++;
    }

    public void increase() {
        this.count++;
    }

    public static void main(String[] args) throws InterruptedException {
        NonStaticSyncMethodTest test = new NonStaticSyncMethodTest();
        Thread thread1 = new Thread(() -> {
            int i = 0;
            while (i++ < 10000) {
                test.syncIncrease();
            }
        });
        Thread thread2 = new Thread(() -> {
            int i = 0;
            while (i++ < 10000) {
                synchronized (test) {
                    test.increase();
                }
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(test.count);
    }
}
