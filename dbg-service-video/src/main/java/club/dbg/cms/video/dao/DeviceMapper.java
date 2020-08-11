package club.dbg.cms.video.dao;

import club.dbg.cms.video.domain.DeviceDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceMapper {
    @Select({
            "SELECT ID, CreateTime, UpdateTime, Status, DeviceName, DevicePassword, Description",
            "FROM Device WHERE DeviceName = #{deviceName}"
    })
    DeviceDO selectByDeviceName(@Param("deviceName") String deviceName);

    @Select({
            "SELECT ID, CreateTime, UpdateTime, Status, DeviceName, DevicePassword, Description",
            "FROM Device WHERE ID = #{id}"
    })
    DeviceDO selectById(@Param("id") Integer id);

    @Insert({
            "INSERT INTO Device(CreateTime, UpdateTime, Status, DeviceName, DevicePassword, Description) ",
            "VALUES(#{createTime}, #{updateTime}, #{status}, #{deviceName}, #{devicePassword}, #{description})"
    })
    Integer insert();

    @Update({
            "UPDATE Device Set UpdateTime = #{updateTime}, Status = #{status}, DeviceName = #{deviceName},",
            "DevicePassword = #{devicePassword}, Description = #{description}"
    })
    Integer update();

    @Delete({
            "DELETE FROM Device WHERE ID = #{id}"
    })
    Integer delete(@Param("id") String id);
}
