package per.lvjc.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": my thread");
    }

    public static void main(String[] args) {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName() + ": my thread");
//            }
//        });
        Thread thread = new Thread(new FutureTask<>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println(Thread.currentThread().getName() + ": my thread");
                return null;
            }
        }));
        thread.start();
        thread.stop();
    }
}
