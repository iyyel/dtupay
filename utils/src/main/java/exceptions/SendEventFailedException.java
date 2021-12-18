package exceptions;

public class SendEventFailedException extends Exception {

    public SendEventFailedException(String message) {
        super(message, null);
    }

    public SendEventFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}