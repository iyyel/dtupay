/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 */
package exceptions;

public class TooManyTokensException extends Exception {
    public TooManyTokensException(String customerId, int amount) {
        super("Customer " + customerId + " cannot request " + amount + " tokens"); // TODO: Make specific errors
    }
}
