package structure.helper;

public class Tuple {
    private String str;
    private Integer value;

    public Tuple(String str, Integer value) {
        this.str = str;
        this.value = value;
    }

    public String getStr() {
        return str;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple tuple = (Tuple) o;

        if (str != null ? !str.equals(tuple.str) : tuple.str != null) return false;
        return value != null ? value.equals(tuple.value) : tuple.value == null;
    }

    @Override
    public int hashCode() {
        int result = str != null ? str.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
