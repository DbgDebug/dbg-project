package club.dbg.cms.admin.utils;

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
    @Value("${verificationCode.redisHeader}")
    private String redisHeader;

    private Integer timeout;

    @Value("${verificationCode.timeout}")
    public void setTimeout(Integer timeout) {
        if (timeout != null && timeout > 0) {
            this.timeout = timeout;
        } else {
            this.timeout = 300;
        }
    }

    private Integer length;

    @Value("${verificationCode.length}")
    public void setLength(Integer length) {
        if (length != null && length > 0) {
            this.length = length;
        } else {
            this.length = 4;
        }
    }

    private final
    RedisUtils redisUtils;

    private final
    EmailUtils emailUtils;

    @Autowired
    public VerificationCodeUtils(RedisUtils redisUtils, EmailUtils emailUtils) {
        this.redisUtils = redisUtils;
        this.emailUtils = emailUtils;
    }

    /**
     * 确认验证码
     *
     * @param code
     * @param token
     * @return
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
        String imageBase64 = ImageUtils.getImageByBase64(code);
        verificationCode.setVerificationToken(token);
        verificationCode.setVerificationCode(imageBase64);
        return verificationCode;
    }

    public VerificationCodeDTO getEmailCode(String email) {
        String token = UUIDUtils.getUUIDNotHyphen();
        VerificationCodeDTO verificationCode = new VerificationCodeDTO();
        String code = getCode();
        if (!redisUtils.set(redisHeader + token, code, timeout)) {

        }
        String imageBase64 = ImageUtils.getImageByBase64(code);
        verificationCode.setVerificationToken(token);
        verificationCode.setVerificationCode(imageBase64);
        emailUtils.sendEmail(email, "登录验证", code);
        return verificationCode;
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
