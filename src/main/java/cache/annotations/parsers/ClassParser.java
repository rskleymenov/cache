package cache.annotations.parsers;

import cache.annotations.Column;
import cache.annotations.Id;
import cache.annotations.Table;
import cache.annotations.parsers.models.ClassInfo;
import cache.annotations.parsers.models.FieldInfo;
import cache.exceptions.CacheTableNameMustBeProvided;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ClassParser {

    public static ClassInfo getClassInfo(Class<?> clazz) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setFields(new ArrayList<FieldInfo>());
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            String tableName = tableAnnotation.name();
            classInfo.setTableName(tableName);
        } else {
            throw new CacheTableNameMustBeProvided();
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                String sqlFieldName = columnAnnotation.name();
                String classFieldName = field.getName();
                Class<?> fieldType = field.getType();
                FieldInfo fieldInfo = new FieldInfo(sqlFieldName, classFieldName, fieldType);
                classInfo.getFields().add(fieldInfo);
            }
        }
        return classInfo;
    }

    public static ClassInfo getClassInfoWithObjectValues(Object obj) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setFields(new ArrayList<FieldInfo>());

        Table tableAnnotation = obj.getClass().getAnnotation(Table.class);
        if (tableAnnotation != null) {
            String tableName = tableAnnotation.name();
            classInfo.setTableName(tableName);
        } else {
            throw new CacheTableNameMustBeProvided();
        }

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            Id idAnnotation = field.getAnnotation(Id.class);
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (idAnnotation != null) {
                Long id = FieldHelper.readPrivateField(field, obj);
                classInfo.setId(id);
            } else if (columnAnnotation != null) {
                String sqlFieldName = columnAnnotation.name();
                String fieldName = field.getName();
                Object fieldValue = FieldHelper.readPrivateField(field, obj);
                Class<?> fieldType = field.getType();
                FieldInfo fieldInfo = new FieldInfo(sqlFieldName, fieldName, fieldValue, fieldType);
                classInfo.getFields().add(fieldInfo);
            }
        }
        return classInfo;
    }
}
