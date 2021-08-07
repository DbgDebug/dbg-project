package club.dbg.cms.util;

import org.junit.Test;

public class BCryptEncoderTest {

    @Test
    public void encodeTest() {
        long time = System.currentTimeMillis();
        String hash = BCryptEncoder.encode("113331122");
        System.out.println(System.currentTimeMillis() - time);
        System.out.println(hash);
        System.out.println(BCryptEncoder.matches("113331122", hash));
    }
}