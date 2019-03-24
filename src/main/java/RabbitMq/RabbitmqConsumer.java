package RabbitMq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;


public class RabbitmqConsumer extends RabbitmqClient implements Runnable, Consumer {



    public RabbitmqConsumer(String endPointName) throws IOException {
        super(endPointName);
    }

    public void run() {
        try {
            //start consuming messages. Auto acknowledge messages.
            channel.basicConsume(endPointName, true,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the consumer is registered by a call to any of the Channel.basicConsume methods.
     * Parameters:
     * consumerTag the consumer tag associated with the consumer
     */
    public void handleConsumeOk(String consumerTag) {

    }

    /**
     * Called when new message is available.
     */
    public void handleDelivery(String consumerTag, Envelope env,
                               AMQP.BasicProperties props, byte[] body) throws IOException {

        System.out.println(new String(body));
    }

    /**
     * Called when the consumer is cancelled for reasons other than by a call to Channel.basicCancel
     */
    public void handleCancel(String consumerTag) {}

    /**
     * Called when the consumer is cancelled by a call to Channel.basicCancel.
     */
    public void handleCancelOk(String consumerTag) {}

    /**
     * Called when a basic.recover-ok is received in reply to a basic.
     */
    public void handleRecoverOk(String consumerTag) {}

    /**
     * Called when a basic.recover-ok is received in reply to a basic.
     */
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {}
}
