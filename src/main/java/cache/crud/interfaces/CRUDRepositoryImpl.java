package cache.crud.interfaces;

import cache.annotations.parsers.ClassParser;
import cache.annotations.parsers.FieldHelper;
import cache.annotations.parsers.models.ClassInfo;
import cache.annotations.parsers.models.FieldInfo;
import cache.exceptions.CacheGenericRuntimeException;
import cache.exceptions.CacheItemNotDeletedException;
import cache.exceptions.CacheItemNotInsertedException;
import cache.exceptions.CacheSQLException;
import cache.jdbc.ConnectionFactory;
import cache.sql.SQLFormatter;
import org.apache.log4j.Logger;

import java.sql.*;

public class CRUDRepositoryImpl implements CRUDRepository {

    private static final Logger logger = Logger.getLogger(CRUDRepository.class);

    private ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

    public <T> T getItem(long id, Class<T> clazz) {
        ClassInfo classInfo = ClassParser.getClassInfo(clazz);
        String sql = SQLFormatter.formatGetItemSQL(classInfo);
        logger.info("Formatted sql: " + sql);
        classInfo.setId(id);
        Connection connection = connectionFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            logger.info(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                for (FieldInfo fieldInfo : classInfo.getFields()) {
                    Object fieldValue = resultSet.getObject(fieldInfo.getSqlFieldName());
                    fieldInfo.setFieldValue(fieldValue);
                }
                T foundEntity = createInstance(classInfo, clazz);
                logger.info(foundEntity);
                return foundEntity;
            } else {
                logger.info("Nothing found by id: [" + id + "] in table: [" + classInfo.getTableName() + "]");
                return null;
            }
        } catch (SQLException exc) {
            logger.error(exc);
            throw new CacheSQLException(exc);
        } finally {
            closeConnection(connection);
        }
    }

    public long saveItem(Object obj) {
        ClassInfo classInfo = ClassParser.getClassInfoWithObjectValues(obj);
        String sql = SQLFormatter.formatInsertItemSQL(classInfo);
        Connection connection = connectionFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int indx = 0;
            for (FieldInfo fieldInfo : classInfo.getFields()) {
                preparedStatement.setObject(++indx, fieldInfo.getFieldValue());
            }
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                logger.error(String.format("Item [%s] is not inserted in table [%]", obj, classInfo.getTableName()));
                throw new CacheItemNotInsertedException();
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                logger.error(String.format("Item [%s] is not inserted in table [%]", obj, classInfo.getTableName()));
                throw new CacheItemNotInsertedException();
            }

        } catch (SQLException exc) {
            logger.error(exc);
            throw new CacheSQLException(exc);
        } finally {
            closeConnection(connection);
        }
    }

    public void removeItem(long id, Class<?> clazz) {
        ClassInfo classInfo = ClassParser.getClassInfo(clazz);
        String sql = SQLFormatter.formatDeleteItemSQL(classInfo);
        Connection connection = connectionFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            logger.info(preparedStatement);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new CacheItemNotDeletedException();
            }
        } catch (SQLException exc) {
            logger.error(exc);
            throw new CacheSQLException(exc);
        } finally {
            closeConnection(connection);
        }
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException exc) {
            logger.error(exc);
            throw new CacheSQLException(exc);
        }
    }

    private <T> T createInstance(ClassInfo classInfo, Class<T> clazz) {
        try {
            T createdInstance = clazz.newInstance();
            FieldHelper.writePrivateField("id", classInfo.getId(), createdInstance);
            for (FieldInfo fieldInfo : classInfo.getFields()) {
                FieldHelper.writePrivateField(fieldInfo.getClassFieldName(), fieldInfo.getFieldValue(), createdInstance);
            }
            return createdInstance;
        } catch (Exception exc) {
            throw new CacheGenericRuntimeException(exc);
        }
    }

}
