package icu.dbgdev.pay.service.alipay;

import com.alipay.api.AlipayApiException;
import icu.dbgdev.pay.dto.QRCodePayResponseDTO;
import icu.dbgdev.pay.dto.TradeInfoDTO;

public interface IAlipayService {
    QRCodePayResponseDTO createPay(TradeInfoDTO tradeInfoDTO) throws AlipayApiException;
}
