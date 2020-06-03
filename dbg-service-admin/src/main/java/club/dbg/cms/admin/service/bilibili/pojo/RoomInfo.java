package club.dbg.cms.admin.service.bilibili.pojo;

import club.dbg.cms.admin.service.bilibili.DanmuReceiveThread;

import java.util.concurrent.Future;

public class RoomInfo {
    private int id;
    private String up;
    private int roomid;
    private int port;
    private DanmuReceiveThread danmuThread;
    private Future<?> task;

    public Future<?> getTask() {
        return task;
    }

    public void setTask(Future<?> task) {
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public DanmuReceiveThread getDanmuThread() {
        return danmuThread;
    }

    public void setDanmuThread(DanmuReceiveThread danmuThread) {
        this.danmuThread = danmuThread;
    }

    public void close() {
        closeNow();
    }

    public void closeNow() {
        danmuThread.closeNow();
    }
}
