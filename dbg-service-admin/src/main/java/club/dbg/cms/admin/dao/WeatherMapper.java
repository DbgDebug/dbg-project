package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.WeatherDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherMapper {
    @Insert({
            "INSERT INTO tb_weather(device_id, creation_time, temperature, humidity, atmospheric_pressure)",
            "VALUES(#{deviceId}, #{creationTime}, #{temperature}, #{humidity}, #{atmosphericPressure})"
    })
    int insert(WeatherDO weatherDO);

    @Select({
            "SELECT creation_time, temperature, humidity ",
            "FROM tb_weather WHERE device_id = #{deviceId} ORDER BY id DESC LIMIT 30"
    })
    List<WeatherDO> selectByDeviceId(@Param("deviceId") Integer deviceId);
}
