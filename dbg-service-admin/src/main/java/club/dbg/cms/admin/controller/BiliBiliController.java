package club.dbg.cms.admin.controller;

import club.dbg.cms.domain.admin.LiveRoomDO;
import club.dbg.cms.admin.service.bilibili.BiliBiliServiceImpl;
import club.dbg.cms.admin.service.bilibili.pojo.DanmuList;
import club.dbg.cms.admin.service.bilibili.pojo.UpDTO;
import club.dbg.cms.rpc.pojo.ResponseResultDTO;
import club.dbg.cms.util.ResponseBuild;
import com.alibaba.fastjson.JSON;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author dbg
 */

@RestController
@Validated
public class BiliBiliController {
    public static final Logger log = LoggerFactory.getLogger(BiliBiliController.class);
    private final
    BiliBiliServiceImpl bilibiliService;

    public BiliBiliController(BiliBiliServiceImpl bilibiliService) {
        this.bilibiliService = bilibiliService;
    }

    @RequestMapping(value = "get_danmu_list", name = "获取弹幕列表", method = RequestMethod.GET)
    public ResponseEntity<ResponseResultDTO> getDanmuList(
            @Range(min = -1, max = 999999999, message = "房间id范围-1-999999999")
            @RequestParam(value = "roomid", defaultValue = "-1") Integer roomid,
            @Min(value = 1, message = "开始时间不能小于1")
            @RequestParam(value = "startTime") Long startTime,
            @Min(value = 1, message = "结束时间不能小于1")
            @RequestParam(value = "endTime") Long endTime,
            @Range(min = 1, max = 10000000, message = "页数为1-10000000")
            @RequestParam(value = "page") Integer page,
            @Range(min = 1, max = 200, message = "页大小为1-200")
            @RequestParam(value = "pageSize") Integer pageSize) {
        DanmuList danmuList = bilibiliService.getDanmuList(roomid, startTime, endTime, page, pageSize);
        ResponseResultDTO response = new ResponseResultDTO();
        response.setData(danmuList);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "get_cache_danmu", name = "获取缓存弹幕", method = RequestMethod.GET)
    public ResponseEntity<ResponseBuild<DanmuList>> getCacheDanmu() {
        return ResponseBuild.build(bilibiliService.getCacheDanmuList());
    }

    @RequestMapping(value = "get_up_list", name = "获取UP列表", method = RequestMethod.GET)
    public ResponseEntity<ResponseResultDTO> getUpList() {
        List<UpDTO> upList = bilibiliService.getUpList();
        ResponseResultDTO response = new ResponseResultDTO();
        response.setData(upList);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "get_room_list", name = "获取直播间列表", method = RequestMethod.GET)
    public ResponseEntity<ResponseResultDTO> getRoomList() {
        List<LiveRoomDO> liveRooms = bilibiliService.getRoomList();
        ResponseResultDTO response = new ResponseResultDTO();
        response.setData(liveRooms);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "get_monitor_room_list", name = "获取已连接的直播间列表", method = RequestMethod.GET)
    public ResponseEntity<ResponseResultDTO> getMonitorRoomList() {
        List<LiveRoomDO> roomInfos = bilibiliService.getMonitorRoomList();
        ResponseResultDTO response = new ResponseResultDTO();
        response.setData(roomInfos);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "add_room", name = "添加直播间", method = RequestMethod.POST)
    public ResponseEntity<ResponseResultDTO> addRoom(
            @Range(min = 0, max = 999999999, message = "房间id范围0-999999999")
            @RequestParam("roomid") Integer roomid,
            @Length(min = 1, max = 16, message = "up名字长度为1-16")
            @RequestParam("up") String up) {
        LiveRoomDO liveRoom = new LiveRoomDO();
        liveRoom.setRoomid(roomid);
        liveRoom.setUp(up);
        log.info(JSON.toJSONString(liveRoom));
        Integer id = bilibiliService.addRoom(liveRoom);
        ResponseResultDTO response = new ResponseResultDTO();
        response.setData(id);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "edit_room", name = "编辑直播间", method = RequestMethod.POST)
    public ResponseEntity<ResponseResultDTO> editRoom(
            @Range(min = 0, max = 2147483647, message = "id范围0-2147483647")
            @RequestParam("id") Integer id,
            @Range(min = 0, max = 999999999, message = "房间id范围0-999999999")
            @RequestParam("roomid") Integer roomid,
            @Length(min = 1, max = 16, message = "up名字长度为1-16")
            @RequestParam("up") String up) {
        LiveRoomDO liveRoom = new LiveRoomDO();
        liveRoom.setId(id);
        liveRoom.setRoomid(roomid);
        liveRoom.setUp(up);
        ResponseResultDTO response = new ResponseResultDTO();
        response.setData(bilibiliService.editRoom(liveRoom));
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "delete_room", name = "删除直播间", method = RequestMethod.POST)
    public ResponseEntity<ResponseResultDTO> deleteRoom(
            @Range(min = 0, max = 2147483647, message = "id范围0-2147483647")
            @RequestParam("id") Integer id) {
        boolean result = bilibiliService.deleteRoom(id);
        ResponseResultDTO response = new ResponseResultDTO();
        response.setData(result);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "start", name = "直播间弹幕服务连接", method = RequestMethod.POST)
    public ResponseEntity<ResponseResultDTO> start(
            @Range(min = 0, max = 2147483647, message = "id范围0-2147483647")
            @RequestParam("id") Integer id) {
        ResponseResultDTO response = new ResponseResultDTO();
        response.setData(bilibiliService.start(id));
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "stop", name = "直播间弹幕服务切断", method = RequestMethod.POST)
    public ResponseEntity<ResponseResultDTO> stop(
            @Range(min = 0, max = 2147483647, message = "id范围0-2147483647")
            @RequestParam("id") Integer id) {
        boolean result = bilibiliService.stop(id);
        ResponseResultDTO response = new ResponseResultDTO();
        response.setData(result);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "refresh_statistics", name = "刷新统计数据", method = RequestMethod.GET)
    public ResponseEntity<ResponseBuild<Boolean>> refreshStatistics(@Range(min = 0, max = Integer.MAX_VALUE, message = "范围0-2147483647")
                                                                    @RequestParam("date") Integer date) {
        return ResponseBuild.build(bilibiliService.refreshStatistics(date));
    }

    /**
     @PermissionMapping(name = "获取真实房间号")
     @RequestMapping(value = "get_real_room_id", method = RequestMethod.GET)
     public ResponseEntity<ResponseDTO> getRealRoomId(
     @Range(min = 0, max = 999999999, message = "房间id范围0-999999999")
     @RequestParam(value = "room_id") Integer roomId) {
     ResponseDTO response = new ResponseDTO();
     response.setData(bilibiliService.getRoomId(roomId));
     return ResponseEntity.ok(response);
     }
     */
}
