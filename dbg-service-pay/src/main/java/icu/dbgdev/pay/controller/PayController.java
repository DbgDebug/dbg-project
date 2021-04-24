package icu.dbgdev.pay.controller;

import club.dbg.cms.util.ResponseBuild;
import icu.dbgdev.pay.dto.QRCodePayResponseDTO;
import icu.dbgdev.pay.service.pay.PayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PayController {
    private final PayService payService;

    public PayController(PayService payService) {
        this.payService = payService;
    }

    @RequestMapping(value = "/pay_qrcode", method = RequestMethod.POST)
    public ResponseBuild<QRCodePayResponseDTO> getPaymentQRCode(
            @RequestParam("paymentWay") String paymentWay,
            @RequestParam("orderId") Long orderId) {
        return ResponseBuild.ok(payService.getPaymentQRCode(paymentWay, orderId));
    }
}
