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

/**
 * Guests could book and cancel rooms or meal in this class.
 */
public class Booking {

    /**
     * Prevent create an object of this class.
     */
    private Booking() {
    }

    /**
     * It is an assistant part of <code>startInterface(int userID, String username)</code><br>
     * This <code>startInterface(int userID)</code> method will fetch the guest name from database.<br>
     * It will give a feedback to the user when they login their account.<br>
     * They can check whether it is their account or not.
     *
     * @param userID
     * @return int array (byte key, userID)
     */
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

    /**
     * The main part of startInterface method, and it is the
     * third step control panel of entire program. (Guest Path)
     *
     * @param userID
     * @param username
     * @return int array (byte key, userID)
     */
    private static int[] startInterface(int userID, String username) {
        DB_Utility.printCurrentTime();
        int[] ints = {1, userID};
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Book Rooms");
        System.out.println("2. Modify Booked Rooms");
        System.out.println("3. Book Meal");
        System.out.println("4. Cancel Booked Meal");
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
                case "book rooms":
                    ints[0] = 11;
                    return ints;
                case "2":
                case "modify booked rooms":
                    ints[0] = 12;
                    return ints;
                case "3":
                case "book meal":
                    ints[0] = 13;
                    return ints;
                case "4":
                case "cancel booked meal":
                    ints[0] = 14;
                    return ints;
                case "5":
                case "update personal details":
                    ints[0] = 15;
                    return ints;
                case "6":
                case "log out":
                    return ints;
                case "7":
                case "quit the system":
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

    /**
     * Guest will book rooms in this method.<br>
     * <br>
     * <p>
     * It will initially display all room types, then show the general layout of the hotel.
     * Guest should select room type at first or click [Enter] to skip it,
     * then they need to specify the check-in and check-out date.
     * If they click [Enter] to skip specify the check-in date,
     * the check-in date will be automatically set as today.
     * If they click [Enter] to skip specify the check-out date, then check-out date will be set as
     * the same day of check-in date.
     * </p>
     * <br>
     * <p>
     * If the guest's specified room type and time interval has more than one empty room,
     * they can choose the specific room ID which they want to live in. If there is only one available
     * room for their specified date and type, then a suitable condition room would be prepared for them.
     * If there is no room for their specified date and type, then this system will display an "Exception"
     * message for them and give some suggested room type for them in the same time interval. If there is
     * no suggested room shown, then guest should change all their specified information again to book a room.
     * </p>
     * <pre>
     *     Notice: The following issue has been partially solved.
     * </pre>
     * <p>
     * This is because this solution cannot prevent already shown information
     * stay valid after other guest book that room.
     * </p>
     * <br>
     * <p>
     * This function block is not thread safety, because the specified room might be booked by other
     * guest. If they spend too much time and other guest who has conflict Time&Room type
     * book the room at first, this specified room will also be shown to the current guest. It might
     * be solved if I set the <code>Global Transaction Isolation Level</code> from
     * <code>TRANSACTION_REPEATABLE_READ</code> to <code>TRANSACTION_READ_COMMITTED</code> then set it
     * back to previous status.
     * </p>
     * <br>
     * <p>
     * The remaining issue might be solve by using "Overtime" reminder.
     * </p>
     *
     * @param userID
     * @return int array (byte key, userID)
     */
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

    /**
     * This method will help user to randomly choose a room with one specified type.<br>
     * The randomly chosen Room ID would be used to <code>INSERT INTO</code> the
     * <code>BookedRoom</code> Table in database.
     *
     * @param userID
     * @param roomType_ID
     * @param checkInDate
     * @param checkOutDate
     */
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

    /**
     * This method will choose a room for user if they skip to specify a Room ID.<br>
     * It is implemented by pass an object of <code>ResultSet</code> and randomly
     * select one row from this result set via <code>Math.random()</code>
     *
     * @param resultSet
     * @return int (Random Room ID)
     * @throws Exception
     */
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

    /**
     * This method will help guest to check whether a specified room type is available or not.<br>
     * If there is no room available, it will return 0.<br>
     * If there is only one room available, it will return 1.<br>
     * If there are many rooms available, it will return 2.
     *
     * @param roomType_ID
     * @param checkInDate
     * @param checkOutDate
     * @return byte key (For multiple Room Check)
     */
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
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                notHasBeenBooked = 1;
            }
            if (resultSet.next()) {
                notHasBeenBooked = 2;
            }
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        } catch (Exception e) {
            if (connection != null) {
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
        return notHasBeenBooked;
    }

    /**
     * It will check whether the guest specified room is available or not.
     *
     * @param roomID
     * @param checkInDate
     * @param checkOutDate
     * @return boolean (The specified room is available or not.)
     */
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

            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) isAvailable = true;
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        } catch (Exception e) {
            if (connection != null) {
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
        return isAvailable;
    }

    /**
     * This method will show guest which room they can choose in the same specified time&type or
     * show all available rooms in the same time interval.
     *
     * @param roomType_ID
     * @param checkInDate
     * @param checkOutDate
     */
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

            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            resultSet = preparedStatement.executeQuery();
            System.out.println("--------*------------------");
            System.out.println("  Room  |      Type");
            while (resultSet.next()) {
                System.out.print("  " + resultSet.getString(1) + "\t|" + resultSet.getString(2));
                System.out.println();
            }
            System.out.println("--------*------------------");
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        } catch (Exception e) {
            if (connection != null) {
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
    }

    /**
     * This method will help guest to check whether a room is available in other time interval.<br>
     * It works for modifying Check-in Date or Check-out Date method.
     * It supports transaction, because other guests might change their check-in or check-out
     * date when different guests operate on the same function block.
     *
     * @param connection
     * @param BookedRoom_ID
     * @param roomID
     * @param checkInDate
     * @param checkOutDate
     * @return boolean (Check whether a room is available)
     * @throws Exception
     */
    private static boolean checkBookedRoom(Connection connection, int BookedRoom_ID, int roomID, LocalDate checkInDate, LocalDate checkOutDate) throws Exception {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        boolean hasBeenBooked = false;
        String sql;

        sql = "SELECT * FROM Room LEFT OUTER JOIN BookedRoom USING (roomID) WHERE roomID = ?" +
                " AND bookedRoom_ID IN(SELECT bookedRoom_ID FROM bookedroom WHERE (checkInDate >= ? AND checkOutDate <= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?)OR(checkInDate <= ? AND checkOutDate >= ?))" +
                " AND bookedRoom_ID <> ?";
        preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, roomID);
        preparedStatement.setDate(2, Date.valueOf(checkInDate));
        setData2(checkOutDate, checkInDate, preparedStatement, Date.valueOf(checkInDate), Date.valueOf(checkOutDate));
        preparedStatement.setDate(9, Date.valueOf(checkOutDate));
        preparedStatement.setInt(10, BookedRoom_ID);

        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            hasBeenBooked = true;
        }

        connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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

    /**
     * Gather the duplicate code block here for setting SQL statement
     * which checks for room whether is available or not.
     *
     * @param checkInDate
     * @param checkOutDate
     * @param preparedStatement
     * @throws Exception
     */
    private static void setData1(LocalDate checkInDate, LocalDate checkOutDate, PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setDate(1, Date.valueOf(checkInDate));
        preparedStatement.setDate(2, Date.valueOf(checkOutDate));
        setData2(checkInDate, checkOutDate, preparedStatement, Date.valueOf(checkInDate), Date.valueOf(checkOutDate));
    }

    /**
     * Gather another duplicate code block here for setting SQL statement
     * which checks whether a room is available or not.
     * It has some preset parameter index in the caller.
     *
     * @param checkInDate
     * @param checkOutDate
     * @param preparedStatement
     * @param date1
     * @param date2
     * @throws Exception
     */
    private static void setData2(LocalDate checkInDate, LocalDate checkOutDate, PreparedStatement preparedStatement, Date date1, Date date2) throws Exception {
        preparedStatement.setDate(3, Date.valueOf(checkInDate));
        preparedStatement.setDate(4, date1);
        preparedStatement.setDate(5, Date.valueOf(checkOutDate));
        preparedStatement.setDate(6, date2);
        preparedStatement.setDate(7, Date.valueOf(checkInDate));
        preparedStatement.setDate(8, Date.valueOf(checkOutDate));
    }

    /**
     * This method would INSERT data INTO the BookedRoom Table after a series of valid data inspection.
     *
     * @param userID
     * @param roomID
     * @param checkInDate
     * @param checkOutDate
     */
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

    /**
     * It will filter out the input data from guest.<br>
     * The user input date might be different from the required format, so
     * this method will help them to check it and avoid crashing when
     * program is running.
     *
     * @param scanner
     * @param input
     * @param isCheckInDate
     * @return LocalDate (A Valid Date)
     */
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

    /**
     * It gathers the duplicate code block of
     * <code>getValidDate(Scanner scanner, String input, boolean isCheckInDate)</code>
     * method and show the "Exception" message to guest.
     *
     * @param scanner
     * @return String (A Date)
     */
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

    /**
     * This method only print out a general layout of this hotel.
     * It could be substituted by GUI layout display approach.
     */
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

    /**
     * Guest has multiple chooses:
     * <pre>
     *     1. Modify Check-in Date
     *     2. Modify Check-out Date
     *     3. Cancel Booked Room
     * </pre>
     * They cannot modify check-in date if current date is their live-in date.<br>
     * They cannot modify check-out date after they leave the hotel.<br>
     * They cannot cancel booked room when they are living in hotel or has already left the hotel.
     *
     * @param userID
     * @return int array (byte key, userID)
     */
    public static int[] modifyRooms(int userID) {
        DB_Utility.printCurrentTime();
        int[] userInfo = {10, userID};
        //If guest wants to leave hotel before or after check-out date, they can modify the check-out date
        //The record cannot be "deleted" after guest live in the hotel
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Modify Check-in Date");
        System.out.println("2. Modify Check-out Date");
        System.out.println("3. Cancel Booked Room");
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

    /**
     * The detail implementation of modify a check-in date,
     * it will also check whether the guest input is valid or not.
     * The input date will also be checked with other same room to prevent time conflict.
     * It supports transaction.
     *
     * @param userID
     * @return int array (byte key, userID)
     */
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
                            System.out.print("Please choose other check-in date and try again: ");
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
                    connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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

    /**
     * The detail implementation of modify a check-out date,
     * it will check whether the guest input date is valid or not.
     * The input date will also be checked with other same room to prevent time conflict.
     * It supports transaction.
     *
     * @param userID
     * @return int array (byte key, userID)
     */
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

                    System.out.println("---------------------------------------------------------------------");
                    System.out.println("Notice: The date regulation is the same as booking a room.");
                    System.out.println("Click [Enter] to set the check-out day same as check-in date.");
                    System.out.println("If you have passed check-in date, press [Enter] will set it as today.");
                    System.out.print("Please type in the new leave date: ");
                    String newLeaveDate = scanner.nextLine().trim().toLowerCase();
                    LocalDate newCheckOutDate = getValidDate(scanner, newLeaveDate, false);
                    if (newCheckOutDate == LocalDate.MIN) {
                        LocalDate check_In_Date = resultSet.getDate("checkInDate").toLocalDate();
                        if (check_In_Date.isAfter(LocalDate.now())) {
                            newCheckOutDate = check_In_Date;
                        } else {
                            newCheckOutDate = LocalDate.now();
                        }
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
                            System.out.print("Please choose other check-out date and try again: ");
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
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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

    /**
     * It will display the booked rooms of a specific person in formatted form.
     *
     * @param resultSet
     * @param orderCodes
     * @param index
     * @throws Exception
     */
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

    /**
     * The detail implementation of cancel booked rooms,
     * it will check whether the guest input "Order Code" is valid or not.
     * It supports transaction.
     *
     * @param userID
     * @return int array (byte key, userID)
     */
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

    /**
     * It will help <code>cancelBookedRoom(int userID)</code> method to
     * check whether the guest input code is exist or not.
     *
     * @param orderCodes
     * @param input
     * @return boolean (has the code or not)
     */
    private static boolean codesContains(int[] orderCodes, int input) {
        if (orderCodes.length == 0) return false;//Empty set cannot have any order code
        for (int eachCode : orderCodes) {
            if (eachCode == input) {
                return true;//The array contains the input order code.
            }
        }
        return false;//Input is not consistent with any order code in the array.
    }

    /**
     * This method will help guest to book meal.
     * The guests could have 20% price discount if they satisfy these requirements:
     * <pre>
     *     1. Book the meal for the day after tomorrow
     *     2. Living in the hotel at that time
     * </pre>
     * This method has adopted GUI display for meal information and chefs information.
     * It could check whether guest input Date&Time is valid or not.
     * It will check whether the specified date is consistent with chefs' workday.
     * Guest can also set the number of each dish as they want to enjoy.
     *
     * @param userID
     * @return int array (byte key, userID)
     */
    public static int[] bookMeal(int userID) {
        DB_Utility.printCurrentTime();
        //No matter ordinary customer or guest of hotel can book meal in the hotel, but guest can get 20% discount.
        //ordinary customer bookedRoom condition will set to false(0)
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
        printWorkdayOfChefs();
        //End
        //Start: Specify meal data and time
        LocalDateTime serveDateTime;
        while (true) {
            System.out.println("(Notice: If you do not set a date, system will set the service date as today.)");
            System.out.println("(If you set the service date as today, you cannot get the 20% discount.)");
            System.out.print("Please type in the service date: ");
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
                    printWorkdayOfChefs();
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

    /**
     * The specific implementation of check chefs' workday.<br>
     * It is feasible to utilise the <code>Calender</code> class, but it is
     * not thread safety and API is not readable than the classes in <code>Time</code> package.
     * In this class, the <code>DayOfWeek</code> was used instead of <code>Calendar</code>
     *
     * @param dishesType_ID
     * @param serveDate
     * @return boolean (Whether the chef work on a specified date)
     */
    private static boolean verifyDayOfWeek(int dishesType_ID, LocalDate serveDate) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        boolean isWorkDay = false;
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
                    isWorkDay = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, statement, resultSet);
        }
        return isWorkDay;
    }

    /**
     * The final step of order a meal.
     * It will check whether the guest is consistent with discount requirements.
     * Afterwards, a discounted total price would
     * be set on their meal if these requirements are satisfied.
     *
     * @param userID
     * @param dishesType_ID
     * @param serveDateTime
     * @param count
     * @param rightDay
     */
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

    /**
     * It will get the price of each dish from database.
     *
     * @param dishesType_ID
     * @return float (Price of each dish)
     */
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

    /**
     * It will check whether guest has booked room on the service date.
     *
     * @param userID
     * @param serveDate
     * @return boolean (Whether guest booked room)
     */
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

    /**
     * This method will help <code>bookMeal(int userID)</code> method to get a valid time
     * of one day. The valid time is the meal available time.
     * It can automatically set a time for guest with consideration of their current time,
     * if they do not want to specify a time.
     * It can also set seconds to 0 if guest has not set a specific second.
     * It will automatically parse some time input, such as
     * <pre>
     *     12:88:67 ----> 13:39:07
     * </pre>
     *
     * @param scanner
     * @param serveTime
     * @return LocalTime (A valid time)
     */
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

    /**
     * It gathers the duplicate part of <code>getValidTime(Scanner scanner, String serveTime)</code> method.
     *
     * @param scanner
     * @return String (A Time)
     */
    private static String validTime(Scanner scanner) {
        System.out.println("============================");
        System.out.print("Please type in a valid time: ");
        return scanner.nextLine().trim();
    }

    /**
     * It will display a chefs' workday list in a float window. (GUI)
     */
    private static void printWorkdayOfChefs() {
        String sql = "SELECT chefName AS 'Chef Name', day_Name AS 'Workday' FROM `schedule` NATURAL JOIN chef NATURAL JOIN OneWeek";
        TablePrinter.display(sql, "The Workday of Chefs");
        System.out.println();
    }

    /**
     * It will display a meal list of different dishes and chefs.
     * For guest input verification, it will return the maximum row number.
     *
     * @return int (Max Row Number)
     */
    private static int printChefAndMeal() {
        String sql = "SELECT dishesType_ID AS 'Row Number',chefName AS 'Chef Name', dishes AS 'Dishes', price AS 'Price per Dish' FROM meal NATURAL JOIN chef";
        int maxRow = TablePrinter.display(sql, "Chefs with Dishes, and Price");
        System.out.println();
        return maxRow;
    }

    /**
     * The guest could cancel their meal order here.<br>
     * The meal could only be cancelled before the service date.
     *
     * @param userID
     * @return int array (byte key, userID)
     */
    public static int[] cancelMeal(int userID) {
        DB_Utility.printCurrentTime();
        int[] userInfo = {10, userID};
        boolean isEmpty = getOrderCodes(userID).length == 0;
        if (isEmpty) {
            System.out.println("====================================================================");
            System.out.println("You should book a meal at first.");
            System.out.println("Notice: You cannot cancel the meal order with service date at today.");
            System.out.println("====================================================================");
        } else {
            cancelMealOrder(userID);
        }
        return userInfo;
    }

    /**
     * The meal order of guest will be displayed on float window. (GUI)<br>
     * The guest could cancel the meal by type in the order code.<br>
     * It supports transaction.
     *
     * @param userID
     */
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
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (Exception throwable) {
                    throwable.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            DB_Utility.close(connection, preparedStatement);
        }
    }

    /**
     * The <code>getOrderCodes(int userID)</code> method has the same function as this method.<br>
     * The <code>getOrderCodes(int userID)</code> method could implement
     * this method by judging whether the length of return int array is 0 or not.
     *
     * @param userID
     * @return boolean (Whether the guest has a meal order)
     */
    @Deprecated
    private static boolean verifyEmpty(int userID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        boolean isEmpty = true;
        try {
            connection = DB_Utility.connect();
            statement = connection.createStatement();
            String sql = "SELECT bookedMeal_ID FROM bookedmeal WHERE userID = " + userID + " AND serveDate > '"
                    + LocalDateTime.of(LocalDate.now(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "'";
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

    /**
     * It will display the meal orders which could cancelled.<br>
     * It will also return the corresponding order codes array at the same time.
     *
     * @param userID
     * @return int array (Order Codes)
     */
    private static int[] printBookedMeal(int userID) {
        String sql = "SELECT bookedMeal_ID AS 'Order Code',chefName AS 'Chef Name',dishes AS 'Dish Name',serveDate AS 'Service Date',count AS 'Total Count',totalPrice AS 'Total Price'" +
                " FROM BookedMeal NATURAL JOIN meal NATURAL JOIN chef" +
                " WHERE userID = " + userID + " AND serveDate > '" + LocalDateTime.of(LocalDate.now(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "'";
        TablePrinter.display(sql, "Your Booked Meal");
        int[] orderCodes = getOrderCodes(userID);
        System.out.println();
        return orderCodes;
    }

    /**
     * Ihe detail implementation of get order codes of booked meal.<br>
     * It cooperates with <code>cancelMealOrder(int userID)</code> method to implement the
     * transaction function.
     *
     * @param userID
     * @return int array (Order Codes of booked meal)
     */
    private static int[] getOrderCodes(int userID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int[] orderCodes = null;
        try {
            connection = DB_Utility.connect();
            String sql = "SELECT bookedMeal_ID FROM BookedMeal WHERE userID = ? AND serveDate > ?";
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, userID);
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
            if (connection != null) {
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

    /**
     * Guest could also update their personal information after login their account.
     *
     * @param userID
     * @return int array (byte key, userID)
     */
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
