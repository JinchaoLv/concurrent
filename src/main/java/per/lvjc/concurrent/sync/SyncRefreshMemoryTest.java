package per.lvjc.concurrent.sync;

public class SyncRefreshMemoryTest {

    private static int x = 0;
    private static int y = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread write = new Thread(() -> {
            x = 1;
            y = 1;
            while (true) {
                System.currentTimeMillis();
            }
        });
        Thread read = new Thread(() -> {
            while (true) {
                if (x == 1 && y ==1) {
                    break;
                }
                Thread.yield();
            }
            System.out.println("read exit");
        });
        read.start();
        Thread.sleep(100);
        write.start();
    }
}
