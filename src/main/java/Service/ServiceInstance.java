package Service;

import Utility.Constant;

import java.net.InetAddress;
import java.util.Collection;
import java.util.UUID;

public class ServiceInstance {
    private final String        name;
    private final String        id;
    private final String        address;
    private final Integer       port;
    private final long          registrationTimeUTC;


    private final String  startPath;
    private final String checkPath;
    private final String stopPath;
    private final String configPath;
    private final Constant.ConfigType configType;


    public static ServiceInstanceBuilder builder() throws Exception
    {
        String                  address = null;
        Collection<InetAddress> ips = ServiceInstanceBuilder.getAllLocalIPs();
        if ( ips.size() > 0 )
        {
            address = ips.iterator().next().getHostAddress();   // default to the first address
        }

        String                  id = UUID.randomUUID().toString();

        return new ServiceInstanceBuilder().address(address).id(id).registrationTimeUTC(System.currentTimeMillis());
    }

    public ServiceInstance(String name, String id, String address, Integer port, long registrationTimeUTC, String startPath, String checkPath, String stopPath, String configPath, Constant.ConfigType configType) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.port = port;
        this.registrationTimeUTC = registrationTimeUTC;
        this.startPath = startPath;
        this.checkPath = checkPath;
        this.stopPath = stopPath;
        this.configPath = configPath;
        this.configType = configType;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
    }

    public long getRegistrationTimeUTC() {
        return registrationTimeUTC;
    }

    public String getStartPath() {
        return startPath;
    }

    public String getCheckPath() {
        return checkPath;
    }

    public String getStopPath() {
        return stopPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public Constant.ConfigType getConfigType() {
        return configType;
    }
}
