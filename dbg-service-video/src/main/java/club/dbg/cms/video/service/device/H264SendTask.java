package club.dbg.cms.video.service.device;

import club.dbg.cms.video.controller.UserWSController;
import club.dbg.cms.video.service.video.pojo.H264Byte;
import club.dbg.cms.video.service.websocket.IWebSocketSendTask;

import javax.websocket.Session;
import java.nio.ByteBuffer;
import java.util.Iterator;

public class H264SendTask implements IWebSocketSendTask {
    private final H264Byte h264Byte;

    public H264SendTask(H264Byte h264Byte) {
        this.h264Byte = h264Byte;
    }

    @Override
    public void task() {
        Iterator<Session> iterator = UserWSController.userQueue.iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            if (session.isOpen()) {
                session.getAsyncRemote().sendBinary(ByteBuffer.wrap(h264Byte.getH264Bytes()));
            } else {
                System.out.println("session is close");
                iterator.remove();
            }
        }
    }
}
