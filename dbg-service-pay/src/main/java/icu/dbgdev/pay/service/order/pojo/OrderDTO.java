package icu.dbgdev.pay.service.order.pojo;

import icu.dbgdev.pay.service.product.pojo.ProductDTO;

import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {
    private Long orderId;
    private BigDecimal totalAmount;
    private Integer createTime;
    private Integer paymentTime;
    private Integer deliveryTime;
    private Integer expressTime;
    private Integer finishTime;
    private Integer splitTime;
    private String splitReason;
    private List<ProductDTO> productList;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Integer paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getExpressTime() {
        return expressTime;
    }

    public void setExpressTime(Integer expressTime) {
        this.expressTime = expressTime;
    }

    public Integer getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getSplitTime() {
        return splitTime;
    }

    public void setSplitTime(Integer splitTime) {
        this.splitTime = splitTime;
    }

    public String getSplitReason() {
        return splitReason;
    }

    public void setSplitReason(String splitReason) {
        this.splitReason = splitReason;
    }

    public List<ProductDTO> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDTO> productList) {
        this.productList = productList;
    }
}
