package RabbitMq;

import Utility.Constant;
import ZkClient.ZkClientManage;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import org.apache.zookeeper.server.admin.JsonOutputter;

import java.io.IOException;
import java.util.Set;


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
        //json:{"path":"/services/ip/UUID","cmd":"stop","config":[{"ip":"1","port":"2"}]}
        if(getCmd(msg)!=null){zkClientManage.writeToCmd(getPath(msg),getCmd(msg));}
        if(getConfig(msg)!=null){zkClientManage.writeToConfigUpdate(getPath(msg),getConfig(msg));}

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


    public String getPath(String msg){
        JSONObject obj =JSONObject.parseObject(msg);
        Set<String> keySet = obj.keySet();
        if (keySet.contains(Constant.MsgJsonKey.PATHKEY)){
            return (String)obj.get(Constant.MsgJsonKey.PATHKEY);
        }
        return null;

    }
    public String getCmd(String msg){
        JSONObject obj =JSONObject.parseObject(msg);
        Set<String> keySet = obj.keySet();
        if (keySet.contains(Constant.MsgJsonKey.CMDKEY)){
            return (String)obj.get(Constant.MsgJsonKey.CMDKEY);
        }
        return null;

    }
    public String getConfig(String msg){
        JSONObject obj =JSONObject.parseObject(msg);
        Set<String> keySet = obj.keySet();
        if (keySet.contains(Constant.MsgJsonKey.CONFIGKEY)){
            return (String)obj.get(Constant.MsgJsonKey.CONFIGKEY);
        }
        return null;
    }

}
