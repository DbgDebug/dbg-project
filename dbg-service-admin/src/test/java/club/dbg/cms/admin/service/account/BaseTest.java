package club.dbg.cms.admin.service.account;

import club.dbg.cms.rpc.pojo.Operator;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {

    @Test
    public void patternTest(){
        //中文，字母，数字，不能以数字开头，长度为0-16
        String roleP = "^[^0-9][\\u4E00-\\u9FA5A-Za-z0-9_]{0,16}$";
        String p = "^[^0-9][\\w_]{4,9}$";
        String str = "哈哈2";
        System.out.println(str.matches(roleP));
    }

    @Test
    public void paramTest(){
        List<Integer> idList = new ArrayList<>();
        idList.add(1);
        idList.add(2);
        idList.add(3);
        System.out.println(JSON.toJSONString(idList));
    }

    @Test
    public void accountResponseTest() {
        Operator operator = new Operator();
        if(operator.getId() == null){
            System.out.println("null");
        }
    }
}
