package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Scanner;

public class Staff {

    private Staff() {
    }

    public static byte startInterface() {
        DB_Utility.printCurrentTime();
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Staff Login");
        System.out.println("2. Back to previous options");
        System.out.println("3. quit the system");
        System.out.println();
        System.out.println("(Now in: Staff Mode)");
        System.out.print("Please type in the corresponding number to choose your option: ");
        String input = scanner.nextLine().trim().toLowerCase();
        while (true) {
            switch (input) {
                case "1":
                case "staff login":
                    return 7;
                case "2":
                case "back to the previous options":
                    return 8;
                case "3":
                case "quit the system":
                    return -1;
                default:
                    System.out.println("===================================================================================");
                    System.out.println("(Now in: Staff Mode)");
                    System.out.print("Please type in a valid number(1 for Login, 2 for Register, 3 for back, 4 for quit): ");
                    input = scanner.nextLine().trim().toLowerCase();
                    break;
            }
        }
    }

    public static int[] login() throws Exception {
        int[] ints = {2, 0};
        Map<String, String> userLoginInfo = Guest.verify();
        if (userLoginInfo == null) {
            return ints;
        }
        String username = userLoginInfo.get("username");
        String password = userLoginInfo.get("password");

        boolean result = false;

        Connection connection = DB_Utility.connect();

        String sql = "SELECT staffID FROM Staff WHERE username = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            result = true;
            ints[1] = resultSet.getInt("staffID");
        }

        System.out.println("---------------------------------------------------------");
        System.out.println(result ? "Welcome back! " + username : "Login failed. Please input correct username and password.");
        System.out.println("---------------------------------------------------------");

        DB_Utility.close(connection, preparedStatement, resultSet);

        return ints;
    }

}
