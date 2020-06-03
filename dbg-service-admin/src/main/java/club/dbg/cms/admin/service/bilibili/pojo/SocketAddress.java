package club.dbg.cms.admin.service.bilibili.pojo;

public class SocketAddress {
    private String ip;
    private int port;
    private boolean leisure;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isLeisure() {
        return leisure;
    }

    public void setLeisure(boolean leisure) {
        this.leisure = leisure;
    }
}
