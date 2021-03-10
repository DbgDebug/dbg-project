package icu.dbgdev.pay.exception;

import icu.dbgdev.pay.config.PayStatus;

public class PayException extends RuntimeException {
    private Integer code;
    private String message;
    private Throwable throwable;

    public PayException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static PayException build(Integer code, String message) {
        return new PayException(code, message);
    }

    public static PayException build(String message) {
        return new PayException(PayStatus.FAILED.getCode(), message);
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
