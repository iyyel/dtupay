/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 */
package exceptions;

public class CustomerAlreadyRegisteredException extends Exception {
    public CustomerAlreadyRegisteredException(String customerId) {
        super("Customer " + customerId + " is already registered.");
    }
}