package utils;

import java.sql.*;
import java.util.ResourceBundle;

public class DB_Connection {

    private DB_Connection() {
    }

    /**
     * Connect with database
     *
     * @return
     * @throws Exception
     */
    protected static Connection connect() throws Exception {
        ResourceBundle resource = ResourceBundle.getBundle("information");
        String url = resource.getString("URL");
        String username = resource.getString("administrator");
        String password = resource.getString("password");
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    /**
     * Deallocate resources
     *
     * @param connection
     * @param preparedStatement
     * @param resultSet
     */
    protected static void close(Connection connection, Statement preparedStatement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
