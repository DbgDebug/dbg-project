package club.dbg.cms.blog.service.schedule;

import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduleService implements IScheduleService {
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1,
            new ScheduleThreadFactory());

    @Override
    public void submit(IScheduleTask scheduleTask, long delay) {
        scheduledExecutorService.schedule(scheduleTask, delay, TimeUnit.SECONDS);
    }
}
