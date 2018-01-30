package cache.annotations.parsers.models;

public class FieldInfo {
    private String fieldName;
    private Object fieldValue;
    private Class<?> fieldType;

    public FieldInfo(String fieldName, Object fieldValue, Class<?> fieldType) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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
                "fieldName='" + fieldName + '\'' +
                ", fieldValue=" + fieldValue +
                ", fieldType=" + fieldType +
                '}';
    }
}
