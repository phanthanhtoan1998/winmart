package com.winmart.common.mapper;

import java.lang.reflect.Field;

public class Mapper {

    public static <S, T> T convert(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }

        try {
            // Tạo đối tượng đích
            T target = targetClass.getDeclaredConstructor().newInstance();

            // Lấy tất cả các trường của đối tượng nguồn
            Field[] sourceFields = source.getClass().getDeclaredFields();

            // Duyệt qua từng trường và sao chép giá trị
            for (Field sourceField : sourceFields) {
                sourceField.setAccessible(true); // Cho phép truy cập vào trường private
                String fieldName = sourceField.getName();
                Object fieldValue = sourceField.get(source);

                // Lấy trường tương ứng trong đối tượng đích
                Field targetField;
                try {
                    targetField = targetClass.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    // Bỏ qua nếu trường không tồn tại trong đối tượng đích
                    continue;
                }

                targetField.setAccessible(true); // Cho phép truy cập vào trường private
                targetField.set(target, fieldValue); // Gán giá trị
            }

            return target;
        } catch (Exception e) {
            throw new RuntimeException("Error converting entity", e);
        }
    }
}
