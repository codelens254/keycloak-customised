package codelens.utils;

import codelens.dtos.Staff;

import java.util.ArrayList;
import java.util.List;

public class StaffNumberHelper {

    public static List<Staff> getStaffs() {
        List<Staff> staffList = new ArrayList<>();
        staffList.add(new Staff("someone@gmail.com", "34567"));
        staffList.add(new Staff("eric@gmail.com", "98765"));
        staffList.add(new Staff("codelens@gmail.com", "412900"));

        return staffList;
    }
}
