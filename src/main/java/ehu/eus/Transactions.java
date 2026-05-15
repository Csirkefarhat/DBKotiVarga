package ehu.eus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.ResultSet;

public class Transactions {

    public static void insertGuideWithLanguage(String guideId, String name, int phone, String lang, Connection conn) {
        Savepoint afterGuide = null;
        try {
            conn.setAutoCommit(false);

            PreparedStatement ps1 = conn.prepareStatement(
                "INSERT INTO tourguide (GuideId, guidename, guidephone) VALUES (?, ?, ?)");
            ps1.setString(1, guideId);
            ps1.setString(2, name);
            ps1.setInt(3, phone);
            ps1.executeUpdate();
            ps1.close();

            // Savepoint after guide is safely inserted
            afterGuide = conn.setSavepoint("afterGuide");

            PreparedStatement ps2 = conn.prepareStatement(
                "INSERT INTO languages (GuideId, Lang) VALUES (?, ?)");
            ps2.setString(1, guideId);
            ps2.setString(2, lang);
            ps2.executeUpdate();
            ps2.close();

            conn.commit();
            System.out.println("Guide and language committed successfully.");

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            try {
                if (afterGuide == null) {
                    conn.rollback(); 
                    System.out.println("Full rollback — guide not inserted.");
                } else {
                    conn.rollback(afterGuide); 
                    conn.commit();
                    System.out.println("Partial rollback — guide kept, language skipped.");
                }
            } catch (SQLException ex) {
                System.out.println("Rollback failed: " + ex.getMessage());
            }finally {
                try { if (conn != null) conn.setAutoCommit(true); }
                catch (SQLException ex) { System.out.println("Error resetting auto-commit: " + ex.getMessage()); }
            }
        }
    }

    public static void promoteToManager(String ssn, String deptNumber, double newSalary, Connection conn) {
        try {
            conn.setAutoCommit(false);

            PreparedStatement ps1 = conn.prepareStatement(
                "UPDATE employee SET Salary = ? WHERE Ssn = ?");
            ps1.setDouble(1, newSalary);
            ps1.setString(2, ssn);
            int rows1 = ps1.executeUpdate();
            ps1.close();
            if (rows1 == 0) throw new SQLException("Employee not found: " + ssn);

            PreparedStatement ps2 = conn.prepareStatement(
                "UPDATE department SET Mgr_ssn = ?, Mgr_start_date = CURDATE() WHERE Dnumber = ?");
            ps2.setString(1, ssn);
            ps2.setInt(2, Integer.parseInt(deptNumber));
            int rows2 = ps2.executeUpdate();
            ps2.close();
            if (rows2 == 0) throw new SQLException("Department not found: " + deptNumber);

            conn.commit();
            System.out.println("Promotion committed: " + ssn + " is now manager of dept " + deptNumber);

        } catch (SQLException e) {
            System.out.println("Transaction failed: " + e.getMessage());
            try { if (conn != null) conn.rollback(); System.out.println("Rolled back."); }
            catch (SQLException ex) { System.out.println("Rollback error: " + ex.getMessage()); }
        }finally {
            try { if (conn != null) conn.setAutoCommit(true); }
            catch (SQLException ex) { System.out.println("Error resetting auto-commit: " + ex.getMessage()); }
        }
    }
}
