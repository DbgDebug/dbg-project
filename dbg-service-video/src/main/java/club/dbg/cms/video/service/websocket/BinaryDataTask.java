package club.dbg.cms.video.service.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BinaryDataTask implements IWebSocketSendTask {
    private static final Logger log = LoggerFactory.getLogger(BinaryDataTask.class);
    private final ByteBuffer byteBuffer;
    private final ConcurrentLinkedQueue<Session> sessions;

    public BinaryDataTask(ByteBuffer byteBuffer, ConcurrentLinkedQueue<Session> sessions) {
        this.byteBuffer = byteBuffer;
        this.sessions = sessions;
    }

    public static BinaryDataTask build(ByteBuffer byteBuffer, ConcurrentLinkedQueue<Session> sessions) {
        return new BinaryDataTask(byteBuffer, sessions);
    }

    @Override
    public void task() throws IOException {
        if (sessions == null) {
            log.info("sessions is null");
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
