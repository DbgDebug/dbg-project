package icu.dbgdev.pay.config;

public enum PayStatus {
    SUCCESS(20000, "支付成功"),
    FAILED(40000, "支付失败");

    private final Integer code;
    private final String message;

    PayStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
