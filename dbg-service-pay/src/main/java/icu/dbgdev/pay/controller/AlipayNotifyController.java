package icu.dbgdev.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping(value = "/alipay")
public class AlipayNotifyController {
    private final Logger log = LoggerFactory.getLogger(AlipayNotifyController.class);

    private final String alipayPublicKey;

    public AlipayNotifyController(@Value("${alipay.alipayPublicKey}")String alipayPublicKey) {

        this.alipayPublicKey = alipayPublicKey;
    }

    @RequestMapping(value = "/notify")
    public String payNotify(@RequestParam Map<String, String> map) throws AlipayApiException {
        log.info("支付宝扫码支付回调：{}", JSON.toJSONString(map));
        AlipaySignature.rsaCheckV1(map, alipayPublicKey, "utf-8", "RSA2");
        return "success";
    }
}
