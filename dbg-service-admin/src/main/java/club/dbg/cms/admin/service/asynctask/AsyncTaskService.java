package club.dbg.cms.admin.service.asynctask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class AsyncTaskService implements IAsyncTaskService {
    private static final Logger log = LoggerFactory.getLogger(AsyncTaskService.class);

    private final ThreadPoolExecutor taskThread = new ThreadPoolExecutor(
            1,
            1,
            0,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1000),
            new ThreadPoolExecutor.AbortPolicy());

    private final ThreadPoolExecutor executorThread = new ThreadPoolExecutor(
            1,
            1,
            0,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1000),
            new ThreadPoolExecutor.AbortPolicy());

    @Override
    public void execute(Runnable runnable, Long timeout, TimeUnit timeUnit) {
        AsyncTask asyncTask = new AsyncTask(executorThread, runnable, timeout, timeUnit);
        taskThread.execute(asyncTask);
    }
}
