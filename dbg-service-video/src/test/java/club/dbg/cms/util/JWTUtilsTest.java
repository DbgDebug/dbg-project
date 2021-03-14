package club.dbg.cms.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class JWTUtilsTest {

    @Test
    public void createToken() {
        System.out.println(JWTUtils.createToken(1, "dbg", 30, 1 + "salt"));
    }

    @Test
    public void verifyToken() {
        JWTUtils.verifyToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkYmciLCJhY2NvdW50SWQiOjEsImV4cCI6MTYxNTYyODI4NCwiaWF0IjoxNjE1NjI2NDg0LCJ1c2VybmFtZSI6ImRiZyJ9.dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk", "121");
    }
}