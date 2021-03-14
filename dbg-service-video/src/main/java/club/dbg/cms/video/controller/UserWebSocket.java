package club.dbg.cms.video.controller;

import club.dbg.cms.video.service.websocket.pojo.TextMessage;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
@ServerEndpoint("/user")
public class UserWebSocket {
    private final static Logger log = LoggerFactory.getLogger(UserWebSocket.class);

    public final static ConcurrentLinkedQueue<Session> userQueue = new ConcurrentLinkedQueue<>();

    // 加入设备会话的用户, key-deviceId(设备), value-用户session队列
    private static final ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Session>> liveGroup = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) throws InterruptedException, IOException {
        log.info("user open ws");
        session.setMaxBinaryMessageBufferSize(5000000);
        joinLive(1, session);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("user close ws");
    }

    /**
     * @param byteBuffer 字节数据
     * @param session    会话信息
     */
    @OnMessage
    public void onMessage(ByteBuffer byteBuffer, Session session) throws IOException {
    }

    /**
     * @param message 数据
     * @param session 会话信息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        TextMessage textMessage = JSON.parseObject(message, TextMessage.class);
        if (textMessage.getType() == 1) {
            if (userQueue.contains(session)) {
                return;
            }
            userQueue.add(session);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Error while websocket. {}", session.getId(), error);
    }

    public void joinLive(Integer liveId, Session session) {
        synchronized (UserWebSocket.class) {
            ConcurrentLinkedQueue<Session> sessions = liveGroup.get(liveId);
            if(sessions != null) {
                System.out.println(sessions.size());
            }
            if (sessions == null) {
                sessions = new ConcurrentLinkedQueue<>();
                sessions.add(session);
                liveGroup.put(1, sessions);
            }
            sessions.add(session);
            log.info("加入直播间");
        }
    }

    public static ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Session>> getLiveGroup() {
        return liveGroup;
    }
}
