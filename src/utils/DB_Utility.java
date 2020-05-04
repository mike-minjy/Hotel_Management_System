package utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DB_Utility {

    private DB_Utility() {
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
     * give a current time feedback to user
     */
    public static void printCurrentTime() {
        System.out.println("***************************************");
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("The current time is " + currentTime.format(formatter));
        System.out.println("***************************************");
    }

    /**
     * Deallocate resources
     *
     * @param connection
     * @param statement
     * @param resultSet
     */
    protected static void close(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        close(connection, statement);
    }

    /**
     * Overload the close(Connection,Statement,ResultSet) method which did not have created a ResultSet instance.
     *
     * @param connection
     * @param statement
     */
    protected static void close(Connection connection, Statement statement) {
        if (statement != null) {
            try {
                statement.close();
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
