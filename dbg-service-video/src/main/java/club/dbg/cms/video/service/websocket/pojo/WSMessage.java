package club.dbg.cms.video.service.websocket.pojo;

import com.alibaba.fastjson.JSON;

public class WSMessage {
    private Integer type;
    private Object data;

    public static WSMessage build(Integer type, Object data) {
        WSMessage wsMessage = new WSMessage();
        wsMessage.setType(type);
        wsMessage.setData(data);
        return wsMessage;
    }

    public static String buildToString(Integer type, Object data) {
        WSMessage wsMessage = new WSMessage();
        wsMessage.setType(type);
        wsMessage.setData(data);
        return JSON.toJSONString(wsMessage);
    }

    public static WSMessage build(Object data) {
        WSMessage wsMessage = new WSMessage();
        wsMessage.setType(0);
        wsMessage.setData(data);
        return wsMessage;
    }

    public static String buildToString(Object data) {
        WSMessage wsMessage = new WSMessage();
        wsMessage.setType(0);
        wsMessage.setData(data);
        return JSON.toJSONString(wsMessage);
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
