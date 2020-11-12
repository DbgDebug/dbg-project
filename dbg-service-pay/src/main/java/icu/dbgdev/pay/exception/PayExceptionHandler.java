package icu.dbgdev.pay.exception;

import club.dbg.cms.util.ResponseBuild;
import com.alipay.api.AlipayApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PayExceptionHandle {
    private static final Logger log = LoggerFactory.getLogger(PayExceptionHandle.class);

    @ExceptionHandler(value = AlipayApiException.class)
    public ResponseBuild<String> alipayApiException(AlipayApiException alipayApiException) {
        log.warn("", alipayApiException);
        return ResponseBuild.bad("支付异常");
    }
}
