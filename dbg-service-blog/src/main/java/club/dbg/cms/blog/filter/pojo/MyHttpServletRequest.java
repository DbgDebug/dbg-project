package club.dbg.cms.blog.filter.pojo;

import club.dbg.cms.rpc.pojo.Operator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author dbg
 */
public class MyHttpServletRequest extends HttpServletRequestWrapper {
    public MyHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    private Operator operator;

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }
}
