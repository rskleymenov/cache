package cache.crud;

import cache.instance.Cache;
import cache.sql.SQLFormatter;

import java.util.List;

public class CacheableCRUDRepo extends CRUDRepo {

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
        String filledCacheQuery = SQLFormatter.formatCacheQuery(query, args);
        if (isQueryCacheEnabled) {
            List<T> items = cache.getQuery(filledCacheQuery);
            if (!items.isEmpty()) {
                return items;
            } else {
                List<T> resultItems = super.selectQueryItems(query, args, clazz);
                if (resultItems != null && !resultItems.isEmpty()) {
                    cache.putQuery(filledCacheQuery, resultItems);
                }
                return resultItems;
            }
        }
        return super.selectQueryItems(query, args, clazz);
    }

    public long saveItem(Object obj) {
        //TODO invalidate query cache
        return super.saveItem(obj);
    }

    public void removeItem(long id, Class<?> clazz) {
        //TODO invalidate query cache
        //TODO removeItem from default storage
        super.removeItem(id, clazz);
    }


    public Boolean isCacheEnabled() {
        return isCacheEnabled;
    }

    public void setCacheEnabled(Boolean cacheEnabled) {
        this.isCacheEnabled = cacheEnabled;
    }
}
