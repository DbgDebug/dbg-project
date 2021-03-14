package club.dbg.cms.video.service.user;

import club.dbg.cms.util.BCrypt;
import club.dbg.cms.util.JWTUtils;
import club.dbg.cms.util.UUIDUtils;
import club.dbg.cms.video.config.SaltConfig;
import club.dbg.cms.video.dao.UserMapper;
import club.dbg.cms.video.domain.UserDO;
import club.dbg.cms.video.exception.VideoException;
import club.dbg.cms.video.pojo.LoginDTO;
import club.dbg.cms.video.pojo.TokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
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
        tokenDTO.setToken(JWTUtils.createToken(userDO.getId(), userDO.getUserName(), 10080, userDO.getId() + SaltConfig.SALT));
        return tokenDTO;
    }

    @Override
    public TokenDTO getWebSocketToken(String token) {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(UUIDUtils.getUUIDByMD5());
        return tokenDTO;
    }
}
