package per.lvjc.concurrent.pool;

/**
 * 当任务被拒绝时调用
 */
public interface LvjcRejectedExecutionHandler {

    void rejectedExecution(Runnable r, LvjcThreadPoolExecutor executor);

}
