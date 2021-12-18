package exceptions.transaction;

public class TransactionNotFoundException extends Exception {

    public TransactionNotFoundException(String message) {
        super(message, null);
    }

    public TransactionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
