package messaging.rabbitmq;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import messaging.Event;
import messaging.EventReceiver;

public class RabbitMqListener {

	private static final String EXCHANGE_NAME = "eventsExchange";
	private static final String QUEUE_TYPE = "topic";
	private static final String TOPIC = "events";

	EventReceiver service;

	public RabbitMqListener(EventReceiver service) {
		this.service = service;
	}

	public void listen() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("rabbitMq");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, QUEUE_TYPE);
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, TOPIC);

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println("[x] receiving "+message);

			Event event = new Gson().fromJson(message, Event.class);
			try {
				service.receiveEvent(event);
			} catch (Exception e) {
				throw new Error(e);
			}
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}
}
