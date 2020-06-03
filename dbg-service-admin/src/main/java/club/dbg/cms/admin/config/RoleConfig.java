package club.dbg.cms.admin.config;

import org.springframework.stereotype.Component;

/**
 * 设置系统和超级管理员id
 * @author dbg
 */
@Component
public class RoleConfig {
    private Integer systemId = 1;

    private Integer superAdministratorId = 2;

    private void init(){
        this.systemId = 1;
        this.superAdministratorId = 2;
    }

    public Integer getSuperAdministratorId() {
        return superAdministratorId;
    }

    public Integer getSystemId() {
        return systemId;
    }
}
