package com.winmart.common.util;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringUtilJr extends org.springframework.util.StringUtils {
    private StringUtilJr() {
    }

    private static final String TEXT_PATTERN = "^[a-zA-Z0-9._]{1,}$";

    public static <T, U> List<U> transform(List<T> list, Function<T, U> function) {
        return list.stream()
                .map(function)
                .collect(Collectors.toList());
    }

    public static String buildRandomFileName(String identifier) {
        if (identifier != null) {
            return identifier + "_" + UUID.randomUUID().toString();
        } else {
            return UUID.randomUUID().toString();
        }
    }

    public static boolean isCheckValue(String value, String valueCompare) {
        return isNotNullOrEmpty(value) && valueCompare.equalsIgnoreCase(value);
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isNotNullOrEmpty(String value) {
        return !isNullOrEmpty(value);
    }

    public static boolean isEmpty(String str) {
        return (str == null || "".equals(str.trim()));
    }

    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    public static String trimToNull(String str) {
        return str == null ? null : str.trim();
    }
}
