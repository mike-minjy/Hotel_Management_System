import utils.Guest;
import utils.Staff;

import java.util.Scanner;

public class HMS {

    public static void main(String[] args) {

        try {
//            connection = DB_Connection.connect();

            System.out.println("                                         *-------------------------------------*");
            System.out.println("                                         *     Welcome to use this system!     *");
            System.out.println("                                         *-------------------------------------*");

            processing();

        } catch (Exception e) {
            e.printStackTrace();
//            if (e.getMessage() != null) {
//                System.err.println(e.getMessage());
//            }
        }
    }

    private static void processing() throws Exception {
        byte step = welcome();
        while (true) {
            if (step == -1) {
                break;
            }

            if (step == 0) {
                step = welcome();
            }

            //Dispose the information of the first step
            if (step == 1) {//--------------------------------Guest Mode selected
                step = Guest.startInterface();
            } else if (step == 2) {//-------------------------Staff Mode selected
                step = Staff.startInterface();
            }

            //Dispose the information of the second step
            //Feedback from Guest Mode
            if (step == 3) {//--------------------------------Guest Login selected
                step = Guest.login();
            } else if (step == 4) {//-------------------------Guest Sign Up selected
                step = Guest.register();
            } else if (step == 5) {
                step = 0;
            }
            //Feedback from Staff Mode
            if (step == 6) {//--------------------------------Staff Login selected
                step = Staff.login();
            } else if (step == 7) {//-------------------------Staff Sign Up selected
                step = Staff.register();
            } else if (step == 8) {
                step = 0;
            }
        }
    }

    private static byte welcome() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Guest Mode   (Type in \"1\" to select this mode)");
        System.out.println("2. Staff Mode   ((Type in \"2\" to select this mode))");
        System.out.println("3. quit the system");
        System.out.print("Please type in the corresponding number of different modes to select your identity: ");
        String input = scanner.nextLine().trim();
        while (true) {
            if (input.equals("1")) {
                System.out.println();
                return 1;                   //Selected Guest Mode, return 1.
            } else if (input.equals("2")) {
                System.out.println();
                return 2;                   //Selected Staff Mode, return 2.
            } else if (input.equals("3")) {
                System.out.println();
                System.out.println("Bye");
                return -1;
            } else {
                System.out.print("Please type in a valid number(1 for Guest, 2 for Staff, 3 for quit): ");
                input = scanner.nextLine().trim();
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
