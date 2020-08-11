package club.dbg.cms.video.controller;

import club.dbg.cms.video.service.websocket.pojo.WSSession;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import club.dbg.cms.video.config.ApplicationContextRegister;
import club.dbg.cms.video.pojo.DeviceSession;
import club.dbg.cms.video.redis.RedisUtils;
import club.dbg.cms.video.service.device.H264CallbackTask;
import club.dbg.cms.video.service.device.ImagesToH264Task;
import club.dbg.cms.video.service.video.IVideoEncodeService;
import club.dbg.cms.video.service.video.pojo.ImageByte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dbg
 */
@ServerEndpoint("/device/video")
@RestController
public class DeviceWSController {
    private static final Logger log = LoggerFactory.getLogger(DeviceWSController.class);
    private final IVideoEncodeService videoEncodeService;
    private final RedisUtils redisUtils;
    private Integer deviceId;

    public DeviceWSController() {
        ApplicationContext applicationContext = ApplicationContextRegister.getApplicationContext();
        videoEncodeService = applicationContext.getBean(IVideoEncodeService.class);
        redisUtils = applicationContext.getBean(RedisUtils.class);
    }

    // 存储会话信息
    private static final ConcurrentHashMap<String, WSSession> wsSessionMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Session> deviceSessionMap = new ConcurrentHashMap<>();

    public void close(Integer deviceId) {
        Session session = deviceSessionMap.remove(deviceId);
        if(session != null){
            try {
                session.close();
            } catch (IOException e) {
                log.info("关闭WebSocket连接失败:", e);
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) throws InterruptedException, IOException {
        session.setMaxBinaryMessageBufferSize(1000000);
        log.info("Open a websocket.");
    }

    @OnClose
    public void onClose(Session session) {
        WSSession wsSession = wsSessionMap.remove(session.getId());
        if(wsSession == null){
            log.info("Close a websocket：wsSession null");
            return;
        }
        deviceSessionMap.remove(wsSession.getId());
        videoEncodeService.close(wsSession.getId());
        log.info("Close a websocket：{}", session.getId());
    }

    /**
     * 接收字节数据
     * 编码完成后直接提交到发送队列
     * @param byteBuffer 字节数据
     * @param session    会话信息
     */
    @OnMessage
    public void onMessage(ByteBuffer byteBuffer, Session session) throws IOException {
        if(deviceId == null){
            return;
        }
        ImageByte imageByte = new ImageByte(deviceId, byteBuffer);
        H264CallbackTask h264CallbackTask = new H264CallbackTask(deviceId);
        ImagesToH264Task imagesToH264Task = new ImagesToH264Task(imageByte, h264CallbackTask);
        if(deviceId == null){
            return;
        }
        videoEncodeService.submit(deviceId, imagesToH264Task);
    }

    /**
     * @param message 数据
     * @param session 会话信息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info(message);
        if (wsSessionMap.containsKey(session.getId())) {
            return;
        }
        JSONObject jsonObj = JSON.parseObject(message);
        String tokenAuth = jsonObj.getString("token");
        if (tokenAuth == null) {
            log.info("tokenAuth == null");
            return;
        }
        String deviceToken = (String) redisUtils.get("WS_AUTH_DEVICE-" + tokenAuth);
        if(deviceToken == null){
            log.info("deviceToken == null");
            return;
        }
        DeviceSession deviceSession = (DeviceSession) redisUtils.get("DEVICE-" + deviceToken);
        if(deviceSession == null){
            log.info("deviceSession == null");
            return;
        }
        WSSession wsSession = new WSSession();
        wsSession.setId(deviceSession.getId());
        wsSession.setSession(session);
        deviceId = wsSession.getId();
        wsSessionMap.put(session.getId(), wsSession);
        deviceSessionMap.put(deviceId, session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Error while websocket. {}", session.getId(), error);
    }
}