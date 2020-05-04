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
        DB_Utility.printCurrentTime();
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Guest Login");
        System.out.println("2. Guest Sign Up");
        System.out.println("3. Update Personal Details");
        System.out.println("4. Back to previous options");
        System.out.println("5. quit the system");
        System.out.println();
        System.out.println("(Now in: Guest Mode)");
        System.out.print("Please type in the corresponding number to choose your option: ");
        String input = scanner.nextLine().trim().toLowerCase();
        while (true) {
            switch (input) {
                case "1":
                case "guest login":
                    return 3;
                case "2":
                case "guest sign up":
                    return 4;
                case "3":
                case "update personal details":
                    return 5;
                case "4":
                case "back to the previous options":
                    return 6;
                case "5":
                case "quit the system":
                    return -1;
                default:
                    System.out.println();
                    System.out.println("===================================================================================");
                    System.out.println("(Now in: Guest Mode)");
                    System.out.print("Please type in a valid number(1 for Login, 2 for Register, 3 for update, 4 for back, 5 for quit): ");
                    input = scanner.nextLine().trim().toLowerCase();
                    break;
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
            connection = DB_Utility.connect();

            String sql = "SELECT userID FROM Guest WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = true;
            }
            System.out.println("---------------------------");
            System.out.println(result ? "Welcome back! " + username : "Login failed.");
            System.out.println("---------------------------");
        } catch (Exception e) {
            e.printStackTrace();
//            if (e.getMessage() != null) {
//                System.err.println(e.getMessage());
//            }
            System.exit(-1);
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return 1;
    }

    private static Map<String, String> verify() {
        DB_Utility.printCurrentTime();
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
            } else {
                System.out.println("Your username or password is empty.");
            }
        }
        personalInfo.put("username", username);
        personalInfo.put("password", password);
        return personalInfo;
    }

    public static byte register() {

        String[] registerInfo = getInfo();

        if (registerInfo == null) return 1;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DB_Utility.connect();

            String sql = "INSERT INTO Guest (username,password,realName,passportID,telephoneNumber,email,operationTime) VALUES (?,?,?,?,?,?,NOW())";
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < registerInfo.length; i++) {
                if (registerInfo[i] == null && i == registerInfo.length - 1) {
                    preparedStatement.setObject(i + 1, null);
                    break;
                } else if (i == registerInfo.length - 2) {
                    preparedStatement.setBigDecimal(i + 1, BigDecimal.valueOf(Long.parseLong(registerInfo[i])));
                }
                preparedStatement.setString(i + 1, registerInfo[i]);
            }
            preparedStatement.executeUpdate();

            System.out.println();
            System.out.println("New guest \"" + registerInfo[0] + "\" registered successfully! Welcome to the family of Sunny Isle.");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
//            if (e.getMessage() != null) {
//                System.err.println(e.getMessage());
//            }
            System.exit(-1);
        } finally {
            DB_Utility.close(connection, preparedStatement);
        }
        return 1;
    }

    private static String[] getInfo() {
        DB_Utility.printCurrentTime();
        Scanner scanner = new Scanner(System.in);
        String[] personalInfo = new String[6];
        boolean check = true;
        String information = "", input = "";
        String repeated = "Please type in ";
        System.out.println("This is the Guest Sign Up Mode.");
        System.out.println("Warning: You cannot use [Blank Character] or [Tab] as your username and password at the front and behind.");
        System.out.println(repeated + "your personal information: ");
        System.out.println("1.Username, 2.Password, 3. Your Real Name, 4. Your passport ID, 5. Your Phone Number, 6. Email (Optional)");
        System.out.println("Type in \"Return\" to cancel the Sign up process.");
        System.out.println("You cannot use \"Return\" as your information.");

        for (int i = 0; i < personalInfo.length; i++) {
            switch (i) {
                case 0:
                    information = "the username: ";
                    break;
                case 1:
                    information = "the password: ";
                    break;
                case 2:
                    information = "your Real Name: ";//Checking is still developing
                    break;
                case 3:
                    information = "your passport ID: ";//Checking is still developing
                    break;
                case 4:
                    information = "your Phone Number: ";//Checking is still developing
                    break;
                case 5:
                    information = "your Email (Optional, click [Enter] to skip it): ";//Checking is still developing
                    break;
            }
            System.out.print(repeated + information);
            input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("Return")) {
                System.out.println();
                return null;
            }
            while (input.isEmpty()) {
                if (i == personalInfo.length - 1) {
                    personalInfo[i] = null;
                    return personalInfo;
                }
                System.out.println("===========================================================");
                System.out.print(repeated + "a valid information of " + information);
                input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("Return")) {
                    System.out.println();
                    return null;
                }
            }
            while (i == 0 && check) {
                check = checkUsername(input);
                if (!check) {
                    break;
                }
                System.out.println("===========================================================");
                System.out.print("This username has already exist, please choose another one: ");
                input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("Return")) {
                    System.out.println();
                    return null;
                }
                check = checkUsername(input);
            }
            while (i == 3 && input.length() > 9) {
                System.out.println("===========================================================================");
                System.out.println("The standard passport ID length is not longer than 9 with digits and chars.");
                System.out.print("Please input an valid Passport ID: ");
                input = scanner.nextLine().trim();
            }
            personalInfo[i] = input;
        }
        return personalInfo;
    }

    public static byte update() {
        Map<String, String> loginVerification = verify();
        if (loginVerification == null) {
            return 1;
        }
        String username = loginVerification.get("username");
        String password = loginVerification.get("password");
        Scanner scanner = new Scanner(System.in);
        String changeInfo = "", input = "", sql = "";
        boolean result;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DB_Utility.connect();

//            System.out.println("You can use front digit or type in the string of that type to choose it.");
            sql = "SELECT userID,username,password,realName AS 'Real Name',passportID AS 'Passport ID',telephoneNumber AS 'Phone Number',email FROM Guest WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = true;
                System.out.println();
                System.out.println("-------------------------------------------------------------------");
                System.out.println("Verification succeed! You can change your personal information now.");
                System.out.println("-------------------------------------------------------------------");
                System.out.println();
            } else {
                System.out.println();
                System.out.println("=====================================================================");
                System.out.println("Verification failed. Please type in the correct username or password.");
                System.out.println("Programme returned to the previous page.");
                System.out.println("=====================================================================");
                return 1;
            }

            Map<String, String> attributes = new HashMap<>();
            attributes.put("1", "Username");
            attributes.put("2", "Password");
            attributes.put("3", "Real Name");
            attributes.put("4", "Passport ID");
            attributes.put("5", "Phone Number");
            attributes.put("6", "Email");

            loopFlag:
            while (result) {
                System.out.println("Your latest information listed as below: ");
                System.out.println("-----------------------------------------------------");
                resultSet.first();
                for (int i = 2; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.println("Your " + resultSet.getMetaData().getColumnLabel(i) + ": " + resultSet.getString(i));
                }
                System.out.println("-----------------------------------------------------");

                int userID = resultSet.getInt("userID");

                connection.setAutoCommit(false);        //Start transaction
                System.out.println("1. " + attributes.get("1"));
                System.out.println("2. " + attributes.get("2"));
                System.out.println("3. " + attributes.get("3"));
                System.out.println("4. " + attributes.get("4"));
                System.out.println("5. " + attributes.get("5"));
                System.out.println("6. " + attributes.get("6"));
                System.out.println("7. Cancel All Updates and Return");
                System.out.println("8. Save and Return to previous page");
                System.out.println("9. quit the system (Not record your changes)");
                System.out.println();
                System.out.println("(Now in: Guest Information Alteration Mode)");
                System.out.print("Please type in your choice: ");
                changeInfo = scanner.nextLine().trim().toLowerCase();
                switch (changeInfo) {
                    case "1":
                    case "username":
                        sql = "UPDATE Guest SET username = ? WHERE userID = ?";
                        System.out.print("Please type in your new Username: ");
                        break;
                    case "2":
                    case "password":
                        sql = "UPDATE Guest SET password = ? WHERE userID = ?";
                        System.out.print("Please type in your new Password: ");
                        break;
                    case "3":
                    case "real name"://Checking is still developing
                        sql = "UPDATE Guest SET realName = ? WHERE userID = ?";
                        System.out.print("Please type in your new Real Name: ");
                        break;
                    case "4":
                    case "passport id"://Checking is still developing
                        sql = "UPDATE Guest SET passportID = ? WHERE userID = ?";
                        System.out.print("Please type in your new Passport ID: ");
                        break;
                    case "5":
                    case "phone number"://Checking is still developing
                        sql = "UPDATE Guest SET telephoneNumber = ? WHERE userID = ?";
                        System.out.print("Please type in your new Phone Number: ");
                        break;
                    case "6":
                    case "email"://Checking is still developing
                        sql = "UPDATE Guest SET email = ? WHERE userID = ?";
                        System.out.print("Please type in your new Email: ");
                        break;
                    case "7":
                    case "cancel all updates and return":
                        connection.rollback();
                        System.out.println("All information has been reset to initial contents!");
                        return 1;
                    case "8":
                    case "save and return to previous page":
                        connection.commit();
                        DB_Utility.close(connection, preparedStatement, resultSet);
                        return 1;
                    case "9":
                    case "quit the system":
                        connection.rollback();
                        DB_Utility.close(connection, preparedStatement, resultSet);
                        return -1;
                    default:
                        System.out.println();
                        System.out.println("===========================================================================");
                        System.out.println("Please type in the corresponding number or full text to choose your option.");
                        System.out.println("===========================================================================");
                        System.out.println();
                        continue loopFlag;
                }
                input = scanner.nextLine().trim();
                while (input.equals("") && !(changeInfo.equals("6") || changeInfo.equals("email"))) {
                    System.out.println("This information cannot be empty!");
                    if (changeInfo.length() != 1) {
                        System.out.print("Please type in the valid content of " + changeInfo + ": ");
                    } else {
                        System.out.print("Please type in the valid content of " + attributes.get(changeInfo) + ": ");
                    }
                    input = scanner.nextLine().trim();
                }
                preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                preparedStatement.setString(1, input);
                preparedStatement.setInt(2, userID);
                preparedStatement.executeUpdate();

                System.out.println("-------------------");
                System.out.println("Alteration succeed!");
                System.out.println("-------------------");
                sql = "SELECT userID,username,password,realName AS 'Real Name',passportID AS 'Passport ID',telephoneNumber AS 'Phone Number',email FROM Guest WHERE userID = ?";
                preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                preparedStatement.setInt(1, userID);
                resultSet = preparedStatement.executeQuery();
            }
            System.out.println();
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (Exception throwable) {
                    throwable.printStackTrace();
                    System.exit(-1);
                }
            }
            e.printStackTrace();
            System.exit(-1);
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return 1;
    }

    private static boolean checkUsername(String input) {
        String username = input;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean result = false;

        try {
            connection = DB_Utility.connect();

            String sql = "SELECT userID FROM Guest WHERE username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return result;
    }
}
