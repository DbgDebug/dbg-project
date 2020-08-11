package club.dbg.cms.video.service.user;

import club.dbg.cms.util.BCrypt;
import club.dbg.cms.util.UUIDUtils;
import club.dbg.cms.video.dao.UserMapper;
import club.dbg.cms.video.domain.UserDO;
import club.dbg.cms.video.exception.VideoException;
import club.dbg.cms.video.pojo.LoginDTO;
import club.dbg.cms.video.pojo.TokenDTO;
import club.dbg.cms.video.pojo.UserSession;
import club.dbg.cms.video.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;

    private final RedisUtils redisUtils;

    public UserServiceImpl(UserMapper userMapper, RedisUtils redisUtils) {
        this.userMapper = userMapper;
        this.redisUtils = redisUtils;
    }

    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        UserDO userDO = userMapper.selectByUserName(loginDTO.getUserName());
        if (userDO == null) {
            throw new VideoException("用户名或密码错误");
        }
        if (!BCrypt.checkpw(loginDTO.getPassword(), userDO.getPassword())) {
            throw new VideoException("用户名或密码错误");
        }
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(UUIDUtils.getUUID());
        String token = (String) redisUtils.get("USER_ID-" + userDO.getId());
        if(token != null){
            redisUtils.delete("USER-" + token);
            redisUtils.delete("USER_ID-" + userDO.getId());
        }
        UserSession userSession = new UserSession();
        userSession.setId(userDO.getId());
        userSession.setToken(tokenDTO.getToken());
        redisUtils.set("USER-" + userSession.getToken(), userSession, 1800);
        redisUtils.set("USER_ID-" + userSession.getId(), userSession.getToken(), 1800);
        return tokenDTO;
    }

    @Override
    public TokenDTO getWebSocketToken(String token) {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(UUIDUtils.getUUIDByMD5());
        redisUtils.set("WS_AUTH_USER-" + tokenDTO.getToken(), token, 20);
        return tokenDTO;
    }
}
