package per.lvjc.concurrent.interrupt;

public class InterruptTest {

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 begin");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread1 end");
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 begin");
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread2 end");
        });
        thread1.start();
        thread2.start();
        Thread.sleep(100);
        thread2.interrupt();
    }
}
