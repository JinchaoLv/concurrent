package per.lvjc.concurrent.stop;

public class StopTest {

    public static void main(String[] args) throws InterruptedException {
//        Thread thread = new Thread(() -> {
//            try {
//                System.out.println("begin： " + System.currentTimeMillis());
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("end:" + System.currentTimeMillis());
//            } catch (ThreadDeath threadDeath) {
//                System.out.println("catch end:" + System.currentTimeMillis());
//                throw threadDeath;
//            }
//        });
        Thread thread = new Thread(() -> {
            System.out.println("begin");
            Thread.currentThread().suspend();
            System.out.println("end");
        });
        thread.start();
        Thread.sleep(1000);
        thread.resume();
        Thread.sleep(1000);
        thread.stop();
    }
}
