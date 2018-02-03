package cache;

import cache.crud.interfaces.CRUDRepositoryImpl;
import cache.models.User;
import cache.models.UserJobInfo;
import org.apache.log4j.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] argv) {

        for (int i = 0; i < 1000; i++) {
            execute();
        }

    }

    private static void execute() {
        CRUDRepositoryImpl crudRepository = new CRUDRepositoryImpl();
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
