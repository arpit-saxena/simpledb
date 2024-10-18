import java.sql.*;
import java.util.Scanner;
import simpledb.jdbc.embedded.EmbeddedDriver;
import simpledb.jdbc.network.NetworkDriver;

public class SimpleIJ {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      System.out.println("Connect> ");
      String s = sc.nextLine();
      Driver d = (s.contains("//")) ? new NetworkDriver() : new EmbeddedDriver();

      try (Connection conn = d.connect(s, null);
           Statement stmt = conn.createStatement()) {
         System.out.print("\nSQL> ");
         String cmd = "";
         while (sc.hasNextLine()) {
            // process one line of input
            String line = sc.nextLine().trim();
            int lineIdx = 0;
            int semicolonIdx = line.indexOf(";", lineIdx);
            
            boolean exit = false;
            while (semicolonIdx != -1 && lineIdx < line.length()) {
               cmd += line.substring(lineIdx, semicolonIdx);
               // System.out.println("Will execute cmd: " + cmd);
               if (cmd.startsWith("exit")) {
                  exit = true;
                  break;
               } else if (cmd.startsWith("select")) {
                  doQuery(stmt, cmd);
               } else {
                  doUpdate(stmt, cmd);
               }

               cmd = "";
               lineIdx = semicolonIdx + 1;
            }

            if (exit) break;

            if (lineIdx < line.length()) {
               // some other command is in progress, which has not been ended by a semicolon
               cmd += line.substring(lineIdx) + " ";
               System.out.print("> ");
            } else {
               System.out.print("SQL> ");
            }
         }
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
      sc.close();
   }

   private static void doQuery(Statement stmt, String cmd) {
      try (ResultSet rs = stmt.executeQuery(cmd)) {
         ResultSetMetaData md = rs.getMetaData();
         int numcols = md.getColumnCount();
         int totalwidth = 0;

         // print header
         for(int i=1; i<=numcols; i++) {
            String fldname = md.getColumnName(i);
            int width = md.getColumnDisplaySize(i);
            totalwidth += width;
            String fmt = "%" + width + "s";
            System.out.format(fmt, fldname);
         }
         System.out.println();
         for(int i=0; i<totalwidth; i++)
            System.out.print("-");
         System.out.println();

         // print records
         while(rs.next()) {
            for (int i=1; i<=numcols; i++) {
               String fldname = md.getColumnName(i);
               int fldtype = md.getColumnType(i);
               String fmt = "%" + md.getColumnDisplaySize(i);
               if (fldtype == Types.INTEGER) {
                  int ival = rs.getInt(fldname);
                  System.out.format(fmt + "d", ival);
               }
               else {
                  String sval = rs.getString(fldname);
                  System.out.format(fmt + "s", sval);
               }
            }
            System.out.println();
         }
      }
      catch (SQLException e) {
         System.out.println("SQL Exception: " + e.getMessage());
      }
   }

   private static void doUpdate(Statement stmt, String cmd) {
      try {
         int howmany = stmt.executeUpdate(cmd);
         System.out.println(howmany + " records processed");
      }
      catch (SQLException e) {
         System.out.println("SQL Exception: " + e.getMessage());
      }
   }
}