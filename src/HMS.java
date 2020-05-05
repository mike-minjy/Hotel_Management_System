import utils.DB_Utility;
import utils.Guest;
import utils.Staff;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class HMS {
    private HMS() {
    }

    public static void main(String[] args) {

        try {
//            connection = DB_Connection.connect();

            System.out.println("                                 *-------------------------------------*");
            System.out.println("                                 *     Welcome to use this system!     *");
            System.out.println("                                 *-------------------------------------*");

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
        int loginPerson;
        while (true) {
            if (step == -1) {
                System.out.println();
                break;
            }

            if (step == 0) {
                step = welcome();
            }

            //Dispose the information of the first step
            if (step == 1) {//--------------------------------Guest Mode selected
                System.out.println();
                step = Guest.startInterface();
            } else if (step == 2) {//-------------------------Staff Mode selected
                System.out.println();
                step = Staff.startInterface();
            }

            //Dispose the information of the second step
            //Feedback from Guest Mode
            if (step == 3) {//--------------------------------Guest Login selected
                System.out.println();
                int[] userInformation = Guest.login();
                step = (byte) userInformation[0];
                loginPerson = userInformation[1];//Get the login person
            } else if (step == 4) {//-------------------------Guest Sign Up selected
                System.out.println();
                step = Guest.register();
            } else if (step == 5) {//-------------------------Update Personal Details selected
                System.out.println();
                step = Guest.update();
            } else if (step == 6) {//-------------------------Go back to previous page
                System.out.println();
                step = 0;
            }

            //Feedback from Staff Mode
            if (step == 6) {//--------------------------------Staff Login selected
                step = Staff.login();
            } else if (step == 7) {//-------------------------Go back to previous page
                System.out.println();
                step = 0;
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
