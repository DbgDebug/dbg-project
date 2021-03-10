package icu.dbgdev.pay.service.pay;

import icu.dbgdev.pay.dto.QRCodePayResponseDTO;

public interface IPayService {
    QRCodePayResponseDTO getPaymentQRCode(String paymentWay, Long orderId);
}
