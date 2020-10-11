package club.dbg.cms.admin.service.role;

import club.dbg.cms.admin.dao.RolePermissionMapper;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleServiceImplTest {

    @Autowired
    RolePermissionMapper rolePermissionMapper;

    @Test
    public void roleInfoTest() {
        HashSet<Integer> ids = new HashSet<>();
        ids.add(1);
    }
}