package per.lvjc.concurrent.pool;

public class DefaultRejectExecutionHandler implements LvjcRejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, LvjcThreadPoolExecutor executor) {
        //处理被拒绝的任务
    }
}
