package club.dbg.cms.video.exception;

/**
 * @author dbg
 */
public class VideoException extends RuntimeException {
    private int code = 40000;
    private String message;

    public VideoException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public VideoException(String message) {
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
