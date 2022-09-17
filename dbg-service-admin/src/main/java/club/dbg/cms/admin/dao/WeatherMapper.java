package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.WeatherDO;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherMapper {
    @Insert({
            "INSERT INTO tb_weather(device_id, creation_time, temperature, humidity, atmospheric_pressure)",
            "VALUES(#{deviceId}, #{creationTime}, #{temperature}, #{humidity}, #{atmosphericPressure})"
    })
    int insert(WeatherDO weatherDO);
}
