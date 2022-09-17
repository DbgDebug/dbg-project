package club.dbg.cms.admin.controller;

import club.dbg.cms.admin.config.ConfigConsts;
import club.dbg.cms.admin.filter.pojo.MyHttpServletRequest;
import club.dbg.cms.admin.pojo.VerificationCodeDTO;
import club.dbg.cms.admin.service.common.CommonService;
import club.dbg.cms.admin.service.login.LoginService;
import club.dbg.cms.admin.service.login.pojo.LoginRequest;
import club.dbg.cms.rpc.pojo.ResponseResultDTO;
import club.dbg.cms.rpc.pojo.TokenDTO;
import club.dbg.cms.util.ResponseBuild;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author dbg
 * @date 2019/08/17
 */
@Validated
@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final
    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(value = "/login", name = "登录", method = RequestMethod.POST)
    public ResponseBuild<TokenDTO> login(
            MyHttpServletRequest request,
            @Length(min = ConfigConsts.USERNAME_MIN,
                    max = ConfigConsts.USERNAME_MAX,
                    message = ConfigConsts.USERNAME_DESCRIBE)
            @RequestParam("username") String username,
            @Length(min = ConfigConsts.PASSWORD_MIN,
                    max = ConfigConsts.PASSWORD_MAX,
                    message = ConfigConsts.PASSWORD_DESCRIBE)
            @RequestParam("password") String password,
            @RequestParam(value = "verificationCode", required = false) String verificationCode,
            @RequestParam(value = "verificationToken", required = false) String verificationToken) {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        loginRequest.setVerificationCode(verificationCode);
        loginRequest.setVerificationToken(verificationToken);
        loginRequest.setIp(request.getOperator().getIp());
        return ResponseBuild.ok(loginService.login(request.getOperator(), loginRequest));
    }

    @RequestMapping(value = "/logout", name = "登出", method = RequestMethod.GET)
    public ResponseBuild<Boolean> logout(MyHttpServletRequest request) {
        int id = request.getOperator().getId();
        String token = request.getOperator().getAccessToken();
        return ResponseBuild.ok(loginService.logout(id, token));
    }

    @RequestMapping(value = "/get_verification_code", name = "获取验证码", method = RequestMethod.GET)
    public ResponseBuild<VerificationCodeDTO> getVerificationCode() {
        return ResponseBuild.ok(loginService.getVerificationCode());
    }

    @RequestMapping(value = "/get_email_code", name = "获取邮箱验证码", method = RequestMethod.GET)
    public ResponseBuild<VerificationCodeDTO> getEmailCode() {
        return ResponseBuild.ok(null);
    }
}
