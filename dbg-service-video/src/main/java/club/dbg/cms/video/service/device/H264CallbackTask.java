package club.dbg.cms.video.service.device;

import club.dbg.cms.video.service.video.IH264DataCallbackTask;
import club.dbg.cms.video.service.video.pojo.H264Byte;
import club.dbg.cms.video.service.websocket.DataSendThread;

import javax.websocket.Session;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class H264CallbackTask implements IH264DataCallbackTask {
    private final ConcurrentLinkedQueue<Session> sessions;

    public H264CallbackTask(ConcurrentLinkedQueue<Session> sessions) {
        this.sessions = sessions;
    }

    @Override
    public void callback(H264Byte h264Byte) {
        DataSendThread.submit(new H264SendTask(ByteBuffer.wrap(h264Byte.getH264Bytes()), sessions));
    }
}
