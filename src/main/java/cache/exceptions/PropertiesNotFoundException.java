package cache.exceptions;

public class PropertiesNotFoundException extends RuntimeException {
    public PropertiesNotFoundException(String msg, Throwable exc) {
        super(msg, exc);
    }
}
