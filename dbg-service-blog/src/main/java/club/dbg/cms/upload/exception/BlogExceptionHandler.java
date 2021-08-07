package club.dbg.cms.upload.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Component
public class BlogExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(BlogExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ExceptionReponse exception(Exception e) {
        log.error("Exception:", e);
        return ExceptionReponse.build(9000, "系统异常");
    }

    @ExceptionHandler(value = BlogException.class)
    public ExceptionReponse blogException(BlogException e) {
        return ExceptionReponse.build(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ExceptionReponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindResult = e.getBindingResult();
        FieldError fieldError = bindResult.getFieldError();
        if (fieldError == null) {
            return ExceptionReponse.build(9009, "数据验证失败");
        }

        return ExceptionReponse.build(9009, fieldError.getField() + ":" + fieldError.getDefaultMessage());
    }
}
