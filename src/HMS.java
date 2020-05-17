import utils.*;

import java.util.Scanner;

public class HMS {
//    private HMS() {
//    }

    public static void main(String[] args) {

        try {

            System.out.println("                                 *-------------------------------------*");
            System.out.println("                                 *     Welcome to use this system!     *");
            System.out.println("                                 *-------------------------------------*");

//            System.out.println("-----------------------------------------");
//            System.out.println("System needs a few seconds to initialise.");
//            System.out.println("-----------------------------------------");
//            System.out.println();
//
//            This method will backup the overdue booked room information to another table.
//            It will also get the current bookedRoom information from future_room_info table,
//            and put the future booked room information to future_room_info table.
//            Booking.backupInfo();
            processing();

            System.out.println("Bye");

        } catch (Exception e) {
            e.printStackTrace();
//            if (e.getMessage() != null) {
//                System.err.println(e.getMessage());
//            }
        }
    }

    private static void processing() throws Exception {
        byte step = welcome();
        int[] userInformation;
        int ID = 0;
        while (true) {
            switch (step) {
                case -1:
                    System.out.println();
                    return;
                case 0:
                    step = welcome();
                    break;
                //Dispose the information of the first step
                case 1://--------------------------------------Guest Mode selected
                    System.out.println();
                    step = Guest.startInterface();
                    break;
                case 2://--------------------------------------Staff Mode selected
                    System.out.println();
                    step = Staff.startInterface();
                    break;
                //Dispose the information of the second step
                //Feedback from Guest Mode
                case 3://--------------------------------------Guest Login selected
                    System.out.println();
                    userInformation = Guest.login();
                    step = (byte) userInformation[0];
                    ID = userInformation[1];//Get the login person
                    break;
                case 4://--------------------------------------Guest Sign Up selected
                    System.out.println();
                    step = Guest.register();
                    break;
                case 5://--------------------------------------Update Personal Details selected
                    System.out.println();
                    step = Guest.update();
                    break;
                case 6://--------------------------------------Go back to previous page
                case 9://--------------------------------------Go back to previous page
                    System.out.println();
                    step = 0;
                    break;
                case 7://--------------------------------------Staff Login selected
                    System.out.println();
                    userInformation = Staff.login();
                    step = (byte) userInformation[0];
                    ID = userInformation[1];//Get the login person
                    break;
                case 8://--------------------------------------Staff password change selected
                    System.out.println();
                    step = Staff.changePassword();
                    break;
                //Dispose the information of the third step from guest
                case 10:
                    System.out.println();
                    userInformation = Booking.startInterface(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 11:
                    System.out.println();
                    userInformation = Booking.bookRooms(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 12:
                    System.out.println();
                    userInformation = Booking.modifyRooms(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 13:
                    System.out.println();
                    userInformation = Booking.bookMeal(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 14:
                    System.out.println();
                    userInformation = Booking.cancelMeal(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 15:
                    System.out.println();
                    userInformation = Booking.update(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                //Dispose the information of the third step from staff
                case 16:
                    System.out.println();
                    userInformation = StaffOperation.startInterface(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 17:
                    System.out.println();
                    userInformation = StaffOperation.roomManagement(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 18:
                    System.out.println();
                    userInformation = StaffOperation.changePassword(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
//                case 19:
//                    System.out.println();
//                    userInformation = StaffOperation.addBlackList(ID);
//                    step = (byte) userInformation[0];
//                    ID = userInformation[1];
//                    break;
                case 20:
                    System.out.println();
                    userInformation = Booking.modifyLiveInDate(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 21:
                    System.out.println();
                    userInformation = Booking.modifyLeaveDate(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 22:
                    System.out.println();
                    userInformation = Booking.cancelBookedRoom(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
            }
        }
    }

    private static byte welcome() {
        DB_Utility.printCurrentTime();
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Guest Mode   (Type in \"1\" or full name of mode to select this mode)");
        System.out.println("2. Staff Mode   (Type in \"2\" or full name of mode to select this mode)");
        System.out.println("3. quit the system");
        System.out.print("Please type in the corresponding number of different modes to select your identity: ");
        String input = scanner.nextLine().trim().toLowerCase();
        while (true) {
            switch (input) {
                case "1":
                case "guest mode":
                    return 1;                   //Selected Guest Mode, return 1.
                case "2":
                case "staff mode":
                    return 2;                   //Selected Staff Mode, return 2.
                case "3":
                case "quit the system":
                    return -1;
                default:
                    System.out.println();
                    System.out.println("====================================================================");
                    System.out.print("Please type in a valid number(1 for Guest, 2 for Staff, 3 for quit): ");
                    input = scanner.nextLine().trim().toLowerCase();
                    break;
            }
        }
    }

//    private static void quit() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Type in \"quit\" or \"exit\" to leave the system.");
//        String input = scanner.nextLine().trim().toLowerCase();
//        if (input.equals("quit") || input.equals("exit") || input.equals("leave")) {
//            System.out.println("Bye");
//            System.exit(0);
//        }
//    }
}
