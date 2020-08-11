package club.dbg.cms.video.service.user;

import club.dbg.cms.video.pojo.LoginDTO;
import club.dbg.cms.video.pojo.TokenDTO;

public interface UserService {
    TokenDTO login(LoginDTO loginDTO);

    TokenDTO getWebSocketToken(String token);
}
