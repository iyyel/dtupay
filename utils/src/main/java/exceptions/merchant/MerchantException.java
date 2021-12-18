package exceptions.merchant;

/**
 * @author Troels (s161791)
 * MerchantException to be used when a merchant cannot be found.
 */
public class MerchantException extends Exception {

    public MerchantException(String message) {
        super(message, null);
    }

    public MerchantException(String message, Throwable cause) {
        super(message, cause);
    }

}