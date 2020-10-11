package club.dbg.cms.admin;

import club.dbg.cms.admin.dao.AccountMapper;
import club.dbg.cms.util.AESUtils;
import club.dbg.cms.util.MD5;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE )
public class PermissionRegisterTest extends ClassLoader {

    @Autowired
    AccountMapper accountMapper;

    @Test
    public void permissionMakeTest() {
        System.out.println(JSON.toJSONString(accountMapper.selectAccountById(1)));
    }

    @Test
    public void permissionSignTest() {
        System.out.println(System.currentTimeMillis());
        String sign = MD5.md5("class:ArticleController,method:articleList,parameter count:[class java.lang.String, class java.lang.Integer, class java.lang.Integer]");
        if (sign.equals("1f1ee2ccb2502abbe833d6c9de287b2e")) {
            System.out.println("true");
        }
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void permissionAES() throws Exception {
        Class<?> c = findClass("./Test.class");
        c.getMethod("task").invoke(c.newInstance());//方法名字
        //Class<?> c = defineClass();
        //byte[] bytes = AESUtils.aesEncryptToBytes("aesssss", "1111111241223125");
        //System.out.println(Arrays.toString(bytes));
        //System.out.println(AESUtils.aesDecryptByBytes(bytes, "1111111241223125"));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Class<?> findClass(String className){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream in = null;
        byte[] b = new byte[1024];
        int len;
        try{
            in = new FileInputStream(new File("./Test.class"));
            while((len = in.read(b)) != -1){
                baos.write(b,0, len);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(baos.toByteArray().length == 0){
            return null;
        }
        return defineClass(baos.toByteArray(), 0, baos.toByteArray().length);
    }
}
