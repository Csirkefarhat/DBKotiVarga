package ehu.eus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			conn = DriverManager.getConnection("jdbc:mysql://dif-mysql.ehu.es:23306/DBI70", "DBI70", "DBI70"); 
			System.out.println(" --> Connection Established"); 

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			System.out.println("ooops  --> Connection Failed");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				System.out.println(" --> Connection Closed");
			} catch (SQLException e) {
				System.out.println("ooops  --> Closing the Connection Failed");
			}

		}
    }
}