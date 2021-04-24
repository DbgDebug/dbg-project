package icu.dbgdev.pay.dto;

public class QRCodePayResponseDTO {
    private String outTradeNo;
    private String subject;
    private String totalAmount;
    private String qrCodeUrl;
    private Long createTime;

    public QRCodePayResponseDTO() {

    }


    public QRCodePayResponseDTO(String outTradeNo, String qrCodeUrl) {
        this.outTradeNo = outTradeNo;
        this.qrCodeUrl = qrCodeUrl;
        createTime = System.currentTimeMillis() / 1000;
    }

    public QRCodePayResponseDTO(String code, String outTradeNo, String subject, String totalAmount, String qrCodeUrl) {
        this.outTradeNo = outTradeNo;
        this.subject = subject;
        this.totalAmount = totalAmount;
        this.qrCodeUrl = qrCodeUrl;
        createTime = System.currentTimeMillis() / 1000;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "QRCodePayResponseDTO{" +
                "outTradeNo='" + outTradeNo + '\'' +
                ", subject='" + subject + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", qrCodeUrl='" + qrCodeUrl + '\'' +
                '}';
    }
}
