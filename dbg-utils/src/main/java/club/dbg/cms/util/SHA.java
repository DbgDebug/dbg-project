package club.dbg.cms.util;

import org.apache.commons.codec.digest.DigestUtils;

public class SHA {
    public static String sha256(String value) {
        return DigestUtils.sha256Hex(value);
    }
}
