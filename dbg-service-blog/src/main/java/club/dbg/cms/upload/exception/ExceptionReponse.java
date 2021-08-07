package club.dbg.cms.upload.exception;

public class ExceptionReponse {
    private final Integer code;
    private final String message;

    private ExceptionReponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ExceptionReponse build(Integer code, String message) {
        return new ExceptionReponse(code, message);
    }


    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
