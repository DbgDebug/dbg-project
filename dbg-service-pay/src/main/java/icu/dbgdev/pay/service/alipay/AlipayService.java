package icu.dbgdev.pay.service.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import icu.dbgdev.pay.dto.QRCodePayResponseDTO;
import icu.dbgdev.pay.dto.TradeInfoDTO;
import icu.dbgdev.pay.exception.PayException;
import icu.dbgdev.pay.service.order.pojo.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AlipayService implements IAlipayService {
    private static final Logger log = LoggerFactory.getLogger(AlipayService.class);

    private final AlipayClient alipayClient;

    private final String notifyUrl;

    private final Map<String, QRCodePayResponseDTO> qrcodeMap = new ConcurrentHashMap<>();

    private final ReentrantLock reentrantLock = new ReentrantLock();

    private final AtomicInteger atomicInteger = new AtomicInteger();

    public AlipayService(
            @Value("${alipay.notifyUrl}")
                    String notifyUrl,
            AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
        this.notifyUrl = notifyUrl;
    }

    @Override
    public QRCodePayResponseDTO createPayQRCode(TradeInfoDTO tradeInfoDTO) throws AlipayApiException {
        if (qrcodeMap.containsKey(tradeInfoDTO.getOutTradeNo())) {
            QRCodePayResponseDTO qr = qrcodeMap.get(tradeInfoDTO.getOutTradeNo());
            if (qr != null) {
                long timestamp = System.currentTimeMillis() / 1000;
                if (timestamp - qr.getCreateTime() < 1700) {
                    return qr;
                }
            }
        }
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
            throw PayException.build("获取支付二维码失败");
        }
        log.info("alipay.trade.precreate:{}", response.getBody());
        QRCodePayResponseDTO qrcode = new QRCodePayResponseDTO(tradeInfoDTO.getOutTradeNo(), response.getQrCode());
        qrcodeMap.put(tradeInfoDTO.getOutTradeNo(), qrcode);

        if (atomicInteger.addAndGet(1) > 100) {
            atomicInteger.set(0);
            CompletableFuture.runAsync(this::refreshQrcodeMap);
        }

        return qrcode;
    }

    public void refreshQrcodeMap() {
        final ReentrantLock lock = reentrantLock;
        if (!lock.tryLock()) {
            return;
        }
        try {
            Set<Map.Entry<String, QRCodePayResponseDTO>> entrySet = qrcodeMap.entrySet();
            Iterator<Map.Entry<String, QRCodePayResponseDTO>> iterator = entrySet.iterator();
            int timestamp = (int) (System.currentTimeMillis() / 1000);
            while (iterator.hasNext()) {
                Map.Entry<String, QRCodePayResponseDTO> entry = iterator.next();
                QRCodePayResponseDTO payResponseDTO = entry.getValue();
                if (timestamp - payResponseDTO.getCreateTime() > 1700) {
                    iterator.remove();
                }
            }
        } finally {
            lock.unlock();
        }

    }
}
