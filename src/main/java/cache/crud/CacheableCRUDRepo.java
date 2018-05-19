package cache.crud;

import cache.annotations.parsers.ClassParser;
import cache.annotations.parsers.models.ClassInfo;
import cache.instance.Cache;
import cache.sql.SQLFormatter;
import org.apache.log4j.Logger;

import java.util.List;

public class CacheableCRUDRepo extends CRUDRepo {

    private static final Logger logger = Logger.getLogger(CacheableCRUDRepo.class);

    private volatile Boolean isCacheEnabled = Boolean.TRUE;
    private volatile Boolean isQueryCacheEnabled = Boolean.TRUE;
    private final Cache cache = Cache.getInstance();

    public <T> T getItem(long id, Class<T> clazz) {
        if (isCacheEnabled) {
            T cachedValue = cache.get(id, clazz);
            if (cachedValue != null) {
                return cachedValue;
            } else {
                T repoItem = super.getItem(id, clazz);
                if (repoItem != null) {
                    cache.put(id, repoItem);
                }
                return repoItem;
            }
        }
        return super.getItem(id, clazz);
    }

    public <T> List<T> selectQueryItems(String query, Object[] args, Class<T> clazz) {
        if (isQueryCacheEnabled) {
            ClassInfo classInfo = ClassParser.getClassInfo(clazz);
            String filledCacheQuery = SQLFormatter.formatCacheQuery(query, args, classInfo);
            logger.info("Cache query is: " + filledCacheQuery);
            Cache.Tuple<T> tuple = cache.getQuery(filledCacheQuery);
            List<T> cachedItems = tuple.getCachedObjects();
            List<Cache.Key<T>> invalidatedKeys = tuple.getInvalidatedKeys();
            if (!cachedItems.isEmpty()) {
                if (!invalidatedKeys.isEmpty()) {
                    for (Cache.Key<T> invalidatedKey : invalidatedKeys) {
                        Object invalidatedItemId = invalidatedKey.getId();
                        logger.info(String.format("For query [%s] found invalidated item with id: %s", filledCacheQuery, invalidatedItemId));
                        T item = getItem(Long.valueOf(invalidatedItemId.toString()), invalidatedKey.getClazz());
                        logger.info(String.format("Fetched item: %s", item));
                        cachedItems.add(item);
                    }
                }
                return cachedItems;
            } else {
                List<T> resultItems = super.selectQueryItems(query, args, clazz);
                if (resultItems != null && !resultItems.isEmpty()) {
                    //TODO fix sorting of cached objects - problem inside in queryResult
                    //TODO because of map structure [NEED CHANGE TO LIST<TUPLE>]
                    cache.putQuery(filledCacheQuery, resultItems);
                }
                return resultItems;
            }
        }
        return super.selectQueryItems(query, args, clazz);
    }

    public long saveItem(Object obj) {
        long saveItem = super.saveItem(obj);
        cache.invalidateQueryCache();
        return saveItem;
    }

    public void removeItem(long id, Class<?> clazz) {
        super.removeItem(id, clazz);
        cache.invalidateQueryCache();
        cache.remove(id, clazz);
    }


    public Boolean isCacheEnabled() {
        return isCacheEnabled;
    }

    public void setCacheEnabled(Boolean cacheEnabled) {
        this.isCacheEnabled = cacheEnabled;
    }
}
