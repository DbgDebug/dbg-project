package club.dbg.cms.admin.service.asynctask;

import java.util.concurrent.TimeUnit;

public interface IAsyncTaskService {
    void execute(Runnable runnable, Long timeout, TimeUnit timeUnit);
}
