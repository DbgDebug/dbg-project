package club.dbg.cms.video.service.websocket;

import club.dbg.cms.video.pojo.AccountDTO;

import javax.websocket.Session;

public interface WebSocketService {
    AccountDTO deviceLogin(String token);

    AccountDTO userLogin(String token);

    void heartbeat(String name, Session session);

    void remove(String name);
}
