package ehu.eus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Queries {
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
}
