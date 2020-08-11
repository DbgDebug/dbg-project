package club.dbg.cms.video.exception;

import club.dbg.cms.util.ResponseBuild;
import club.dbg.cms.video.pojo.ExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dbg
 */

@RestControllerAdvice
@Component
public class MyExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    /**
     * 缺少参数时

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseResult requestParameterException(MissingServletRequestParameterException e) {
        ResponseResult response = new ResponseResult();
        response.setCode(10502);
        response.setMessage(e.getMessage());
        return response;
    }
     */

    /**
     * @ Valid
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseBuild<ExceptionMessage>> validHandle(MethodArgumentNotValidException exception) {
        ExceptionMessage message = new ExceptionMessage();

        if (exception == null) {
            message.setCode(10502);
            message.setMessage("数据验证失败");
            return ResponseBuild.build(message);
        }

        BindingResult bindResult = exception.getBindingResult();

        /// 不返回多个错误
        /*
        Map<String, String> errorMap = new HashMap<>(16);
        for (FieldError fieldError : bindResult.getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        */

        FieldError fieldError = bindResult.getFieldError();
        if(fieldError == null){
            message.setCode(10502);
            message.setMessage("数据验证失败");
            return ResponseBuild.build(message);
        }
        message.setCode(10502);
        message.setMessage(fieldError.getDefaultMessage());
        return ResponseBuild.build(message);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ResponseBuild<ExceptionMessage>> validatedHandle(ConstraintViolationException exception) {
        ExceptionMessage message = new ExceptionMessage();
        message.setCode(10502);
        if (exception == null) {
            message.setMessage("数据验证失败");
            return ResponseBuild.build(message);
        }

        List<String> msgList = new ArrayList<>();
        exception.getConstraintViolations().forEach(constraintViolation ->
                msgList.add(constraintViolation.getMessage()));
        if(msgList.size() == 0){
            message.setMessage("数据验证失败");
            return ResponseBuild.build(message);
        }
        message.setMessage(msgList.get(0));
        return ResponseBuild.build(message);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseBuild<ExceptionMessage>> exception(Exception e) {
        log.error("Exception:", e);
        ExceptionMessage message = new ExceptionMessage();
        message.setCode(9000);
        message.setMessage("系统异常");
        return ResponseBuild.build(message);
    }


    @ExceptionHandler(value = VideoException.class)
    public ResponseEntity<ResponseBuild<ExceptionMessage>> myException(VideoException e) {
        ExceptionMessage message = new ExceptionMessage();
        message.setCode(e.getCode());
        message.setMessage(e.getMessage());
        return ResponseBuild.build(message);
    }
}
