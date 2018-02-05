package cache.instance;

import cache.annotations.parsers.ClassParser;
import cache.annotations.parsers.models.ClassInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    private static final Map<Key, Object> cache = new ConcurrentHashMap<Key, Object>();

    private static final Map<String, List<Key>> queryCache = new ConcurrentHashMap<String, List<Key>>();

    private static volatile Cache instance = null;

    private Cache() {
        // suppress incorrect creation
    }

    public static synchronized Cache getInstance() {
        if (instance == null) {
            synchronized (Cache.class) {
                if (instance == null) {
                    instance = new Cache();
                }
            }
        }
        return instance;
    }

    public <T> void putQuery(String query, List<T> resultItems) {
        Map<Cache.Key, T> queryResult = new HashMap<Key, T>();
        for (T item : resultItems) {
            ClassInfo classInfo = ClassParser.getClassInfoWithObjectValues(item);
            Key key = new Key(classInfo.getId(), item.getClass());
            queryResult.put(key, item);
        }
        putQuery(query, queryResult);
    }

    public <T> void putQuery(String query, Map<Key, T> queryResult) {
        List<Key> queryIdentifiers = new ArrayList<Key>();
        for (Map.Entry<Key, T> entry : queryResult.entrySet()) {
            Key key = entry.getKey();
            Object value = entry.getValue();
            queryIdentifiers.add(key);
            cache.put(key, value);
        }
        queryCache.put(query, queryIdentifiers);
    }

    public <T> List<T> getQuery(String query) {
        List<T> cachedObjects = new ArrayList<T>();
        List<Key> keys = queryCache.get(query);
        if (keys != null) {
            for (Key key : keys) {
                T cachedObj = get(key);
                cachedObjects.add(cachedObj);
            }
        }
        return cachedObjects;
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
        return (T) cache.get(key);
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
