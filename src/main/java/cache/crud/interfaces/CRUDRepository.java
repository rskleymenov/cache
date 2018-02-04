package cache.crud.interfaces;

import java.util.List;

public interface CRUDRepository {

    <T> T getItem(long id, Class<T> clazz);

    long saveItem(Object obj);

    void removeItem(long id, Class<?> clazz);

    <T> List<T> selectQueryItems(String statement, Object[] args, Class<T> clazz);


}
