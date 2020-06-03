package club.dbg.cms.admin.service.account;

import club.dbg.cms.admin.dao.AccountMapper;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountServiceImplTest {
    @Autowired
    AccountMapper accountMapper;

    @Test
    public void selectAccountListTest(){
        System.out.println(JSON.toJSON(accountMapper.selectAccountList(null, 0, 1)));
    }
}
