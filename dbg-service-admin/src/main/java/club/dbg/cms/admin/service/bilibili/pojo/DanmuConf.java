package club.dbg.cms.admin.service.bilibili.pojo;

public class DanmuConf {
    private String host;
    private Integer port;
    private Integer wss_port;
    private Integer ws_port;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getWss_port() {
        return wss_port;
    }

    public void setWss_port(Integer wss_port) {
        this.wss_port = wss_port;
    }

    public Integer getWs_port() {
        return ws_port;
    }

    public void setWs_port(Integer ws_port) {
        this.ws_port = ws_port;
    }
}
