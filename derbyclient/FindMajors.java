import java.sql.*;
import java.util.Scanner;
import org.apache.derby.jdbc.ClientDataSource;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;

public class FindMajors {
   private static void displayJTable(JTable table) {
      JFrame frame = new JFrame("JTable Test Display");
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      JScrollPane scrollPane = new JScrollPane(table);
      table.setFillsViewportHeight(true);

      panel.add(scrollPane, BorderLayout.CENTER);
      frame.getContentPane().add(panel);

      frame.pack();
      frame.setVisible(true);
   }

   private static void displayData(ResultSet rs) throws SQLException {
      rs.last();
      int rowCount = rs.getRow();
      rs.beforeFirst();
      int columnCount = rs.getMetaData().getColumnCount();

      TableModel dataModel = new AbstractTableModel() {
         public int getColumnCount() { return columnCount; }
         public int getRowCount() { return rowCount; }
         public Object getValueAt(int rowIndex, int columnIndex) {
            try {
               rs.absolute(rowIndex + 1);
               Object o = rs.getObject(columnIndex + 1);
               return o;
            } catch (SQLException e) {
               System.out.println(e.toString());
               return null;
            }
         }

      };

      displayJTable(new JTable(dataModel));
   }

   public static void main(String[] args) {
      System.out.print("Enter a department name: ");
      Scanner sc = new Scanner(System.in);
      String major = sc.next();
      sc.close();
      String qry = "select sname, gradyear "
            + "from student, dept "
            + "where did = majorid "
            + "and dname = '" + major + "'";

      ClientDataSource ds = new ClientDataSource();
      ds.setServerName("localhost");
      ds.setDatabaseName("studentdb");
      try (Connection conn = ds.getConnection()) {
         conn.setAutoCommit(false);
         Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
         ResultSet rs = stmt.executeQuery(qry);

         displayData(rs);
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }
}
