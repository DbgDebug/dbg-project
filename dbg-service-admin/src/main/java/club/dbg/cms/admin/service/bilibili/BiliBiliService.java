package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.domain.admin.LiveRoomDO;
import club.dbg.cms.admin.service.bilibili.pojo.DanmuList;
import club.dbg.cms.admin.service.bilibili.pojo.UpDTO;

import java.util.List;

public interface BiliBiliService {
    List<UpDTO> getUpList();

    DanmuList getDanmuList(Integer roomid, Long startTime, Long endTime, Integer page, Integer pageSize);

    DanmuList getCacheDanmuList();

    Integer addRoom(LiveRoomDO room);

    Boolean editRoom(LiveRoomDO liveRoom);

    Boolean deleteRoom(Integer id);

    List<LiveRoomDO> getRoomList();

    List<LiveRoomDO> getMonitorRoomList();

    Boolean start(Integer id);

    Boolean stop(Integer id);

    Boolean stopNow(Integer id);

    boolean refreshStatistics(Integer date);
}
