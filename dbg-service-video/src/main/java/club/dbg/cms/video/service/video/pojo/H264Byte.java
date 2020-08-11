package club.dbg.cms.video.service.video.pojo;

public class H264Byte {
    private final Integer deviceId;
    private final byte[] h264Bytes;

    public H264Byte(Integer deviceId, byte[] h264Bytes) {
        this.deviceId = deviceId;
        this.h264Bytes = h264Bytes;
    }

    public byte[] getH264Bytes() {
        return h264Bytes;
    }

    public Integer getDeviceId() {
        return deviceId;
    }
}
