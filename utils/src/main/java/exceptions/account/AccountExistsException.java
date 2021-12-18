package exceptions.account;

public class AccountExistsException extends Exception {

    public AccountExistsException () {
        super();
    }

    public AccountExistsException (String errorMessage) {
        super(errorMessage);
    }

}