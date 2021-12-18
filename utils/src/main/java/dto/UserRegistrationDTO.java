package dto;

import java.io.Serializable;

public class UserRegistrationDTO implements Serializable {

    private String firstName;
    private String lastName;
    private String cprNumber;
    private BankRegistrationDTO bankAccount;

    public UserRegistrationDTO() {

    }

    public UserRegistrationDTO(String firstName, String lastName,
                               String cprNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cprNumber = cprNumber;
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

    public String getCprNumber() {
        return cprNumber;
    }

    public void setCprNumber(String cprNumber) {
        this.cprNumber = cprNumber;
    }

    public BankRegistrationDTO getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankRegistrationDTO bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", cprNumber='" + cprNumber + '\'' +
                ", bankAccount=" + bankAccount +
                '}';
    }

}
