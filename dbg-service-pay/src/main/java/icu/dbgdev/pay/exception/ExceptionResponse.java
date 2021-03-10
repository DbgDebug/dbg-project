package icu.dbgdev.pay.exception;

public class ExceptionResponse {
    private final Integer code;
    private final String message;

    private ExceptionResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ExceptionResponse build(Integer code, String message) {
        return new ExceptionResponse(code, message);
    }


    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
