package exceptions.account;

public class BankAccountException extends Exception {

    public BankAccountException() {
        super();
    }

    public BankAccountException(String errorMessage) {
        super(errorMessage);
    }
    
}
