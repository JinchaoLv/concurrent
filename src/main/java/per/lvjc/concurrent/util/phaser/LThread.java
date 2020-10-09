package per.lvjc.concurrent.util.phaser;

public class LThread extends Thread {

    public LThread(Runnable runnable, String threadName) {
        super(runnable, threadName);
    }

    public void print(String str) {
        System.out.println(this.getName() + ": " + str);
    }
}
