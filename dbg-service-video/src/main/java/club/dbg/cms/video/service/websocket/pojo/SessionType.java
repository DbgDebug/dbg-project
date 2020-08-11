package club.dbg.cms.video.service.websocket.pojo;

import javax.websocket.Session;

public class SessionType {
    private Integer id;
    private Integer type;
    private Session session;
    private Integer deviceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SessionType) {
            return id.hashCode() == object.hashCode();
        }
        return false;
    }
}
