package club.dbg.cms.video.service.device;

import club.dbg.cms.util.BCrypt;
import club.dbg.cms.util.UUIDUtils;
import club.dbg.cms.video.dao.DeviceMapper;
import club.dbg.cms.video.domain.DeviceDO;
import club.dbg.cms.video.exception.VideoException;
import club.dbg.cms.video.pojo.DeviceSession;
import club.dbg.cms.video.pojo.LoginDTO;
import club.dbg.cms.video.pojo.TokenDTO;
import club.dbg.cms.video.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {
    private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceMapper deviceMapper;

    private final RedisUtils redisUtils;

    public DeviceServiceImpl(DeviceMapper deviceMapper, RedisUtils redisUtils) {
        this.deviceMapper = deviceMapper;
        this.redisUtils  = redisUtils;
    }

    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        DeviceDO device = deviceMapper.selectByDeviceName(loginDTO.getUserName());
        if(device == null){
            throw new VideoException("用户名或密码错误");
        }
        if (!BCrypt.checkpw(loginDTO.getPassword(), device.getDevicePassword())) {
            throw new VideoException("用户名或密码错误");
        }
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setId(device.getId());
        tokenDTO.setToken(UUIDUtils.getUUID());
        String token = (String) redisUtils.get("DEVICE_ID-" + device.getId());
        if(token != null){
            redisUtils.delete("DEVICE-" + token);
            redisUtils.delete("DEVICE_ID-" + device.getId());
        }
        // 关闭ws连接

        DeviceSession deviceSession = new DeviceSession();
        deviceSession.setId(device.getId());
        deviceSession.setDeviceName(device.getDeviceName());
        deviceSession.setToken(tokenDTO.getToken());
        deviceSession.setType(0);
        redisUtils.set("DEVICE_ID-" + deviceSession.getId(), deviceSession.getToken(), 3600);
        redisUtils.set("DEVICE-" + deviceSession.getToken(), deviceSession, 3600);

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
        redisUtils.set("WS_AUTH_DEVICE-" + tokenStr, token, 60);
        return tokenDTO;
    }
}
