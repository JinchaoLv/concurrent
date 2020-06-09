package per.lvjc.concurrent;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class MqServer {

    private final Map<String, Queue<String>> queues;

    public MqServer() {
        this.queues = new HashMap<>();
    }

    /**
     * 创建队列，非线程安全
     * @param name 队列名称
     */
    public void createQueue(String name) {
        queues.put(name, new ArrayDeque<>());
        System.out.println("成功创建队列： " + name);
    }

    /**
     * 根据队列名称获取队列，非线程安全
     * @param name 队列名称
     * @return
     */
    public Queue<String> getQueueByName(String name) {
        Queue<String> queue = queues.get(name);
        if (queue == null) {
            System.out.println("队列 [" + name + "] 不存在！");
            return null;
        }
        return queue;
    }
}
