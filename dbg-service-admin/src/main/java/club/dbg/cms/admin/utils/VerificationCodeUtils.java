package club.dbg.cms.admin.utils;

import club.dbg.cms.admin.exception.BusinessException;
import club.dbg.cms.admin.pojo.VerificationCodeDTO;
import club.dbg.cms.admin.redis.RedisUtils;
import club.dbg.cms.util.ImageUtils;
import club.dbg.cms.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author dbg
 * @date 2019/08/17
 */

@Component
public class VerificationCodeUtils {
    private final String redisHeader;

    private final Integer timeout;

    private final Integer length;

    private final
    RedisUtils redisUtils;

    private final
    EmailUtils emailUtils;

    public VerificationCodeUtils(
            @Value("${verificationCode.redisHeader}")
                    String redisHeader,
            @Value("${verificationCode.timeout}")
                    Integer timeout,
            @Value("${verificationCode.length}")
                    Integer length,
            RedisUtils redisUtils,
            EmailUtils emailUtils) {
        this.redisHeader = redisHeader;
        this.redisUtils = redisUtils;
        this.emailUtils = emailUtils;

        if (timeout != null && timeout > 0) {
            this.timeout = timeout;
        } else {
            this.timeout = 300;
        }

        if (length != null && length > 0) {
            this.length = length;
        } else {
            this.length = 4;
        }
    }

    /**
     * 确认验证码
     *
     * @param code String
     * @param token String
     * @return  boolean
     */
    public boolean confirmCode(String code, String token) {
        String checkCode = (String) redisUtils.get(redisHeader + token);
        if (!code.equalsIgnoreCase(checkCode)) {
            redisUtils.delete(redisHeader + token);
            return true;
        }
        redisUtils.delete(redisHeader + token);
        return false;
    }

    /**
     * 返回包含token和base64编码的图片
     *
     * @return VerificationCode
     */
    public VerificationCodeDTO getVerificationCode() {
        String token = UUIDUtils.getUUIDNotHyphen();
        VerificationCodeDTO verificationCode = new VerificationCodeDTO();
        String code = getCode();

        if (!redisUtils.set(redisHeader + token, code, timeout)) {

        }
        String imageBase64 = ImageUtils.getImageBase64(code);
        verificationCode.setVerificationToken(token);
        verificationCode.setVerificationCode(imageBase64);
        return verificationCode;
    }

    public void getEmailCode(String email) {
        String token = UUIDUtils.getUUIDNotHyphen();
        String code = getCode();
        if (!redisUtils.set(redisHeader + token, code, timeout)) {
            throw new BusinessException("获取邮件验证码失败");
        }
        emailUtils.sendEmail(email, "登录验证", code);
    }

    private String getCode() {
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
                'm', 'n', 'o', 'p', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }

        return stringBuilder.toString();
    }
}
