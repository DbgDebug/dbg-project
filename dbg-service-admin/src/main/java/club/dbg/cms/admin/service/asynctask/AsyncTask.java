package club.dbg.cms.admin.service.asynctask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class AsyncTask implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(AsyncTask.class);

    private final Runnable runnable;
    private final ThreadPoolExecutor executorThread;
    private final Long timeout;
    private final TimeUnit timeUnit;

    protected AsyncTask(ThreadPoolExecutor executor, Runnable runnable, Long timeout, TimeUnit timeUnit) {
        this.runnable = runnable;
        this.executorThread = executor;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    @Override
    public void run() {
        Future<?> future = null;
        try {
            future = executorThread.submit(runnable);
            System.out.println(System.currentTimeMillis() / 1000);
            future.get(timeout, timeUnit);
            System.out.println(System.currentTimeMillis() / 1000);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            future.cancel(true);
            log.info("异步任务执行失败：", e);
        }
    }
}
