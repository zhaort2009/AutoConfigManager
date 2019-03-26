package RabbitMq;

import Utility.ConfigProperties;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;

public class RabbitmqProducer extends  RabbitmqClient{


    public RabbitmqProducer(String endpointName) throws IOException {
        super(endpointName);
    }

    public void sendMessage(String str) throws IOException {
        channel.basicPublish("",endPointName, null,str.getBytes("UTF-8") );
    }


    public static void main(String []args){
        String configUpdateStr="{\"path\":\"/services/ip/UUID\",\"config\":[{\"ip\":\"1\",\"port\":\"2\"}]}";
        RabbitmqProducer producer = null;
        try {
            producer = new RabbitmqProducer(ConfigProperties.getInstance().get("rabbitmq.queue"));
            producer.sendMessage(configUpdateStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
