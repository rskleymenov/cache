package cache.exceptions;

public class CachePropertiesNotFoundException extends RuntimeException {
    public CachePropertiesNotFoundException(Throwable exc) {
        super(exc);
    }
}
