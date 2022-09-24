package club.dbg.cms.admin.service.esp;

import club.dbg.cms.admin.dao.WeatherMapper;
import club.dbg.cms.admin.service.esp.pojo.EspLoginDTO;
import club.dbg.cms.admin.service.esp.pojo.EspWeatherDataDTO;
import club.dbg.cms.admin.service.login.LoginService;
import club.dbg.cms.admin.service.login.pojo.LoginRequest;
import club.dbg.cms.domain.admin.WeatherDO;
import club.dbg.cms.rpc.pojo.Operator;
import club.dbg.cms.rpc.pojo.TokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspServiceImpl implements EspService {
    private final static Logger log = LoggerFactory.getLogger(EspServiceImpl.class);

    private final WeatherMapper weatherMapper;

    private final LoginService loginService;

    public EspServiceImpl(
            WeatherMapper weatherMapper,
            LoginService loginService) {
        this.weatherMapper = weatherMapper;
        this.loginService = loginService;
    }

    @Override
    public String login(Operator operator, EspLoginDTO loginDTO) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(loginDTO.getUsername());
        loginRequest.setPassword(loginDTO.getPassword());
        try {
            TokenDTO tokenDTO = loginService.login(operator, loginRequest);
            return tokenDTO.getAccessToken();
        } catch (Exception e) {
            log.error("esp 设备登录失败", e);
            return "FAIL";
        }
    }

    @Override
    public String receive(Operator operator, EspWeatherDataDTO weatherDataDTO) {
        WeatherDO weatherDO = new WeatherDO();
        weatherDO.setCreationTime((int)(System.currentTimeMillis() / 1000));
        weatherDO.setDeviceId(operator.getId());
        weatherDO.setTemperature(weatherDataDTO.getT());
        weatherDO.setHumidity(weatherDataDTO.getH());
        weatherDO.setAtmosphericPressure(weatherDataDTO.getAp());
        weatherMapper.insert(weatherDO);
        return "OK";
    }

    @Override
    public List<WeatherDO> getWeatherDataList(Integer deviceId, Integer startTime, Integer endTime, Integer page) {
        final int pageSize = 30;
        page = (page - 1) * pageSize;
        return weatherMapper.selectByDeviceId(deviceId, startTime, endTime, page);
    }
}
