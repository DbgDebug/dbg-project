package club.dbg.cms.domain.admin;

public class GiftDO {
    private Integer id;
    private Integer roomId;
    private Integer uid;
    private Integer giftNum;
    private Integer giftId;
    private Integer price;
    private String giftName;
    private String username;
    private Integer sendTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSendTime() {
        return sendTime;
    }

    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }
}
