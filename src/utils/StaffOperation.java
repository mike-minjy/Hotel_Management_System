package utils;

public class StaffOperation {
    private StaffOperation() {
    }

    public static byte startInterface(int account) {
        return 2;
    }

    private static byte startInterface(String username) {
        return 2;
    }

    public static int[] roomManagement(int account) {
        int[] staffInfo = {-2/*Unknown parameter*/, account};
        return staffInfo;
    }

    //Maybe I will develop a function of adding blacklist
//    public static int[] addBlackList(int account){
//        int[] staffInfo = {-2/*Unknown parameter*/, account};
//        return staffInfo;
//    }
}
