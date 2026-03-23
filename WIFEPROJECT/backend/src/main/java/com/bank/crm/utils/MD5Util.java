package com.bank.crm.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * MD5工具类
 */
public class MD5Util {

    /**
     * 生成盐值
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    /**
     * 使用盐值加密密码
     */
    public static String encryptPassword(String password, String salt) {
        return md5(password + salt);
    }

    /**
     * MD5加密
     */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            return bytesToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    /**
     * 字节数组转十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * 验证密码
     */
    public static boolean verifyPassword(String inputPassword, String encryptedPassword, String salt) {
        String encryptedInput = encryptPassword(inputPassword, salt);
        return encryptedInput.equals(encryptedPassword);
    }
}