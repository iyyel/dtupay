/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 *
 *  RabbitMQ sender based on code given by Hubert
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

public class TokenSender implements EventSender {

    private static final String EXCHANGE_NAME = "message-hub";
    private static final String QUEUE_TYPE = "topic";
    private static final String TOPIC = "dtupay.*";

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
        System.out.println("[x] sending " + message);
        channel.basicPublish(EXCHANGE_NAME, TOPIC, null, message.getBytes(StandardCharsets.UTF_8));
    }

}
