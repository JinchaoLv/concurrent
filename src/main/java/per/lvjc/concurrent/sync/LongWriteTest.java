package per.lvjc.concurrent.sync;

public class LongWriteTest {

    private static long value = 0;

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            while (true) {
                value = 1;
            }
        });
        Thread thread2 = new Thread(() -> {
            while (true) {
                value = 2;
            }
        });
        thread1.start();
        thread2.start();
        while (true) {
            long wrongValue = value;
            if (wrongValue != 1 && wrongValue != 2) {
                System.out.println("value = " + value);
                System.out.println(System.currentTimeMillis() + wrongValue);
                break;
            }
        }
    }
}
