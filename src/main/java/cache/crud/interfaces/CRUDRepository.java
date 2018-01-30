package cache.crud.interfaces;

public interface CRUDRepository<T> {

    T getItem(long id);
}
