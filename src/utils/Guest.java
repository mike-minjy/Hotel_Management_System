package utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Guest {

    /**
     * Prevent create an object of this class.
     */
    private Guest() {
    }

    /**
     * The second control panel of entire program. (Guest Path)
     *
     * @return byte key
     */
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
                    System.out.println("=================================================================================================");
                    System.out.println("(Now in: Guest Mode)");
                    System.out.print("Please type in a valid number(1 for Login, 2 for Register, 3 for update, 4 for back, 5 for quit): ");
                    input = scanner.nextLine().trim().toLowerCase();
                    break;
            }
        }
    }

    /**
     * The Login implementation method <code>login()</code><br>
     * It obtains two parameter from <code>verify()</code> method
     * to check whether "username" and "password" are existing in guest table.
     *
     * @return int array: byte key, guestID
     */
    public static int[] login() {
        int[] ints = {10, 0};
        Map<String, String> userLoginInfo = verify();
        if (userLoginInfo == null) {//If user input equals to "Return"
            ints[0] = 1;
            return ints;
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
                ints[1] = resultSet.getInt("userID");
            } else {
                ints[0] = 1;
            }
            System.out.println();
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(result ? "Welcome back! " + username : "Login failed. Please register at first or type in correct username and password.");
            System.out.println("--------------------------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
//            if (e.getMessage() != null) {
//                System.err.println(e.getMessage());
//            }
            System.exit(-1);
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return ints;
    }

    /**
     * The specific implementation of <code>verify()</code> method.<br>
     * It utilise <code>HashMap</code> to handle two inputs:
     * "username" and "password"<br>
     * If user input "Return" (which is case-insensitive), it will return to previous page.
     * And the empty input is not acceptable, it will give user an "Exception" message.
     *
     * @return Map (contains user login information)
     */
    protected static Map<String, String> verify() {
        DB_Utility.printCurrentTime();
        Scanner scanner = new Scanner(System.in);
        String username = "", password = "";
        HashMap<String, String> personalInfo = new HashMap<>();
        while (true) {
            if (username.trim().isEmpty()) {
                System.out.print("Please type in your username (type \"Return\" to cancel input): ");
                username = scanner.nextLine();
                if (username.trim().equalsIgnoreCase("Return")) {
                    return null;
                }
            }
            if (password.trim().isEmpty()) {
                System.out.print("Please type in your password (type \"Return\" to cancel input): ");
                password = scanner.nextLine();
                if (password.trim().equalsIgnoreCase("Return")) {
                    return null;
                }
            }
            if (!username.trim().isEmpty() && !password.trim().isEmpty()) {
                break;
            } else {
                System.out.println("===================================");
                System.out.println("Your username or password is empty.");
                System.out.println("===================================");
            }
        }
        personalInfo.put("username", username);
        personalInfo.put("password", password);
        return personalInfo;
    }

    /**
     * The Sign Up implementation method <code>register()</code><br>
     * It is capable to get valid information from user via <code>getInfo()</code> method.<br>
     * Finally, this method will load data into <code>guest</code> table.
     *
     * @return byte key
     */
    public static byte register() {

        String[] registerInfo = getInfo();

        if (registerInfo == null) {//If "Return" input get here
            return 1;
        }

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

    /**
     * The detail implementation of how to get information from user.<br>
     * It will require user to input information in sequence:<br>
     * <pre>
     *     1. Username
     *     2. Password
     *     3. Real Name
     *     4. Passport ID
     *     5. Phone Number
     *     6. Email (Optional)
     * </pre>
     * The user inputs will be checked by regular expression.<br>
     * User could type in "Return" (case-insensitive) to cancel sign up progress.
     *
     * @return String array (contains formatted user information)
     */
    private static String[] getInfo() {
        DB_Utility.printCurrentTime();
        Scanner scanner = new Scanner(System.in);
        String[] personalInfo = new String[6];
        String information = "", input = "";
        String repeated = "Please type in ";
        System.out.println("This is the Guest Sign Up Mode.");
        System.out.println("Warning: You cannot use [Blank Character] or [Tab] as your username and password at the front and behind.");
        System.out.println(repeated + "your personal information: ");
        System.out.println("1.Username, 2.Password, 3. Your Real Name, 4. Your passport ID, 5. Your Phone Number, 6. Email (Optional)");
        System.out.println("Type in \"Return\" to cancel the Sign up process.");
        System.out.println("You cannot only use an entire string \"Return\" as your information.");

        for (int i = 0; i < personalInfo.length; i++) {
            switch (i) {
                case 0:
                    information = "the username: ";
                    break;
                case 1:
                    information = "the password: ";
                    break;
                case 2:
                    information = "your Real Name: ";//Regular expression verification
                    break;
                case 3:
                    information = "your Passport ID: ";//Regular expression verification
                    break;
                case 4:
                    information = "your Phone Number: ";//Regular expression verification
                    break;
                case 5:
                    information = "your Email (Optional, click [Enter] to skip it): ";//Regular expression verification
                    break;
            }
            System.out.print(repeated + information);
            input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("Return")) {
                return null;
            }
            if (i == 3) {
                input = input.toUpperCase();//Automatically change characters to uppercase
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
                    return null;
                }
            }
            while (i == 0) {//Username further checking
                boolean check = checkUsername(input);
                if (!check) {
                    break;
                }
                System.out.println("===========================================================");
                System.out.print("This username has already exist, please choose another one: ");
                input = verifyEmpty(scanner, information, repeated);
                if (input == null) return null;
            }
            while (i == 2) {//real name further checking
                Pattern pattern = Pattern.compile("^[[A-Za-z]|\\s]+$");
                if (pattern.matcher(input).matches()) {
                    break;
                } else {
                    System.out.println("============================================================");
                    System.out.print("Invalid input. Name could only includes character and space: ");
                    input = verifyEmpty(scanner, information, repeated);
                    if (input == null) return null;
                }
            }
            while (i == 3) {//passport ID further checking
                Pattern pattern = Pattern.compile("^[A-Z0-9]{1,9}$");
                if (pattern.matcher(input).matches()) {
                    break;
                } else {
                    System.out.println("=============================================================================================================");
                    System.out.print("Invalid input. The standard passport ID length is not longer than 9 bits including digits or UPPERCASE chars: ");
                    input = verifyEmpty(scanner, information, repeated);
                    if (input == null) return null;
                    input = input.toUpperCase();//Automatically change to uppercase without user specify it.
                }
            }
            while (i == 4) {//phone number further checking
                Pattern pattern = Pattern.compile("^[0-9]{1,18}$");
                if (pattern.matcher(input).matches()) {
                    break;
                } else {
                    System.out.println("=======================================================");
                    System.out.print("Invalid input. Phone Number could only includes digits: ");
                    input = verifyEmpty(scanner, information, repeated);
                    if (input == null) return null;
                }
            }
            while (i == 5) {//email further checking
                Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
                if (pattern.matcher(input).matches() || input.isEmpty()) {
                    break;
                } else {
                    System.out.println("==============================================");
                    System.out.print("Invalid input. Your Email format is incorrect: ");
                    input = verifyEmpty(scanner, information, repeated);
                    if (input == null) return null;
                }
            }
            personalInfo[i] = input;
        }
        return personalInfo;
    }

    /**
     * User input "Return" (case-insensitive) means cancel input and back to previous options.<br>
     * It could give an accurate "Exception" message to user.
     *
     * @param scanner
     * @param information
     * @param repeated
     * @return String (Not empty)
     */
    protected static String verifyEmpty(Scanner scanner, String information, String repeated) {
        String input;
        input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.println("==================================================================");
            System.out.print(repeated + "a valid information of " + information);
            input = scanner.nextLine().trim();
            if (input.isEmpty() && information.contains("Email")) {
                return input;
            }
            if (input.equalsIgnoreCase("Return")) {
                return null;
            }
        }
        if (input.equalsIgnoreCase("Return")) {
            return null;
        }
        return input;
    }

    /**
     * The user should type in their "username" and "password" ar first.<br>
     * It is more reasonable to be accessible after user login his/her account.<br>
     * Thus, I have created another <code>update(int userID)</code> method
     * to implement the information changes after user login their account.<br>
     * It is also used regular expression to verify the information.
     *
     * @return byte key
     */
    public static byte update() {
        Map<String, String> loginVerification = verify();
        if (loginVerification == null) {
            return 1;
        }
        String username = loginVerification.get("username");
        String password = loginVerification.get("password");
        Scanner scanner = new Scanner(System.in);
        String repeated = "Please type in ";
        byte normalReturn = 1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DB_Utility.connect();

//            System.out.println("You can use front digit or type in the string of that information you want to modify.");
            String sql = "SELECT userID,username,password,realName AS 'Real Name',passportID AS 'Passport ID',telephoneNumber AS 'Phone Number',email FROM Guest WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println();
                System.out.println("-------------------------------------------------------------------");
                System.out.println("Verification succeed! You can change your personal information now.");
                System.out.println("-------------------------------------------------------------------");
                System.out.println();
            } else {
                System.out.println();
                System.out.println("=====================================================================");
                System.out.println("Verification failed. Please type in the correct username or password.");
                System.out.println("(Notice: Program returned to the previous page.)");
                System.out.println("=====================================================================");
                return 1;
            }

            normalReturn = sharedUpdate(repeated, scanner, connection, preparedStatement, resultSet);
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
        return normalReturn;
    }

    /**
     * It would be "shared" with another <code>update(int userID)</code> method in
     * <code>Booking</code> class.<br>
     * It supports transaction and regular expression verification.<br>
     * User could withdraw their changes via transaction.
     *
     * @param repeated "Please type in "
     * @param scanner
     * @param connection
     * @param preparedStatement
     * @param resultSet
     * @return byte key
     * @throws Exception
     */
    protected static byte sharedUpdate(String repeated, Scanner scanner, Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws Exception {
        String sql, changeInfo, input;
        Map<String, String> attributes = new HashMap<>();
        attributes.put("1", "Username");
        attributes.put("2", "Password");
        attributes.put("3", "Real Name");
        attributes.put("4", "Passport ID");
        attributes.put("5", "Phone Number");
        attributes.put("6", "Email");
        connection.setAutoCommit(false);//Start transaction

        loopFlag:
        while (true) {
            System.out.println("Your latest information listed as below: ");
            System.out.println("-----------------------------------------------------");
            resultSet.first();
            for (int i = 2; i <= resultSet.getMetaData().getColumnCount(); i++) {
                System.out.println("Your " + resultSet.getMetaData().getColumnLabel(i) + ": " + resultSet.getString(i));
            }
            System.out.println("-----------------------------------------------------");

            int userID = resultSet.getInt("userID");

            for (int i = 1; i <= attributes.size(); i++) {
                System.out.println((i) + ". " + attributes.get(String.valueOf(i)));
            }
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
                    System.out.print(repeated + "your new Username: ");
                    break;
                case "2":
                case "password":
                    sql = "UPDATE Guest SET password = ? WHERE userID = ?";
                    System.out.print(repeated + "your new Password: ");
                    break;
                case "3":
                case "real name":
                    sql = "UPDATE Guest SET realName = ? WHERE userID = ?";
                    System.out.print(repeated + "your new Real Name: ");
                    break;
                case "4":
                case "passport id":
                    sql = "UPDATE Guest SET passportID = ? WHERE userID = ?";
                    System.out.print(repeated + "your new Passport ID: ");
                    break;
                case "5":
                case "phone number":
                    sql = "UPDATE Guest SET telephoneNumber = ? WHERE userID = ?";
                    System.out.print(repeated + "your new Phone Number: ");
                    break;
                case "6":
                case "email":
                    sql = "UPDATE Guest SET email = ? WHERE userID = ?";
                    System.out.print(repeated + "your new Email: ");
                    break;
                case "7":
                case "cancel all updates and return":
                    connection.rollback();
                    System.out.println("All information has been reset to initial contents!");
                    return 1;
                case "8":
                case "save and return to previous page":
                    connection.commit();
                    System.out.println("All changed information has been saved!");
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
            //Further checking same as register, still need improvement
            while (input.equals("") && !(changeInfo.equals("6") || changeInfo.equals("email"))) {
                System.out.println("This information cannot be empty!");
                if (changeInfo.length() != 1) {
                    System.out.print(repeated + "the valid content of " + changeInfo + ": ");
                } else {
                    System.out.print(repeated + "the valid content of " + attributes.get(changeInfo) + ": ");
                }
                input = scanner.nextLine().trim();
            }
            if (changeInfo.equals("4") || changeInfo.equals("passport id")) {
                input = input.toUpperCase();//Automatically change characters to uppercase
            }
            while (changeInfo.equals("1") || changeInfo.equals("username")) {//Username further checking
                boolean check = checkUsername(input);
                if (!check) {
                    break;
                }
                System.out.println("===========================================================");
                System.out.print("This username has already exist, please choose another one: ");
                input = verifyEmpty(scanner, "your new Username: ", repeated);
                while (input == null) {
                    System.out.println("=========================");
                    System.out.println("You cannot return at here");
                    input = verifyEmpty(scanner, "your new Username: ", repeated);
                }
            }
            while (changeInfo.equals("3") || changeInfo.equals("real name")) {//real name further checking
                Pattern pattern = Pattern.compile("^[[A-Za-z]|\\s]+$");
                if (pattern.matcher(input).matches()) {
                    break;
                } else {
                    System.out.println("=============================================================");
                    System.out.print("Invalid input. Name could only includes characters and space: ");
                    input = verifyEmpty(scanner, "your Real Name: ", repeated);
                    while (input == null) {
                        System.out.println("===============================================================================================");
                        System.out.print("You cannot return at here, you can update your information here and cancel it in previous page: ");
                        input = verifyEmpty(scanner, "your Real Name: ", repeated);
                    }
                }
            }
            while ((changeInfo.equals("4") || changeInfo.equals("passport id"))) {//passport ID further checking
                Pattern pattern = Pattern.compile("^[A-Z0-9]{1,9}$");
                if (pattern.matcher(input).matches()) {
                    break;
                } else {
                    System.out.println("=============================================================================================================");
                    System.out.print("Invalid input. The standard passport ID length is not longer than 9 bits including digits or UPPERCASE chars: ");
                    input = verifyEmpty(scanner, "your Passport ID: ", repeated);
                    while (input == null) {
                        System.out.println("===============================================================================================");
                        System.out.print("You cannot return at here, you can update your information here and cancel it in previous page: ");
                        input = verifyEmpty(scanner, "your Passport ID: ", repeated);
                    }
                    input = input.toUpperCase();
                }
            }
            while (changeInfo.equals("5") || changeInfo.equals("phone number")) {//phone number further checking
                Pattern pattern = Pattern.compile("^[0-9]{1,18}$");
                if (pattern.matcher(input).matches()) {
                    break;
                } else {
                    System.out.println("=======================================================");
                    System.out.print("Invalid input. Phone Number could only includes digits: ");
                    input = verifyEmpty(scanner, "your Phone Number: ", repeated);
                    while (input == null) {
                        System.out.println("===============================================================================================");
                        System.out.print("You cannot return at here, you can update your information here and cancel it in previous page: ");
                        input = verifyEmpty(scanner, "your Phone Number: ", repeated);
                    }
                }
            }
            while (changeInfo.equals("6") || changeInfo.equals("email")) {//email further checking
                Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
                if (pattern.matcher(input).matches() || input.isEmpty()) {
                    break;
                } else {
                    System.out.println("==============================================");
                    System.out.print("Invalid input. Your Email format is incorrect: ");
                    input = verifyEmpty(scanner, "your Email: ", repeated);
                    while (input == null) {
                        System.out.println("===============================================================================================");
                        System.out.print("You cannot return at here, you can update your information here and cancel it in previous page: ");
                        input = verifyEmpty(scanner, "your Email: ", repeated);
                    }
                }
            }
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, input);
            preparedStatement.setInt(2, userID);
            preparedStatement.executeUpdate();

            System.out.println();
            System.out.println("-------------------");
            System.out.println("Alteration succeed!");
            System.out.println("-------------------");
            System.out.println();

            sql = "SELECT userID,username,password,realName AS 'Real Name',passportID AS 'Passport ID',telephoneNumber AS 'Phone Number',email FROM Guest WHERE userID = ?";
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, userID);
            resultSet = preparedStatement.executeQuery();
        }
    }

    /**
     * The Username of different user cannot be the same one.<br>
     * Thus, this method created for repeated Username checking.
     *
     * @param input
     * @return boolean (Username exist or not)
     */
    private static boolean checkUsername(String input) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean result = false;

        try {
            connection = DB_Utility.connect();

            String sql = "SELECT userID FROM Guest WHERE username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, input);
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
