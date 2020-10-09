package per.lvjc.concurrent.stop;

public class CanNotStopThread extends Thread {

    private static final Object monitor = new Object();

    private boolean success = false;

    int sum = 0;

    @Override
    public void run() {
        synchronized (monitor) {
            while (!success) {
                try {
                    rollback();
                    work();
                    System.out.println("sum = " + sum);
                    success = true;
                } catch (ThreadDeath threadDeath) {
                    //do nothing
                    try {
                        System.out.println("catch");
                    } catch (ThreadDeath e) {

                    }
                } finally {
                    try {
                        System.out.println("finally");
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (ThreadDeath threadDeath) {

                    }
                }
            }
        }
    }

    private void rollback() {
        System.out.println("rollback");
        this.sum = 0;
    }

    /* 累加 1~100 */
    private void work() {
        int i = 1;
        while (i < 101) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                System.out.println("What the fuck?");
            }
            System.out.println("i = " + i);
            sum += i++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CanNotStopThread canNotStopThread = new CanNotStopThread();
        Thread thread = new Thread(() -> {
           synchronized (monitor) {
               System.out.println("acquired lock");
           }
        });
        canNotStopThread.start();
        sleep(1000);
        thread.start();
        for (int i = 0; i < 100; i++) {
            canNotStopThread.stop();
            sleep(100);
        }
    }
}
