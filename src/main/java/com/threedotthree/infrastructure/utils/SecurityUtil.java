package com.threedotthree.infrastructure.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class SecurityUtil {

    /**
     * 대칭키
     */
    public static final String key = "IWANTTOSZSLETSGO";

    /**
     * Salt 발급
     */
    public static String generateSalt() {
        return SHA256Util.generateSalt();
    }

    /**
     * 비밀번호 암호화
     */
    public static String passwordHash(String password, String salt) {
        return SHA256Util.getEncrypt(password, salt);
    }

    /**
     * 문자열 암호화
     */
    public static byte[] encryptToBytes(String value) throws Exception  {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 바이트 배열 암호화 문자열을 Base64로 Encode
     */
    public static String strToEncrypt(String value) throws Exception {
        return Base64.getEncoder().encodeToString(encryptToBytes(value));
    }

    /**
     * 바이트 배열 암호화 문자열을 복호화
     */
    public static byte[] decryptToBytes(byte[] bytesToDecrypt) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(bytesToDecrypt);
    }

    /**
     * Base64 문자열을 복호화
     */
    public static String strToDecrypt(String value) throws Exception {
        byte[] bytesToDecrypt = Base64.getDecoder().decode(value);
        return new String(decryptToBytes(bytesToDecrypt));
    }

}
