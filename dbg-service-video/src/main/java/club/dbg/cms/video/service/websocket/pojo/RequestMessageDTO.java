package club.dbg.cms.video.service.websocket.pojo;

public class RequestMessageDTO {
    private Integer type;
    private String token;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
