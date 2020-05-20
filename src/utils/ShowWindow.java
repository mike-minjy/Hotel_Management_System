package utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

class ShowWindow extends JFrame {
    JTable table;
    DefaultTableModel defaultModel;

    ShowWindow(ResultSet rs, Vector<String> title, String tableTitle) throws Exception {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int row;
        ResultSetMetaData rsMetaData = rs.getMetaData();
        defaultModel = new DefaultTableModel(null, title);
        table = new JTable(defaultModel);
        table.setPreferredScrollableViewportSize(new Dimension(600, 600));
        JScrollPane s = new JScrollPane(table);
        JPanel panel = new JPanel();

        for (int i = 1; rs.next(); i++) {
            if (i == 1) {
                rs.first();
            }
            row = 1;
            title = new Vector<>();
            while (true) {
                title.add(rs.getString(row));
                if (row == rsMetaData.getColumnCount()) {
                    break;
                }
                row += 1;
            }
            defaultModel.addRow(title);
        }

        Container contentPane = frame.getContentPane();
        contentPane.add(panel, BorderLayout.NORTH);
        contentPane.add(s, BorderLayout.CENTER);
        frame.setTitle(tableTitle);
        frame.pack();
        frame.setVisible(true);
        table.revalidate();
    }
}
