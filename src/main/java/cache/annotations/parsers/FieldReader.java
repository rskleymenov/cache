package cache.annotations.parsers;


import cache.exceptions.CacheIllegalAccessException;

import java.lang.reflect.Field;

public class FieldReader {
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
}
