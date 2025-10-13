package com.winmart.common.util;

public class StringUtil extends org.springframework.util.StringUtils {
    public static boolean isNullOrEmpty(String str) {
        return (str == null || "".equals(str.trim()));
    }
}
