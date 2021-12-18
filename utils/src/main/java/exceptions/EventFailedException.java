package exceptions;

public class EventFailedException extends Exception {

    public EventFailedException(String message) {
        super(message, null);
    }

    public EventFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}