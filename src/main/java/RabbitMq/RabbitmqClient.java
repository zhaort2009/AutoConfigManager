package RabbitMq;

import Utility.ConfigProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public abstract class RabbitmqClient{

    protected Channel channel;
    protected Connection connection;
    protected String endPointName;

    public RabbitmqClient(String endpointName) throws IOException {
        this.endPointName = endpointName;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(ConfigProperties.getInstance().get("rabbitmq.ip"));
        factory.setPort(Integer.parseInt(ConfigProperties.getInstance().get("rabbitmq.port")));
        factory.setUsername(ConfigProperties.getInstance().get("rabbitmq.userName"));
        factory.setPassword(ConfigProperties.getInstance().get("rabbitmq.password"));
        try{
            connection = factory.newConnection();
        }catch (TimeoutException ex) {
            System.out.println(ex);
            connection = null;
        }
        channel = connection.createChannel();
        channel.queueDeclare(endpointName, false, false, false, null);
    }



    public void close() throws IOException{
        try{
            this.channel.close();
        } catch (TimeoutException ex){
            System.out.println("ex" + ex);
        }
        this.connection.close();
    }
}