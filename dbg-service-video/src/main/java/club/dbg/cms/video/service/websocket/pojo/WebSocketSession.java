package club.dbg.cms.video.service.websocket.pojo;

import javax.websocket.Session;

public class WebSocketSession {
    private Integer id;
    private Session session;
    private Boolean isEncode;
    private String videoId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Boolean isEncode() {
        return isEncode;
    }

    public void setEncode(Boolean encode) {
        isEncode = encode;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
