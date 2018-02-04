package cache.annotations.parsers.models;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
    private String tableName;
    private long id;
    private List<FieldInfo> fields;

    public ClassInfo() {

    }

    public ClassInfo(ClassInfo classInfo) {
        this.tableName = classInfo.getTableName();
        this.id = classInfo.getId();
        this.fields = new ArrayList<FieldInfo>(classInfo.getFields());
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<FieldInfo> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "tableName='" + tableName + '\'' +
                ", id=" + id +
                ", fields=" + fields +
                '}';
    }
}
