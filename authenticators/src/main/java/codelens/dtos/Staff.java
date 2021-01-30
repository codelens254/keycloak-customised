package codelens.dtos;

public class Staff {

    private String email;
    private String staffNumber;

    public Staff(String email, String staffNumber) {
        this.email = email;
        this.staffNumber = staffNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }
}
