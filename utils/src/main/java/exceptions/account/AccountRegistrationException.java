package exceptions.account;

public class AccountRegistrationException extends Exception {

    public AccountRegistrationException() {
        super();
    }

    public AccountRegistrationException(String errorMessage) {
        super(errorMessage);
    }

}