package utils;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;


public class TablePrinter {

    static int display(String DQL, String tableTitle) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Vector<String> title = new Vector<>();
        int maxRow = 0;
        try {
            connection = DB_Utility.connect();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(DQL);
            resultSet.last();
            maxRow = resultSet.getRow();
            resultSet.beforeFirst();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                title.add(resultSet.getMetaData().getColumnName(i));
            }
            if (!resultSet.next() || resultSet.getString(1).equals("0")) {
                JOptionPane.showMessageDialog(null, "not found");
            } else {
                new ShowWindow(resultSet, title, tableTitle);
            }
        } catch (Exception e) {
            String info = getErrorInfoFromException(e);
            JOptionPane.showMessageDialog(null, info);
        } finally {
            DB_Utility.close(connection, statement, resultSet);
        }
        return maxRow;
    }

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