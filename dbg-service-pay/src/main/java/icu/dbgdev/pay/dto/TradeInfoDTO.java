package icu.dbgdev.pay.dto;

import java.math.BigDecimal;

public class TradeInfoDTO {
    private String outTradeNo;
    private BigDecimal totalAmount;
    // 商品的标题/交易标题/订单标题/订单关键字等
    private String subject;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
