package cache.instance;

import cache.annotations.parsers.ClassParser;
import cache.annotations.parsers.models.ClassInfo;
import cache.tree.RadixTree;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class Cache {

    private static final String QUERY_STORAGE_NAME = "java.util.Map";

    private static final Logger logger = Logger.getLogger(Cache.class);

    private static volatile Cache instance = null;
    private static final CacheManager cacheManager = CacheManager.getInstance();

    private static final Map<Key, Object> storage = new ConcurrentHashMap<Key, Object>();
    private static final Map<String, List<Key>> queryStorage;

    static {
        if (QUERY_STORAGE_NAME.equals("java.util.Map")) {
            queryStorage = new ConcurrentHashMap<>();
        } else {
            queryStorage = new RadixTree<>();
        }
    }

    private Cache() {
        // suppress incorrect creation
    }

    public static synchronized Cache getInstance() {
        if (instance == null) {
            synchronized (Cache.class) {
                if (instance == null) {
                    instance = new Cache();
                    logger.info("Cache instance created!");
                }
            }
        }
        return instance;
    }

    protected Map<Key, Object> getStorage() {
        return storage;
    }

    public void invalidateCache() {
        storage.clear();
        queryStorage.clear();
        logger.info("Cache totally invalidated");
        logger.info("Cache size is: " + storage.size());
        logger.info("Query cache size is: " + queryStorage.size());
    }

    public void invalidateQueryCache() {
        logger.info("Invalidating query cache...");
        for (List<Key> keys : queryStorage.values()) {
            for (Key key : keys) {
                storage.remove(key);
                logger.info("Invalidated item is " + key.getId() + " class: " + key.getClazz());
            }
        }
        queryStorage.clear();
        logger.info("Invalidating query cache is done!");
        logger.info("Query cache size is: " + queryStorage.size());
    }

    public <T> void putQuery(String query, List<T> resultItems) {
        Map<Cache.Key, T> queryResult = new HashMap<Key, T>();
        for (T item : resultItems) {
            ClassInfo classInfo = ClassParser.getClassInfoWithObjectValues(item);
            Key<T> key = new Key(classInfo.getId(), item.getClass());
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
            storage.put(key, value);
        }
        queryStorage.put(query, queryIdentifiers);
    }

    public <T> Tuple<T> getQuery(String query) {
        List<T> cachedObjects = new ArrayList<T>();
        List<Key<T>> invalidatedKeys = new ArrayList<Key<T>>();
        List<Key> keys = queryStorage.get(query);
        if (keys != null) {
            for (Key key : keys) {
                T cachedObj = get(key);
                if (cachedObj == null) {
                    invalidatedKeys.add(key);
                } else {
                    cachedObjects.add(cachedObj);
                }
            }
        }
        return new Tuple<T>(cachedObjects, invalidatedKeys);
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
        storage.put(key, value);
    }

    public <T> T get(Key key) {
        return (T) storage.get(key);
    }

    public <T> void remove(Object identifier, Class<T> clazz) {
        Key<T> key = new Key<T>(identifier, clazz);
        storage.remove(key);
    }

    public class Key<T> {
        private Object id;
        private Class<? extends T> clazz;
        private Date creationDate = new Date();

        public Key(Object id, Class<? extends T> clazz) {
            this.id = id;
            this.clazz = clazz;
        }

        public Object getId() {
            return id;
        }

        public Class<? extends T> getClazz() {
            return clazz;
        }

        public Date getCreationDate() {
            return creationDate;
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

    public class Tuple<T> {
        private List<T> cachedObjects;
        private List<Key<T>> invalidatedKeys;

        public Tuple(List<T> cachedObjects, List<Key<T>> invalidatedKeys) {
            this.cachedObjects = cachedObjects;
            this.invalidatedKeys = invalidatedKeys;
        }

        public List<T> getCachedObjects() {
            return cachedObjects;
        }

        public List<Key<T>> getInvalidatedKeys() {
            return invalidatedKeys;
        }
    }


}
