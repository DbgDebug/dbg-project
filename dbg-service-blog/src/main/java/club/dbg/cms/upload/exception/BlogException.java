package club.dbg.cms.upload.exception;

public class BlogException extends RuntimeException {
    private int code;
    private String message;

    public BlogException(String message){
        this.code = 40000;
        this.message = message;
    }
    public BlogException(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
