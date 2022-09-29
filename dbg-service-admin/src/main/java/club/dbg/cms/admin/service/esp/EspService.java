package club.dbg.cms.admin.service.esp;

import club.dbg.cms.admin.service.esp.pojo.EspLoginDTO;
import club.dbg.cms.admin.service.esp.pojo.EspWeatherDataDTO;
import club.dbg.cms.admin.service.esp.pojo.WeatherDataListDTO;
import club.dbg.cms.domain.admin.WeatherDO;
import club.dbg.cms.rpc.pojo.Operator;

import java.util.List;

public interface EspService {
    String login(Operator operator, EspLoginDTO loginDTO);
    String receive(Operator operator, EspWeatherDataDTO weatherDataDTO);

    WeatherDataListDTO getWeatherDataList(Integer deviceId, Integer startTime, Integer endTime, Integer page);
}
