package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DB_Utility {

    /**
     * Prevent create an object of this class.<br>
     * This class is only an <code>util</code> class.
     */
    private DB_Utility() {
    }

    /**
     * Connect with database
     *
     * @return Connection
     * @throws Exception
     */
    protected static Connection connect() throws Exception {
        ResourceBundle resource = ResourceBundle.getBundle("information");
        String url = resource.getString("URL");
        String username = resource.getString("administrator");
        String password = resource.getString("password");
        return DriverManager.getConnection(url, username, password);
        //Initialise the connection with database schema.
//        Connection connection = DriverManager.getConnection(url, username, password);
//        return connection;
    }

    /**
     * Give a current time feedback to user.
     */
    public static void printCurrentTime() {
        System.out.println("************************************");
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current time --> " + currentTime.format(formatter));
        System.out.println("************************************");
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
     * Overload the <code>close(Connection,Statement,ResultSet)</code> method which did not have created an ResultSet instance.
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
