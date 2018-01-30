package cache.exceptions;

public class CacheIllegalAccessException extends RuntimeException {
    public CacheIllegalAccessException(Throwable exc) {
        super(exc);
    }
}
