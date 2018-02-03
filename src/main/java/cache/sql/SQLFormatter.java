package cache.sql;

import cache.annotations.parsers.models.ClassInfo;
import cache.annotations.parsers.models.FieldInfo;

import java.util.Iterator;

public class SQLFormatter {

    private static final String GET_ITEM_SQL_TEMPLATE = "select %s from %s where id = ?";
    private static final String DELETE_ITEM_SQL_TEMPLATE = "delete from %s where id = ?";
    private static final String INSERT_ITEM_SQL_TEMPLATE = "insert into %s (%s) values (%s)";
    private static final String DELIMITER = ", ";
    private static final String VALUE_PATTERN = "?";

    public static String formatGetItemSQL(ClassInfo classInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("id");
        for (FieldInfo fieldInfo : classInfo.getFields()) {
            String fieldName = fieldInfo.getSqlFieldName();
            sb.append(DELIMITER);
            sb.append(fieldName);
        }
        String tableName = classInfo.getTableName();
        return String.format(GET_ITEM_SQL_TEMPLATE, sb, tableName);
    }

    public static String formatInsertItemSQL(ClassInfo classInfo) {
        StringBuilder fieldsBuilder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();
        Iterator<FieldInfo> iterator = classInfo.getFields().iterator();
        while (iterator.hasNext()) {
            FieldInfo fieldInfo = iterator.next();
            String fieldName = fieldInfo.getSqlFieldName();
            fieldsBuilder.append(fieldName);
            valuesBuilder.append(VALUE_PATTERN);
            if (iterator.hasNext()) {
                fieldsBuilder.append(DELIMITER);
                valuesBuilder.append(DELIMITER);
            }
        }
        return String.format(INSERT_ITEM_SQL_TEMPLATE, classInfo.getTableName(), fieldsBuilder, valuesBuilder);
    }

    public static String formatDeleteItemSQL(ClassInfo classInfo) {
        return String.format(DELETE_ITEM_SQL_TEMPLATE, classInfo.getTableName());
    }

}
