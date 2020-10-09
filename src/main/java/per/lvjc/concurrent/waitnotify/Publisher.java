package per.lvjc.concurrent.waitnotify;

import java.util.Queue;

public class Publisher {

    private MqServer server;

    public Publisher(MqServer server) {
        this.server = server;
    }

    /**
     * 发布消息
     * @param queueName
     * @param message
     */
    public void publishMessage(String queueName, String message) {
        final Queue<String> queue = server.getQueueByName(queueName);
        if (queue == null) {
            return;
        }
        synchronized (queue) {
            queue.offer(message);
            System.out.println("成功发布消息 [" + message + "] 到队列 [" + queueName + "]");
            //唤醒在此对象上等待的所有线程
            queue.notify();
        }
    }
}
