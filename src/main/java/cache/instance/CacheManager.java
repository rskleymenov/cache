package cache.instance;

import cache.exceptions.CacheGenericRuntimeException;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

class CacheManager {
    private static final int SCANNING_TIME = 500;
    private static final int SECONDS_TO_LIVE = 30;
    private static final Cache cache = Cache.getInstance();
    private static final Logger logger = Logger.getLogger(CacheManager.class);

    private static volatile CacheManager instance = null;

    private CacheManager() {
        // protect against creation
    }

    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                    logger.info("CacheManager instance created!");
                }
            }
        }
        return instance;
    }

    static {
        Thread cleanerThread = new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            while (true) {
                                logger.info("CacheManager scanning for expired objects...");
                                Map<Cache.Key, Object> storage = cache.getStorage();
                                Date currentTime = new Date();
                                Iterator<Map.Entry<Cache.Key, Object>> iterator = storage.entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry<Cache.Key, Object> entry = iterator.next();
                                    Cache.Key key = entry.getKey();
                                    Date creationDate = key.getCreationDate();
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(creationDate);
                                    cal.add(Calendar.SECOND, SECONDS_TO_LIVE);
                                    Date expirationDate = cal.getTime();
                                    if (expirationDate.before(currentTime)) {
                                        iterator.remove();
                                        logger.info("CacheManager found an expired object in the Cache.");
                                    }
                                }
                                Thread.sleep(SCANNING_TIME);
                            }
                        } catch (Exception exc) {
                            throw new CacheGenericRuntimeException(exc);
                        }
                    }
                });
        cleanerThread.setPriority(Thread.MIN_PRIORITY);
        // TODO uncomment in final version
        // cleanerThread.setDaemon(true);
        cleanerThread.start();
    }
}
