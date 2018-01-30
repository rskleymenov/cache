package cache;

import cache.jdbc.ConnectionFactory;
import org.apache.log4j.Logger;

import java.sql.Connection;

public class App {
    private static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] argv) {
        Connection connection = ConnectionFactory.getInstance().getConnection();
        assert connection!=null;

    }

}
