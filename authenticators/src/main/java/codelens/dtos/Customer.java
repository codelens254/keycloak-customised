package codelens.dtos;

import java.util.List;

public class Customer {

    private String firstName;
    private String lastName;
    private String email;
    private List<Device> devices;

    public Customer(String firstName, String lastName, String email, List<Device> devices) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.devices = devices;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}
