package club.dbg.cms.admin.utils;

import club.dbg.cms.util.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author dbg
 */
@Component
public class EmailUtils {
    @Value("${email.user}")
    private String emailUser;

    @Value("${email.password}")
    private String emailPwd;

    public boolean sendEmail(String email, String title, String content){
        return Email.sendEmail(emailUser, emailPwd, email, title, content);
    }

    public boolean sendEmail(String[] emails, String title, String content){

        return Email.sendEmail(emailUser, emailPwd, emails, title, content);
    }
}
