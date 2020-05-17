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
        System.out.println("2. Change Password");
        System.out.println("3. Back to previous options");
        System.out.println("4. quit the system");
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
                case "change password":
                    return 8;
                case "3":
                case "back to the previous options":
                    return 9;
                case "4":
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
            ints[0] = 16;
            ints[1] = resultSet.getInt("staffID");
        }

        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println(result ? "Welcome back! " + username : "Login failed. Please input correct username and password.");
        System.out.println("---------------------------------------------------------");

        DB_Utility.close(connection, preparedStatement, resultSet);

        return ints;
    }

    public static byte changePassword() {
        Map<String, String> userLoginInfo = Guest.verify();
        if (userLoginInfo == null) {
            return 2;
        }
        int staffID = 0;
        String username = userLoginInfo.get("username");
        String password = userLoginInfo.get("password");

        boolean result = false;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DB_Utility.connect();

            String sql = "SELECT staffID FROM Staff WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = true;
                staffID = resultSet.getInt(1);//Staff ID
            }

            System.out.println();
            System.out.println("-----------------------------------------------------------------");
            System.out.println(result ? "Staff: " + username + ", Welcome back! You can change your password now." : "Login failed. Please input correct username and password.");
            System.out.println("-----------------------------------------------------------------");

            while (result) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("------------------------------");
                System.out.println("Your old password: " + password);
                System.out.println("------------------------------");
                System.out.print("Please type in your new password: ");
                String input = Guest.verifyEmpty(scanner, "your new password: ", "Please type in ");

                if (input == null) {
                    DB_Utility.close(connection, preparedStatement, resultSet);
                    return 2;
                }

                if (password.equalsIgnoreCase(input)) {
                    System.out.println();
                    System.out.println("============================================================================================");
                    System.out.println("Please type in a new password! You cannot set it same as before.");
                    System.out.println("Notice: The password is case-insensitive. 'P' and 'p' will be treated as the same character.");
                    System.out.println("Type \"Return\" back to the previous page.");
                    System.out.println("============================================================================================");
                    System.out.println();
                    continue;
                }

                sql = "UPDATE Staff SET password = ? WHERE staffID = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, input);
                preparedStatement.setInt(2, staffID);
                preparedStatement.executeUpdate();
                System.out.println("-------------------------------------------------------");
                System.out.println("Now your password has been changed to \"" + input + "\"");
                System.out.println("-------------------------------------------------------");

                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return 2;
    }

}
