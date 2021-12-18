/**
 * @primary-author Tobias (s173899)
 * @co-author Emil (s174265)
 *
 * RabbitMQ sender based on code given by Hubert
 */

package interfaces.rabbitmq;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import infrastructure.ConfigService;
import infrastructure.IConfigService;
import messaging.Event;
import messaging.EventSender;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportSender implements EventSender {

	private static final String EXCHANGE_NAME = "message-hub";
	private static final String QUEUE_TYPE = "topic";
	private static final String TOPIC = "dtupay.*";

	private final static Logger LOGGER = Logger.getLogger(ReportSender.class.getName());

	@Override
	public void sendEvent(Event event) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		IConfigService config = new ConfigService();
		factory.setHost(config.getProp("rabbitmq.host"));
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(EXCHANGE_NAME, QUEUE_TYPE);
			String message = new Gson().toJson(event);
			LOGGER.log(Level.INFO, "RABBITMQ: Sending message: " + message);
			channel.basicPublish(EXCHANGE_NAME, TOPIC, null, message.getBytes(StandardCharsets.UTF_8));
		}
	}


	/*
	private EventReceiver service;
	private Channel channel;

	public void initConnection() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		IConfigService config = new ConfigService();
		factory.setHost(config.getProp("rabbitmq.host"));
		Connection connection = factory.newConnection();
		channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, QUEUE_TYPE);
	}

	@Override
	public void sendEvent(Event event) throws Exception {
		String message = new Gson().toJson(event);
		System.out.println("[x] sending "+message);
		channel.basicPublish(EXCHANGE_NAME, TOPIC, null, message.getBytes(StandardCharsets.UTF_8));
	}

	 */
}
