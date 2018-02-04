package cache;

import cache.crud.CRUDRepo;
import cache.crud.CacheableCRUDRepo;
import cache.crud.interfaces.CRUDRepository;
import cache.models.User;
import cache.models.UserJobInfo;
import org.apache.log4j.Logger;

import java.util.List;

public class App {
    private static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] argv) {
        CRUDRepo crudRepository = new CRUDRepo();
        List<User> users = crudRepository.selectQueryItems("where id in (?, ?, ?)", new Object[]{3, 4, 1}, User.class);
        System.out.println(users);
    }

    private static void testCache() {
        CRUDRepository crudRepository = new CacheableCRUDRepo();
        for (int i = 1; i <= 9; i++) {
            User user = crudRepository.getItem(i, User.class);
            logger.info(user);
        }

        for (int i = 1; i <= 9; i++) {
            User user = crudRepository.getItem(i, User.class);
            logger.info(user);
        }
    }

    private static void executeNonCacheable() {
        CRUDRepository crudRepository = new CRUDRepo();
        User item = crudRepository.getItem(1, User.class);
        logger.info(item);

        User newUser = new User();
        newUser.setName("Nekit");
        newUser.setSurname("Shmekit");
        long userId = crudRepository.saveItem(newUser);

        User newlyCreatedUser = crudRepository.getItem(userId, User.class);
        logger.info(newlyCreatedUser);

        crudRepository.removeItem(newlyCreatedUser.getId(), User.class);

        User maybeDeletedUser = crudRepository.getItem(userId, User.class);
        logger.info(maybeDeletedUser);

        UserJobInfo userJobInfo = new UserJobInfo();
        userJobInfo.setUserId(1);
        userJobInfo.setCountry("Ukraine");
        userJobInfo.setCity("Kyiv");
        userJobInfo.setTitle("Middle Java Developer");
        userJobInfo.setJobName("Programmer");
        long userJobInfoId = crudRepository.saveItem(userJobInfo);

        UserJobInfo instertedItem = crudRepository.getItem(userJobInfoId, UserJobInfo.class);

        crudRepository.removeItem(instertedItem.getId(), UserJobInfo.class);
        logger.info(instertedItem);
    }

}
