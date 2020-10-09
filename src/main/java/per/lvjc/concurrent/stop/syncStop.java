package per.lvjc.concurrent.stop;

public class syncStop {

    private static final Object monitor = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (monitor) {
                    int i = 0;
                    while (i < 1000) {
                        longRunningOperation(1000);
                        System.out.println("thread1: i = " + i);
                        i++;
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
        thread1.start();
        thread2.start();
        //让它先跑一会，再stop
        longRunningOperation(1000000);
        thread1.stop();
    }



    //重复获取系统时间，作为耗时操作
    private static void longRunningOperation(int loopTimes) {
        int i = 0;
        while (i++ < loopTimes){
            System.currentTimeMillis();
        }
    }
}
