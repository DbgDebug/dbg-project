package club.dbg.cms.admin.service.login;

import club.dbg.cms.admin.pojo.VerificationCodeDTO;
import club.dbg.cms.admin.service.login.pojo.LoginRequest;
import club.dbg.cms.rpc.pojo.TokenDTO;

/**
 * LoginService.class
 *
 * @author dbg
 * @date 2019/08/16
 */
public interface LoginService {
    /**
     * 登录
     *
     * @param loginRequest 登录信息
     * @return Response
     */
    TokenDTO login(LoginRequest loginRequest);

    /**
     * 登出
     * @param id id
     * @return Boolean
     */
    Boolean logout(Integer id, String token);

    /**
     * 获取验证码
     *
     * @return VerificationCode
     */
    VerificationCodeDTO getVerificationCode();

    /**
     * 发送邮箱验证码
     * @param username 用户名
     * @return VerificationCode
     */
    VerificationCodeDTO getEmailCode(String username);
}
