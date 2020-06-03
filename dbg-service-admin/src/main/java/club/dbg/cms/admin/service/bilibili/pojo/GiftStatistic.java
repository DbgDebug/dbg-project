package club.dbg.cms.admin.service.bilibili.pojo;

import java.util.HashMap;

public class GiftStatistic {
    private int roomId;
    private Integer startTime;
    private Integer endTime;
    private HashMap<Integer, GiftCount> giftMap = new HashMap<>();

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public HashMap<Integer, GiftCount> getGiftMap() {
        return giftMap;
    }

    public void setGiftMap(HashMap<Integer, GiftCount> giftMap) {
        this.giftMap = giftMap;
    }
}
