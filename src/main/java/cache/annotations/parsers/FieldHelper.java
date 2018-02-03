package cache.annotations.parsers;


import cache.exceptions.CacheIllegalAccessException;

import java.lang.reflect.Field;

public class FieldHelper {
    public static <T> T readPrivateField(Field field, Object obj) {
        Object value;
        boolean isNotAccessible = !field.isAccessible();
        if (isNotAccessible) {
            field.setAccessible(true);
        }
        try {
            value = field.get(obj);
        } catch (IllegalAccessException exc) {
            throw new CacheIllegalAccessException(exc);
        } finally {
            if (isNotAccessible) {
                field.setAccessible(false);
            }
        }
        return (T) value;
    }

    public static void writePrivateField(String fieldName, Object value, Object target) {
        Field field = null;
        boolean isNotAccessible = false;
        try {
            field = target.getClass().getDeclaredField(fieldName);
            isNotAccessible = !field.isAccessible();
            if (isNotAccessible) {
                field.setAccessible(true);
            }
            field.set(target, value);
        } catch (Exception exc) {
            throw new CacheIllegalAccessException(exc);
        } finally {
            if (isNotAccessible) {
                field.setAccessible(false);
            }
        }
    }
}
