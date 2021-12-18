package exceptions.transaction;

/**
 * @author Troels (s161791)
 * TransactionException to use when a transaction fails.
 */
public class TransactionException extends Exception {

    public TransactionException(String message) {
        super(message, null);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

}