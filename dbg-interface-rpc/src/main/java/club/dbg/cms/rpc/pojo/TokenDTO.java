package club.dbg.cms.rpc.pojo;

/**
 * @author dbg
 * @date 2019/08/26
 */
public class TokenDTO {
    private String initToken;
    private String accessToken;
    private String uuid;

    public String getInitToken() {
        return initToken;
    }

    public void setInitToken(String initToken) {
        this.initToken = initToken;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
