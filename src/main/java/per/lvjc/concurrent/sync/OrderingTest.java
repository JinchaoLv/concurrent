package per.lvjc.concurrent.sync;

public class OrderingTest {

    private static int a = 0;
    private static int b = 0;
    private static int x = 0;
    private static int y = 0;


    public static void main(String[] args) {
        int i = 0;
        while (i < 10) {
            foo();
            i++;
        }
    }

    private static void foo() {
        a = 0; b = 0; x = 0; y = 0;
        Thread thread1 = new Thread(() -> {
            x = a;
            b = 1;
        });
        Thread thread2 = new Thread(() -> {
            y = b;
            a = 2;
        });
        thread1.start();
        thread2.start();
        System.out.println("x = " + x + ", y = " + y);
    }
}
