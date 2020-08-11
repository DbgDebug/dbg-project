package club.dbg.cms.video.service.video.pojo;

import java.nio.ByteBuffer;

public class ImageByte {
    private final Integer deviceId;
    private final ByteBuffer imgBytes;

    public ImageByte(Integer deviceId, ByteBuffer imgBytes) {
        this.deviceId = deviceId;
        this.imgBytes = imgBytes;
    }


    public Integer getDeviceId() {
        return deviceId;
    }

    public ByteBuffer getImgBytes() {
        return imgBytes;
    }

}