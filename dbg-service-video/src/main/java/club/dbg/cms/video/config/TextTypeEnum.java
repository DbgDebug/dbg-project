package club.dbg.cms.video.config;

public enum TextTypeEnum {
    HEARTBEAT(0, "心跳包"),
    MSG(1, "提示信息"),
    LOGIN(100, "登录"),
    JOIN_LIVE(200, "加入直播间"),
    SETTING(300, "参数设置")
    ;
    private final Integer value;
    private final String explain;

    TextTypeEnum(Integer value, String explain) {
        this.value = value;
        this.explain = explain;
    }

    public Integer value() {
        return value;
    }

    public String explain() {
        return explain;
    }
}
