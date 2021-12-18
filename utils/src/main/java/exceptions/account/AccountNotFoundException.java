package exceptions.account;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}