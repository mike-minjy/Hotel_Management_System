package utils;

import java.util.Scanner;

public class Staff {

    private Staff() {
    }

    public static byte startInterface() {
        DB_Utility.printCurrentTime();
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Staff Login");
        System.out.println("2. Back to previous options");
        System.out.println("3. quit the system");
        System.out.println();
        System.out.println("(Now in: Staff Mode)");
        System.out.print("Please type in the corresponding number to choose your option: ");
        String input = scanner.nextLine().trim().toLowerCase();
        while (true) {
            switch (input) {
                case "1":
                case "staff login":
                    System.out.println();
                    return 6;
                case "2":
                case "back to the previous options":
                    return 7;
                case "3":
                case "quit the system":
                    return -1;
                default:
                    System.out.println("===================================================================================");
                    System.out.println("(Now in: Staff Mode)");
                    System.out.print("Please type in a valid number(1 for Login, 2 for Register, 3 for back, 4 for quit): ");
                    input = scanner.nextLine().trim().toLowerCase();
                    break;
            }
        }
    }

    public static byte login() throws Exception {
        return 0;
    }

}
