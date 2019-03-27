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
        String configUpdateStr="{\"path\":\"/services/192.168.8.100/8fb60240-8fc4-4b18-9998-ecadcd5258cd\",\"config\":[{\"ip\":\"192.168.1.100\",\"port\":\"2\"}]}";
       // String cmdUpdateStr="{\"path\":\"/services/192.168.8.100/50a54d38-e9b8-400b-b0cf-0bedb139eb67\",\"cmd\":\"stop\"}";
        RabbitmqProducer producer = null;
        try {
            producer = new RabbitmqProducer(ConfigProperties.getInstance().get("rabbitmq.queue"));
            producer.sendMessage(configUpdateStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
