package RabbitMq;

import ZkClient.ZkClientManage;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import org.apache.zookeeper.server.admin.JsonOutputter;

import java.io.IOException;


public class RabbitmqConsumer extends RabbitmqClient implements Runnable, Consumer {
    private ZkClientManage zkClientManage;


    public RabbitmqConsumer(String endPointName, ZkClientManage zkClientManage) throws IOException {
        super(endPointName);
        zkClientManage = zkClientManage;
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

        String msg = new String(body,"UTF-8");
        //json:{"uuid":"1","cmd":"stop","config":[{"ip":"1","port":"2"}]}
        zkClientManage.writeToCmd();
        System.out.println(msg);
        Thread t = Thread.currentThread();
        System.out.println(t.getName());
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

    public String getCmd(String msg){
        Object obj =JSONObject.parse(msg);

    }
    public String getConfig(){}

}
