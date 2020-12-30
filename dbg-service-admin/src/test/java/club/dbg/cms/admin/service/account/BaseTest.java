package club.dbg.cms.admin.service.account;

import club.dbg.cms.rpc.pojo.Operator;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BaseTest {
    @Test
    public void patternTest() throws IOException {
        //aa(1, 1, 3);
        System.out.println(Fib(30, 0, 1));
        //中文，字母，数字，不能以数字开头，长度为0-16
//        String roleP = "^[^0-9][\\u4E00-\\u9FA5A-Za-z0-9_]{0,16}$";
//        String p = "^[^0-9][\\w_]{4,9}$";
//        String str = "哈哈2";
//        System.out.println(str.matches(roleP));
    }

    int sum = 0;
    public void aa(int a, int b, int count){
        if(count == 30){
            System.out.println(a + b);
            return;
        }
        aa(b, a + b, ++count);
    }

    /* n:求斐波那契数列的第n项
     * first:Fib(0) = 0
     * second: Fib(1) = 1
     * */
    public static int Fib(int n,int first,int second) {
        if(n < 2) {
            return n;
        }
        if(n == 2) {
            return first + second;//递归基
        }else{
            return Fib(n - 1,second,first+second);
        }
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
