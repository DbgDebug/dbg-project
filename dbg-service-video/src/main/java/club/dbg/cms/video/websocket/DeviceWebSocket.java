package club.dbg.cms.video.websocket;

import club.dbg.cms.util.FileUtils;
import club.dbg.cms.util.JWTUtils;
import club.dbg.cms.util.UUIDUtils;
import club.dbg.cms.video.service.file.FileWriteService;
import club.dbg.cms.video.service.websocket.BinaryDataTask;
import club.dbg.cms.video.service.websocket.DataSendThread;
import club.dbg.cms.video.service.websocket.pojo.TextMessage;
import club.dbg.cms.video.service.websocket.pojo.WebSocketSession;
import com.alibaba.fastjson.JSON;
import club.dbg.cms.video.config.ApplicationContextRegister;
import club.dbg.cms.video.service.video.IVideoEncodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author dbg
 */
@Component
@ServerEndpoint("/device")
public class DeviceWebSocket {
    private static final Logger log = LoggerFactory.getLogger(DeviceWebSocket.class);
    private volatile IVideoEncodeService videoEncodeService = null;
    private FileWriteService fileWriteService = null;
    private Integer deviceId;

    private final String savePath;

    public DeviceWebSocket() {
        savePath = "E:/video/";
        ApplicationContext applicationContext = ApplicationContextRegister.getApplicationContext();
        videoEncodeService = applicationContext.getBean(IVideoEncodeService.class);
        fileWriteService = applicationContext.getBean(FileWriteService.class);
    }

    // 存储会话信息
    private static final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Session> deviceMap = new ConcurrentHashMap<>();

    public void close(Integer deviceId) {
        Session session = deviceMap.remove(deviceId);
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                log.info("关闭WebSocket连接失败:", e);
            }
        }
    }

    private void init() {

    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        init();
        log.info("Open a websocket.");
        session.setMaxBinaryMessageBufferSize(10000000);
        WebSocketSession webSocketSession = new WebSocketSession();
        webSocketSession.setId(1);
        webSocketSession.setSession(session);
        webSocketSession.setEncode(false);
        webSocketSession.setVideoId(UUIDUtils.getUUIDNotHyphen());
        sessionMap.put(session.getId(), webSocketSession);
        deviceMap.put(webSocketSession.getId(), session);
        // session.getBasicRemote().sendBinary(ByteBuffer.wrap(FileUtils.readFileByBytes("E:\\video\\19.mp4")));
    }

    @OnClose
    public void onClose(Session session) {
        WebSocketSession wsSession = sessionMap.remove(session.getId());
        if (wsSession == null) {
            log.info("Close a websocket: wsSession null-{}", session.getId());
            return;
        }
        deviceMap.remove(wsSession.getId());
        // videoEncodeService.close(wsSession.getId());
        log.info("Close a websocket：{}", session.getId());
    }

    /**
     * 接收字节数据
     * 编码完成后直接提交到发送队列
     *
     * @param byteBuffer 字节数据
     * @param session    会话信息
     */
    @OnMessage
    public void onMessage(ByteBuffer byteBuffer, Session session) throws InterruptedException, IOException {
        WebSocketSession wsSession = sessionMap.get(session.getId());
        fileWriteService.writeAsync(savePath, wsSession.getVideoId() + ".webm", byteBuffer.array(), true);
        ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Session>> liveGroup = UserWebSocket.getLiveGroup();
        ConcurrentLinkedQueue<Session> sessions = liveGroup.get(wsSession.getId());
        if (sessions == null) {
            sessions = new ConcurrentLinkedQueue<>();
            liveGroup.put(wsSession.getId(), sessions);
        }
        if (wsSession.isEncode()) {
            // 需要进行编码
            System.out.println("编码");
            videoEncodeService.submit(deviceId, byteBuffer, sessions);
            return;
        }
        DataSendThread.submit(BinaryDataTask.build(byteBuffer, sessions));
    }

    /**
     * @param message 文本数据
     * @param session 会话信息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info(message);
        if (sessionMap.containsKey(session.getId())) {
            return;
        }
        if (message == null || message.isEmpty()) {
            return;
        }
        TextMessage msg = JSON.parseObject(message, TextMessage.class);
        if (msg == null || msg.getData() == null || msg.getData().isEmpty()) {
            log.warn("message:{}", message);
            return;
        }
        switch (msg.getType()) {
            case 0:
                break;
            case 1:
                break;
            case 100:
                login(session, msg.getData());
                break;
            case 200:
                break;
            case 300:
                break;
            default:
                break;
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Error while websocket. {}", session.getId());
        log.error("Error while websocket. ", error);
    }

    private void login(Session session, String token) {
        if (sessionMap.containsKey(session.getId())) {
            return;
        }
        session.setMaxBinaryMessageBufferSize(5000000);
        Integer id = JWTUtils.getClaimByName(token, "accountId").asInt();
        if (id == null) {
            return;
        }
        WebSocketSession webSocketSession = new WebSocketSession();
        webSocketSession.setId(id);
        webSocketSession.setSession(session);
        webSocketSession.setEncode(false);
        deviceId = webSocketSession.getId();
        sessionMap.put(session.getId(), webSocketSession);
        deviceMap.put(deviceId, session);
    }
}