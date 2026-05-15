package ehu.eus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Queries {
    //-----------------Lili-----------------
    public static void queryAboveAvgSalary(Connection conn) throws SQLException, ClassNotFoundException {
        String sql = """
        SELECT e.Fname, e.Lname, e.Salary, d.Dname
        FROM employee e
        JOIN department d ON e.Dno = d.Dnumber
        WHERE e.Salary > (
            SELECT AVG(e2.Salary)
            FROM employee e2
            WHERE e2.Dno = e.Dno
        )
        ORDER BY d.Dname, e.Salary DESC
    """;
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Fname\t\tLname\t\tSalary\t\tDept");
            while (rs.next()) {
                System.out.printf("%s\t%s\t%.2f\t%s%n",
                    rs.getString("Fname"), rs.getString("Lname"),
                    rs.getDouble("Salary"), rs.getString("Dname"));
            }
        }
    }

    public static void queryRestaurantsAbvAvg(Connection conn) throws SQLException, ClassNotFoundException {
        String sql = """
        SELECT s.restaurname, SUM(s.amount) AS totalRevenue
        FROM sales s
        GROUP BY s.restaurname
        HAVING SUM(s.amount) > (
            SELECT AVG(totalPerRestaurant)
            FROM (
                SELECT SUM(amount) AS totalPerRestaurant
                FROM sales
                GROUP BY restaurname
            ) AS subq
        )
        ORDER BY totalRevenue DESC
        """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Restaurant\t\tTotal Revenue");
            while (rs.next()) {
                System.out.printf("%s\t\t%.2f%n",
                    rs.getString("restaurname"),
                    rs.getDouble("totalRevenue"));
            }
        }
    }

    public static void queryCustomersAllMadridTrips(Connection conn) throws SQLException, ClassNotFoundException {
        String sql = """
            SELECT c.CustomerId, c.custname
            FROM customer c
            WHERE EXISTS (
                SELECT 1 FROM hotel_trip_customer htc
                WHERE htc.CustomerId = c.CustomerId
            )
            AND NOT EXISTS (
                SELECT 1
                FROM hotel_trip_customer htc
                JOIN trip t ON htc.TripTo = t.TripTo 
                        AND htc.DepartureDate = t.DepartureDate
                WHERE htc.CustomerId = c.CustomerId
                AND t.CityDeparture != 'Donostia'
            )
        """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("CustomerId\tName");
            while (rs.next()) {
                System.out.println(rs.getString("CustomerId") + "\t" + rs.getString("custname"));
            }
        }
    }

    //-----------------Kata-----------------
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

            System.out.println("");

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

            System.out.println(":");

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
