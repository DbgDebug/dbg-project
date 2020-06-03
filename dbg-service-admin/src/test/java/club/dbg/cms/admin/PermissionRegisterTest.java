package club.dbg.cms.admin;

import club.dbg.cms.util.AESUtils;
import club.dbg.cms.util.MD5;
import org.junit.Test;

import java.util.Arrays;

public class PermissionRegisterTest {

    @Test
    public void permissionMakeTest() {
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
        byte[] bytes = AESUtils.aesEncryptToBytes("aesssss", "1111111241223125");
        System.out.println(Arrays.toString(bytes));
        System.out.println(AESUtils.aesDecryptByBytes(bytes, "1111111241223125"));
    }
}
