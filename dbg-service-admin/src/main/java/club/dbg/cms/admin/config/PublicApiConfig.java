package club.dbg.cms.admin.config;

import club.dbg.cms.util.MD5;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;

/**
 * 无需验证的api
 */
@Component
public class PublicApiConfig {

    private final String serviceName;

    private final HashSet<String> apiSet = new HashSet<>();

    public PublicApiConfig(@Value("${spring.application.name}") String serviceName) {
        this.serviceName = serviceName;
    }

    @PostConstruct
    public void init(){
        apiSet.add("/login");
        apiSet.add(MD5.md5(serviceName + "club.dbg.cms.admin.controller.LoginControllerlogin"));
        apiSet.add("/get_verification_code");
        apiSet.add(MD5.md5(serviceName + "club.dbg.cms.admin.controller.LoginControllergetVerificationCode"));
        apiSet.add("/get_email_code");
        apiSet.add(MD5.md5(serviceName + "club.dbg.cms.admin.controller.LoginControllergetEmailCode"));
        apiSet.add("/jmeter");
        apiSet.add(MD5.md5(serviceName + "club.dbg.cms.admin.controller.TestControllerjmeter"));
        //apiSet.add("/logout");
        //apiSet.add(MD5.md5(serviceName + "club.dbg.cms.admin.controller.LoginControllerlogout"));
        apiSet.add("/rpc/permission/register");
        apiSet.add(MD5.md5(serviceName + "club.dbg.cms.admin.controller.PermissionRegisterControllerpermissionRegister"));

    }

    public Boolean contains (String path) {
        return apiSet.contains(path);
    }
}
