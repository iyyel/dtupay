package exceptions.customer;

/**
 * @author Troels (s161791)
 * CustomerNotFoundException to be used when a customer cannot be found.
 */
public class CustomerNotFoundException extends CustomerException {

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}