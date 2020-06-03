package club.dbg.cms.admin.service.permission;

import club.dbg.cms.admin.dao.PermissionMapper;
import club.dbg.cms.admin.dao.ServiceMapper;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class PermissionServiceImplTest {
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    ServiceMapper serviceMapper;

    @Value("${spring.application.name}")
    private String serviceName;


    @Test
    public void deleteTest() {
        int[] arr = {1, 2};
    }

    @Test
    public void serviceTest() {
        System.out.println(serviceName);
        System.out.println(JSON.toJSONString(serviceMapper.selectServiceByName("dbg-service-admin")));
    }
}
