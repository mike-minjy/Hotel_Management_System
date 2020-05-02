package utils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Guest {

    private Guest() {
    }

    public static byte startInterface() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Guest Login");
        System.out.println("2. Guest Sign Up");
        System.out.println("3. Update Personal Details");//Still developing
        System.out.println("3. Back to the previous options");
        System.out.println("4. quit the system");
        System.out.println();
        System.out.println("(Now in: Guest Mode)");
        System.out.print("Please type in the corresponding number to choose your option: ");
        String input = scanner.nextLine().trim();
        while (true) {
            if (input.equals("1")) {
                System.out.println();
                return 3;
            } else if (input.equals("2")) {
                System.out.println();
                return 4;
            } else if (input.equals("3")) {
                System.out.println();
                return 5;
            } else if (input.equals("4")) {
                System.out.println();
                System.out.println("Bye");
                return -1;
            } else {
                System.out.println();
                System.out.println("(Now in: Guest Mode)");
                System.out.print("Please type in a valid number(1 for Login, 2 for Register, 3 for back, 4 for quit): ");
                input = scanner.nextLine().trim();
            }
        }
    }

    public static byte login() {
        Map<String, String> userLoginInfo = verify();
        if (userLoginInfo == null) {
            return 1;
        }
        String username = userLoginInfo.get("username");
        String password = userLoginInfo.get("password");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean result = false;

        try {
            connection = DB_Connection.connect();

            String sql = "SELECT userID FROM Guest WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = true;
            }
            System.out.println("-------------");
            System.out.println(result ? "Login succeed!" : "Login failed.");
            System.out.println("-------------");
        } catch (Exception e) {
            e.printStackTrace();
//            if (e.getMessage() != null) {
//                System.err.println(e.getMessage());
//            }
            System.exit(-1);
        } finally {
            DB_Connection.close(connection, preparedStatement, resultSet);
        }
        return 1;
    }

    private static Map<String, String> verify() {
        Scanner scanner = new Scanner(System.in);
        String username = "", password = "";
        HashMap<String, String> personalInfo = new HashMap<>();
        while (true) {
            if (username.trim().isEmpty()) {
                System.out.print("Please type in your username (type \"Return\" to cancel input): ");
                username = scanner.nextLine();
                if (username.trim().equalsIgnoreCase("Return")) {
                    System.out.println();
                    return null;
                }
            }
            if (password.trim().isEmpty()) {
                System.out.print("Please type in your password (type \"Return\" to cancel input): ");
                password = scanner.nextLine();
                if (password.trim().equalsIgnoreCase("Return")) {
                    System.out.println();
                    return null;
                }
            }
            if (!username.trim().isEmpty() && !password.trim().isEmpty()) {
                break;
            }
        }
        personalInfo.put("username", username);
        personalInfo.put("password", password);
        return personalInfo;
    }

    public static byte register() {

        String[] registerInfo = getInfo();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DB_Connection.connect();

            String sql = "INSERT INTO Guest (username,password,realName,passportID,telephoneNumber,email) VALUES (?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < registerInfo.length; i++) {
                if (registerInfo[i] == null) {
                    preparedStatement.setObject(i + 1, null);
                    break;
                } else if (i == registerInfo.length - 2) {
                    preparedStatement.setBigDecimal(i + 1, BigDecimal.valueOf(Long.parseLong(registerInfo[i])));
                }
                preparedStatement.setString(i + 1, registerInfo[i]);
            }
            preparedStatement.executeUpdate();

            System.out.println("New guest \"" + registerInfo[0] + "\" registered successfully!");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
//            if (e.getMessage() != null) {
//                System.err.println(e.getMessage());
//            }
            System.exit(-1);
        } finally {
            DB_Connection.close(connection, preparedStatement, resultSet);
        }
        return 1;
    }

    private static String[] getInfo() {
        Scanner scanner = new Scanner(System.in);
        String[] personalInfo = new String[6];
        String information = "", input = "";
        String repeated = "Please type in ";
        System.out.println("This is the Guest Sign Up Mode. (Warning: You cannot use \"Blank Character\" or \"Tab\" to your username and password at front and behind.)");
        System.out.println(repeated + "your personal information 1.Username, 2.Password, 3. Your Real Name, 4. Your passport ID, 5. Your Phone Number, 6. Email (Optional)");

        for (int i = 0; i < personalInfo.length; i++) {
            switch (i) {
                case 0:
                    information = "the username: ";
                    break;
                case 1:
                    information = "the password: ";
                    break;
                case 2:
                    information = "your Real Name: ";
                    break;
                case 3:
                    information = "your passport ID: ";
                    break;
                case 4:
                    information = "your Phone Number: ";
                    break;
                case 5:
                    information = "your Email (Optional, click [Enter] to skip it): ";
                    break;
            }
            System.out.print(repeated + information);
            input = scanner.nextLine();
            while (input.trim().isEmpty()) {
                if (i == personalInfo.length - 1) {
                    personalInfo[i] = null;
                    return personalInfo;
                }
                System.out.print(repeated + "a valid information of " + information);
                input = scanner.nextLine();
            }
            personalInfo[i] = input;
        }
        return personalInfo;
    }
}
