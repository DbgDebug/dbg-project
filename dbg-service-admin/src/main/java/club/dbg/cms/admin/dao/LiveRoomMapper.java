package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.LiveRoomDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dbg
 */
@Repository
public interface LiveRoomMapper {

    @Select({
            "select tb_live_room.* from tb_live_room where id = #{id}"
    })
    LiveRoomDO getRoomById(@Param("id") Integer id);

    @Select({
            "select tb_live_room.* from tb_live_room where roomid = #{roomid}"
    })
    LiveRoomDO getRoomByRoomId(@Param("roomid") Integer roomId);

    @Select({
            "select tb_live_room.* from tb_live_room"
    })
    List<LiveRoomDO> getRoomList();

    @Insert({
            "insert into tb_live_room(roomid, up, create_time, update_time) ",
            "values(#{roomid}, #{up}, #{createTime}, #{updateTime})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertRoom(LiveRoomDO liveRoom);

    @Update({
            "update tb_live_room set roomid = #{roomid}, up = #{up}, update_time = #{updateTime} where id = #{id}"
    })
    int updateRoom(LiveRoomDO liveRoom);

    @Delete({
            "delete from tb_live_room where id = #{id}"
    })
    int deleteRoom(Integer id);
}
