package club.dbg.cms.util;

import java.util.UUID;

/**
 * @author dbg
 */
public class UUIDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getUUIDNotHyphen() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getUUIDBySHA256() {
        return SHA.sha256(getUUID());
    }

    public static String getUUIDByMD5() {
        return MD5.md5(getUUID());
    }
}
