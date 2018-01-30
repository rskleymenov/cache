package cache.crud.interfaces;

import cache.exceptions.CacheSQLException;
import cache.jdbc.ConnectionFactory;
import cache.models.User;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CRUDRepositoryImpl implements CRUDRepository<User>{

    private static final Logger logger = Logger.getLogger(CRUDRepository.class);

    private ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

    public User getItem(long id) {
        Connection connection = connectionFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select id, name, surname from user where id = ?");
            preparedStatement.setLong(1, id);
            logger.info(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            logger.info(resultSet);
            resultSet.next();
            int userId = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");

            User user = new User();
            user.setId(userId);
            user.setName(name);
            user.setSurname(surname);
            return user;
        } catch (SQLException exc) {
            logger.error(exc);
            throw new CacheSQLException(exc);
        }
    }
}
