package Service;

import Utility.Constant;
import com.google.common.collect.Lists;
import org.apache.curator.x.discovery.LocalIpFilter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ServiceInstanceBuilder {


    private String name;
    private String id;
    private String address;
    private Integer port;
    private long registrationTimeUTC;

    private  String  startPath;
    private  String checkPath;
    private  String stopPath;
    private  String configPath;
    private Constant.ConfigType configType;


    private static final AtomicReference<LocalIpFilter> localIpFilter = new AtomicReference<LocalIpFilter>
            (
                    new LocalIpFilter()
                    {

                        public boolean use(NetworkInterface nif, InetAddress adr) throws SocketException
                        {
                            return (adr != null) && !adr.isLoopbackAddress() && (nif.isPointToPoint() || !adr.isLinkLocalAddress());
                        }
                    }
            );

    /**
     * Replace the default local ip filter used by {@link #getAllLocalIPs()}
     *
     * @param newLocalIpFilter the new local ip filter
     */
    public static void setLocalIpFilter(LocalIpFilter newLocalIpFilter)
    {
        localIpFilter.set(newLocalIpFilter);
    }

    /**
     * Return the current local ip filter used by {@link #getAllLocalIPs()}
     *
     * @return ip filter
     */
    public static LocalIpFilter getLocalIpFilter()
    {
        return localIpFilter.get();
    }

    ServiceInstanceBuilder()
    {
    }

    /**
     * Return a new instance with the currently set values
     *
     * @return instance
     */
    public ServiceInstance build()
    {
        return new ServiceInstance(name, id, address, port, registrationTimeUTC, startPath,checkPath, stopPath, configPath, configType);
    }

    public ServiceInstanceBuilder name(String name)
    {
        this.name = name;
        return this;
    }

    public ServiceInstanceBuilder address(String address)
    {
        this.address = address;
        return this;
    }

    public ServiceInstanceBuilder id(String id)
    {
        this.id = id;
        return this;
    }

    public ServiceInstanceBuilder port(int port)
    {
        this.port = port;
        return this;
    }





    public ServiceInstanceBuilder registrationTimeUTC(long registrationTimeUTC)
    {
        this.registrationTimeUTC = registrationTimeUTC;
        return this;
    }


    public ServiceInstanceBuilder startPath(String startPath)
    {
        this.startPath = startPath;
        return this;
    }
    public ServiceInstanceBuilder checkPath(String checkPath)
    {
        this.checkPath = checkPath;
        return this;
    }

    public ServiceInstanceBuilder stopPath(String stopPath)
    {
        this.stopPath = stopPath;
        return this;
    }

    public ServiceInstanceBuilder configPath(String configPath)
    {
        this.configPath = configPath;
        return this;
    }

    public ServiceInstanceBuilder configType(Constant.ConfigType configType)
    {
        this.configType = configType;
        return this;
    }

    /**
     * based on http://pastebin.com/5X073pUc
     * <p>
     *
     * Returns all available IP addresses.
     * <p>
     * In error case or if no network connection is established, we return
     * an empty list here.
     * <p>
     * Loopback addresses are excluded - so 127.0.0.1 will not be never
     * returned.
     * <p>
     * The "primary" IP might not be the first one in the returned list.
     *
     * @return  Returns all IP addresses (can be an empty list in error case
     *          or if network connection is missing).
     * @since   0.1.0
     * @throws SocketException errors
     */
    public static Collection<InetAddress> getAllLocalIPs() throws SocketException
    {
        List<InetAddress> listAdr = Lists.newArrayList();
        Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
        if (nifs == null) return listAdr;

        while (nifs.hasMoreElements())
        {
            NetworkInterface nif = nifs.nextElement();
            // We ignore subinterfaces - as not yet needed.

            Enumeration<InetAddress> adrs = nif.getInetAddresses();
            while ( adrs.hasMoreElements() )
            {
                InetAddress adr = adrs.nextElement();
                if ( localIpFilter.get().use(nif, adr) )
                {
                    listAdr.add(adr);
                }
            }
        }
        return listAdr;
    }
}
