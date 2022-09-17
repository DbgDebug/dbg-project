package club.dbg.cms.upload.exception;

public class ActionException extends RuntimeException {
    private int code = 20000;
    private String message;
    private int action = 20000;

    public ActionException(String message){
        this.message = message;
    }

    public ActionException(int action, String message){
        this.action = action;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
