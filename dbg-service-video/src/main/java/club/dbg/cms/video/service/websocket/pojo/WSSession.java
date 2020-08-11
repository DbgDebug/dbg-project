package club.dbg.cms.video.service.websocket.pojo;

import javax.websocket.Session;

public class WSSession {
    private Integer id;
    private Session session;

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
}
