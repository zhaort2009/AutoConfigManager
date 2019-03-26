package ZkClient;

import Utility.ConfigProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.KeeperException;

public abstract class ZkClient {

    protected final String connectStr;
    protected CuratorFramework client = null;

    protected final String ROOTPATH = "/services";

    public ZkClient() {
        connectStr = ConfigProperties.getInstance().get("zk.connectStr");
        client = CuratorFrameworkFactory.newClient(connectStr, new ExponentialBackoffRetry(1000, 3));
        client.start();
    }

    public void createAndSetData(String path, byte[] data) {
        if (!isExist(path)) {
            try {
                client.create().forPath(path, data);
            } catch (KeeperException.NoNodeException e) {
                try {
                    client.create().creatingParentContainersIfNeeded().forPath(path, data);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                client.setData().forPath(path, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isExist(String path) {
        boolean isExists = false;
        try {
            if (client.checkExists().forPath(path) != null) {
                isExists = true;
            }
        } catch (Exception e) {

        }

        return isExists;

    }

    public void deleteNode(String path) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        CloseableUtils.closeQuietly(client);
    }

}
