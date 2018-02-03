package cache.crud.interfaces;

public interface CRUDRepository {

    <T> T getItem(long id, Class<T> clazz);

    long saveItem(Object obj);

    void removeItem(long id, Class<?> clazz);


}
