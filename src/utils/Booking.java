package utils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Booking {

    /**
     * Prevent create an object of this class.
     */
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
        System.out.println("2. Modify Booked Rooms");
        System.out.println("3. Book Meal");//Still developing
        System.out.println("4. Cancel Booked Meal");//Still developing
        System.out.println("5. Update Personal Details");
        System.out.println("6. Log out");
        System.out.println("7. quit the system");
        System.out.println();
        System.out.println("Account: " + username);
        System.out.print("Please type in the corresponding number to choose your option: ");
        String input = scanner.nextLine().trim().toLowerCase();
        while (true) {
            switch (input) {
                case "1":
                case "book rooms"://Almost done
                    ints[0] = 11;
                    return ints;
                case "2":
                case "modify booked rooms":
                    ints[0] = 12;
                    return ints;
                case "3":
                case "book meal"://Still developing
                    ints[0] = 13;
                    return ints;
                case "4":
                case "cancel booked meal"://Still developing
                    ints[0] = 14;
                    return ints;
                case "5":
                case "update personal details"://Almost done
                    ints[0] = 15;
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
        int[] userInfo = {10, userID};
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
                        //Try not make this happened
//                        System.out.println("================================================================================================================================");
//                        System.out.println("Notice: The last few rooms might be booked by other guests during this period.");
//                        System.out.println("If you choose a room which has been booked after the selection, you will be random distribute a room for the same time interval.");
//                        System.out.println("================================================================================================================================");
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
                System.out.println("The room type \"" + roomType.get((int) roomType_ID) + "\" is not available in this period.");
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
        String sql;
        try {
            connection = DB_Utility.connect();

            sql = "SELECT DISTINCT roomID FROM Room NATURAL JOIN RoomType LEFT OUTER JOIN BookedRoom USING (roomID) WHERE " +
                    "roomTypeID = ? AND (roomID NOT IN(SELECT roomID FROM bookedroom WHERE (checkInDate >= ? AND checkOutDate <= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?))" +
                    " OR (checkInDate IS NULL AND checkOutDate IS NULL))";

            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setByte(1, roomType_ID);
            preparedStatement.setDate(2, Date.valueOf(checkInDate));
            setData2(checkOutDate, checkInDate, preparedStatement, Date.valueOf(checkInDate), Date.valueOf(checkOutDate));
            preparedStatement.setDate(9, Date.valueOf(checkOutDate));
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
                sql = "SELECT DISTINCT roomID FROM Room NATURAL JOIN RoomType LEFT OUTER JOIN BookedRoom USING (roomID)" +
                        " WHERE roomID NOT IN(SELECT roomID FROM bookedroom WHERE (checkInDate >= ? AND checkOutDate <= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)) OR (checkInDate IS NULL AND checkOutDate IS NULL)";
                preparedStatement = connection.prepareStatement(sql);
                setData1(checkInDate, checkOutDate, preparedStatement);
            } else {
                sql = "SELECT DISTINCT roomID FROM Room NATURAL JOIN RoomType LEFT OUTER JOIN BookedRoom USING (roomID)" +
                        " WHERE roomTypeID = ? AND " +
                        "((roomID NOT IN(SELECT roomID FROM bookedroom WHERE (checkInDate >= ? AND checkOutDate <= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)))" +
                        " OR (checkInDate IS NULL AND checkOutDate IS NULL))";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, roomType_ID);
                preparedStatement.setDate(2, Date.valueOf(checkInDate));
                setData2(checkOutDate, checkInDate, preparedStatement, Date.valueOf(checkInDate), Date.valueOf(checkOutDate));
                preparedStatement.setDate(9, Date.valueOf(checkOutDate));
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
        String sql;
        try {
            connection = DB_Utility.connect();

            sql = "SELECT DISTINCT roomID FROM Room NATURAL JOIN RoomType LEFT OUTER JOIN BookedRoom USING (roomID) WHERE roomID = ? AND" +
                    " ((roomID NOT IN(SELECT roomID FROM bookedroom WHERE (checkInDate >= ? AND checkOutDate <= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?))) OR (checkInDate IS NULL AND checkOutDate IS NULL))";


            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, roomID);
            preparedStatement.setDate(2, Date.valueOf(checkInDate));
            setData2(checkOutDate, checkInDate, preparedStatement, Date.valueOf(checkInDate), Date.valueOf(checkOutDate));
            preparedStatement.setDate(9, Date.valueOf(checkOutDate));

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
                sql = "SELECT DISTINCT roomID,roomType FROM Room NATURAL JOIN RoomType LEFT OUTER JOIN BookedRoom USING(roomID) WHERE " +
                        "(roomID NOT IN(SELECT roomID FROM bookedroom WHERE (checkInDate >= ? AND checkOutDate <= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)))" +
                        " OR (checkInDate IS NULL AND checkOutDate IS NULL)";
                preparedStatement = connection.prepareStatement(sql);
                setData1(checkInDate, checkOutDate, preparedStatement);
            } else {
                sql = "SELECT DISTINCT roomID,roomType FROM Room NATURAL JOIN RoomType LEFT OUTER JOIN BookedRoom USING(roomID) WHERE roomTypeID = ?" +
                        " AND ((roomID NOT IN(SELECT roomID FROM bookedroom WHERE (checkInDate >= ? AND checkOutDate <= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)))" +
                        " OR (checkInDate IS NULL AND checkOutDate IS NULL))";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, roomType_ID);
                preparedStatement.setDate(2, Date.valueOf(checkInDate));
                setData2(checkOutDate, checkInDate, preparedStatement, Date.valueOf(checkInDate), Date.valueOf(checkOutDate));
                preparedStatement.setDate(9, Date.valueOf(checkOutDate));
            }

            resultSet = preparedStatement.executeQuery();
            System.out.println("--------*------------------");
            System.out.println("  Room  |      Type");
            while (resultSet.next()) {
                System.out.print("  " + resultSet.getString(1) + "\t|" + resultSet.getString(2));
                System.out.println();
            }
            System.out.println("--------*------------------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
    }

    private static boolean checkBookedRoom(Connection connection, int BookedRoom_ID, int roomID, LocalDate checkInDate, LocalDate checkOutDate) throws Exception {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        boolean hasBeenBooked = false;
        String sql;

//        if (roomType_ID == 0) {
//            sql = "SELECT * FROM Room LEFT OUTER JOIN BookedRoom USING (roomID) WHERE" +
//                    " roomID IN(SELECT roomID FROM BookedRoom WHERE (checkInDate >= ? AND checkOutDate <= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?))" +
//                    " AND bookedRoom_ID = ?";
//            preparedStatement = connection.prepareStatement(sql);
//            setData1(checkInDate, checkOutDate, preparedStatement);
//            preparedStatement.setInt(9,BookedRoom_ID);
//        } else {
        sql = "SELECT * FROM Room LEFT OUTER JOIN BookedRoom USING (roomID) WHERE roomID = ?" +
                " AND bookedRoom_ID IN(SELECT bookedRoom_ID FROM bookedroom WHERE (checkInDate >= ? AND checkOutDate <= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?))" +
                " AND bookedRoom_ID <> ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, roomID);
        preparedStatement.setDate(2, Date.valueOf(checkInDate));
        setData2(checkOutDate, checkInDate, preparedStatement, Date.valueOf(checkInDate), Date.valueOf(checkOutDate));
        preparedStatement.setDate(9, Date.valueOf(checkOutDate));
        preparedStatement.setInt(10, BookedRoom_ID);
//        }
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            hasBeenBooked = true;
        }

        resultSet.close();
        preparedStatement.close();

        return hasBeenBooked;
    }

    private static void insertData(int userID, LocalDate checkInDate, LocalDate checkOutDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int roomID = 0;
        String sql;
        try {
            connection = DB_Utility.connect();

            sql = "SELECT DISTINCT roomID FROM Room LEFT OUTER JOIN BookedRoom USING (roomID)" +
                    " WHERE (roomID NOT IN(SELECT roomID FROM bookedroom WHERE (checkInDate >= ? AND checkOutDate <= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)))" +
                    " OR (checkInDate IS NULL AND checkOutDate IS NULL)";

            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            setData1(checkInDate, checkOutDate, preparedStatement);
            resultSet = preparedStatement.executeQuery();
            roomID = getRandomRoom(resultSet);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        insertData(userID, roomID, checkInDate, checkOutDate);
    }

    private static void setData1(LocalDate checkInDate, LocalDate checkOutDate, PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setDate(1, Date.valueOf(checkInDate));
        preparedStatement.setDate(2, Date.valueOf(checkOutDate));
        setData2(checkInDate, checkOutDate, preparedStatement, Date.valueOf(checkInDate), Date.valueOf(checkOutDate));
    }

    private static void setData2(LocalDate checkInDate, LocalDate checkOutDate, PreparedStatement preparedStatement, Date date1, Date date2) throws Exception {
        preparedStatement.setDate(3, Date.valueOf(checkInDate));
        preparedStatement.setDate(4, date1);
        preparedStatement.setDate(5, Date.valueOf(checkOutDate));
        preparedStatement.setDate(6, date2);
        preparedStatement.setDate(7, Date.valueOf(checkInDate));
        preparedStatement.setDate(8, Date.valueOf(checkOutDate));
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
        Pattern pattern = Pattern.compile("^[-/_. 0-9]{1,10}$");
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
                int intYear = LocalDate.now().getYear();
                input = intYear + "-" + input;
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
                System.out.println("=====================================");
                System.out.println("You cannot set the date before today.");
                System.out.println("=====================================");
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

    public static int[] modifyRooms(int userID) {
        DB_Utility.printCurrentTime();
        int[] userInfo = {10, userID};
        //Set the room cannot be canceled after guest leaved hotel
        //If guest wants to leave hotel before check-out date, they can modify the check-out date
        //the record cannot be "deleted" after guest live in the hotel
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Modify Check-in Date");
        System.out.println("2. Modify Check-out Date");
        System.out.println("3. Cancel Booked Room");
//        System.out.println("4. Change room");
        System.out.println("4. Back to previous page");
        System.out.println("5. Log out");
        System.out.println("6. quit the system");
        System.out.println();
        System.out.print("Please type in the corresponding number to choose your option: ");
        String input = scanner.nextLine().trim().toLowerCase();
        while (true) {
            switch (input) {
                case "1":
                case "modify check-in date":
                    userInfo[0] = 20;
                    return userInfo;
                case "2":
                case "modify check-out date":
                    userInfo[0] = 21;
                    return userInfo;
                case "3":
                case "cancel booked room":
                    userInfo[0] = 22;
                    return userInfo;
                case "4":
                case "back to previous page":
                    return userInfo;
                case "5":
                case "log out":
                    userInfo[0] = 1;
                    return userInfo;
                case "6":
                case "quit the system":
                    userInfo[0] = -1;
                    return userInfo;
                default:
                    System.out.println();
                    System.out.println("=====================================================");
                    System.out.print("Please type in a valid number or full text of option: ");
                    input = scanner.nextLine().trim().toLowerCase();
                    break;
            }
        }
    }

    public static int[] modifyLiveInDate(int userID) {
        DB_Utility.printCurrentTime();
        int[] userInfo = {12, userID};
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql;
        try {
            connection = DB_Utility.connect();
            sql = "SELECT username AS 'Username',bookedRoom_ID AS 'Order Code',roomID AS 'Room ID',checkInDate AS 'Check-in Date',checkOutDate AS 'Check-out Date'" +
                    "FROM bookedroom LEFT OUTER JOIN Guest USING (userID) WHERE checkInDate > ? AND userID = ?";
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
            preparedStatement.setInt(2, userID);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("============================================================================");
                System.out.println("You will not live in the hotel in the future, please booked a room at first.");
                System.out.println("If you are living in the hotel, you can only modify the check-out date.");
                System.out.println("============================================================================");
            } else {
                String username = resultSet.getString("Username");
                Scanner scanner = new Scanner(System.in);
                connection.setAutoCommit(false);
                outerLoop:
                while (true) {
                    resultSet.last();
                    int[] orderCodes = new int[resultSet.getRow()];
                    int index = 0;

                    System.out.println();
                    System.out.println("Username: " + username);
                    System.out.println("Your booked future live in room listed as below:");
                    printBookedRooms(resultSet, orderCodes, index);
                    System.out.println("You can modify the check-in date of a specific room now.");
                    System.out.println("Type in \"Return\" back to the previous page.");
                    System.out.println("Type in \"Save\" to submit all changes.");
                    System.out.print("Please type in the Order Code which you want to modify the check-in date: ");
                    String input = scanner.nextLine().trim().toLowerCase();
                    while (true) {
                        Pattern pattern = Pattern.compile("^[0-9]{1,10}$");
                        if (input.equals("return") || input.equals("save")) {
                            System.out.println("---------------------");
                            System.out.println("1. Save changes");
                            System.out.println("2. Cancel changes");
                            System.out.println("3. Continue modifying");
                            System.out.println("---------------------");
                            System.out.print("Please choose an option to continue: ");
                            input = scanner.nextLine().trim().toLowerCase();
                            switch (input) {
                                case "1":
                                case "save changes":
                                    connection.commit();
                                    System.out.println();
                                    System.out.println("-----------------------------");
                                    System.out.println("Your changes have been saved.");
                                    System.out.println("-----------------------------");
                                    break outerLoop;
                                case "2":
                                case "cancel changes":
                                    connection.rollback();
                                    System.out.println();
                                    System.out.println("--------------------------------");
                                    System.out.println("Your changes have been canceled.");
                                    System.out.println("--------------------------------");
                                    break outerLoop;
                                case "3":
                                case "continue modifying":
                                    System.out.println("------------------------------");
                                    System.out.print("Please type in the Order Code: ");
                                    input = scanner.nextLine().trim().toLowerCase();
                                    break;
                            }
                        } else if (!pattern.matcher(input).matches() || !codesContains(orderCodes, Integer.parseInt(input))) {
                            System.out.println("==================================");
                            System.out.print("Please type in a valid order code: ");
                            input = scanner.nextLine().trim().toLowerCase();
                        } else {
                            break;
                        }
                    }
                    sql = "SELECT roomID,roomTypeID,userID,checkInDate,checkOutDate FROM room LEFT OUTER JOIN bookedroom USING (roomID)" +
                            " WHERE userID = " + userID + " AND bookedRoom_ID = " + Integer.parseInt(input);
                    resultSet = preparedStatement.executeQuery(sql);
                    resultSet.next();

                    System.out.println("----------------------------------------------------------");
                    System.out.println("Notice: The date regulation is the same as booking a room.");
                    System.out.println("Click [Enter] to set the check-in day as today.");
                    System.out.print("Please type in the new live in date: ");
                    String newLiveInDate = scanner.nextLine().trim().toLowerCase();
                    LocalDate newCheckInDate = getValidDate(scanner, newLiveInDate, true);
                    while (newCheckInDate == null) {
                        System.out.println("---------------------");
                        System.out.println("1. Save changes");
                        System.out.println("2. Cancel changes");
                        System.out.println("3. Continue modifying");
                        System.out.println("---------------------");
                        System.out.print("Please choose an option to continue: ");
                        newLiveInDate = scanner.nextLine().trim().toLowerCase();
                        switch (newLiveInDate) {
                            case "1":
                            case "save changes":
                                connection.commit();
                                System.out.println();
                                System.out.println("-----------------------------");
                                System.out.println("Your changes have been saved.");
                                System.out.println("-----------------------------");
                                break outerLoop;
                            case "2":
                            case "cancel changes":
                                connection.rollback();
                                System.out.println();
                                System.out.println("--------------------------------");
                                System.out.println("Your changes have been canceled.");
                                System.out.println("--------------------------------");
                                break outerLoop;
                            case "3":
                            case "continue modifying":
                                sql = "SELECT username AS 'Username',bookedRoom_ID AS 'Order Code',roomID AS 'Room ID',checkInDate AS 'Check-in Date',checkOutDate AS 'Check-out Date'" +
                                        "FROM bookedroom LEFT OUTER JOIN Guest USING (userID) WHERE checkInDate > ? AND userID = ?";
                                preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
                                preparedStatement.setInt(2, userID);
                                resultSet = preparedStatement.executeQuery();
                                continue outerLoop;
                            default:
                                System.out.println("==============================");
                                System.out.println("Please type in a valid option.");
                                System.out.println("==============================");
                                break;
                        }
                    }
                    while (checkBookedRoom(connection, Integer.parseInt(input), resultSet.getInt("roomID"), newCheckInDate, resultSet.getDate("checkOutDate").toLocalDate())
                            || newCheckInDate.isAfter(resultSet.getDate("checkOutDate").toLocalDate())) {
                        if (newCheckInDate.isAfter(resultSet.getDate("checkOutDate").toLocalDate())) {
                            System.out.println("==================================================");
                            System.out.println("The check-in date cannot set after check-out date.");
                            System.out.print("Please type in a valid check-in date: ");
                        } else {
                            System.out.println("=================================================");
                            System.out.println("There exists other guest living in this period.");
                            System.out.print("Please type in other check-in date and try again: ");
                        }
                        newLiveInDate = scanner.nextLine().trim().toLowerCase();
                        if (newLiveInDate.equals("return")) {
                            sql = "SELECT username AS 'Username',bookedRoom_ID AS 'Order Code',roomID AS 'Room ID',checkInDate AS 'Check-in Date',checkOutDate AS 'Check-out Date'" +
                                    "FROM bookedroom LEFT OUTER JOIN Guest USING (userID) WHERE checkInDate > ? AND userID = ?";
                            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
                            preparedStatement.setInt(2, userID);
                            resultSet = preparedStatement.executeQuery();
                            continue outerLoop;
                        }
                        newCheckInDate = getValidDate(scanner, newLiveInDate, true);
                    }

                    sql = "UPDATE BookedRoom SET checkInDate = ? WHERE bookedRoom_ID = ? AND userID = ?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setDate(1, Date.valueOf(newCheckInDate));
                    preparedStatement.setInt(2, Integer.parseInt(input));
                    preparedStatement.setInt(3, userID);
                    preparedStatement.executeUpdate();

                    sql = "SELECT username AS 'Username',bookedRoom_ID AS 'Order Code',roomID AS 'Room ID',checkInDate AS 'Check-in Date',checkOutDate AS 'Check-out Date'" +
                            "FROM bookedroom LEFT OUTER JOIN Guest USING (userID) WHERE checkInDate > ? AND userID = ?";
                    preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
                    preparedStatement.setInt(2, userID);
                    resultSet = preparedStatement.executeQuery();
                }
            }
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

    public static int[] modifyLeaveDate(int userID) {
        DB_Utility.printCurrentTime();
        int[] userInfo = {12, userID};
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql;
        try {
            connection = DB_Utility.connect();
            sql = "SELECT username AS 'Username',bookedRoom_ID AS 'Order Code',roomID AS 'Room ID',checkInDate AS 'Check-in Date',checkOutDate AS 'Check-out Date'" +
                    "FROM bookedroom LEFT OUTER JOIN Guest USING (userID) WHERE checkOutDate >= ? AND userID = ?";
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
            preparedStatement.setInt(2, userID);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("=================================================================================");
                System.out.println("You will not leave any room, we hope you will live in our hotel in the future.");
                System.out.println("If you have left a room, we hope you will live in Sunny Isle again in the future.");
                System.out.println("=================================================================================");
            } else {
                String username = resultSet.getString("Username");
                Scanner scanner = new Scanner(System.in);
                connection.setAutoCommit(false);
                outerLoop:
                while (true) {
                    resultSet.last();
                    int[] orderCodes = new int[resultSet.getRow()];
                    int index = 0;

                    System.out.println();
                    System.out.println("Username: " + username);
                    System.out.println("Your living hotel room listed as below:");
                    printBookedRooms(resultSet, orderCodes, index);
                    System.out.println("You can modify the check-out date of a specific room now.");
                    System.out.println("Type in \"Return\" back to the previous page.");
                    System.out.println("Type in \"Save\" to submit all changes.");
                    System.out.print("Please type in the Order Code which you want to modify the check-out date: ");
                    String input = scanner.nextLine().trim().toLowerCase();
                    while (true) {
                        Pattern pattern = Pattern.compile("^[0-9]{1,10}$");
                        if (input.equals("return") || input.equals("save")) {
                            System.out.println("---------------------");
                            System.out.println("1. Save changes");
                            System.out.println("2. Cancel changes");
                            System.out.println("3. Continue modifying");
                            System.out.println("---------------------");
                            System.out.print("Please choose an option to continue: ");
                            input = scanner.nextLine().trim().toLowerCase();
                            switch (input) {
                                case "1":
                                case "save changes":
                                    connection.commit();
                                    System.out.println();
                                    System.out.println("-----------------------------");
                                    System.out.println("Your changes have been saved.");
                                    System.out.println("-----------------------------");
                                    break outerLoop;
                                case "2":
                                case "cancel changes":
                                    connection.rollback();
                                    System.out.println();
                                    System.out.println("--------------------------------");
                                    System.out.println("Your changes have been canceled.");
                                    System.out.println("--------------------------------");
                                    break outerLoop;
                                case "3":
                                case "continue modifying":
                                    System.out.println("------------------------------");
                                    System.out.print("Please type in the Order Code: ");
                                    input = scanner.nextLine().trim().toLowerCase();
                                    break;
                            }
                        } else if (!pattern.matcher(input).matches() || !codesContains(orderCodes, Integer.parseInt(input))) {
                            System.out.println("==================================");
                            System.out.print("Please type in a valid order code: ");
                            input = scanner.nextLine().trim().toLowerCase();
                        } else {
                            break;
                        }
                    }
                    sql = "SELECT roomID,roomTypeID,userID,checkInDate,checkOutDate FROM room LEFT OUTER JOIN bookedroom USING (roomID)" +
                            " WHERE userID = " + userID + " AND bookedRoom_ID = " + Integer.parseInt(input);
                    resultSet = preparedStatement.executeQuery(sql);
                    resultSet.next();

                    System.out.println("-------------------------------------------------------------");
                    System.out.println("Notice: The date regulation is the same as booking a room.");
                    System.out.println("Click [Enter] to set the check-out day same as check-in date.");
                    System.out.print("Please type in the new leave date: ");
                    String newLeaveDate = scanner.nextLine().trim().toLowerCase();
                    LocalDate newCheckOutDate = getValidDate(scanner, newLeaveDate, false);
                    if (newCheckOutDate == LocalDate.MIN) {
                        newCheckOutDate = resultSet.getDate("checkInDate").toLocalDate();
                    }
                    while (newCheckOutDate == null) {
                        System.out.println("---------------------");
                        System.out.println("1. Save changes");
                        System.out.println("2. Cancel changes");
                        System.out.println("3. Continue modifying");
                        System.out.println("---------------------");
                        System.out.print("Please choose an option to continue: ");
                        newLeaveDate = scanner.nextLine().trim().toLowerCase();
                        switch (newLeaveDate) {
                            case "1":
                            case "save changes":
                                connection.commit();
                                System.out.println();
                                System.out.println("-----------------------------");
                                System.out.println("Your changes have been saved.");
                                System.out.println("-----------------------------");
                                break outerLoop;
                            case "2":
                            case "cancel changes":
                                connection.rollback();
                                System.out.println();
                                System.out.println("--------------------------------");
                                System.out.println("Your changes have been canceled.");
                                System.out.println("--------------------------------");
                                break outerLoop;
                            case "3":
                            case "continue modifying":
                                sql = "SELECT username AS 'Username',bookedRoom_ID AS 'Order Code',roomID AS 'Room ID',checkInDate AS 'Check-in Date',checkOutDate AS 'Check-out Date'" +
                                        "FROM bookedroom LEFT OUTER JOIN Guest USING (userID) WHERE checkOutDate >= ? AND userID = ?";
                                preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
                                preparedStatement.setInt(2, userID);
                                resultSet = preparedStatement.executeQuery();
                                continue outerLoop;
                            default:
                                System.out.println("==============================");
                                System.out.println("Please type in a valid option.");
                                System.out.println("==============================");
                                break;
                        }
                    }
                    while (checkBookedRoom(connection, Integer.parseInt(input)/*BookedRoom_ID*/, resultSet.getInt("roomID"), resultSet.getDate("checkInDate").toLocalDate(), newCheckOutDate)
                            || newCheckOutDate.isBefore(resultSet.getDate("checkInDate").toLocalDate())) {
                        if (newCheckOutDate.isBefore(resultSet.getDate("checkInDate").toLocalDate())) {
                            System.out.println("=======================================================");
                            System.out.println("You cannot set the check-out date before check-in date.");
                            System.out.print("Please type in a valid check-out date: ");
                        } else {
                            System.out.println("==================================================");
                            System.out.println("There exists other guest living in this period.");
                            System.out.print("Please type in other check-out date and try again: ");
                        }
                        newLeaveDate = scanner.nextLine().trim().toLowerCase();
                        if (newLeaveDate.equals("return")) {
                            sql = "SELECT username AS 'Username',bookedRoom_ID AS 'Order Code',roomID AS 'Room ID',checkInDate AS 'Check-in Date',checkOutDate AS 'Check-out Date'" +
                                    "FROM bookedroom LEFT OUTER JOIN Guest USING (userID) WHERE checkOutDate >= ? AND userID = ?";
                            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
                            preparedStatement.setInt(2, userID);
                            resultSet = preparedStatement.executeQuery();
                            continue outerLoop;
                        }
                        newCheckOutDate = getValidDate(scanner, newLeaveDate, false);
                    }

                    sql = "UPDATE BookedRoom SET checkOutDate = ? WHERE bookedRoom_ID = ? AND userID = ?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setDate(1, Date.valueOf(newCheckOutDate));
                    preparedStatement.setInt(2, Integer.parseInt(input));
                    preparedStatement.setInt(3, userID);
                    preparedStatement.executeUpdate();

                    sql = "SELECT username AS 'Username',bookedRoom_ID AS 'Order Code',roomID AS 'Room ID',checkInDate AS 'Check-in Date',checkOutDate AS 'Check-out Date'" +
                            "FROM bookedroom LEFT OUTER JOIN Guest USING (userID) WHERE checkOutDate >= ? AND userID = ?";
                    preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
                    preparedStatement.setInt(2, userID);
                    resultSet = preparedStatement.executeQuery();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return userInfo;
    }

    private static void printBookedRooms(ResultSet resultSet, int[] orderCodes, int index) throws Exception {
        resultSet.beforeFirst();
        if (resultSet.next()) {
            resultSet.beforeFirst();
            while (resultSet.next()) {
                orderCodes[index] = resultSet.getInt(2);//put order code into an array.
                index++;
                System.out.println("*************************** " + resultSet.getRow() + ". row ***************************");
                for (int i = 2; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.println(resultSet.getMetaData().getColumnLabel(i) + ": " + resultSet.getString(i));
                }
            }
            System.out.println("*************************** end row ***************************");
        } else {
            System.out.println("*********");
            System.out.println("Empty Set");
            System.out.println("*********");
        }
        System.out.println();
    }

    public static int[] cancelBookedRoom(int userID) {
        int[] userInfo = {12, userID};
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DB_Utility.connect();

            String sql = "SELECT username AS 'Username',bookedRoom_ID AS 'Order Code',roomID AS 'Room ID',checkInDate AS 'Check-in Date',checkOutDate AS 'Check-out Date'" +
                    " FROM bookedroom LEFT OUTER JOIN Guest USING (userID) WHERE checkInDate > ? AND userID = ?";
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
            preparedStatement.setInt(2, userID);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("==================================================================================================");
                System.out.println("You have not booked a future live in room, the function is only available for future live in room.");
                System.out.println("You should book a room at first, and this function will be available.");
                System.out.println("If you live in a room today, this function is also not available for you.");
                System.out.println("==================================================================================================");
            } else {
                String username = resultSet.getString("Username");
                Scanner scanner = new Scanner(System.in);
                connection.setAutoCommit(false);
                outerLoop:
                while (true) {
                    resultSet.last();
                    int[] orderCodes = new int[resultSet.getRow()];
                    int index = 0;

                    System.out.println("Username: " + username);
                    System.out.println();
                    System.out.println("Your booked future live in room listed as below:");
                    printBookedRooms(resultSet, orderCodes, index);
                    System.out.println("You can type in the Order Code to cancel that booking.");
                    System.out.println("You can type in \"Return\" back to the previous page.");
                    System.out.println("Type in \"Save\" to submit all changes.");
                    System.out.print("Please type in the Order Code: ");
                    String input = scanner.nextLine().trim().toLowerCase();
                    while (true) {
                        Pattern pattern = Pattern.compile("^[0-9]{1,10}$");
                        if (input.equals("return") || input.equals("save")) {
                            System.out.println("---------------------");
                            System.out.println("1. Save changes");
                            System.out.println("2. Cancel changes");
                            System.out.println("3. Continue modifying");
                            System.out.println("---------------------");
                            System.out.print("Please choose an option to continue: ");
                            input = scanner.nextLine().trim().toLowerCase();
                            switch (input) {
                                case "1":
                                case "save changes":
                                    connection.commit();
                                    System.out.println();
                                    System.out.println("-----------------------------");
                                    System.out.println("Your changes have been saved.");
                                    System.out.println("-----------------------------");
                                    break outerLoop;
                                case "2":
                                case "cancel changes":
                                    connection.rollback();
                                    System.out.println();
                                    System.out.println("--------------------------------");
                                    System.out.println("Your changes have been canceled.");
                                    System.out.println("--------------------------------");
                                    break outerLoop;
                                case "3":
                                case "continue modifying":
                                    System.out.println("------------------------------");
                                    System.out.print("Please type in the Order Code: ");
                                    input = scanner.nextLine().trim().toLowerCase();
                                    break;
                            }
                        } else if (!pattern.matcher(input).matches() || !codesContains(orderCodes, Integer.parseInt(input))) {
                            System.out.println("==================================");
                            System.out.print("Please type in a valid Order Code: ");
                            input = scanner.nextLine().trim().toLowerCase();
                        } else {
                            break;
                        }
                    }
                    sql = "DELETE FROM BookedRoom WHERE userID = ? AND bookedRoom_ID = ?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, userID);
                    preparedStatement.setInt(2, Integer.parseInt(input));
                    preparedStatement.executeUpdate();

                    sql = "SELECT username AS 'Username',bookedRoom_ID AS 'Order Code',roomID AS 'Room ID',checkInDate AS 'Check-in Date',checkOutDate AS 'Check-out Date'" +
                            " FROM bookedroom LEFT OUTER JOIN Guest USING (userID) WHERE checkInDate > ? AND userID = ?";
                    preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
                    preparedStatement.setInt(2, userID);
                    resultSet = preparedStatement.executeQuery();
                }
            }
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

    private static boolean codesContains(int[] orderCodes, int input) {
        if (orderCodes.length == 0) return false;//Empty set cannot have any order code
        for (int eachCode : orderCodes) {
            if (eachCode == input) {
                return true;//The array contains the input order code.
            }
        }
        return false;//Input is not consistent with any order code in the array.
    }

    public static int[] bookMeal(int userID) {
        DB_Utility.printCurrentTime();
        //No matter ordinary customer or guest of hotel can book meal in the hotel, but guest can get 20% discount.
        //ordinary customer bookedRoom will set to false(0)
        int[] userInfo = {10, userID};
        System.out.println("Sunny Isles Hotel provides various of meal.");
        System.out.println("You can obtain a 20% discount for the total price of booked meal.");
        System.out.println("-------------------------------------------");
        System.out.println("1. Book the meal for the day after tomorrow");
        System.out.println("2. Living in the hotel at that time");
        System.out.println("-------------------------------------------");
        System.out.println("If you are consistent with all these condition, congratulations!");
        System.out.println("The chef workday listed in the float window.");
        //Start: Show Dish, Chef, and Price of meal
        int maxRowOfMeal = printChefAndMeal();
        Scanner scanner = new Scanner(System.in);
        System.out.print("You can type in the corresponding Row Number to select meal of specific chef: ");
        String mealAndChef = scanner.nextLine().trim().toLowerCase();
        int dishesType_ID;
        Pattern pattern = Pattern.compile("^[0-9]{1,9}$");
        while (true) {
            if (mealAndChef.equals("return")) {
                return userInfo;
            } else if (mealAndChef.equals("show")) {
                printChefAndMeal();
                System.out.print("You can type in the corresponding Row Number to select meal of specific chef: ");
                mealAndChef = scanner.nextLine().trim().toLowerCase();
            } else if (!pattern.matcher(mealAndChef).matches() || Integer.parseInt(mealAndChef) > maxRowOfMeal || mealAndChef.equals("0")) {
                System.out.println("========================================================================================");
                System.out.println("(Notice: You cannot click [Enter] to select meal in random.)");
                System.out.println("If you have closed the Chef&Meal information window, type in \"Show\" to display it again.");
                System.out.print("Please type in a valid Row Number: ");
                mealAndChef = scanner.nextLine().trim().toLowerCase();
            } else {
                dishesType_ID = Integer.parseInt(mealAndChef);
                break;
            }
        }
        //Selection end
        //Start: Show Weekday of chefs
        System.out.println("----------------------------------------------------");
        System.out.println("The weekday of chefs are listed in the float window.");
        System.out.println("You can reference the table for your meal booking.");
        System.out.println("----------------------------------------------------");
        printWeekdayOfChefs();
        //End
        //Start: Specify meal data and time
        LocalDateTime serveDateTime;
        while (true) {
            System.out.println("(Notice: If you do not set a date, system will set the serve date as today.)");
            System.out.println("(If you set the serve date as today, you cannot get the 20% discount.)");
            System.out.print("Please type in the date of serve date: ");
            String stringServeDateTime = scanner.nextLine().trim();
            LocalDate serveDate = getValidDate(scanner, stringServeDateTime, true);
            if (serveDate == null) {
                return userInfo;//Exit meal booking
            }
            System.out.println("---------------------------------------------------------------------------------------------------");
            System.out.println("(Notice: The time format is \"Hour:Minute(:Second)\")");
            System.out.println("(\"Second\" is not necessary to fill in.)");
            System.out.println("(If you click [Enter] to skip it, the time will be set to 17:30 if your current time before 17:30.)");
            System.out.println("(Similarly: morning --> 7:30, forenoon --> 12:30, night snack --> 21:00, then next day morning)");
            System.out.println("(The meal is only available between 7:00 and 22:00, late order will set time to the next date.)");
            System.out.print("Please type in the time that you want to enjoy the meal: ");
            stringServeDateTime = scanner.nextLine().trim();
            if (stringServeDateTime.equalsIgnoreCase("Return")) {
                return userInfo;//Exit meal booking
            }
            LocalTime serveTime = getValidTime(scanner, stringServeDateTime);
            if (serveTime == null) {
                return userInfo;//Exit meal booking
            } else if (serveTime == LocalTime.MIDNIGHT) {
                serveDate = serveDate.plusDays(1);
                serveTime = LocalTime.of(7, 30, 0);
            }
            serveDateTime = LocalDateTime.of(serveDate, serveTime);
            if (serveDateTime.isBefore(LocalDateTime.now())) {
                System.out.println("========================================");
                System.out.println("You cannot set the Date&Time before now.");
                System.out.println("========================================");
                continue;
            }
            //Verify whether the chef is working on that day
            if (!verifyDayOfWeek(dishesType_ID, serveDateTime.toLocalDate())) {
                System.out.println("=================================================");
                System.out.println("The chef is not available at this specified time.");
                System.out.println("=================================================");
                System.out.println();
                System.out.println("If you have closed the Chef&WeekDay window, type in \"Show\" to display it again.");
                System.out.print("Or press [Enter] to continue: ");
                String buffer = scanner.nextLine().trim().toLowerCase();
                if (buffer.equals("show")) {
                    printWeekdayOfChefs();
                }
                continue;
            }
            System.out.println();
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Order succeed, your serve date time has been recorded --> " + serveDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            System.out.println("-----------------------------------------------------------------------------");
            break;
        }
        //End
        //Start: How many dishes to order?
        System.out.println();
        //how many dishes they want?
        System.out.println("------------------------------------------------------");
        System.out.println("You can click [Enter] to set it as default number \"1\".");
        System.out.print("How many dishes do you want to order: ");
        String stringDishes = scanner.nextLine().trim().toLowerCase();
        int count = 1;
        while (true) {
            if (stringDishes.isEmpty()) {
                break;
            } else if (stringDishes.equals("return")) {
                return userInfo;
            } else if (!pattern.matcher(stringDishes).matches() || stringDishes.equals("0")) {
                System.out.println("======================================================");
                System.out.println("You can click [Enter] to set it as default number \"1\".");
                System.out.print("Please type in valid dishes number you want to order: ");
                stringDishes = scanner.nextLine().trim().toLowerCase();
            } else {
                count = Integer.parseInt(stringDishes);
                break;
            }
        }
        //End
        //Whether the serve date is the day after tomorrow
        boolean rightDay = !serveDateTime.toLocalDate().isBefore(LocalDate.now().plusDays(2));
        //Insert data into table
        insertOrderMealData(userID, dishesType_ID, serveDateTime, count, rightDay);
        System.out.println("---------------------------------------------------------------");
        System.out.println("Order succeed, welcome to enjoy the meal at " + serveDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("---------------------------------------------------------------");
        return userInfo;
    }

    private static boolean verifyDayOfWeek(int dishesType_ID, LocalDate serveDate) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        boolean isWeekDay = false;
        int chefID = 0;
        String sql;
        try {
            connection = DB_Utility.connect();

            //select chefID from meal&chef table from next weekday verification.
            statement = connection.createStatement();
            sql = "SELECT chefID FROM meal WHERE dishesType_ID = " + dishesType_ID;
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                chefID = resultSet.getInt(1);//chefID
            }

            //Weekday verification.
            sql = "SELECT day_ID FROM `schedule` NATURAL JOIN chef NATURAL JOIN OneWeek WHERE chefID = " + chefID;
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                if (serveDate.getDayOfWeek().getValue() == resultSet.getInt(1)) {
                    isWeekDay = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, statement, resultSet);
        }
        return isWeekDay;
    }

    private static void insertOrderMealData(int userID, int dishesType_ID, LocalDateTime serveDateTime, int count, boolean rightDay) {
        boolean bookedRoom = checkWhetherHasBookedRoom(userID, serveDateTime.toLocalDate());
        float eachPrice = getPrice(dishesType_ID);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        float totalPrice;
        try {
            connection = DB_Utility.connect();
            String sql = "INSERT INTO bookedmeal(userID, bookedRoom, dishesType_ID, orderDate, serveDate, count, totalPrice)" +
                    " VALUES (?,?,?,NOW(),?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            preparedStatement.setBoolean(2, bookedRoom);
            preparedStatement.setInt(3, dishesType_ID);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(serveDateTime));
            preparedStatement.setInt(5, count);
            if (bookedRoom && rightDay) {
                totalPrice = (float) (0.8 * eachPrice * count);
            } else {
                totalPrice = eachPrice * count;
            }
            preparedStatement.setFloat(6, totalPrice);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement);
        }
    }

    private static float getPrice(int dishesType_ID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        float eachPrice = 0;
        try {
            connection = DB_Utility.connect();
            statement = connection.createStatement();
            String sql = "SELECT price FROM meal WHERE dishesType_ID = " + dishesType_ID;
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                eachPrice = resultSet.getFloat(1);//Price of each dish
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, statement, resultSet);
        }
        return eachPrice;
    }

    private static boolean checkWhetherHasBookedRoom(int userID, LocalDate serveDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean bookedRoom = false;
        try {
            connection = DB_Utility.connect();
            String sql = "SELECT bookedRoom_ID FROM bookedroom WHERE userID = ? AND ? BETWEEN checkInDate AND checkOutDate";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            preparedStatement.setDate(2, Date.valueOf(serveDate));
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                bookedRoom = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return bookedRoom;
    }

    private static LocalTime getValidTime(Scanner scanner, String serveTime) {
        Pattern pattern = Pattern.compile("^[-:_/. 0-9]{1,8}$");
        int hour, minute, second;
        loopFlag:
        while (true) {
            String[] time = serveTime.split("[-:_/. ]");
            if (serveTime.equalsIgnoreCase("Return")) {
                return null;
            }
            if (serveTime.isEmpty()) {
                if (LocalTime.now().isBefore(LocalTime.of(7, 30, 0))) {
                    return LocalTime.of(7, 30, 0);
                } else if (LocalTime.now().isBefore(LocalTime.of(12, 30, 0))) {
                    return LocalTime.of(12, 30, 0);
                } else if (LocalTime.now().isBefore(LocalTime.of(17, 30, 0))) {
                    return LocalTime.of(17, 30, 0);
                } else if (LocalTime.now().isBefore(LocalTime.of(21, 0, 0))) {
                    return LocalTime.of(21, 0, 0);
                } else {
                    return LocalTime.MIDNIGHT;
                }
            }
            for (String each : time) {
                if (each.isEmpty()) {
                    serveTime = validTime(scanner);
                    continue loopFlag;
                }
            }
            if (!pattern.matcher(serveTime).matches() || !(time.length == 3 || time.length == 2)) {
                serveTime = validTime(scanner);
                continue;
            }
            if (time.length == 2) {
                int intSecond = 0;
                serveTime = serveTime + "-" + intSecond;
                time = serveTime.split("[-:_/. ]");
            }
            hour = Integer.parseInt(time[0]);
            minute = Integer.parseInt(time[1]);
            second = Integer.parseInt(time[2]);
            if (second > 59 || second < 0) {//Check whether it is a valid second input
                if (second >= 60) {
                    minute += 1;
                    second -= 60;
                } else {
                    serveTime = validTime(scanner);
                    continue;
                }
            }
            if (minute > 59 || minute < 0) {//Check whether it is a valid minute input
                if (minute >= 60) {
                    hour += 1;
                    minute -= 60;
                } else {
                    serveTime = validTime(scanner);
                    continue;
                }
            }
            if (hour > 21 || hour < 6) {//Check whether it is a valid hour input
                if ((hour > 0 && hour < 7) || (hour > 21 && hour < 24) || (hour == 24 && minute == 0 && second == 0)) {
                    System.out.println("========================================================");
                    System.out.println("The Food Service is only available between 7:00 to 22:00");
                    System.out.println("========================================================");
                    System.out.print("Please choose another time: ");
                    serveTime = scanner.nextLine().trim();
                } else {
                    serveTime = validTime(scanner);
                }
                continue;
            }
            break;
        }
        return LocalTime.of(hour, minute, second);
    }

    private static String validTime(Scanner scanner) {
        System.out.println("============================");
        System.out.print("Please type in a valid time: ");
        return scanner.nextLine().trim();
    }

    private static void printWeekdayOfChefs() {
        String sql = "SELECT day_Name AS 'Weekday', chefName AS 'Chef Name' FROM `schedule` NATURAL JOIN chef NATURAL JOIN OneWeek";
        TablePrinter.display(sql, "The Weekday of Chefs");
        System.out.println();
    }

    private static int printChefAndMeal() {
        String sql = "SELECT dishesType_ID AS 'Row Number',chefName AS 'Chef Name', dishes AS 'Dishes', price AS 'Price per Dish' FROM meal NATURAL JOIN chef";
        int maxRow = TablePrinter.display(sql, "Chefs with Dishes, and Price");
        System.out.println();
        return maxRow;
    }

    public static int[] cancelMeal(int userID) {
        DB_Utility.printCurrentTime();
        int[] userInfo = {10, userID};
        boolean isEmpty = getOrderCodes(userID).length == 0;
        if (isEmpty) {
            System.out.println("==================================================================");
            System.out.println("You should book a meal at first.");
            System.out.println("Notice: You cannot cancel the meal order with serve date at today.");
            System.out.println("==================================================================");
        } else {
            cancelMealOrder(userID);
        }
        return userInfo;
    }

    private static void cancelMealOrder(int userID) {
        System.out.println("---------------------------------------------------");
        System.out.println("Your booked would be presented in the float window.");
        System.out.println("---------------------------------------------------");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DB_Utility.connect();
            connection.setAutoCommit(false);

            int[] orderCodes = printBookedMeal(userID);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Type in \"Return\" back to previous page.");
            System.out.print("You can type in the \"Order Code\" to cancel that order of meal: ");
            String input = scanner.nextLine().trim().toLowerCase();
            Pattern pattern = Pattern.compile("^[0-9]{1,9}$");
            whileLoop:
            while (true) {
                if (input.equals("return")) {
                    System.out.println("-----------------------");
                    System.out.println("1. Save changes");
                    System.out.println("2. Cancel changes");
                    System.out.println("3. Continue cancel meal");
                    System.out.println("-----------------------");
                    System.out.print("What you want to do next: ");
                    input = scanner.nextLine().trim().toLowerCase();
                    switch (input) {
                        case "1":
                        case "save changes":
                            connection.commit();
                            System.out.println("--------------");
                            System.out.println("Changes saved.");
                            System.out.println("--------------");
                            break whileLoop;
                        case "2":
                        case "cancel changes":
                            connection.rollback();
                            System.out.println("------------------");
                            System.out.println("Changes cancelled.");
                            System.out.println("------------------");
                            break whileLoop;
                        case "3":
                        case "continue cancel meal":
                            System.out.println("You can type in \"Return\" back to previous page.");
                            System.out.println("You can type in \"Show\" to display the Meal Order again.");
                            System.out.print("You could also type in other Order Code to continue cancel: ");
                            input = scanner.nextLine().trim().toLowerCase();
                            break;
                        default:
                            System.out.println("=============================");
                            System.out.print("Please choose a valid option: ");
                            input = scanner.nextLine().trim().toLowerCase();
                            break;
                    }
                } else if (input.equals("show")) {
                    orderCodes = printBookedMeal(userID);
                    System.out.println("You can type in \"Return\" back to previous page.");
                    System.out.println("You can type in \"Show\" to display the Meal Order again.");
                    System.out.print("You could also type in other Order Code to continue cancel: ");
                    input = scanner.nextLine().trim().toLowerCase();
                } else if (!pattern.matcher(input).matches()) {
                    System.out.println("==============================");
                    System.out.print("Please type in a valid number:");
                    input = scanner.nextLine().trim().toLowerCase();
                } else if (!codesContains(orderCodes, Integer.parseInt(input))) {
                    System.out.println("===================================");
                    System.out.print("Please type in an exist Order Code: ");
                    input = scanner.nextLine().trim().toLowerCase();
                } else {
                    String sql = "DELETE FROM bookedmeal WHERE bookedMeal_ID = ?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(input));
                    preparedStatement.executeUpdate();
                    System.out.println("--------------------------");
                    System.out.println("Cancel Meal Order succeed.");
                    System.out.println("--------------------------");
                    //Delete order data from orderCodes array.
                    for (int i = 0; i < orderCodes.length; i++) {
                        if (orderCodes[i] == Integer.parseInt(input)) {
                            orderCodes[i] = -1;//Reset data (stands for remove it)
                            break;
                        }
                    }
                    System.out.println("You can type in \"Return\" back to previous page.");
                    System.out.println("You can type in \"Show\" to display the Meal Order again.");
                    System.out.print("You could also type in other Order Code to continue cancel: ");
                    input = scanner.nextLine().trim().toLowerCase();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (Exception throwable) {
                    throwable.printStackTrace();
                }
            }
        } finally {
            DB_Utility.close(connection, preparedStatement);
        }
    }

    @Deprecated//It's function is a subset of getOrderCodes(int) method.
    private static boolean verifyEmpty(int userID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        boolean isEmpty = true;
        try {
            connection = DB_Utility.connect();
            statement = connection.createStatement();
            String sql = "SELECT bookedMeal_ID FROM bookedmeal WHERE userID = " + userID + " AND serveDate > '" + LocalDate.now().toString() + "'";
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                isEmpty = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, statement, resultSet);
        }
        return isEmpty;
    }

    private static int[] printBookedMeal(int userID) {
        String sql = "SELECT bookedMeal_ID AS 'Order Code',chefName AS 'Chef Name',dishes AS 'Dish Name',serveDate AS 'Service Date',count AS 'Total Count',totalPrice AS 'Total Price'" +
                " FROM BookedMeal NATURAL JOIN meal NATURAL JOIN chef" +
                " WHERE userID = " + userID + " AND serveDate > '" + LocalDateTime.of(LocalDate.now(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "'";
        TablePrinter.display(sql, "Your Booked Meal");
        int[] orderCodes = getOrderCodes(userID);
        System.out.println();
        return orderCodes;
    }

    private static int[] getOrderCodes(int userID) {
        Connection connection=null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int[] orderCodes = null;
        try {
            connection=DB_Utility.connect();
            String sql = "SELECT bookedMeal_ID FROM BookedMeal WHERE userID = ? AND serveDate > ?";
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1,userID);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            resultSet = preparedStatement.executeQuery();
            resultSet.last();
            orderCodes = new int[resultSet.getRow()];
            resultSet.beforeFirst();
            while (resultSet.next()) {
                int index = resultSet.getRow() - 1;
                orderCodes[index] = resultSet.getInt(1);//The order code
            }
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        } catch (Exception e) {
            if (connection!=null){
                try {
                    connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                } catch (Exception throwable) {
                    throwable.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement, resultSet);
        }
        return orderCodes;
    }

    public static int[] update(int userID) {
        DB_Utility.printCurrentTime();
        int[] userInfo = {10, userID};
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
