package club.dbg.cms.video.pojo;

public class ExceptionMessage {
    private Integer code;
    private String message;


    public ExceptionMessage() {
    }

    public ExceptionMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
