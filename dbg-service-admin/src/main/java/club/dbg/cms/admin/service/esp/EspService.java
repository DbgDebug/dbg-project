package club.dbg.cms.admin.service.esp;

import club.dbg.cms.admin.service.esp.pojo.EspLoginDTO;
import club.dbg.cms.admin.service.esp.pojo.EspWeatherDataDTO;
import club.dbg.cms.rpc.pojo.Operator;

public interface EspService {
    String login(Operator operator, EspLoginDTO loginDTO);
    String receive(Operator operator, EspWeatherDataDTO weatherDataDTO);
}
