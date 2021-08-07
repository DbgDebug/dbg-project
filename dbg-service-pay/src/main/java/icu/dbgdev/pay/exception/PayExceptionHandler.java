package icu.dbgdev.pay.exception;

import club.dbg.cms.util.ResponseBuild;
import com.alipay.api.AlipayApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
public class PayExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(PayExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ExceptionResponse exception(Exception e) {
        log.error("Exception:", e);
        return ExceptionResponse.build(9000, "系统异常");
    }

    @ExceptionHandler(value = AlipayApiException.class)
    public ExceptionResponse alipayApiException(AlipayApiException alipayApiException) {
        log.warn("支付宝:", alipayApiException);
        return ExceptionResponse.build(40081,"支付请求异常");
    }

    @ExceptionHandler(value = PayException.class)
    public ExceptionResponse payException(PayException payException) {
        log.warn("", payException);
        return ExceptionResponse.build(payException.getCode(), payException.getMessage());
    }
}
