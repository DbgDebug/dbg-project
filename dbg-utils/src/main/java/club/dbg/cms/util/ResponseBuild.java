package club.dbg.cms.util;

import org.springframework.http.ResponseEntity;

/**
 * @author dbg
 * @param <T>
 */
public class ResponseBuild<T> {
    private final Integer code;
    private final T data;
    private final String message;

    private ResponseBuild(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T>  ResponseEntity<ResponseBuild<T>> build(T data){
        ResponseBuild<T> resultEntity = new ResponseBuild<>(20000, data, "");
        return ResponseEntity.ok(resultEntity);
    }

    public static <T> ResponseEntity<ResponseBuild<T>> build(Integer code, T data){
        ResponseBuild<T> resultEntity = new ResponseBuild<>(code, data,"");
        return ResponseEntity.ok(resultEntity);
    }

    public static <T> ResponseEntity<ResponseBuild<T>> build(Integer code, T data, String message){
        ResponseBuild<T> resultEntity = new ResponseBuild<>(code, data, message);
        return ResponseEntity.ok(resultEntity);
    }

    public static <T> ResponseEntity<ResponseBuild<T>> build(Integer code, String message){
        ResponseBuild<T> resultEntity = new ResponseBuild<>(code, null, message);
        return ResponseEntity.ok(resultEntity);
    }

    public Integer getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
