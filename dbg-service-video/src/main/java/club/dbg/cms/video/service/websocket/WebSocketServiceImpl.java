package club.dbg.cms.video.service.websocket;

import club.dbg.cms.video.pojo.AccountDTO;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    private final static Logger log = LoggerFactory.getLogger(WebSocketServiceImpl.class);

    private static final LinkedBlockingQueue<IWebSocketSendTask> dataTaskQueue = new LinkedBlockingQueue<>();

    private static final DataSendThread dataSendThread = DataSendThread.getInstance();

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
