package club.dbg.cms.rpc.pojo;

/**
 * @author dbg
 * @date 2019/12/23
 */
public class ResponseResultDTO {
    private Integer code;
    private String message;
    private Integer action;
    private Object data;

    public ResponseResultDTO() {
        this.code = 20000;
        this.action = 20000;
    }

    public ResponseResultDTO(Object data) {
        this.code = 20000;
        this.action = 20000;
        this.data = data;
    }

    public ResponseResultDTO ok() {
        this.code = 20000;
        this.action = 20000;
        return this;
    }

    public static ResponseResultDTO build(Object data){
        ResponseResultDTO responseResult = new ResponseResultDTO();
        responseResult.setData(data);
        return responseResult;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", action=" + action +
                ", data=" + data +
                '}';
    }
}
