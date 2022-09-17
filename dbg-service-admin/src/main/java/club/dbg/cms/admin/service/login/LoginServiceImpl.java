package club.dbg.cms.admin.service.login;

import club.dbg.cms.admin.dao.AccountMapper;
import club.dbg.cms.domain.admin.AccountDO;
import club.dbg.cms.admin.dao.AccountRoleMapper;
import club.dbg.cms.admin.exception.ActionException;
import club.dbg.cms.admin.exception.BusinessException;
import club.dbg.cms.admin.pojo.VerificationCodeDTO;
import club.dbg.cms.admin.redis.RedisUtils;
import club.dbg.cms.admin.service.rediscache.LoginCacheService;
import club.dbg.cms.admin.service.login.pojo.LoginRequest;
import club.dbg.cms.admin.utils.VerificationCodeUtils;
import club.dbg.cms.rpc.pojo.Operator;
import club.dbg.cms.rpc.pojo.TokenDTO;
import club.dbg.cms.util.BCrypt;
import club.dbg.cms.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * LoginServiceImpl.class
 *
 * @author dbg
 * @date 2019/08/16
 */

@Service
public class LoginServiceImpl implements LoginService {

    private final VerificationCodeUtils codeUtils;

    private final AccountMapper accountMapper;

    private final RedisUtils redisUtils;

    private final AccountRoleMapper accountRoleMapper;

    private final LoginCacheService loginCacheService;

    private final Integer loginTimeOut;

    private final Integer loginCount;

    private final String loginCountFlag;

    public LoginServiceImpl(
            RedisUtils redisUtils,
            AccountMapper accountMapper,
            AccountRoleMapper accountRoleMapper,
            LoginCacheService loginCacheService,
            VerificationCodeUtils verificationCodeUtils,
            @Value("${login.timeout}")
            Integer loginTimeOut,
            @Value("${login.loginCount}")
            Integer loginCount,
            @Value("${login.loginFlag}")
            String loginCountFlag) {
        this.redisUtils = redisUtils;
        this.accountMapper = accountMapper;
        this.accountRoleMapper = accountRoleMapper;
        this.loginCacheService = loginCacheService;
        this.codeUtils = verificationCodeUtils;
        this.loginTimeOut = loginTimeOut;
        this.loginCount = loginCount;
        this.loginCountFlag = loginCountFlag;
    }

    @Override
    public TokenDTO login(Operator session, LoginRequest login) {
        int count = verificationCode(login);
        AccountDO account = accountMapper.selectAccountByUsername(login.getUsername());
        if (account == null) {
            throw new BusinessException("用户名或密码错误");
        }
        String checkPassword = account.getPassword();
        if (!BCrypt.checkpw(login.getPassword(), checkPassword)) {
            redisUtils.set(loginCountFlag + login.getUsername(), ++count, loginTimeOut);
            throw new BusinessException("用户名或密码错误");
        }
        HashSet<Integer> roleIds = accountRoleMapper.selectRoleIdByAccountId(account.getId());
        String accessToken = UUIDUtils.getUUID();
        Operator operator = new Operator();
        operator.setId(account.getId());
        operator.setUsername(account.getUsername());
        operator.setRoleIds(roleIds);
        operator.setAccessToken(accessToken);
        // 将登录信息存入redis
        loginCacheService.set(accessToken, operator);
        // 移除登录计数
        redisUtils.delete(loginCountFlag + operator.getUsername());
        account.setLastTime(System.currentTimeMillis() / 1000);
        account.setLastIp(session.getIp());
        accountMapper.updateLoginInfo(account);
        // 设置返回的token
        TokenDTO token = new TokenDTO();
        token.setAccessToken(accessToken);
        return token;
    }

    @Override
    public Boolean logout(Integer id, String token) {
        loginCacheService.remove(token);
        return true;
    }

    @Override
    public VerificationCodeDTO getVerificationCode() {
        return codeUtils.getVerificationCode();
    }

    @Override
    public VerificationCodeDTO getEmailCode(String username) {
        AccountDO account = accountMapper.selectAccountByUsername(username);
        if (account == null) {
            throw new BusinessException("请输入正确的用户名");
        }
        codeUtils.getEmailCode(account.getEmail());
        return null;
    }

    private int verificationCode(LoginRequest login) {
        Integer count = (Integer) redisUtils.get(loginCountFlag + login.getUsername());
        if (count == null) {
            count = 0;
        }

        if (count >= loginCount && count <= loginCount * 2) {
            if (login.getVerificationCode() == null || login.getVerificationCode().isEmpty()) {
                throw new ActionException(10001, "请输入验证码");
            }
            if (codeUtils.confirmCode(login.getVerificationCode(), login.getVerificationToken())) {
                throw new ActionException(10004, "验证码错误");
            }
        } else if (count > loginCount * 2 && count <= loginCount * 3) {
            if (login.getVerificationCode() == null || login.getVerificationCode().isEmpty()) {
                throw new ActionException(10002, "请输入邮箱验证码");
            }
            if (codeUtils.confirmCode(login.getVerificationCode(), login.getVerificationToken())) {
                throw new ActionException(10004, "验证码错误");
            }
        } else if (count > loginCount * 3) {
            throw new BusinessException("账号已锁定");
        }

        return count;
    }
}
