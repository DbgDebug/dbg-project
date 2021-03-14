package club.dbg.cms.video.config;

public enum BinaryTypeEnum {
    IMAGES((short) 10, "图片"),
    H264((short) 20, "H264数据"),
    WEBM_VP8((short) 30, "webm-vp8数据"),
    WEBM_VP9((short) 40, "webm-vp9数据"),
    H265((short) 50, "H265"),
    AV1((short) 60, "AV1编码数据");

    private final short value;
    private final String explain;

    BinaryTypeEnum(short value, String explain) {
        this.value = value;
        this.explain = explain;
    }

    public short value() {
        return value;
    }

    public String explain() {
        return explain;
    }
}
