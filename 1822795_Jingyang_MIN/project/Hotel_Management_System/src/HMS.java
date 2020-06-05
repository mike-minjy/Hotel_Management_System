import utils.*;

import java.util.Scanner;

/**
 * The program entrance class (Main class)
 */
public class HMS {

    /**
     * Prevent create an object of this class from outside this package.<br>
     * The function of this Hotel Management System is capable to be extended in the future.<br>
     * Due to the consideration of concurrency, it might be able to create object in future.
     */
    private HMS(){

    }

    /**
     * The welcome panel for user to engage in the system.<br>
     * It is an entrance of the whole program.
     *
     * @param args
     */
    public static void main(String[] args) {

        try {

            System.out.println("                                 *-------------------------------------*");
            System.out.println("                                 *     Welcome to use this system!     *");
            System.out.println("                                 *-------------------------------------*");

            processing();//The main processing branch for the entire program.

            System.out.println("Bye");//It shows at the end of the program.

        } catch (Exception e) {
            e.printStackTrace();
//            if (e.getMessage() != null) {
//                System.err.println(e.getMessage());
//            }
        } finally {
            System.exit(0);//If there are float window here, it will force it to close.
        }
    }

    /**
     * All function of this system processed in this method.
     *
     * @throws Exception
     */
    private static void processing() throws Exception {
        byte step = welcome();//First, show the initial welcome panel.
        int[] userInformation;//An int array to store two parameters: 1. Which branch at next step  2. ID of staff or guest.
        int ID = 0;//Store the log in ID for next step processing.
        while (true) {
            switch (step) {
                case -1://Choose to quit the whole program
                    System.out.println();
                    return;
                case 0://Back to the initial page
                    step = welcome();//Hold the enter code of next step
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
                    ID = userInformation[1];//Get the login guest
                    break;
                case 4://--------------------------------------Guest Sign Up selected
                    System.out.println();
                    step = Guest.register();
                    break;
                case 5://--------------------------------------Update Personal Details selected
                    System.out.println();
                    step = Guest.update();
                    break;
                case 6://--------------------------------------Go back to the welcome page
                case 9://--------------------------------------Go back to the welcome page
                    System.out.println();
                    step = 0;
                    break;
                case 7://--------------------------------------Staff Login selected
                    System.out.println();
                    userInformation = Staff.login();
                    step = (byte) userInformation[0];
                    ID = userInformation[1];//Get the login staff
                    break;
                case 8://--------------------------------------Staff password change selected
                    System.out.println();
                    step = Staff.changePassword();
                    break;
                //Dispose the information of the third step from guest
                case 10://--------------------------------------Guest Login successful
                    System.out.println();
                    userInformation = Booking.startInterface(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 11://--------------------------------------Guest Book Room selected
                    System.out.println();
                    userInformation = Booking.bookRooms(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 12://--------------------------------------Guest Modify Room selected
                    System.out.println();
                    userInformation = Booking.modifyRooms(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 13://--------------------------------------Guest Book Meal selected
                    System.out.println();
                    userInformation = Booking.bookMeal(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 14://--------------------------------------Guest Cancel Meal selected
                    System.out.println();
                    userInformation = Booking.cancelMeal(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 15://--------------------------------------Guest update personal information inside
                    System.out.println();
                    userInformation = Booking.update(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                //Dispose the information of the third step from staff
                case 16://--------------------------------------Staff Login successful
                    System.out.println();
                    userInformation = StaffOperation.startInterface(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 17://--------------------------------------Staff Check Booked Rooms
                    System.out.println();
                    userInformation = StaffOperation.roomManagement(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 18://--------------------------------------Staff Check Booked Meal
                    System.out.println();
                    userInformation = StaffOperation.mealManagement(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 19://--------------------------------------Staff change password inside
                    System.out.println();
                    userInformation = StaffOperation.changePassword(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                //Dispose the information of the fourth step from guest
                case 20://--------------------------------------Guest change Check-In Date selected
                    System.out.println();
                    userInformation = Booking.modifyLiveInDate(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 21://--------------------------------------Guest change Check-Out Date selected
                    System.out.println();
                    userInformation = Booking.modifyLeaveDate(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
                case 22://--------------------------------------Guest Cancel Booked Room
                    System.out.println();
                    userInformation = Booking.cancelBookedRoom(ID);
                    step = (byte) userInformation[0];
                    ID = userInformation[1];
                    break;
            }
        }
    }

    /**
     * The initial page of control panel in this program.
     *
     * @return byte key
     */
    private static byte welcome() {
        DB_Utility.printCurrentTime();
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Guest Mode   (Type in \"1\" or full name of mode to select this mode)");
        System.out.println("2. Staff Mode   (Type in \"2\" or full name of mode to select this mode)");
        System.out.println("3. quit the system (Exit)");
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
                case "exit":
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
}
