package per.lvjc.concurrent;

import java.util.Scanner;

public class Main {

    private static final String QUEUE_1 = "queue1";
    private static final String QUEUE_2 = "queue2";

    public static void main(String[] args) throws InterruptedException {
        //启动 MqServer，并创建两个队列
        MqServer server = new MqServer();
        server.createQueue(QUEUE_1);
        server.createQueue(QUEUE_2);
        //创建 Listener 1，监听 queue 1
        cooperation.Listener listener1 = new cooperation.Listener(server, "listener1");
        listener1.listen(QUEUE_1);
        //创建 Listener 2，监听 queue2
        cooperation.Listener listener2 = new cooperation.Listener(server, "listener2");
        listener2.listen(QUEUE_2);
        Thread.sleep(100);
        //创建 Listener 3，监听 queue2
        cooperation.Listener listener3 = new cooperation.Listener(server, "listener3");
        listener3.listen(QUEUE_2);
        Thread.sleep(100);
        cooperation.Listener listener4 = new cooperation.Listener(server, "listener4");
        listener4.listen(QUEUE_2);
        //创建 Publisher
        cooperation.Publisher publisher = new cooperation.Publisher(server);

        //在主线程做 发送消息、关闭 Listener 等操作
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if ("0".equals(input)) {
                System.out.println("退出程序");
                //因为 Listener 都是守护线程，主线程结束，程序应该会终止
                break;
            }
            if ("1".equals(input)) {
                listener1.close();
                continue;
            }
            if ("2".equals(input)) {
                listener2.close();
                continue;
            }
            if ("3".equals(input)) {
                listener3.close();
                continue;
            }
            String[] strs = input.split(":");
            if (strs.length != 2) {
                System.out.println("错误输入");
                continue;
            }
            String queueName = strs[0];
            String message = strs[1];
            publisher.publishMessage(queueName, message);
        }
    }
}
