package club.dbg.cms.admin.service.common;

import club.dbg.cms.rpc.pojo.ResponseResultDTO;
import club.dbg.cms.rpc.pojo.TokenDTO;
import club.dbg.cms.util.UUIDUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author dbg
 * @date 2019/08/26
 */

@Service
public class CommonServiceImpl implements CommonService {
    @Override
    public ResponseResultDTO init() {
        ResponseResultDTO response = new ResponseResultDTO();
        response.setCode(HttpStatus.OK.value());
        response.setData("");
        TokenDTO token = new TokenDTO();
        token.setAccessToken(UUIDUtils.getUUID());
        response.setData(token);
        return response;
    }
}
