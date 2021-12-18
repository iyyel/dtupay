/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 */
package exceptions.token;

public class InvalidTokenException extends Exception {
    public InvalidTokenException(String id) {
        super("Token (" + id + ") is invalid!");
    }
}
