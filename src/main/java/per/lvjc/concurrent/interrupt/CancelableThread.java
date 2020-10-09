package per.lvjc.concurrent.interrupt;

public class CancelableThread extends Thread {

    private boolean cancel = false;

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public void run() {
        System.out.println("begin, isCancel:" + cancel);
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(isCancel());
    }

    public static void main(String[] args) throws InterruptedException {
        CancelableThread thread = new CancelableThread();
        thread.start();
        //sleep(1000);
        thread.setDaemon(true);
    }
}
