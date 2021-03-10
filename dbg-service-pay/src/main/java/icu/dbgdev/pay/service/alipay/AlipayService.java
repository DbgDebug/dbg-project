package icu.dbgdev.pay.service.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import icu.dbgdev.pay.dto.QRCodePayResponseDTO;
import icu.dbgdev.pay.dto.TradeInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AlipayService implements IAlipayService {
    private static final Logger log = LoggerFactory.getLogger(AlipayService.class);

    private final AlipayClient alipayClient;

    private final String notifyUrl;

    public AlipayService(
            @Value("${alipay.notifyUrl}")
                    String notifyUrl,
            AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
        this.notifyUrl = notifyUrl;
    }

    @Override
    public QRCodePayResponseDTO createPayQRCode(TradeInfoDTO tradeInfoDTO) throws AlipayApiException {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(notifyUrl);

        Map<String, String> bizContentMap = new HashMap<>();
        bizContentMap.put("out_trade_no", tradeInfoDTO.getOutTradeNo());
        bizContentMap.put("total_amount", tradeInfoDTO.getTotalAmount().toPlainString());
        bizContentMap.put("subject", tradeInfoDTO.getSubject());
        bizContentMap.put("timeout_express", "30m");

        request.setBizContent(JSON.toJSONString(bizContentMap));

        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        if (!response.isSuccess()) {
            log.warn("alipay.trade.precreate call failed:{}", response.getBody());
            return new QRCodePayResponseDTO();
        }
        log.info("alipay.trade.precreate:{}", response.getBody());
        return new QRCodePayResponseDTO(tradeInfoDTO.getOutTradeNo(), response.getQrCode());
    }
}
