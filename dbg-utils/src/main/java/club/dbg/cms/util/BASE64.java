package club.dbg.cms.util;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;

import java.util.Arrays;

public class BASE64 {
    public static String encodeAsString(String str) {
        Base64 base64 = new Base64();
        return base64.encodeAsString(str.getBytes());
    }

    public static String encodeAsString(byte[] bytes) {
        Base64 base64 = new Base64();
        return base64.encodeAsString(bytes);
    }

    public static byte[] encode(String str) {
        Base64 base64 = new Base64();
        return base64.encode(str.getBytes());
    }

    public static byte[] encode(byte[] bytes) {
        Base64 base64 = new Base64();
        return base64.encode(bytes);
    }

    public static String encodeToString(byte[] bytes) {
        Base64 base64 = new Base64();
        return base64.encodeToString(bytes);
    }

    public static String decodeToString(String str) {
        Base64 base64 = new Base64();
        return Arrays.toString(base64.decode(str));
    }

    public static String decodeAsString(byte[] bytes) {
        Base64 base64 = new Base64();
        return Arrays.toString(base64.decode(bytes));
    }

    public static byte[] decode(String str) {
        Base64 base64 = new Base64();
        return base64.decode(str);
    }

    public static byte[] decode(byte[] bytes) {
        Base64 base64 = new Base64();
        return base64.decode(bytes);
    }
}
