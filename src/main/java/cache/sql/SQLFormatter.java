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
    private static final String SELECT_ITEMS_SQL_TEMPLATE = "select %s from %s ";
    private static final String ID_IDENTIFIER = "id";
    private static final String QUOTE_REGEX = "\\?";

    public static String formatGetItemSQL(ClassInfo classInfo) {
        String sqlFields = getSQLSelectFields(classInfo);
        String tableName = classInfo.getTableName();
        return String.format(GET_ITEM_SQL_TEMPLATE, sqlFields, tableName);
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

    public static String formatSelectQuerySQL(ClassInfo classInfo, String appender) {
        String sqlFields = getSQLSelectFields(classInfo);
        return String.format(SELECT_ITEMS_SQL_TEMPLATE, sqlFields, classInfo.getTableName()).concat(appender);
    }

    public static String formatCacheQuery(String query, Object[] args) {
        for (Object argument : args) {
            query = query.replaceFirst(QUOTE_REGEX, argument.toString());
        }
        return query;
    }

    private static String getSQLSelectFields(ClassInfo classInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append(ID_IDENTIFIER);
        for (FieldInfo fieldInfo : classInfo.getFields()) {
            String fieldName = fieldInfo.getSqlFieldName();
            sb.append(DELIMITER);
            sb.append(fieldName);
        }
        return sb.toString();
    }

}
