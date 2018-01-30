package cache.jdbc;

import cache.exceptions.PropertiesNotFoundException;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionFactory {

    private static volatile ConnectionFactory instance = null;

    private ConnectionFactory() {
    }

    private static final Logger logger = Logger.getLogger(ConnectionFactory.class);
    private static final Properties properties;

    private static final String CONNECTION_FAILED_EXCEPTION = "Provided url [%s] or user name [%s] or password [%s] is incorrect";
    private static final String DATABASE_PROPERTIES = "src/main/resources/database.properties";
    private static final String URL = "url";
    private static final String USER_NAME = "user";
    private static final String PASSWORD = "password";

    static {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(DATABASE_PROPERTIES));
        } catch (IOException exc) {
            logger.error("Error occurred during reading default database.properties");
            throw new PropertiesNotFoundException("database.properties is no found", exc);
        }
    }

    public static synchronized ConnectionFactory getInstance() {
        if (instance == null)
            synchronized (ConnectionFactory.class) {
                if (instance == null)
                    instance = new ConnectionFactory();
            }
        return instance;
    }

    public Connection getConnection() {
        String url = properties.getProperty(URL);
        String password = properties.getProperty(PASSWORD);
        String userName = properties.getProperty(USER_NAME);
        return getConnection(url, userName, password);
    }

    public Connection getConnection(String url, String userName, String password) {
        checkDriverPresence();
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            logger.info("Connection established: " + url);
            return connection;
        } catch (SQLException exc) {
            logger.error("Connection Failed! Check output console");
            throw new IllegalArgumentException(String.format(CONNECTION_FAILED_EXCEPTION, url, userName, password), exc);
        }
    }

    private static void checkDriverPresence() {
        try {
            String driverClassName = properties.getProperty("driverClassName");
            Class.forName(driverClassName);
            logger.info(driverClassName + " Driver Registered!");
        } catch (ClassNotFoundException exc) {
            logger.error("Where is your MySQL JDBC Driver?");
            throw new IllegalArgumentException("Driver not found", exc);
        }
    }

}
