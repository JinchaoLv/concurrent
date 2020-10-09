package per.lvjc.concurrent.stop;

public class FinallyStop {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int i = 0;
                    while (i < 1000) {
                        longRunningOperation(1000);
                        System.out.println("thread1: i = " + i);
                        i++;
                    }
                } finally {
                    int i = 0;
                    while (i < 1000) {
                        longRunningOperation(1000);
                        System.out.println("thread1---finally: i = " + i);
                        i++;
                    }
                }
            }
        });

        thread1.start();
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
