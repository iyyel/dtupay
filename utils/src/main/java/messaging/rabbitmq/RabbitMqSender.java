package messaging.rabbitmq;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import messaging.Event;
import messaging.EventSender;

public class RabbitMqSender implements EventSender {

	private static final String EXCHANGE_NAME = "eventsExchange";
	private static final String QUEUE_TYPE = "topic";
	private static final String TOPIC = "events";

	@Override
	public void sendEvent(Event event) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("rabbitMq");
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(EXCHANGE_NAME, QUEUE_TYPE);
			String message = new Gson().toJson(event);
			System.out.println("[x] sending "+message);
			channel.basicPublish(EXCHANGE_NAME, TOPIC, null, message.getBytes("UTF-8"));
		}
	}

}