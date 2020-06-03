package club.dbg.cms.admin.exception;

import club.dbg.cms.rpc.pojo.ResponseResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * LoginExceptionHandler.class
 *
 * @author dbg
 * @date 2019/08/15
 */

@RestControllerAdvice
public class AdminExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(AdminExceptionHandler.class);

    /**
     * 缺少参数时
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseResultDTO requestParameterException(MissingServletRequestParameterException e) {
        ResponseResultDTO response = new ResponseResultDTO();
        response.setCode(10502);
        response.setMessage(e.getMessage());
        return response;
    }

    /**
     * 注解 @Valid 修饰类的参数设置不正确
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResultDTO validHandle(MethodArgumentNotValidException exception) {
        ResponseResultDTO response = new ResponseResultDTO();

        if (exception == null) {
            response.setCode(10502);
            response.setMessage("数据验证失败");
            return response;
        }

        BindingResult bindResult = exception.getBindingResult();

        /// 不返回多个错误
        /*
        Map<String, String> errorMap = new HashMap<>(16);
        for (FieldError fieldError : bindResult.getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        */

        response.setCode(10502);
        FieldError fieldError = bindResult.getFieldError();
        if(fieldError == null){
            response.setMessage("数据验证失败");
            return response;
        }
        response.setMessage(fieldError.getDefaultMessage());
        return response;
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseResultDTO validatedHandle(ConstraintViolationException exception) {
        ResponseResultDTO response = new ResponseResultDTO();
        response.setCode(10502);
        if (exception == null) {
            response.setMessage("数据验证失败");
            return response;
        }

        List<String> msgList = new ArrayList<>();
        exception.getConstraintViolations().forEach(constraintViolation ->
                msgList.add(constraintViolation.getMessage()));
        if(msgList.size() == 0){
            response.setMessage("数据验证失败");
            return response;
        }
        response.setMessage(msgList.get(0));
        return response;
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseResultDTO exception(Exception e) {
        log.error("Exception:", e);
        ResponseResultDTO response = new ResponseResultDTO();
        response.setCode(9000);
        response.setMessage("系统异常");
        return response;
    }


    @ExceptionHandler(value = BusinessException.class)
    public ResponseResultDTO myException(BusinessException e) {
        ResponseResultDTO response = new ResponseResultDTO();
        response.setCode(e.getCode());
        response.setMessage(e.getMessage());
        return response;
    }

    @ExceptionHandler(value = ActionException.class)
    public ResponseResultDTO actionException(ActionException e) {
        ResponseResultDTO response = new ResponseResultDTO();
        response.setCode(e.getCode());
        response.setAction(e.getAction());
        response.setMessage(e.getMessage());
        return response;
    }
}
