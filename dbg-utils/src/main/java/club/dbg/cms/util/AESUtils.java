package club.dbg.cms.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final String KEY_ALGORITHM = "AES";
    //默认的加密算法
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final String CHARSET_NAME = "utf-8";


    public static String aesEncryptToBase64(String content, String encryptKey) throws Exception {
        return BASE64.encodeAsString(aesEncryptToBytes(content, encryptKey));
    }

    public static String aesDecryptByBase64(String base64String, String encryptKey) throws Exception {
        return aesDecryptByBytes(BASE64.decode(base64String), encryptKey);
    }

    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(128);
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM));
        return cipher.doFinal(content.getBytes(CHARSET_NAME));
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(128);
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), KEY_ALGORITHM));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }
}
