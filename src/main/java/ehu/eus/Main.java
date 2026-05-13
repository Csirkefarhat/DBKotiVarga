package ehu.eus;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Properties config = loadDatabaseConfig();
			conn = DriverManager.getConnection(
					config.getProperty("db.url"),
					config.getProperty("db.username"),
					config.getProperty("db.password")
			);
			System.out.println(" --> Connection Established"); 

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			System.out.println("ooops  --> Connection Failed");
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
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

	private static Properties loadDatabaseConfig() throws IOException {
		Properties properties = new Properties();
		try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("db.properties")) {
			if (inputStream == null) {
				throw new IOException("Missing db.properties on the classpath");
			}
			properties.load(inputStream);
		}
        catch (IOException e) {
            throw new IOException("Error loading db.properties", e);
        }
		return properties;
	}
}