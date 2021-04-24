package icu.dbgdev.pay.service.pay;

import icu.dbgdev.pay.dto.QRCodePayResponseDTO;
import icu.dbgdev.pay.dto.TradeInfoDTO;
import icu.dbgdev.pay.exception.PayException;
import icu.dbgdev.pay.service.alipay.IAlipayService;
import icu.dbgdev.pay.service.order.OrderService;
import icu.dbgdev.pay.service.order.pojo.OrderDTO;
import icu.dbgdev.pay.service.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PayService implements IPayService {
    private final Logger log = LoggerFactory.getLogger(PayService.class);

    private final IAlipayService alipayService;

    private final ProductService productService;

    private final OrderService orderService;

    public PayService(
            IAlipayService alipayService,
            ProductService productService,
            OrderService orderService) {
        this.alipayService = alipayService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @Override
    public QRCodePayResponseDTO getPaymentQRCode(String paymentWay, Long orderId) {
        OrderDTO order = orderService.getOrderDetail(orderId);
        if (order == null) {
            throw PayException.build("订单不存在");
        }
        QRCodePayResponseDTO qrCodePayResponseDTO = null;
        TradeInfoDTO tradeInfoDTO = new TradeInfoDTO();
        tradeInfoDTO.setOutTradeNo(order.getOrderId().toString());
        tradeInfoDTO.setTotalAmount(order.getTotalAmount());
        tradeInfoDTO.setSubject("肥宅零食");
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
