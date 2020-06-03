package club.dbg.cms.admin.service.bilibili;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HeartBeatTask{
    private final Logger log = LoggerFactory.getLogger(HeartBeatTask.class);
    private final ConcurrentHashMap<Integer, DanmuReceiveThread> taskMap = new ConcurrentHashMap<>();

    @Scheduled(cron = "*/30 * * * * ?")
    public void run() {
        for (Map.Entry<Integer, DanmuReceiveThread> entry : taskMap.entrySet()) {
            entry.getValue().sendHeartBeat();
        }
    }

    public void submit(DanmuReceiveThread danmuThread) {
        taskMap.put(danmuThread.getId(), danmuThread);
    }

    public void cancel(Integer id) {
        taskMap.remove(id);
    }
}
