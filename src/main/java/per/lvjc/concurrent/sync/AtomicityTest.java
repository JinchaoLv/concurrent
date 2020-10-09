package per.lvjc.concurrent.sync;

public class AtomicityTest {

    private static int sum = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            for (int i = 0; i < 10000; i++) {
                sum++;
            }
        };
        int size = 10;
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
            threads[i] = thread;
        }
        for (int i = 0; i < size; i++) {
            threads[i].join();
        }
        System.out.println("sum = " + sum);
    }
}
