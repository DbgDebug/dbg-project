package club.dbg.cms.video.service.device;

import club.dbg.cms.video.controller.UserWebSocket;
import club.dbg.cms.video.service.video.pojo.H264Byte;
import club.dbg.cms.video.service.websocket.BinaryDataTask;
import club.dbg.cms.video.service.websocket.IWebSocketSendTask;

import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class H264SendTask implements IWebSocketSendTask {
    private final ByteBuffer byteBuffer;
    private final ConcurrentLinkedQueue<Session> sessions;

    public H264SendTask(ByteBuffer byteBuffer, ConcurrentLinkedQueue<Session> sessions) {
        this.byteBuffer = byteBuffer;
        this.sessions = sessions;
    }

    public static BinaryDataTask build(ByteBuffer byteBuffer, ConcurrentLinkedQueue<Session> sessions) {
        return new BinaryDataTask(byteBuffer, sessions);
    }

    @Override
    public void task() throws IOException {
        if (sessions == null) {
            return;
        }
        Iterator<Session> iterator = sessions.iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            if (session == null) {
                continue;
            }
            if (session.isOpen()) {
                session.getBasicRemote().sendBinary(byteBuffer);
            } else {
                iterator.remove();
            }
        }
    }
}
