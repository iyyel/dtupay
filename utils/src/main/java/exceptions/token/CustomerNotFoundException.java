/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 */
package exceptions;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String id) {
        super("Customer " + id + " was not found.");
    }
}
