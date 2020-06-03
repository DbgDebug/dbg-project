package club.dbg.cms.admin.service.bilibili.pojo;

import java.util.HashSet;

public class WelcomeStatistic {
    private Integer roomId;
    private HashSet<Integer> welcomeSet = new HashSet<>();
    private Integer startTime;
    private Integer endTime;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public HashSet<Integer> getWelcomeSet() {
        return welcomeSet;
    }

    public void setWelcomeSet(HashSet<Integer> welcomeSet) {
        this.welcomeSet = welcomeSet;
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
}
