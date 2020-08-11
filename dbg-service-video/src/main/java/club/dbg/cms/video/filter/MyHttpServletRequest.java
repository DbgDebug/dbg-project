package club.dbg.cms.video.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author dbg
 */
public class MyHttpServletRequest extends HttpServletRequestWrapper {
    public MyHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    private Integer id;
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
