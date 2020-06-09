package per.lvjc.concurrent;

import java.util.Queue;

public class Listener {

    private final MqServer server;
    private final String name;
    private Thread thread;

    public Listener(MqServer server, String listenerName) {
        this.server = server;
        this.name = listenerName;
    }

    /**
     * 开启一个线程，监听指定的队列
     * @param queueName
     */
    public void listen(String queueName) {
        if (thread != null) {
            System.out.println(name + " 已处于监听状态，无法重复监听");
            return;
        }
        final Queue<String> queue = server.getQueueByName(queueName);
        if (queue == null) {
            return;
        }
        thread = new Thread(() -> {
            System.out.println(name + " 开始监听 " + queueName);
            synchronized (queue) {
                String message;
                //借用 interrupt 标识位作监听状态判断条件
                while (!Thread.currentThread().isInterrupted()) {
                    message = queue.poll();
                    //取到消息，则处理消息
                    if (message != null) {
                        System.out.println("[" + name + "]" + " 在队列 [" + queueName + "] 上监听到消息 [" + message + "]");
                        continue;
                    }
                    //没有消息，则等待其它线程发布消息
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        //被打断则终止监听
                        break;
                    }
                }
            }
            System.out.println(name + " 停止监听 " + queueName);
        });
        //在后台监听
        thread.setDaemon(true);
        //启动监听
        thread.start();
    }

    /**
     * 关闭 Listener
     */
    public void close() {
        if (thread == null) {
            return;
        }
        System.out.println(name + " 被关闭");
        thread.interrupt();
        thread = null;
    }
}
