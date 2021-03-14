package club.dbg.cms.video.service.device;

import club.dbg.cms.util.BCrypt;
import club.dbg.cms.util.JWTUtils;
import club.dbg.cms.util.UUIDUtils;
import club.dbg.cms.video.config.SaltConfig;
import club.dbg.cms.video.dao.DeviceMapper;
import club.dbg.cms.video.domain.DeviceDO;
import club.dbg.cms.video.exception.VideoException;
import club.dbg.cms.video.pojo.LoginDTO;
import club.dbg.cms.video.pojo.TokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {
    private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceMapper deviceMapper;

    public DeviceServiceImpl(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        DeviceDO deviceDO = deviceMapper.selectByDeviceName(loginDTO.getUserName());
        if (deviceDO == null) {
            throw new VideoException("用户名或密码错误");
        }
        if (!BCrypt.checkpw(loginDTO.getPassword(), deviceDO.getDevicePassword())) {
            throw new VideoException("用户名或密码错误");
        }
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(JWTUtils.createToken(deviceDO.getId(), deviceDO.getDeviceName(), 10080, deviceDO.getId() + SaltConfig.SALT));
        return tokenDTO;
    }

    @Override
    public Integer getDeviceId() {
        return null;
    }

    /**
     *
     * @param token token
     * @return token
     */
    @Override
    public TokenDTO getWebSocketToken(String token) {
        String tokenStr = UUIDUtils.getUUIDByMD5();
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(tokenStr);
        return tokenDTO;
    }
}
