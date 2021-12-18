package exceptions.customer;

/**
 * @author Troels (s161791)
 * CustomerException to be used for customer related errors.
 */
public class CustomerException extends Exception {

    public CustomerException(String message) {
        super(message, null);
    }

    public CustomerException(String message, Throwable cause) {
        super(message, cause);
    }

}