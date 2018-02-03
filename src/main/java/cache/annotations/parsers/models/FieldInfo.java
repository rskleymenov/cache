package cache.annotations.parsers.models;

public class FieldInfo {
    private String sqlFieldName;
    private String classFieldName;
    private Object fieldValue;
    private Class<?> fieldType;

    public FieldInfo(String sqlFieldName, String classFieldName, Object fieldValue) {
        this.sqlFieldName = sqlFieldName;
        this.classFieldName = classFieldName;
        this.fieldValue = fieldValue;
    }

    public FieldInfo(String sqlFieldName, String classFieldName, Object fieldValue, Class<?> fieldType) {
        this.sqlFieldName = sqlFieldName;
        this.classFieldName = classFieldName;
        this.fieldValue = fieldValue;
        this.fieldType = fieldType;
    }

    public String getSqlFieldName() {
        return sqlFieldName;
    }

    public void setSqlFieldName(String sqlFieldName) {
        this.sqlFieldName = sqlFieldName;
    }

    public String getClassFieldName() {
        return classFieldName;
    }

    public void setClassFieldName(String classFieldName) {
        this.classFieldName = classFieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "sqlFieldName='" + sqlFieldName + '\'' +
                ", classFieldName='" + classFieldName + '\'' +
                ", fieldValue=" + fieldValue +
                ", fieldType=" + fieldType +
                '}';
    }
}
