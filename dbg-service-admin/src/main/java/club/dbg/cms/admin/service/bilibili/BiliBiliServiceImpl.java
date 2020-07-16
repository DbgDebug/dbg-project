package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.dao.DanmuMapper;
import club.dbg.cms.admin.dao.LiveRoomMapper;
import club.dbg.cms.domain.admin.LiveRoomDO;
import club.dbg.cms.admin.exception.BusinessException;
import club.dbg.cms.admin.service.bilibili.pojo.DanmuList;
import club.dbg.cms.admin.service.bilibili.pojo.RoomInfo;
import club.dbg.cms.admin.service.bilibili.pojo.UpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author dbg
 */

@Service
public class BiliBiliServiceImpl implements BiliBiliService {
    private static final Logger log = LoggerFactory.getLogger(BiliBiliServiceImpl.class);

    private final DanmuMapper danmuMapper;

    private final LiveRoomMapper liveRoomMapper;

    private final MessageHandleService messageHandleService;

    private final HeartBeatTask heartBeatTask;

    private final BiliBiliApi bilibiliApi;

    private final BiliBiliStatistics biliBiliStatistics;

    private final ConcurrentHashMap<Integer, RoomInfo> roomMap = new ConcurrentHashMap<>();

    /**
     * 核心池大小
     */
    private final int CORE_POOL_SIZE = 1;
    /**
     * 最大房间连接数
     */
    private final int ROOM_MAXIMUM_SIZE = 20;
    /**
     * 线程空闲清除时间,毫秒
     */
    private final int KEEP_ALIVE_TIME = 0;

    /**
     * 用于连接房间弹幕服务器的线程池
     */
    private final ThreadPoolExecutor roomPoolExecutor = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            ROOM_MAXIMUM_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.MILLISECONDS,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.AbortPolicy());

    public BiliBiliServiceImpl(DanmuMapper danmuMapper,
                               BiliBiliApi bilibiliApi,
                               LiveRoomMapper liveRoomMapper,
                               MessageHandleService messageHandleService,
                               HeartBeatTask heartBeatTask, BiliBiliStatistics biliBiliStatistics) {
        this.danmuMapper = danmuMapper;
        this.bilibiliApi = bilibiliApi;
        this.liveRoomMapper = liveRoomMapper;
        this.messageHandleService = messageHandleService;
        this.heartBeatTask = heartBeatTask;
        this.biliBiliStatistics = biliBiliStatistics;
    }

    /**
     * 获取up列表，用于select输入框
     *
     * @return List<Up>
     */
    @Override
    public List<UpDTO> getUpList() {
        List<UpDTO> upList = new LinkedList<>();
        List<LiveRoomDO> liveRooms = liveRoomMapper.getRoomList();
        liveRooms.forEach(item -> {
            UpDTO up = new UpDTO();
            up.setUp(item.getUp());
            up.setRoomid(item.getRoomid());
            upList.add(up);
        });
        return upList;
    }

    /**
     * 获取弹幕
     *
     * @param roomid    房间编号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page      页数
     * @param pageSize  页大小
     * @return DanmuList
     */
    @Override
    public DanmuList getDanmuList(Integer roomid, Long startTime, Long endTime, Integer page, Integer pageSize) {
        DanmuList danmuList = new DanmuList();
        if (startTime > endTime) {
            long time = startTime;
            startTime = endTime;
            endTime = time;
        }
        page = (page - 1) * pageSize;
        danmuList.setDanmuList(danmuMapper.selectDanmuList(roomid, startTime, endTime, page, pageSize));
        danmuList.setTotal(danmuMapper.danmuCount(roomid, startTime, endTime));
        return danmuList;
    }

    /**
     * 获取缓存中的弹幕
     *
     * @return List<DanmuDO>
     */
    @Override
    public DanmuList getCacheDanmuList() {
        DanmuList danmuList = new DanmuList();
        danmuList.setDanmuList(messageHandleService.getDanmuCacheList());
        danmuList.setTotal(danmuList.getDanmuList().size());
        return danmuList;
    }

    /**
     * 添加直播间
     *
     * @param room 房间信息
     * @return 添加房间的id
     */
    @Override
    public Integer addRoom(LiveRoomDO room) {
        String roomid = bilibiliApi.getRoomId(room.getRoomid());
        if (roomid == null) {
            throw new BusinessException("房间号不存在");
        }
        room.setRoomid(Integer.parseInt(roomid));
        long timestamp = System.currentTimeMillis() / 1000;
        room.setCreateTime(timestamp);
        room.setUpdateTime(timestamp);
        synchronized (this) {
            LiveRoomDO liveRoom = liveRoomMapper.getRoomByRoomId(room.getRoomid());
            if (liveRoom != null) {
                throw new BusinessException("房间号已存在");
            }
            liveRoomMapper.insertRoom(room);
        }
        if (room.getId() <= 0) {
            throw new BusinessException("添加房间失败");
        }
        return room.getId();
    }

    /**
     * 编辑房间信息
     *
     * @param liveRoom 房间信息
     * @return Boolean
     */
    @Override
    public Boolean editRoom(LiveRoomDO liveRoom) {
        long timestamp = System.currentTimeMillis() / 1000;
        liveRoom.setUpdateTime(timestamp);
        if (liveRoomMapper.updateRoom(liveRoom) <= 0) {
            throw new BusinessException("更新房间信息失败");
        }
        RoomInfo roomInfo = roomMap.get(liveRoom.getId());
        if (roomInfo != null && liveRoom.getRoomid() != roomInfo.getRoomid()) {
            roomInfo.close();
        }
        return true;
    }

    /**
     * 删除房间
     *
     * @param id 房间编号
     * @return Boolean
     */
    @Override
    public Boolean deleteRoom(Integer id) {
        RoomInfo roomInfo = roomMap.remove(id);
        if (roomInfo != null) {
            roomInfo.close();
        }
        if (liveRoomMapper.deleteRoom(id) <= 0) {
            throw new BusinessException("删除房间失败");
        }
        return true;
    }

    /**
     * 获取所有房间列表，显示房间是否连接状态
     *
     * @return List<LiveRoom>
     */
    @Override
    public List<LiveRoomDO> getRoomList() {
        List<LiveRoomDO> liveRooms = liveRoomMapper.getRoomList();
        liveRooms.forEach((liveRoom) -> {
            if (roomMap.containsKey(liveRoom.getId())) {
                liveRoom.setStatus(1);
            }
        });
        return liveRooms;
    }

    /**
     * 获取正在连接的房间列表
     *
     * @return List<LiveRoom>
     */
    @Override
    public List<LiveRoomDO> getMonitorRoomList() {
        List<LiveRoomDO> roomList = new ArrayList<>();
        this.roomMap.forEach((key, value) -> {
            LiveRoomDO liveRoom = new LiveRoomDO();
            liveRoom.setId(key);
            liveRoom.setRoomid(value.getRoomid());
            liveRoom.setUp(value.getUp());
            roomList.add(liveRoom);
        });
        return roomList;
    }

    /**
     * 开始连接房间获取弹幕
     *
     * @param id 编号
     * @return Boolean
     */
    @Override
    public Boolean start(Integer id) {
        LiveRoomDO liveRoom;
        RoomInfo roomInfo = new RoomInfo();
        synchronized (this) {
            if (roomMap.containsKey(id)) {
                return true;
            }
            roomMap.put(id, roomInfo);
            if (roomPoolExecutor.getActiveCount() >= ROOM_MAXIMUM_SIZE) {
                roomMap.remove(id);
                throw new BusinessException("已达到最大连接数");
            }
        }

        liveRoom = liveRoomMapper.getRoomById(id);
        if (liveRoom == null) {
            roomMap.remove(id);
            throw new BusinessException("房间不存在");
        }
        roomInfo.setId(liveRoom.getId());
        roomInfo.setRoomid(liveRoom.getRoomid());
        roomInfo.setUp(liveRoom.getUp());

        DanmuReceiveThread danmuThread =
                new DanmuReceiveThread(this, messageHandleService, roomInfo, heartBeatTask, bilibiliApi);
        roomInfo.setDanmuThread(danmuThread);
        try {
            Future<?> task = roomPoolExecutor.submit(danmuThread);
            roomInfo.setTask(task);
        } catch (Exception e) {
            roomMap.remove(id);
            log.error("任务被拒绝:{}", e.getMessage());
            throw new BusinessException("任务被拒绝");
        }
        return true;
    }

    /**
     * 停止连接房间
     *
     * @param id 编号
     * @return Boolean
     */
    @Override
    synchronized public Boolean stop(Integer id) {
        RoomInfo roomInfo = roomMap.remove(id);
        if (roomInfo != null) {
            roomInfo.close();
        } else {
            return false;
        }
        return true;
    }

    /**
     * 停止连接房间
     *
     * @param id 编号
     * @return Boolean
     */
    @Override
    synchronized public Boolean stopNow(Integer id) {
        RoomInfo roomInfo = roomMap.remove(id);
        if (roomInfo != null) {
            roomInfo.closeNow();
        } else {
            return false;
        }
        log.info("房间{}已强制退出", roomInfo.getRoomid());
        return true;
    }

    @Override
    public boolean refreshStatistics(Integer date) {
        return biliBiliStatistics.refreshStatistics(date);
    }
}
