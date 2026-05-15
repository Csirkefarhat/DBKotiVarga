package ehu.eus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transactions {

    //Query 1
    public static void employeesAboveAverageSalary(Connection conn) {
        String sql = """
            SELECT Fname, Lname, Salary
            FROM employee
            WHERE Salary > (
                SELECT AVG(Salary)
                FROM employee
            )
            """;

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            System.out.println("Employees with above average salary:");

            while (rs.next()) {
                String fname = rs.getString("Fname");
                String lname = rs.getString("Lname");
                double salary = rs.getDouble("Salary");

                System.out.println(fname + " " + lname + " - " + salary);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error executing query");
            e.printStackTrace();
        }
    }

    //Query 2
    public static void restaurantsByCity(Connection conn) {

        String sql = """
                SELECT city, COUNT(*) AS restaurant_count
                FROM restaurant
                GROUP BY city
                HAVING COUNT(*) > 2
                """;

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            System.out.println("Cities with more than 2 restaurants:");

            while (rs.next()) {

                String city = rs.getString("city");
                int count = rs.getInt("restaurant_count");

                System.out.println(city + " - " + count + " restaurants");
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {

            System.out.println("Error executing Query 2");
            e.printStackTrace();
        }
    }

    // Query 3
    public static void expensiveRestaurants(Connection conn) {

        String sql = """
                SELECT r.restaurname, r.city
                FROM restaurant r
                WHERE EXISTS
                (
                    SELECT *
                    FROM serves s
                    WHERE s.restaurname = r.restaurname
                    AND s.price > 20
                )
                """;

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            System.out.println("Restaurants serving dishes above 20 euros:");

            while (rs.next()) {

                String restaurant = rs.getString("restaurname");
                String city = rs.getString("city");

                System.out.println(restaurant + " - " + city);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {

            System.out.println("Error executing Query 3");
            e.printStackTrace();
        }
    }

}
