package cache.annotations.parsers;

import cache.annotations.Column;
import cache.annotations.Id;
import cache.annotations.Table;
import cache.annotations.parsers.models.ClassInfo;
import cache.annotations.parsers.models.FieldInfo;
import cache.exceptions.CacheTableNameMustBeProvided;
import cache.models.HardUser;
import cache.models.User;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ClassParser {

    private static final Logger logger = Logger.getLogger(ClassParser.class);

    public static ClassInfo getClassInfo(Object obj) {
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
                Long id = FieldReader.readPrivateField(field, obj);
                classInfo.setId(id);
            } else if (columnAnnotation != null) {
                String fieldName = columnAnnotation.name();
                Object fieldValue = FieldReader.readPrivateField(field, obj);
                Class<?> fieldType = field.getType();
                FieldInfo fieldInfo = new FieldInfo(fieldName, fieldValue, fieldType);
                classInfo.getFields().add(fieldInfo);
            }
        }
        return classInfo;
    }


    public static void main(String[] args) {
        User user = new User(55, "Roman", "Kleimenov");
        HardUser hardUser = new HardUser(12, 5, 53.0);
        System.out.println(getClassInfo(user));
        System.out.println(getClassInfo(hardUser));

    }
}
