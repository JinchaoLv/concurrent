package per.lvjc.concurrent.daemon;

public class DaemonThread extends Thread {

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName());
            System.out.println("begin");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            System.out.println("finally begin");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("finally end");
        }
        System.out.println(Thread.currentThread().getName());
    }

    public static void main(String[] args) throws InterruptedException {
        DaemonThread daemonThread = new DaemonThread();
        //daemonThread.setDaemon(true);
        daemonThread.setName("xxx");
        daemonThread.start();

        Thread.sleep(1500);
        System.out.println(daemonThread.getId());
        System.out.println(daemonThread.getName());
    }
}
