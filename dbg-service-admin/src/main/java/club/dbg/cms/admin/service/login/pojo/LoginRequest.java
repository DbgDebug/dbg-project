package club.dbg.cms.admin.service.login.pojo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author dbg
 */
public class LoginRequest {
    @NotBlank
    @Length(min = 4, max = 12)
    private String username;

    @NotBlank
    @Length(min = 6, max = 16)
    private String password;

    @Length(min = 4, max = 6)
    private String verificationCode;

    @Length(min = 8, max = 16)
    private String verificationToken;

    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
}
