package per.lvjc.concurrent.sync;

import java.util.concurrent.locks.LockSupport;

public class SyncSameObjectTest {

    private static Integer integer = 0;
    private static Object object = new Object();

    public static void main(String[] args) throws InterruptedException {
        //System.out.println("hash code: " + System.identityHashCode(integer));
//        Runnable runnable = () -> {
//            int i = 0;
//            while (i++ < 10000) {
//                synchronized (integer) {
//                    integer++;
//                }
//            }
//        };
//        int size = 5;
//        Thread[] threads = new Thread[size];
//        for (int i = 0; i < size; i++) {
//            Thread thread = new Thread(runnable);
//            thread.start();
//            threads[i] = thread;
//        }
//        for (int i = 0; i < size; i++) {
//            threads[i].join();
//        }
//        //System.out.println("hash code: " + System.identityHashCode(integer));
//        System.out.println(SyncSameObjectTest.integer);

        Thread thread1 = new Thread(() -> {
            System.out.println("thread begin");
            try {
                LockSupport.park();
            } finally {
                System.out.println("xxxxx");
            }
            System.out.println("end:" + Thread.currentThread().isInterrupted());
        });
        thread1.start();
        Thread.sleep(100);
        //thread1.interrupt();
        //LockSupport.unpark(thread1);
        thread1.stop();
        System.out.println("...");
    }

}
