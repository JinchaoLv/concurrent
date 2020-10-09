package per.lvjc.concurrent.interrupt;

public class IsInterruptedTest {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
                System.currentTimeMillis();
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("......");
                }
            }
        });
        thread.start();
        System.out.println(thread.isInterrupted());
        thread.interrupt();
        System.out.println(thread.isInterrupted());
    }
}
