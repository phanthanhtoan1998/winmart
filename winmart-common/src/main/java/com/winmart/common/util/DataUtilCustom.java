package com.winmart.common.util;


import com.winmart.common.Constant.AppConstants;
import com.google.common.base.CaseFormat;
import org.apache.tomcat.util.buf.StringUtils;


import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import java.util.*;
import java.util.stream.Collectors;

public class DataUtilCustom {


    


    public static <T> T convertTupleToClass(Tuple tuple, Class<T> clazz) {
        Map<String, Object> tempMap = new HashMap<>();

        for (TupleElement<?> key : tuple.getElements()) {
            tempMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key.getAlias()), tuple.get(key));
        }
        return map(tempMap, clazz);
    }

    public static <D, T> D map(final T entity, Class<D> outClass) {
        return ObjectMapperUtils.map(entity, outClass);
    }

    public static <T> String convertListToString(List<T> list) {
        return list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(AppConstants.COMMA));
    }


    public static <T> List<T> convertStringToList(String str, Class<T> clazz) {
        if (DataUtil.isNullOrEmpty(str)) {
            return new ArrayList<>();
        }
        List<String> split = List.of(str.split(AppConstants.COMMA));
        return split.stream().map(s -> {
            if (clazz == Long.class) {
                return clazz.cast(Long.valueOf(s));
            } else if (clazz == Integer.class) {
                return clazz.cast(Integer.valueOf(s));
            } else if (clazz == String.class) {
                return clazz.cast(s);
            } else {
                throw new IllegalArgumentException("Unsupported type: " + clazz.getName());
            }
        }).collect(Collectors.toList());
    }


    public static Date setTimeToMidnightNextDate(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }



}

