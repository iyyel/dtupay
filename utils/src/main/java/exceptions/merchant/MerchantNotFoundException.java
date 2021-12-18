package exceptions.merchant;

/**
 * @author Troels (s161791)
 * MerchantNotFoundException to be used when a merchant cannot be found.
 */
public class MerchantNotFoundException extends MerchantException {

    public MerchantNotFoundException(String message) {
        super(message);
    }

    public MerchantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}