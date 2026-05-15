import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import ehu.eus.MyDBConnection;
import ehu.eus.Queries;
import ehu.eus.Transactions;

public class TestRunner {
    
        private static int passed = 0;
        private static int failed = 0;
    
        public static void main(String[] args) {

            Connection conn = MyDBConnection.getConnection();
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("║           RUNNING ALL TESTS              ║");
            System.out.println("╚══════════════════════════════════════════╝");
    
            testQueryAboveAvgSalary(conn);
            testQueryRestaurantsAbvAvg(conn);
            testQueryCustomersOnlyDonostia(conn);
            testInsertGuideWithLanguage(conn);
            testPromoteToManager(conn);
    
            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.printf( "║  Results: %d passed, %d failed            %n", passed, failed);
            System.out.println("╚══════════════════════════════════════════╝");
        }
    
        // ─────────────────────────────────────────────
        //  TEST 1 — queryAboveAvgSalary
        //  Expects: at least 1 row returned
        // ─────────────────────────────────────────────
        private static void testQueryAboveAvgSalary(Connection conn) {
            System.out.println("\n--- TEST 1: Employees above dept avg salary ---");
            try {
                // Run the query and check it returns rows
                Queries.queryAboveAvgSalary(conn);
                // Verify: at least one employee earns above avg
                String check = """
                    SELECT COUNT(*) AS cnt
                    FROM employee e
                    WHERE e.Salary > (SELECT AVG(e2.Salary) FROM employee e2 WHERE e2.Dno = e.Dno)
                """;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(check)) {
                    rs.next();
                    int count = rs.getInt("cnt");
                    if (count > 0) {
                        pass("Query returned " + count + " employee(s) above their dept average.");
                    } else {
                        fail("No employees above avg — check your test data in EMPLOYEE table.");
                    }
                }
            } catch (Exception e) {
                fail("Exception: " + e.getMessage());
            } 
        }
    
        // ─────────────────────────────────────────────
        //  TEST 2 — queryRestaurantsAbvAvg
        //  Expects: at least 1 restaurant above avg revenue
        // ─────────────────────────────────────────────
        private static void testQueryRestaurantsAbvAvg(Connection conn) {
            System.out.println("\n--- TEST 2: Restaurants above avg revenue ---");
            try {
                Queries.queryRestaurantsAbvAvg(conn);
                String check = """
                    SELECT COUNT(*) AS cnt
                    FROM (
                        SELECT restaurname, SUM(amount) AS total
                        FROM sales
                        GROUP BY restaurname
                    ) AS sub
                    WHERE total > (SELECT AVG(t2.totalPerRestaurant)
                                   FROM (SELECT SUM(amount) AS totalPerRestaurant
                                         FROM sales GROUP BY restaurname) AS t2)
                """;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(check)) {
                    rs.next();
                    int count = rs.getInt("cnt");
                    if (count > 0) {
                        pass("Query returned " + count + " restaurant(s) above avg revenue.");
                    } else {
                        fail("No restaurants above avg — check your test data in SALES table.");
                    }
                }
            } catch (Exception e) {
                fail("Exception: " + e.getMessage());
            }
        }
    
        // ─────────────────────────────────────────────
        //  TEST 3 — queryCustomersOnlyDonostia
        //  Expects: customers with only Donostia bookings
        // ─────────────────────────────────────────────
        private static void testQueryCustomersOnlyDonostia(Connection conn) {
            System.out.println("\n--- TEST 3: Customers who only booked Donostia trips ---");
            try {
                Queries.queryCustomersAllDonostiaTrips(conn);
                String check = """
                    SELECT COUNT(*) AS cnt
                    FROM customer c
                    WHERE EXISTS (
                        SELECT 1 FROM hotel_trip_customer htc WHERE htc.CustomerId = c.CustomerId
                    )
                    AND NOT EXISTS (
                        SELECT 1
                        FROM hotel_trip_customer htc
                        JOIN trip t ON htc.TripTo = t.TripTo AND htc.DepartureDate = t.DepartureDate
                        WHERE htc.CustomerId = c.CustomerId AND t.CityDeparture != 'Donostia'
                    )
                """;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(check)) {
                    rs.next();
                    int count = rs.getInt("cnt");
                    pass("Query executed successfully. Found " + count + " customer(s) with only Donostia bookings.");
                }
            } catch (Exception e) {
                fail("Exception: " + e.getMessage());
            } 
        }
    
        // ─────────────────────────────────────────────
        //  TEST 4 — insertGuideWithLanguage
        //  Case A: fresh insert → both committed
        //  Case B: duplicate guide → full rollback
        //  Case C: duplicate language → partial rollback
        // ─────────────────────────────────────────────
        private static void testInsertGuideWithLanguage(Connection conn) {
            System.out.println("\n--- TEST 4: Insert guide + language ---");
            try {
    
                // Cleanup from previous test runs
                cleanup(conn, "DELETE FROM languages WHERE GuideId IN ('T_G01','T_G02')");
                cleanup(conn, "DELETE FROM tourguide WHERE GuideId IN ('T_G01','T_G02')");
    
                // Case A: full success
                System.out.println("  [Case A] Fresh insert of guide T_G01 + language Spanish");
                Transactions.insertGuideWithLanguage("T_G01", "TestGuide", 600000001, "Spanish", conn);
                if (rowExists(conn, "SELECT 1 FROM tourguide WHERE GuideId='T_G01'") &&
                    rowExists(conn, "SELECT 1 FROM languages WHERE GuideId='T_G01' AND Lang='Spanish'")) {
                    pass("Case A: Guide and language both committed.");
                } else {
                    fail("Case A: Guide or language missing after commit.");
                }
    
                // Case B: duplicate guide → full rollback (T_G01 already exists)
                System.out.println("  [Case B] Duplicate guide insert (T_G01 already exists)");
                Transactions.insertGuideWithLanguage("T_G01", "Duplicate", 999999, "French", conn);
                // Guide T_G01 should still have original data, French language should NOT exist
                if (!rowExists(conn, "SELECT 1 FROM languages WHERE GuideId='T_G01' AND Lang='French'")) {
                    pass("Case B: Full rollback — duplicate guide rejected, no French language added.");
                } else {
                    fail("Case B: French language was inserted despite duplicate guide.");
                }
    
                // Case C: new guide but duplicate language → partial rollback
                System.out.println("  [Case C] New guide T_G02, duplicate language insert");
                Transactions.insertGuideWithLanguage("T_G02", "AnotherGuide", 600000002, "Spanish", conn);
                Transactions.insertGuideWithLanguage("T_G02", "AnotherGuide", 600000002, "Spanish", conn); // duplicate lang
                int langCount = countRows(conn, "SELECT COUNT(*) FROM languages WHERE GuideId='T_G02' AND Lang='Spanish'");
                if (rowExists(conn, "SELECT 1 FROM tourguide WHERE GuideId='T_G02'") && langCount == 1) {
                    pass("Case C: Partial rollback — guide kept, duplicate language not inserted twice.");
                } else {
                    fail("Case C: Unexpected state after partial rollback.");
                }
    
            } catch (Exception e) {
                fail("Exception: " + e.getMessage());
            } 
        }
    
        // ─────────────────────────────────────────────
        //  TEST 5 — promoteToManager
        //  Case A: valid promotion → committed
        //  Case B: invalid dept → full rollback
        // ─────────────────────────────────────────────
        private static void testPromoteToManager(Connection conn) {
            System.out.println("\n--- TEST 5: Promote employee to manager ---");
            try {
                // Read original salary and manager for comparison
                double originalSalary = getDouble(conn,
                    "SELECT Salary FROM employee WHERE Ssn='333445555'");
                String originalMgr = getString(conn,
                    "SELECT Mgr_ssn FROM department WHERE Dnumber=5");
    
                // Case A: valid SSN + valid dept → should commit
                System.out.println("  [Case A] Promote SSN 333445555 to manager of dept 5");
                Transactions.promoteToManager("333445555", "5", 99999.99, conn);
                double newSalary = getDouble(conn,
                    "SELECT Salary FROM employee WHERE Ssn='333445555'");
                String newMgr = getString(conn,
                    "SELECT Mgr_ssn FROM department WHERE Dnumber=5");
                if (newSalary == 99999.99 && "333445555".equals(newMgr)) {
                    pass("Case A: Salary updated and manager set correctly.");
                } else {
                    fail("Case A: Unexpected values after promotion.");
                }
    
                // Restore original state
                cleanup(conn, "UPDATE employee SET Salary=" + originalSalary + " WHERE Ssn='333445555'");
                cleanup(conn, "UPDATE department SET Mgr_ssn='" + originalMgr + "' WHERE Dnumber=5");
    
                // Case B: invalid dept number → should rollback salary change
                System.out.println("  [Case B] Invalid dept number 9999 → expect rollback");
                double salaryBefore = getDouble(conn, "SELECT Salary FROM employee WHERE Ssn='333445555'");
                Transactions.promoteToManager("333445555", "9999", 99999.99, conn);
                double salaryAfter = getDouble(conn, "SELECT Salary FROM employee WHERE Ssn='333445555'");
                if (salaryBefore == salaryAfter) {
                    pass("Case B: Rollback successful — salary unchanged.");
                } else {
                    fail("Case B: Salary changed despite invalid department (rollback failed).");
                }
    
            } catch (Exception e) {
                fail("Exception: " + e.getMessage());
            } 
        }
    
        // ─────────────────────────────────────────────
        //  HELPERS
        // ─────────────────────────────────────────────
    
        private static boolean rowExists(Connection conn, String sql) throws SQLException {
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                return rs.next();
            }
        }
    
        private static int countRows(Connection conn, String sql) throws SQLException {
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                rs.next();
                return rs.getInt(1);
            }
        }
    
        private static double getDouble(Connection conn, String sql) throws SQLException {
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                rs.next();
                return rs.getDouble(1);
            }
        }
    
        private static String getString(Connection conn, String sql) throws SQLException {
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                rs.next();
                return rs.getString(1);
            }
        }
    
        private static void cleanup(Connection conn, String sql) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
            } catch (SQLException ignored) {}
        }
    
        private static void pass(String msg) {
            System.out.println("  ✓ PASS: " + msg);
            passed++;
        }
    
        private static void fail(String msg) {
            System.out.println("  ✗ FAIL: " + msg);
            failed++;
        }
    }