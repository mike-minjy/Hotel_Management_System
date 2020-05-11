package utils;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Booking {

    private Booking() {
    }

    public static int[] startInterface(int userID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int[] userInformation = new int[2];
        String sql, username;
        try {
            connection = DB_Utility.connect();
            sql = "SELECT username FROM Guest WHERE userID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            username = resultSet.getString("username");
            userInformation = startInterface(userID, username);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return userInformation;
    }

    private static int[] startInterface(int userID, String username) {
        DB_Utility.printCurrentTime();
        int[] ints = {1, userID};
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Book Rooms");
        System.out.println("2. Cancel Booked Rooms");
        System.out.println("3. Book Meal");//Still developing
        System.out.println("4. Cancel Booked Meal");//Still developing
        System.out.println("5. Update Personal Details");//Almost done
        System.out.println("6. Log out");
        System.out.println("7. quit the system");
        System.out.println();
        System.out.println("Account: " + username);
        System.out.print("Please type in the corresponding number to choose your option: ");
        String input = scanner.nextLine().trim().toLowerCase();
        while (true) {
            switch (input) {
                case "1":
                case "book rooms":
                    ints[0] = 10;
                    return ints;
                case "2":
                case "cancel booked rooms":
                    ints[0] = 11;
                    return ints;
                case "3":
                case "book meal"://Still developing
                    ints[0] = 12;
                    return ints;
                case "4":
                case "cancel booked meal"://Still developing
                    ints[0] = 13;
                    return ints;
                case "5":
                case "update personal details"://Almost done
                    ints[0] = 14;
                    return ints;
                case "6":
                case "log out"://Done
                    return ints;
                case "7":
                case "quit the system"://Done
                    ints[0] = -1;
                    return ints;
                default:
                    System.out.println();
                    System.out.println("=====================================================");
                    System.out.println("Account: " + username);
                    System.out.print("Please type in a valid number or full name of option: ");
                    input = scanner.nextLine().trim().toLowerCase();
                    break;
            }
        }
    }

    public static int[] bookRooms(int userID) {
        DB_Utility.printCurrentTime();
        int[] userInfo = {9, userID};
        int roomID = 0;
        byte roomType_ID = 0;
        String input;
        boolean[] identities = {true, false};//boolean[] identities = {notHasBeenBooked, isMultiple};
        LocalDate checkInDate, checkOutDate;
        Map<Integer, String> roomType = new HashMap<>();
        roomType.put(1, "Large double bed");
        roomType.put(2, "Large single bed");
        roomType.put(3, "Small single bed");
        roomType.put(4, "VIP Room");

        System.out.println("You can choose 4 different types of room in the hotel.");
        System.out.println("---------------------------------------------------------");
        System.out.println("1. Large room with double beds. (" + roomType.get(1) + ")");
        System.out.println("2. Large room with a large single bed. (" + roomType.get(2) + ")");
        System.out.println("3. Small room with a single bed. (" + roomType.get(3) + ")");
        System.out.println("4. " + roomType.get(4) + ".");
        System.out.println("---------------------------------------------------------");
        printLayout();

        Scanner scanner = new Scanner(System.in);
        //Begin: Get room type
        //Should specify the room type for guest automatically if they do not specify it
        while (identities[0]) {
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("You can click [Enter] to skip it, but the room type will be chosen in random.");
            System.out.print("You can choose the room for a certain type (Type \"Return\" to cancel the booking step): ");
            input = scanner.nextLine().trim().toLowerCase();
            roomTypeLoop:
            while (true) {
                switch (input) {
                    case "return":
                        return userInfo;
                    case "1":
                    case "large double bed":
                        roomType_ID = 1;
                        break roomTypeLoop;
                    case "2":
                    case "large single bed":
                        roomType_ID = 2;
                        break roomTypeLoop;
                    case "3":
                    case "small single bed":
                        roomType_ID = 3;
                        break roomTypeLoop;
                    case "4":
                    case "vip room":
                        roomType_ID = 4;
                        break roomTypeLoop;
                    case "":
                        break roomTypeLoop;
                    default:
                        System.out.println("===============================================================================================");
                        System.out.print("Please choose a certain type of room in the list or type in \"Return\" back to the previous page: ");
                        input = scanner.nextLine().trim().toLowerCase();
                        break;
                }
            }
            //End: Get room type
            //Begin: Get check-in and check-out date
            System.out.println();
            System.out.println("-------------------------------------------------------------------------------------------------");
            System.out.println("You can type in the check-in and check-out date with \"Year-Month-Day\" or \"Month-Day\".");
            System.out.println("The system will set \"Year\" as the same year of your current time if you not specify the year.");
            System.out.println("The time format could be (All these format could ignore Year): ");
            System.out.println("\"(Year-)Month-Day\", \"(Year/)Month/Day\", \"(Year_)Month_Day\", \"(Year.)Month.Day\",\"(Year )Month Day\"");
            System.out.println("You can also type in \"Return\" to cancel the booking step.");
            System.out.println("If you do not specify the date, system will set it as the same day of today.");
            System.out.print("Please type in when you want to live in the hotel (Click [Enter] to skip it): ");
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                checkInDate = LocalDate.now();
            } else {
                checkInDate = getValidDate(scanner, input, true);
            }
            if (checkInDate == null) {
                return userInfo;
            }
            System.out.println("Please type in when you want to leave the hotel (Click [Enter] to skip it)");
            System.out.print("(If you do not specify the date, system will set it as the same day of check-in date): ");
            input = scanner.nextLine().trim();
            while (true) {
                if (input.isEmpty()) {
                    checkOutDate = checkInDate;
                    break;
                } else {
                    checkOutDate = getValidDate(scanner, input, false);
                    if (checkOutDate == LocalDate.MIN) {
                        checkOutDate = checkInDate;
                        break;
                    } else if (checkOutDate == null) {
                        return userInfo;
                    } else if (checkOutDate.isBefore(checkInDate)) {
                        System.out.println("==================================================================");
                        System.out.print("The check-out date should be equals to or after the check-in date: ");
                        input = scanner.nextLine().trim();
                    } else {
                        break;
                    }
                }
            }
            //End: Get date
            //Check rooms
            byte bookingSituation = checkNotBookedRooms(roomType_ID, checkInDate, checkOutDate);
            if (bookingSituation == 1) {
                identities[0] = false;
            } else if (bookingSituation == 2) {
                identities[0] = false;
                identities[1] = true;
            }
            if (!identities[0]) {//Room have not been booked
                //insert into table
                //if multiple rooms available, guest can choose a specific room.
                if (identities[1]) {
                    while (true) {
                        printAvailableRooms(roomType_ID, checkInDate, checkOutDate);
                        System.out.println("You can click [Enter] to skip this step, then system will automatically arrange an empty room of same type for you.");
                        System.out.print("There are several available rooms, you can type in the number of roomID to choose a specific room now: ");
                        String room_ID_Handler = scanner.nextLine().trim();
                        if (room_ID_Handler.isEmpty()) {
                            break;
                        } else if (Pattern.compile("^[0-9]{3,4}$").matcher(room_ID_Handler).matches() &&
                                Integer.parseInt(room_ID_Handler) <= 1013 && Integer.parseInt(room_ID_Handler) >= 101) {
                            roomID = Integer.parseInt(room_ID_Handler);
                        } else if (room_ID_Handler.equalsIgnoreCase("Return")) {
                            return userInfo;
                        } else {
                            System.out.println("==============================");
                            System.out.println("Please type in a valid roomID.");
                            System.out.println("==============================");
                            continue;
                        }
                        if (checkRoomID(roomID, checkInDate, checkOutDate)) {
                            break;
                        } else {
                            System.out.println("=================================");
                            System.out.println("Please type in an available room.");
                            System.out.println("=================================");
                            //There is an issue about "living one day" guest, they cannot be split from available rooms.
                        }
                    }
                }
                if (roomType_ID == 0) {
                    insertData(userID, checkInDate, checkOutDate);
                } else {
                    if (roomID != 0) {
                        insertData(userID, roomID, checkInDate, checkOutDate);
                    } else {
                        insertData(userID, roomType_ID, checkInDate, checkOutDate);
                    }
                }
            } else {
                System.out.println("===============================================================================");
                System.out.println("The room type \"" + roomType.get(roomType_ID) + "\" is not available in this period.");
                System.out.println("Please change the room type or check-in/check-out date and book the room again.");
                System.out.println("We apologise for the inconvenience in the booking steps.");
                System.out.println("===============================================================================");
                printAvailableRooms(0, checkInDate, checkOutDate);
                System.out.println("================================================================================================");
                System.out.println("These are the other types of room in your specified period.");
                System.out.println("You can reference these available rooms to reset your roomType,check-in date and check-out date.");
                System.out.println("If there are no available rooms, please change check-in/check-out date and try again.");
                System.out.println("================================================================================================");
                System.out.println();
            }
        }
        return userInfo;
    }

    private static void insertData(int userID, byte roomType_ID, LocalDate checkInDate, LocalDate checkOutDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int roomID = 0;
        try {
            connection = DB_Utility.connect();
            String sql = "SELECT DISTINCT roomID FROM Room NATURAL JOIN RoomType LEFT OUTER JOIN BookedRoom USING (roomID) WHERE roomTypeID = ? AND ((checkOutDate < ? OR checkInDate > ?) OR (checkInDate IS NULL AND checkOutDate IS NULL))";
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setByte(1, roomType_ID);
            preparedStatement.setDate(2, Date.valueOf(checkInDate));
            preparedStatement.setDate(3, Date.valueOf(checkOutDate));
            resultSet = preparedStatement.executeQuery();
            roomID = getRandomRoom(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        insertData(userID, roomID, checkInDate, checkOutDate);
    }

    private static int getRandomRoom(ResultSet resultSet) throws Exception {
        int roomID = 0;
        resultSet.last();
        int totalRow = resultSet.getRow();
        int whichTuple = (int) (totalRow * Math.random() + 1);
        resultSet.beforeFirst();
        while (resultSet.next()) {
            if (resultSet.getRow() == whichTuple && whichTuple < totalRow + 1) {
                roomID = resultSet.getInt("roomID");
                break;
            }
        }
        return roomID;
    }

    private static byte checkNotBookedRooms(int roomType_ID, LocalDate checkInDate, LocalDate checkOutDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        byte notHasBeenBooked = 0;
        String sql;
        try {
            connection = DB_Utility.connect();
            if (roomType_ID == 0) {
                sql = "SELECT DISTINCT roomID FROM Room NATURAL JOIN RoomType LEFT OUTER JOIN BookedRoom USING (roomID) WHERE (checkOutDate < ? OR checkInDate > ?) OR (checkInDate IS NULL AND checkOutDate IS NULL)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setDate(1, Date.valueOf(checkInDate));
                preparedStatement.setDate(2, Date.valueOf(checkOutDate));
            } else {
                sql = "SELECT DISTINCT roomID FROM Room NATURAL JOIN RoomType LEFT OUTER JOIN BookedRoom USING (roomID) WHERE roomTypeID = ? AND ((checkOutDate < ? OR checkInDate > ?) OR (checkInDate IS NULL AND checkOutDate IS NULL))";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, roomType_ID);
                preparedStatement.setDate(2, Date.valueOf(checkInDate));
                preparedStatement.setDate(3, Date.valueOf(checkOutDate));
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                notHasBeenBooked = 1;
            }
            if (resultSet.next()) {
                notHasBeenBooked = 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return notHasBeenBooked;
    }

    private static boolean checkRoomID(int roomID, LocalDate checkInDate, LocalDate checkOutDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isAvailable = false;
        try {
            connection = DB_Utility.connect();
            String sql = "SELECT DISTINCT roomID FROM Room NATURAL JOIN RoomType LEFT OUTER JOIN BookedRoom USING (roomID) WHERE roomID = ? AND ((checkOutDate < ? OR checkInDate > ?) OR (checkInDate IS NULL AND checkOutDate IS NULL))";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, roomID);
            preparedStatement.setDate(2, Date.valueOf(checkInDate));
            preparedStatement.setDate(3, Date.valueOf(checkOutDate));
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) isAvailable = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return isAvailable;
    }

    private static void printAvailableRooms(int roomType_ID, LocalDate checkInDate, LocalDate checkOutDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql;
        try {
            connection = DB_Utility.connect();
            if (roomType_ID == 0) {
                sql = "SELECT DISTINCT roomID,roomType FROM Room NATURAL JOIN RoomType LEFT OUTER JOIN BookedRoom USING(roomID) WHERE (checkOutDate < ? OR checkInDate > ?) OR (checkInDate IS NULL AND checkOutDate IS NULL)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setDate(1, Date.valueOf(checkInDate));
                preparedStatement.setDate(2, Date.valueOf(checkOutDate));
            } else {
                sql = "SELECT DISTINCT roomID,roomType FROM Room NATURAL JOIN RoomType LEFT OUTER JOIN BookedRoom USING(roomID) WHERE roomTypeID = ? AND ((checkOutDate < ? OR checkInDate > ?) OR (checkInDate IS NULL AND checkOutDate IS NULL))";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, roomType_ID);
                preparedStatement.setDate(2, Date.valueOf(checkInDate));
                preparedStatement.setDate(3, Date.valueOf(checkOutDate));
            }
            resultSet = preparedStatement.executeQuery();
            System.out.println("--------*----------------");
            System.out.println("  Room  |      Type      ");
            while (resultSet.next()) {
                System.out.print("  " + resultSet.getString(1) + "\t|" + resultSet.getString(2));
                System.out.println();
            }
            System.out.println("--------*----------------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
    }

    @Deprecated
    private static boolean checkBookedRoom(int roomType_ID, LocalDate checkInDate, LocalDate checkOutDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean hasBeenBooked = false;
        String sql;
        try {
            connection = DB_Utility.connect();
            if (roomType_ID == 0) {
                sql = "SELECT * FROM Room NATURAL JOIN RoomType NATURAL JOIN BookedRoom WHERE NOT (checkOutDate < ? OR checkInDate > ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setDate(1, Date.valueOf(checkInDate));
                preparedStatement.setDate(2, Date.valueOf(checkOutDate));
            } else {
                sql = "SELECT * FROM Room NATURAL JOIN RoomType NATURAL JOIN BookedRoom WHERE roomTypeID = ? AND NOT (checkOutDate < ? OR checkInDate > ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, roomType_ID);
                preparedStatement.setDate(2, Date.valueOf(checkInDate));
                preparedStatement.setDate(3, Date.valueOf(checkOutDate));
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                hasBeenBooked = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return hasBeenBooked;
    }

    private static void insertData(int userID, LocalDate checkInDate, LocalDate checkOutDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int roomID = 0;
        try {
            connection = DB_Utility.connect();
            String sql = "SELECT DISTINCT roomID FROM Room LEFT OUTER JOIN BookedRoom USING (roomID) WHERE (checkOutDate < ? OR checkInDate > ?) OR (checkInDate IS NULL AND checkOutDate IS NULL)";
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setDate(1, Date.valueOf(checkInDate));
            preparedStatement.setDate(2, Date.valueOf(checkOutDate));
            resultSet = preparedStatement.executeQuery();
            roomID = getRandomRoom(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        insertData(userID, roomID, checkInDate, checkOutDate);
    }

    private static void insertData(int userID, int roomID, LocalDate checkInDate, LocalDate checkOutDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DB_Utility.connect();
            String sql = "INSERT INTO BookedRoom (userID, roomID, checkInDate, checkOutDate, operationTime) VALUES (?,?,?,?,NOW())";
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, roomID);
            preparedStatement.setDate(3, Date.valueOf(checkInDate));
            preparedStatement.setDate(4, Date.valueOf(checkOutDate));

            preparedStatement.executeUpdate();
            System.out.println();
            System.out.println("---------------------------------------------------------------------------------------------------");
            System.out.println("Reservation Successful! You have booked Room " + roomID + ". We would be appreciate to see you in " + checkInDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".");
            System.out.println("---------------------------------------------------------------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement);
        }
    }

    private static LocalDate getValidDate(Scanner scanner, String input, boolean isCheckInDate) {
        Pattern pattern = Pattern.compile("^[-/_. 0-9]{1,18}$");
        int year, month, day;
        loopFlag:
        while (true) {
            String[] date = input.split("[-/_. ]");
            if (input.equalsIgnoreCase("Return")) {
                return null;
            }
            if (!isCheckInDate && input.isEmpty()) {
                return LocalDate.MIN;
            } else if (isCheckInDate && input.isEmpty()) {
                return LocalDate.now();
            }
            for (String each : date) {
                if (each.isEmpty()) {
                    input = validDate(scanner);
                    continue loopFlag;
                }
            }
            if (!pattern.matcher(input).matches() || !(date.length == 3 || date.length == 2)) {
                input = validDate(scanner);
                continue;
            }
            if (date.length == 2) {
                Calendar getDate = Calendar.getInstance();
                String stringYear = String.valueOf(getDate.get(Calendar.YEAR));
                input = stringYear + "-" + input;
                date = input.split("[-/_. ]");
            }
            year = Integer.parseInt(date[0]);
            if (year > 9999) {
                System.out.println("===========================================");
                System.out.println("The number of year cannot larger than 9999.");
                System.out.println("===========================================");
                input = validDate(scanner);
                continue;
            }
            month = Integer.parseInt(date[1]);
            day = Integer.parseInt(date[2]);
            boolean leapYear = year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
            if (year >= 3200) {
                leapYear = false;
            }
            if (month > 12 || month < 1) {//Check whether it is a valid month
                input = validDate(scanner);
                continue;
            }
            if (leapYear) {
                //Check whether it is a leap year
                if (month == 2 && (day > 29 || day < 1)) {//Check valid day
                    input = validDate(scanner);
                    continue;
                } else if (verifyMonth(month) && (day > 31 || day < 1)) {//Check valid day for longer month
                    input = validDate(scanner);
                    continue;
                } else if (!verifyMonth(month) && (day > 30 || day < 1)) {//Check valid day for shorter month
                    input = validDate(scanner);
                    continue;
                }
            } else {//Not a leap year
                if (month == 2 && (day > 28 || day < 1)) {//Check valid day
                    input = validDate(scanner);
                    continue;
                } else if (verifyMonth(month) && (day > 31 || day < 1)) {//Check valid day for longer month
                    input = validDate(scanner);
                    continue;
                } else if (!verifyMonth(month) && (day > 30 || day < 1)) {//Check valid day for shorter month
                    input = validDate(scanner);
                    continue;
                }
            }
            if (LocalDate.of(year, month, day).isBefore(LocalDate.now())) {
                System.out.println("=========================================");
                System.out.println("You cannot specify the date before today.");
                System.out.println("=========================================");
                input = validDate(scanner);
                continue;
            }
            break;
        }
        return LocalDate.of(year, month, day);
    }

    private static String validDate(Scanner scanner) {
        System.out.println("============================");
        System.out.print("Please type in a valid date: ");
        return scanner.nextLine().trim();
    }

    private static boolean verifyMonth(int month) {
        int[] longerMonth = {1, 3, 5, 7, 8, 10, 12};
        int[] shorterMonth = {4, 6, 9, 11};
        for (int lm : longerMonth) {
            if (month == lm) {//It is a longer month
                return true;
            }
        }
        for (int sm : shorterMonth) {
            if (month == sm) {//It is a shorter month
                return false;
            }
        }
        return false;//Default: shorter month
    }

    private static void printLayout() {
        System.out.println("The basic layout of hotel presented as below:");
        System.out.println("*----------------------------------------------------------------------------------------------*");
        System.out.println("| Large double bed | Large double bed | Large single bed | Large single bed | Small single bed |");
        System.out.println("|     Room X01     |     Room X02     |     Room X03     |     Room X04     |     Room X05     |");
        System.out.println("|----------------------------------------------------------------------------------------------|");
        System.out.println("|                                                                                              |");
        System.out.println("|------------------*                                                        *------------------|");
        System.out.println("|                  |                                                        | Small single bed |");
        System.out.println("|     VIP Room     |                                                        |     Room X06     |");
        System.out.println("|                  |                     Stairs & Lobby                     |------------------|");
        System.out.println("|     Room X13     |                                                        | Small single bed |");
        System.out.println("|                  |                                                        |     Room X07     |");
        System.out.println("|------------------*                                                        *------------------|");
        System.out.println("|                                                                                              |");
        System.out.println("|----------------------------------------------------------------------------------------------|");
        System.out.println("| Large double bed | Large double bed | Large single bed | Large single bed | Small single bed |");
        System.out.println("|     Room X12     |     Room X11     |     Room X10     |     Room X09     |     Room X08     |");
        System.out.println("*----------------------------------------------------------------------------------------------*");
        System.out.println("(\"X\" stands for the number of floor.)");
        System.out.println();
    }

    public static int[] cancelRooms(int userID) {
        int[] userInfo = {9, userID};
        //Set the room cannot be canceled after guest leaved hotel
        //If guest leave hotel before check-out date, the end date will automatically set to leave date
        //the record cannot be "deleted" after guest live in the hotel
        return userInfo;
    }

    public static int[] bookMeal(int userID) {
        int[] userInfo = {9, userID};
        return userInfo;
    }

    public static int[] cancelMeal(int userID) {
        int[] userInfo = {9, userID};
        return userInfo;
    }

    public static int[] update(int userID) {
        DB_Utility.printCurrentTime();
        int[] userInfo = {9, userID};
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DB_Utility.connect();
            String sql = "SELECT userID,username,password,realName AS 'Real Name',passportID AS 'Passport ID',telephoneNumber AS 'Phone Number',email FROM Guest WHERE userID = ?";
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, userID);
            resultSet = preparedStatement.executeQuery();
            Guest.sharedUpdate("Please type in ", scanner, connection, preparedStatement, resultSet);
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (Exception throwable) {
                    throwable.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return userInfo;
    }
}
