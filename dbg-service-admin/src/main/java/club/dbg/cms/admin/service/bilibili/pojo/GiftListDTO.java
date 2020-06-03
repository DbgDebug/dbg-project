package club.dbg.cms.admin.service.bilibili.pojo;

import java.util.List;

public class GiftListDTO {
    private List<GiftDTO> giftList;
    private Integer total;

    public List<GiftDTO> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<GiftDTO> giftList) {
        this.giftList = giftList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
