package com.fisher.base.gateway.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/*/*
 * Created by fisher
 */
public class TokenUtils {
    private static Logger logger = LoggerFactory.getLogger(TokenUtils.class);
    public static final Map<String, List<String>> APP_TOKEN = new HashMap<String, List<String>>();
    private static final String PREFIX = "app";
    private static final String SUFFIX = "2018";

    static {
        fillToken(APP_TOKEN, "app-token.properties");//子任务权限
    }

    public static boolean identityValidate(String identity, String token) {
        if (identity == null || identity.equals("") || token == null || token.equals("")) {
            return false;
        }
        String compareToken = String.valueOf((PREFIX + identity + SUFFIX).hashCode());
        if (compareToken.startsWith("-")) {
            compareToken = compareToken.substring(1);
        }
        return compareToken.equals(token) ? true: false;
    }

    private static void fillToken(Map<String, List<String>> token, String propertyFile) {
        Properties properties = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFile);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Exception:" + e);
        }
        for (Map.Entry<Object, Object> entry: properties.entrySet()) {
            List<String> models = Arrays.asList(entry.getValue().toString().split(","));
            logger.info("Token items:" + models);
            token.put(entry.getKey().toString(), models);
        }
    }

    public static boolean hasPermition(String token, String[] items) {
        Map<String, List<String>> tokenMap = new HashMap<String, List<String>>();
        tokenMap = APP_TOKEN;
        for (String subtype: items) {
            if (tokenMap.get(token).contains(subtype)) {
                return true;
            }
        }
        return false;
    }
}
