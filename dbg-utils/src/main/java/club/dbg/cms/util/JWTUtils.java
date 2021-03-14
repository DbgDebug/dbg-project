package club.dbg.cms.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

import java.util.Calendar;
import java.util.Date;

public class JWTUtils {
    public static String createToken(Integer accountId, String username, Integer expiresAt, String secret) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, expiresAt);
        Date expiresDate = nowTime.getTime();
        return JWT.create()
                .withAudience(username)   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .withClaim("accountId", accountId)  //载荷
                .withClaim("username", username)
                .sign(Algorithm.HMAC256(secret)); //签名
    }

    public static String createToken(Integer accountId, String username, Integer expiresAt, String secret, String iss) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, expiresAt);
        Date expiresDate = nowTime.getTime();
        return JWT.create()
                .withIssuer(iss)
                .withAudience(username)   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .withClaim("accountId", accountId)  //载荷
                .withClaim("username", username)
                .sign(Algorithm.HMAC256(secret)); //签名
    }

    /**
     * 检验合法性，其中secret参数就应该传入的是用户的id
     *
     * @param token jwt创建的字符串
     */
    public static boolean verifyToken(String token, String secret) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            verifier.verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取签发对象
     */
    public static String getAudience(String token) {
        return JWT.decode(token).getAudience().get(0);
    }


    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name) {
        return JWT.decode(token).getClaim(name);
    }
}
