package club.dbg.cms.video.service.device;

import club.dbg.cms.video.service.video.IH264DataCallbackTask;
import club.dbg.cms.video.service.video.pojo.H264Byte;
import club.dbg.cms.video.service.websocket.DataSendThread;

public class H264CallbackTask implements IH264DataCallbackTask {
    private final DataSendThread dataSendThread = DataSendThread.getInstance();
    private final Integer deviceId;

    public H264CallbackTask(Integer deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public void callback(H264Byte h264Byte) {
        dataSendThread.submit(new H264SendTask(h264Byte));
    }
}
