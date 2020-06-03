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
public class PublicApiConfig   {
    @Value("${spring.application.name}")
    private String serviceName;

    private final HashSet<String> apiSet = new HashSet<>();

    @PostConstruct
    public void init(){
        apiSet.add("/login");
        apiSet.add("/get_verification_code");
        apiSet.add("/get_email_code");
        apiSet.add("/jmeter");
        apiSet.add(MD5.md5(serviceName + "club.dbg.cms.admin.controller.LoginControllerlogin"));
        apiSet.add(MD5.md5(serviceName + "club.dbg.cms.admin.controller.LoginControllergetVerificationCode"));
        apiSet.add(MD5.md5(serviceName + "club.dbg.cms.admin.controller.LoginControllergetEmailCode"));
        apiSet.add(MD5.md5(serviceName + "club.dbg.cms.admin.controller.TestControllerjmeter"));
        apiSet.add("/rpc/permission/register");
        apiSet.add(MD5.md5(serviceName + "club.dbg.cms.admin.controller.PermissionRegisterControllerpermissionRegister"));

    }

    public Boolean contains (String path) {
        return apiSet.contains(path);
    }
}
