package club.dbg.cms.video.service.video;

import javax.websocket.Session;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface IVideoEncodeService {
    boolean submit(Integer deviceId, ByteBuffer byteBuffer, ConcurrentLinkedQueue<Session> sessions) throws InterruptedException;
    boolean close(Integer deviceId);
}
