package club.dbg.cms.video.service.websocket;

import club.dbg.cms.video.pojo.AccountDTO;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    private final static Logger log = LoggerFactory.getLogger(WebSocketServiceImpl.class);

    private static final DataSendThread dataSendThread = DataSendThread.getInstance();

    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("").daemon(true).build());

    private static ConcurrentHashMap<String, Session> concurrentHashMap = new ConcurrentHashMap<>();

    public WebSocketServiceImpl() {
        new Thread(dataSendThread).start();
    }

    @Override
    public AccountDTO userLogin(String token) {
        return null;
    }

    @Override
    public AccountDTO deviceLogin(String token) {
        return null;
    }

    @Override
    synchronized public void heartbeat(String name, Session session) {

    }

    private void sendHeartBeat() {

    }

    @Override
    synchronized public void remove(String name) {
    }
}
