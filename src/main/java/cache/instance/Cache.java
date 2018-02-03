package cache.instance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    private static final Map<Key, Object> cache = new ConcurrentHashMap<Key, Object>();

    private static volatile Cache instance = null;

    public static synchronized Cache getInstance() {
        if (instance == null)
            synchronized (Cache.class) {
                if (instance == null)
                    instance = new Cache();
            }
        return instance;
    }

    public void put(Object identifier, Object value) {
        Key key = new Key(identifier, value.getClass());
        put(key, value);
    }

    public <T> T get(Object identifier, Class<T> clazz) {
        Key key = new Key(identifier, clazz);
        return get(key);
    }

    public void put(Key key, Object value) {
        cache.put(key, value);
    }

    public <T> T get(Key key) {
        Object cachedValue = cache.get(key);
        return (T) cachedValue;
    }

    public class Key {
        private Object id;
        private Class<?> clazz;

        public Key(Object id, Class<?> clazz) {
            this.id = id;
            this.clazz = clazz;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (id != null ? !id.equals(key.id) : key.id != null) return false;
            return clazz != null ? clazz.equals(key.clazz) : key.clazz == null;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
            return result;
        }
    }

}
