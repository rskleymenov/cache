package cache.exceptions;


public class CacheSQLException extends RuntimeException{
    public CacheSQLException(Throwable exc) {
        super(exc);
    }
}
