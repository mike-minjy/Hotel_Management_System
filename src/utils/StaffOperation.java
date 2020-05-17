package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Pattern;

public class StaffOperation {
    private StaffOperation() {
    }

    public static int[] startInterface(int staffID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int[] staffInfo = new int[2];
        String sql, staffName;
        try {
            connection = DB_Utility.connect();
            sql = "SELECT username FROM Staff WHERE staffID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, staffID);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            staffName = resultSet.getString("username");
            staffInfo = startInterface(staffID, staffName);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return staffInfo;
    }

    private static int[] startInterface(int staffID, String username) {
        DB_Utility.printCurrentTime();
        int[] staffInfo = {2, staffID};
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Check Rooms");
        System.out.println("2. Change Password");
        System.out.println("3. Add to Black List (Might be developed later)");
        System.out.println("4. Log out");
        System.out.println("5. quit the system");
        System.out.println();
        System.out.println("Staff: " + username);
        System.out.print("Please type in the corresponding number to choose your option: ");
        String input = scanner.nextLine().trim().toLowerCase();
        while (true) {
            switch (input) {
                case "1":
                case "check rooms":
                    staffInfo[0] = 17;
                    return staffInfo;
                case "2":
                case "change password":
                    staffInfo[0] = 18;
                    return staffInfo;
//                case "3":
//                case "add to black list":
//                    staffInfo[0] = 19;
//                    return staffInfo;
                case "4":
                case "log out":
                    return staffInfo;
                case "5":
                case "quit the system":
                    staffInfo[0] = -1;
                    return staffInfo;
                default:
                    System.out.println();
                    System.out.println("=====================================================");
                    System.out.println("Staff: " + username);
                    System.out.print("Please type in a valid number or full text of option: ");
                    input = scanner.nextLine().trim().toLowerCase();
                    break;
            }
        }
    }

    public static int[] roomManagement(int staffID) {
        DB_Utility.printCurrentTime();
        int[] staffInfo = {16, staffID};
        Scanner scanner = new Scanner(System.in);//I might add room type query later
        System.out.println("You can search for the booked room with specific room ID.");
        System.out.println("You can also click [Enter] to ignore it, and it will give you all booked room information.");
        System.out.println("You can also search for the booked room of a certain type.");
        System.out.println("------------------------------");
        System.out.println("1. Large double bed");
        System.out.println("2. Large single bed");
        System.out.println("3. Small single bed");
        System.out.println("4. VIP Room");
        System.out.println("------------------------------");
        System.out.print("or you can type in \"Return\" back to the previous page: ");
        String input = scanner.nextLine().trim().toLowerCase();
        Pattern pattern = Pattern.compile("^[0-9]{3,4}$");
        whileLoop:
        while (true) {
            if (input.isEmpty()) {
                query("0");
            } else if (input.equals("return")) {
                return staffInfo;
            } else if (Pattern.compile("^[A-Z a-z]+$").matcher(input).matches() || Pattern.compile("^[1-4]$").matcher(input).matches()) {
                //Still developing
                switch (input) {
                    case "1":
                    case "large double bed":
                        query("1");
                        break;
                    case "2":
                    case "large single bed":
                        query("2");
                        break;
                    case "3":
                    case "small single bed":
                        query("3");
                        break;
                    case "4":
                    case "vip room":
                        query("4");
                        break;
                    default:
                        System.out.println("======================================================================================");
                        System.out.print("Please type in a valid information, or click [Enter] to query all booked room records: ");
                        input = scanner.nextLine().trim().toLowerCase();
                        continue whileLoop;
                }
            } else if (!pattern.matcher(input).matches()) {
                System.out.println("======================================================================================");
                System.out.print("Please type in a valid information, or click [Enter] to query all booked room records: ");
                input = scanner.nextLine().trim().toLowerCase();
                continue;
            } else {
                query(input);
            }
            System.out.println();
            System.out.print("You can check booked room again or type in \"Return\" back to previous page: ");
            input = scanner.nextLine().trim().toLowerCase();
        }
    }

    private static void query(String roomID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String repeatSql = "SELECT realName,roomID,roomType,checkInDate,checkOutDate FROM BookedRoom NATURAL JOIN room NATURAL JOIN RoomType LEFT OUTER JOIN Guest USING (userID) WHERE roomTypeID = ";
        String sql;
        try {
            connection = DB_Utility.connect();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            switch (roomID) {
                case "0":
                    sql = "SELECT realName,roomID,roomType,checkInDate,checkOutDate FROM BookedRoom NATURAL JOIN room NATURAL JOIN RoomType LEFT OUTER JOIN Guest USING (userID)";
                    break;
                case "1":
                    sql = repeatSql + Integer.parseInt("1");
                    break;
                case "2":
                    sql = repeatSql + Integer.parseInt("2");
                    break;
                case "3":
                    sql = repeatSql + Integer.parseInt("3");
                    break;
                case "4":
                    sql = repeatSql + Integer.parseInt("4");
                    break;
                default:
                    sql = "SELECT realName,roomID,roomType,checkInDate,checkOutDate FROM BookedRoom NATURAL JOIN room NATURAL JOIN RoomType LEFT OUTER JOIN Guest USING (userID) WHERE roomID = " + Integer.parseInt(roomID);
            }
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                System.out.println("*************************** " + resultSet.getRow() + ". row ***************************");
                System.out.println("Real Name of Guest: " + resultSet.getString(1));
                System.out.println("Room ID: " + resultSet.getString(2));
                System.out.println("Room Type: " + resultSet.getString(3));
                System.out.println("Check-in Date: " + resultSet.getString(4));
                System.out.println("Check-out Date: " + resultSet.getString(5));
            }

            resultSet.beforeFirst();
            if (!resultSet.next()){
                System.out.println("*********");
                System.out.println("Empty Set");
                System.out.println("*********");
            }else {
                System.out.println("*************************** end row ***************************");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, statement, resultSet);
        }
    }

    //Maybe I will develop a function of adding blacklist
    public static int[] addBlackList(int staffID) {
        DB_Utility.printCurrentTime();
        int[] staffInfo = {16, staffID};
        return staffInfo;
    }

    public static int[] changePassword(int staffID) {
        DB_Utility.printCurrentTime();
        int[] staffInfo = {16, staffID};
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql, password;
        try {
            connection = DB_Utility.connect();

            sql = "SELECT password FROM staff WHERE staffID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, staffID);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            password = resultSet.getString(1);//Password
            while (true) {

                System.out.println("------------------------------");
                System.out.println("Your old password: " + password);
                System.out.println("------------------------------");
                System.out.print("Please type in your new password: ");
                String input = Guest.verifyEmpty(scanner, "your new password: ", "Please type in ");

                if (input == null) {
                    DB_Utility.close(connection, preparedStatement, resultSet);
                    break;
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

                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return staffInfo;
    }
}
