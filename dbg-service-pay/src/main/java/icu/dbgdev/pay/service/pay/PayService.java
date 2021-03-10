package icu.dbgdev.pay.service.pay;

import com.alipay.api.AlipayApiException;
import icu.dbgdev.pay.dto.QRCodePayResponseDTO;
import icu.dbgdev.pay.dto.TradeInfoDTO;
import icu.dbgdev.pay.exception.PayException;
import icu.dbgdev.pay.service.alipay.IAlipayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PayService implements IPayService {
    private final Logger log = LoggerFactory.getLogger(PayService.class);
    private final IAlipayService alipayService;

    public PayService(IAlipayService alipayService) {
        this.alipayService = alipayService;
    }

    @Override
    public QRCodePayResponseDTO getPaymentQRCode(String paymentWay, Long orderId) {
        QRCodePayResponseDTO qrCodePayResponseDTO = null;
        TradeInfoDTO tradeInfoDTO = new TradeInfoDTO();
        tradeInfoDTO.setOutTradeNo(orderId.toString());
        tradeInfoDTO.setTotalAmount(new BigDecimal("100.00"));
        tradeInfoDTO.setSubject("SSD存储设备");
        try {
            switch (paymentWay) {
                case "alipay":
                    qrCodePayResponseDTO = alipayService.createPayQRCode(tradeInfoDTO);
                    break;
                case "wxpay":
                    break;
                default:
                    throw PayException.build("获取支付二维码失败");
            }
        } catch (Exception e) {
            log.error("获取支付二维码异常:", e);
            throw PayException.build("获取支付二维码失败");
        }

        return qrCodePayResponseDTO;
    }
}
