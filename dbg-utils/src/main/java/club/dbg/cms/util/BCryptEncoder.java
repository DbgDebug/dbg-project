package club.dbg.cms.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

public class BCryptEncoder {
    private static final int STRENGTH = 12;

    public static String encode(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(STRENGTH, new SecureRandom());
        return bCryptPasswordEncoder.encode(password);
    }

    public static boolean matches(String password, String passwordHash) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(password, passwordHash);
    }
}
