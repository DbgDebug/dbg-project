package club.dbg.cms.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {
    public static String md5(String value) {
        return DigestUtils.md5Hex(value);
    }
}
