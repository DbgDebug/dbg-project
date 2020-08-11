package club.dbg.cms.video.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

@ServerEndpoint("/user/video")
@RestController
public class UserWSController {
    private final static Logger log = LoggerFactory.getLogger(UserWSController.class);

    public final static ConcurrentLinkedQueue<Session> userQueue = new ConcurrentLinkedQueue<>();

    @OnOpen
    public void onOpen(Session session) throws InterruptedException, IOException {
        log.info("user open ws");
    }

    @OnClose
    public void onClose(Session session) {
        log.info("user close ws");
    }

    /**
     * 接收字节数据
     * 编码完成后直接提交到发送队列
     * @param byteBuffer 字节数据
     * @param session    会话信息
     */
    @OnMessage
    public void onMessage(ByteBuffer byteBuffer, Session session) throws IOException {
    }

    /**
     * type
     * 0-设备登录
     * 1-用户登录
     * 2-选择查看的设备
     * 3-修改图片参数
     *
     * @param message 数据
     * @param session 会话信息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if(userQueue.contains(session)){
            return;
        }
        userQueue.add(session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Error while websocket. {}", session.getId(), error);
    }
}
