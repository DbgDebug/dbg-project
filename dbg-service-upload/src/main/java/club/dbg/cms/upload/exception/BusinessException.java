package club.dbg.cms.upload.exception;

/**
 * @author dbg
 */
public class BusinessException extends RuntimeException {
    private int code;
    private String msg;

    public BusinessException(int code, String message) {
        super(message, null, false, false);
        this.code = code;
        this.msg = message;
    }

    public BusinessException(String message) {
        super(message, null, false, false);
        this.code = 40000;
        this.msg = message;
    }

    public static BusinessException build(String msg) {
        return new BusinessException(40000, msg);
    }

    public static BusinessException build(int code, String msg) {
        return new BusinessException(code, msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    // 不获取堆栈信息
    /*
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
    */
}
