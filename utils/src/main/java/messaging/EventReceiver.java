package messaging;

public interface EventReceiver {
	void receiveEvent(Event event) throws Exception;
}
