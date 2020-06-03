package club.dbg.cms.admin.service.login;

import club.dbg.cms.domain.admin.AccountDO;
import club.dbg.cms.admin.dao.AccountMapper;
import club.dbg.cms.admin.redis.RedisUtils;
import club.dbg.cms.util.BASE64;
import club.dbg.cms.util.RSACoder;
import com.alibaba.fastjson.JSON;
import io.swagger.models.Swagger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
//@MapperScan(value = {"club.dbg.cms.login.dao.mapper"})
public class LoginServiceImplTest {

    private static String src = "i look";

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    LoginService loginService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    private DocumentationCache documentationCache;
    @Autowired
    private ServiceModelToSwagger2Mapper mapper;

    @Test
    public void swaggerTest(){
        Documentation documentation = this.documentationCache.documentationByGroup("default");
        Swagger swagger = this.mapper.mapDocumentation(documentation);
        System.out.println(JSON.toJSONString(swagger));
    }

    @Test
    public void loginTest() {
        AccountDO account = accountMapper.selectAccountByUsername("admin");
        System.out.println(JSON.toJSONString(account));
    }

    @Test
    public void redisTest() {

        System.out.println(JSON.toJSONString(redisUtils.get("test")));
        //redisUtils.
    }

    @Test
    public void databaseTest() throws InterruptedException {
    }

    @Test
    public void test() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("sign_type", "RSA2");
        map.put("privateKey", "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCjFFPGubsStvsX0f2PMzeOAs6OE08k1zVJDiM2ZVrtTzjdlgZyaTH9vx+aFH6Ko+BzcaGFpNZgr/3ZpWQd7Q7YO3Z1JKK2kGL2ye8GLosXrCzFSdCKa1+eN2yb86PKyLThq8BwY8rb6eVleykLFSan5OlZzmjf2VUk13tpJWyntBUEeoVFyBk284WysoZ0afuJMVWBN76rJpuH6XEjRXd1oW6Qb34dfjFjXZ/ARCq/O33YlDuwxDvBHuI2dph8FskwKwWL7l6DrfxSSBUuYo8p+UmbBGjGnFOMR8ZKybUE42NRpJf9r4+S1TSoacZQt5+JV5m6Mk21BNwIUj515nkxAgMBAAECggEAWk6zZeUMV3zE55f91gMCzL15LOw8c6ZJd79AeKsAA1JPp0WhJFJkwKPKGEUpHhVKAiP20k9J+Bj9dj1Eh93yzoN/Qbeb84VZiFii6MV1eRJ7aHVL6pszRCZlGqd9GJq0sBOwaYyZldaclenx/JUl5uu43WaUZQGxSMmbKcrztJn8BilE2SNfhYOxZ9B2IEYNAfoIfykcqnQcATsAnm5e8Y91svNMwgFpNzN8+PL8E4GGJG21KPpD2yt/2jTy12slRQav8O6Roe6HpAWCZWJIxeQee5KXeJPHWo/S8pH1ZubbRkFrMzpSKJfpx87/II6skn36MwHq/vIzGdTcvIy60QKBgQDtO3lwtTy4oTSX4prZzT/XOhJ4R2bYDJZGLBE0cT5Uq9JXVsNSdjfAHjt0J1eEhFM459vGpr3RQrHR+gZUgHKRsNFm81FDydMkS0yoP+7PyPyW2T3XVh8VXIMV868MXMCaS/ztqroZJJB3X1q494GqoHQWmdcBzgvQJ843zNMDiwKBgQCv+xPoOJESelqhUuo70E07pKYVgex3/Ti8I4FR7oBBdYpObRo91+AU8cBftV2zsxgq8KMO/h9EnlBuds+ZP3UtMpNl8SLw6C8HL4xLpRY51VBMZJmn2TPay3e7+fVF+xZ/AThfb3IjM9HFuUt/V7Pf9y0Qu8Rp46nGPaCeVmTdswKBgQC2BsKBtq6mxBrEx/0lTZXEoSx22eKRt+a+ZSW2ZYX8FfLJTSuF5GuWoBviXiWK6rWonk9GN77CCDjE8uFUiVJfc5rZMa2TJBM8cAKbvqKREJivbnVC+zPSgi+a8mXISIsIWXHzMuKkVCf9UE8eSdtWftNUXCpbmiv6RfC3ZBQtTQKBgBHPnqctOnmdSpjzVGOzZG2571lT6gQG6/wWt9r8AZ0ecV4YqsRBmhtkkGzfZVlvse1+p3I4BCAkyc00VzSSeKCXctWpfzjUtxXKGS/6yHIYBeWFdQvI7vehCkBLPyjTDebSz8rQ88NeVkflxh9gWAM9+CCucGWrNh6rVBtScSkDAoGBAN6pUw629xbJRcVJwPgw1b1oSjY9Qz0NBUFOYMdwq97nnzM9I9dBjliDLnSQZv/HonCg/lY8LUqypYfur9K6ValgLfPAQFjKkNNbAHvKMX3Omb46W0KY12Z7mcOpCi90nWFiDfR1nqcC8Gkul1nlAykZOHzEMhgNjP2xx/Rzw+pz");
        map.put("charset", "utf-8");
        map.put("content", "123456789");
        map.put("publicKey", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoxRTxrm7Erb7F9H9jzM3jgLOjhNPJNc1SQ4jNmVa7U843ZYGcmkx/b8fmhR+iqPgc3GhhaTWYK/92aVkHe0O2Dt2dSSitpBi9snvBi6LF6wsxUnQimtfnjdsm/Ojysi04avAcGPK2+nlZXspCxUmp+TpWc5o39lVJNd7aSVsp7QVBHqFRcgZNvOFsrKGdGn7iTFVgTe+qyabh+lxI0V3daFukG9+HX4xY12fwEQqvzt92JQ7sMQ7wR7iNnaYfBbJMCsFi+5eg638UkgVLmKPKflJmwRoxpxTjEfGSsm1BONjUaSX/a+PktU0qGnGULefiVeZujJNtQTcCFI+deZ5MQIDAQAB");

        //String sign = RSA.rsaSign(map);
        //System.out.println("sign:"+sign);
        //sign = "hq0hGmCoz8I1mmG8qyrKszhncZCn+W+Wl6TqPQ9uWvv4iHdHnvRtWgTyzHzzwX71ZOPmEwlWYMPOUlfOgjGRvm/+fzu/MO6Yg8cdnWCyl2Cv5jlcmS07TwydMok+5BS6KypFvyQgpkEQMupcZ66dY7EFDNHT71KqRj9dsefOeuwzM8sO8iWu3U1lK5lXHw6DgYLVoTSA9rCzwanF5ZK18fC+lHF9o95000ZuV3uCMy5u3Y/84LRkvNeG2NjW3h0JBMzG47i9YTtBDXBadOy0GAh/bTMh9NEE7+m9lHUPgFvgpy5TvyDNyQtmMmQ5daO7gI+SgH1JHZTrpBM+JsfCYg==";
        //System.out.println(RSA.rsaCheck(map, sign));
        byte[] bytes = RSACoder.encryptByPublicKey("123456789".getBytes(), map.get("publicKey"));
        System.out.println("publicKey data:" + BASE64.encodeToString(bytes));
        System.out.println("value:" + new String(RSACoder.decryptByPrivateKey(bytes, map.get("privateKey"))));
        //System.out.println();
        //bytes = RSACoder.encryptByPrivateKey("123456789".getBytes(), map.get("privateKey"));
        //System.out.println("privateKey:"+ new BASE64Encoder().encode(bytes));
        //System.out.println("value:" + new String(RSACoder.decryptByPublicKey(bytes, map.get("publicKey"))));
        //System.out.println();

        //String sign = RSACoder.sign("123456789".getBytes(), map.get("privateKey"));
        //System.out.println("sign:" + sign);
        //System.out.println("checkSign:" + RSACoder.verify("123456789".getBytes(), map.get("publicKey"), sign));
        //sign.equalsIgnoreCase()
    }


    public static void MD5RSASign() {
        try {
            // 生成一对密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");  //获取密钥生成器实例
            keyPairGenerator.initialize(512);  // 初始化长度
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();//生成公钥
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();  // 生成私钥

            //用私钥进行签名
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());  //私钥转换成pkcs8格式
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec); // 用key工厂对象生成私钥
            Signature signature = Signature.getInstance("MD5withRSA");  //  md5 RSA签名对象
            signature.initSign(privateKey);  //初始化签名
            signature.update(src.getBytes());
            byte[] result = signature.sign();  //对消息进行签名
            System.out.println("签名结果：" + result);


            //用公钥进行验证
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean verify = signature.verify(result);
            System.out.println("验证结果:" + verify);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
