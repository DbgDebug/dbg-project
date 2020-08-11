package club.dbg.cms.video.service.device;

import club.dbg.cms.video.pojo.LoginDTO;
import club.dbg.cms.video.pojo.TokenDTO;

public interface DeviceService {
    TokenDTO login(LoginDTO loginDTO);

    Integer getDeviceId();

    TokenDTO getWebSocketToken(String token);
}
