package utils;

import java.util.Scanner;

public class Staff {

    private Staff() {
    }

    public static byte startInterface() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Staff Login");
        System.out.println("2. Staff Sign Up");
        System.out.println("3. Back to the previous options");
        System.out.println("4. quit the system");
        System.out.println();
        System.out.println("(Now in: Staff Mode)");
        System.out.print("Please type in the corresponding number to choose your option: ");
        String input = scanner.nextLine().trim();
        while (true) {
            if (input.equals("1")) {
                System.out.println();
                return 6;
            } else if (input.equals("2")) {
                System.out.println();
                return 7;
            } else if (input.equals("3")) {
                System.out.println();
                return 8;
            } else if (input.equals("4")) {
                System.out.println();
                System.out.println("Bye");
                return -1;
            } else {
                System.out.println();
                System.out.println("(Now in: Staff Mode)");
                System.out.print("Please type in a valid number(1 for Login, 2 for Register, 3 for back, 4 for quit): ");
                input = scanner.nextLine().trim();
            }
        }
    }

    public static byte login() throws Exception {
        return 0;
    }

    public static byte register() throws Exception {
        return 0;
    }
}
