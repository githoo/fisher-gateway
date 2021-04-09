package com.fisher.base.gateway.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fisher
 */
public class MD5Utils {
    private static final Log logger = LogFactory.getLog(MD5Utils.class);

    /**
     * 获取16位md5字符串
     * @param phrase 短语串
     */
    public static String getMd5Str(String phrase) {
        if (phrase == null) {
            return null;
        }

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            return null;
        }

        byte[] bs = digest.digest(phrase.getBytes());
        String hexString = "";
        for (byte b : bs) {
            int temp = b & 255;
            if (temp < 16 && temp >= 0) {
                hexString = hexString + "0" + Integer.toHexString(temp);
            } else {
                hexString = hexString + Integer.toHexString(temp);
            }
        }
        return hexString.substring(8,24);
    }
}