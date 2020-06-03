package club.dbg.cms.domain.admin;

public class DanmuDO {
    private int id;
    private int roomid;
    private int uid;
    private String nickname;
    private String danmu;
    private long sendTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDanmu() {
        return danmu;
    }

    public void setDanmu(String danmu) {
        this.danmu = danmu;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
