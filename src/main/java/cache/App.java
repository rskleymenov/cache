package cache;

import cache.crud.interfaces.CRUDRepositoryImpl;
import cache.jdbc.ConnectionFactory;
import cache.models.User;
import org.apache.log4j.Logger;

import java.sql.Connection;

public class App {
    private static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] argv) {
        CRUDRepositoryImpl crudRepository = new CRUDRepositoryImpl();
        User item = crudRepository.getItem(1);
        System.out.println(item);

    }

}
