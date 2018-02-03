package cache.crud;

import cache.instance.Cache;

public class CacheableCRUDRepo extends CRUDRepo {

    private volatile Boolean isCacheEnabled = Boolean.TRUE;
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

    public long saveItem(Object obj) {
        return super.saveItem(obj);
    }

    public void removeItem(long id, Class<?> clazz) {
        super.removeItem(id, clazz);
    }


    public Boolean isCacheEnabled() {
        return isCacheEnabled;
    }

    public void setCacheEnabled(Boolean cacheEnabled) {
        this.isCacheEnabled = cacheEnabled;
    }
}
