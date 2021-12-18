/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 */
package exceptions;

public class TokenNotFoundException extends Exception {
    public TokenNotFoundException(String id) {
        super("Token " + id + " can not be found.");
    }
}