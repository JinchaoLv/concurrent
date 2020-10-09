package per.lvjc.concurrent.stop;

public class Main {

    private static Object monitor = new Object();

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (monitor) {
                    try {
                        int i = 0;
                        while (i < 1000) {
                            longRunningOperation(1000);
                            System.out.println("thread: i = " + i);
                            i++;
                        }
                    } finally {
                        System.out.println("finally begin...");
                        longRunningOperation(1000 * 1000 * 1000);
                        System.out.println("finally end...");
                    }
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (monitor) {
                    int i = 0;
                    while (i < 1000) {
                        longRunningOperation(1000);
                        System.out.println("thread2: i = " + i);
                        i++;
                    }
                }
            }
        });
        thread.start();
        //延迟一小会再启动 thread2，让 thread 先获得锁
        longRunningOperation(100);
        thread2.start();
        thread.stop();
    }

    //重复获取系统时间，作为耗时操作
    private static void longRunningOperation(int loopTimes) {
        int i = 0;
        while (i++ < loopTimes){
            System.currentTimeMillis();
        }
    }
}
