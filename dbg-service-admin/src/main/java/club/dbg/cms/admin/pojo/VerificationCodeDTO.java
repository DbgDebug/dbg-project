package club.dbg.cms.admin.pojo;

import org.hibernate.validator.constraints.Length;

public class VerificationCodeDTO {
    @Length(min = 4, max = 6)
    private String verificationCode;

    @Length(min = 8, max = 16)
    private String verificationToken;

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
