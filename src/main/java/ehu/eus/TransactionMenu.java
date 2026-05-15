package ehu.eus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class TransactionMenu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        
    Connection conn = MyDBConnection.getConnection();
    boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1 -> query1(conn);
                case 2 -> query2(conn);
                case 3 -> query3(conn);
                case 4 -> query4(conn);
                case 5 -> query5(conn);
                case 6 -> query6(conn);
                case 7 -> transaction1(conn);
                case 8 -> transaction2(conn);
                case 9 -> transaction3(conn);
                case 10 -> transaction4(conn);
                case 0 -> { running = false; System.out.println("Bye!"); }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          DATABASE PROJECT MENU                ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("║  QUERIES                                      ║");
        System.out.println("║  1. Employees above dept avg salary           ║");
        System.out.println("║  2. Restaurants with revenue above avg        ║");
        System.out.println("║  3. Customers who booked only Donostia trips  ║");                                 
        System.out.println("║  4. Employees above avg salary                ║");
        System.out.println("║  5. Cities with more than 2 restaurants       ║");
        System.out.println("║  6. Restaurants serving dishes above 20 euros ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("║  TRANSACTIONS                                 ║");
        System.out.println("║  7. Add tour guide + language (INSERT)        ║");
        System.out.println("║  8. Promote employee to manager (UPDATE)      ║");
        System.out.println("║  9. Add dish and restaurant serving (INSERT)  ║");
        System.out.println("║  10. Pay raise for IT employees by 10%(UPDATE)║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("║  0. Exit                                      ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
    }

    private static void query1(Connection conn) {
        try {
            System.out.println("\n=== Employees earning above their department's average salary ===");
            Queries.queryAboveAvgSalary(conn);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[ERROR] Query failed: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void query2(Connection conn) {
        try {
            System.out.println("\n=== Restaurants with revenue above average ===");
            Queries.queryRestaurantsAbvAvg(conn);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[ERROR] Query failed: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void query3(Connection conn) {
        try {
            System.out.println("\n=== Customers who have ONLY booked trips departing from Donostia ===");
            Queries.queryCustomersAllDonostiaTrips(conn);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[ERROR] Query failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void query4(Connection conn) {
            Queries.employeesAboveAverageSalary(conn);
    }

    private static void query5(Connection conn) {
        Queries.restaurantsByCity(conn);
    }

    private static void query6(Connection conn) {
        Queries.expensiveRestaurants(conn);
    }

    private static void transaction1(Connection conn) {
        Transactions.insertGuideWithLanguage(
            readString("Guide ID: "),
            readString("Guide name: "),
            readInt("Guide phone: "),
            readString("Guide language: "),
            conn
        );
    }


    private static void transaction2(Connection conn) {
        Transactions.promoteToManager(
            readString("Employee SSN: "),
            readString("Department number: "),
            readDouble("New salary: "),
            conn
        );
    }

    private static void transaction3(Connection conn) {
        Transactions.insertDishAndServe(
            readString("Dish name: "),
            readString("Cuisine: "),
            readString("Category: "),
            readString("Difficulty: "),
            readString("Restaurant name: "),
            readDouble("Price: "),
            conn
        );
    }

    private static void transaction4(Connection conn) {
        Transactions.increaseITSalaries(conn);
    }


    //helpers

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid number.");
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}