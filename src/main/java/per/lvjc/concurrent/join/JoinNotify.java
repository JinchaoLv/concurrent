package per.lvjc.concurrent.join;

public class JoinNotify {

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 begin:" + System.currentTimeMillis());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread1 end:" + System.currentTimeMillis());
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 begin:" + System.currentTimeMillis());
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread2 end:" + System.currentTimeMillis());
        });
        thread2.start();
        thread1.start();
        Thread.sleep(1000);
        thread2.interrupt();
        thread1.stop();
//        synchronized (thread1) {
//            System.out.println("main: try notify");
//            thread1.notifyAll();
//        }
    }
}
