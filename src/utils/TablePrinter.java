package utils;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.Vector;

/**
 * A table printer for the program. (GUI)
 */
public class TablePrinter {

    /**
     * Prevent create an object of this class.
     */
    private TablePrinter() {
    }

    /**
     * This display method is only work for Data-Query-Language (DQL).
     * The <code>tableTitle</code> is the "Title" of whole float window.
     *
     * @param DQL
     * @param tableTitle
     * @return int (Maximum Row Number)
     */
    static int display(String DQL, String tableTitle) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Vector<String> title = new Vector<>();
        int maxRow = 0;
        try {
            connection = DB_Utility.connect();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            resultSet = statement.executeQuery(DQL);
            resultSet.last();
            maxRow = resultSet.getRow();
            resultSet.beforeFirst();
            if (DQL.contains(" AS ") || DQL.contains(" as ")) {
                String[] renameTitle = DQL.split("'");
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    title.add(renameTitle[2 * i - 1]);//Corresponding odd index
                }
            } else {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    title.add(resultSet.getMetaData().getColumnName(i));
                }
            }
            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Empty Set");
            } else {
                resultSet.beforeFirst();
                new ShowWindow(resultSet, title, tableTitle);
            }
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                } catch (Exception throwable) {
                    throwable.printStackTrace();
                }
            }
            String info = getErrorInfoFromException(e);
            JOptionPane.showMessageDialog(null, info);
        } finally {
            DB_Utility.close(connection, statement, resultSet);
        }
        return maxRow;
    }

    /**
     * Handle the exception in GUI format.
     *
     * @param e
     * @return String (Exception Message)
     */
    private static String getErrorInfoFromException(Exception e) {
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            return "\r\n" + stringWriter.toString() + "\r\n";
        } catch (Exception ee) {
            return "fatalException: getErrorInfoFromException";
        }
    }

}