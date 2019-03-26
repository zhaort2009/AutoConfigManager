package ZkClient;

import RabbitMq.RabbitmqConsumer;
import Service.ServiceInstance;
import Utility.ConfigProperties;
import Utility.Constant;
import Utility.JDBCUtil;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.utils.ZKPaths;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZkClientManage extends ZkClient {

    public ZkClientManage() {
        super();
    }


    public void start(){
        createRoot();
        setListener();
        startReceiveMq();

    }

    private void createRoot() {
        createAndSetData(ROOTPATH, null);
    }

    private void setListener(){
        TreeCache cache = new TreeCache(client,ROOTPATH);
        TreeCacheListener listener = new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case NODE_ADDED: {
                        if(event.getData().getData()==null){return;}
                        System.out.println("TreeNode added: " + ZKPaths.getNodeFromPath(event.getData().getPath()) + ", value: "
                                + new String(event.getData().getData()));
                        break;
                    }
                    case NODE_UPDATED: {
                        if(event.getData().getData()==null){return;}
                        System.out.println("TreeNode changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()) + ", value: "
                                + new String(event.getData().getData()));
                        break;
                    }
                    case NODE_REMOVED: {

                        System.out.println("TreeNode removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
                    default:
                        System.out.println("Other event: " + event.getType().name());
                }
            }
        };
        cache.getListenable().addListener(listener);
        try {
            cache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void storeChangeInDB(TreeCacheEvent event,TreeCacheEvent.Type type){
        List<String> pathList = ZKPaths.split(event.getData().getPath());
        if(pathList.size()==3){
            ServiceInstance service;
            switch (type){
                case NODE_ADDED:
                    service= SerializationUtils.deserialize(event.getData().getData());
                    JDBCUtil.insert(service);
                    break;
                case NODE_UPDATED:
                    service = SerializationUtils.deserialize(event.getData().getData());
                    JDBCUtil.update(service);
                    break;
                case NODE_REMOVED:
                    JDBCUtil.delete(ZKPaths.getNodeFromPath(event.getData().getPath()));
                    break;
            }
            return;
        }
        if(pathList.size() ==4 &&pathList.get(pathList.size()-1).equals(Constant.ZNodeName.ConfigFileZnode)){
            switch (type){
                case NODE_ADDED:
                    updateConfigFileDB(event.getData().getData(),pathList.get(pathList.size()-2));
                    break;
                case NODE_UPDATED:
                    updateConfigFileDB(event.getData().getData(),pathList.get(pathList.size()-2));
                    break;
            }
            return;
        }
        if(pathList.size() ==4 &&pathList.get(pathList.size()-1).equals(Constant.ZNodeName.StatusZnode)){
            switch (type){
                case NODE_ADDED:
                    try {
                        JDBCUtil.updateStatus(new String(event.getData().getData(),"UTF-8"),pathList.get(pathList.size()-2));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    break;
                case NODE_UPDATED:
                    try {
                        JDBCUtil.updateStatus(new String(event.getData().getData(),"UTF-8"),pathList.get(pathList.size()-2));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return;
        }

    }





    public void writeToConfigUpdate(String path,String json){
        String configUpdate = ZKPaths.makePath(path, Constant.ZNodeName.ConfigUpdateZnode);
        try {
            createAndSetData(configUpdate,json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void writeToCmd(String path,String cmd){
        String cmdPath = ZKPaths.makePath(path,Constant.ZNodeName.CmdZnode);
        try {
            createAndSetData(cmdPath,cmd.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void updateConfigFileDB(byte[] configData,String UUID){
        try {
            Blob blob = new javax.sql.rowset.serial.SerialBlob(configData);
            JDBCUtil.updateConfig(blob,UUID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private void startReceiveMq(){
        try {
            RabbitmqConsumer consumer = new RabbitmqConsumer(ConfigProperties.getInstance().get("rabbitmq.queue"),this);
            ExecutorService es = Executors.newSingleThreadExecutor();
            es.submit(consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static  void main(String [] args){
        ZkClientManage manage = new ZkClientManage();
        manage.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("manage byebye");
                manage.close();
            }
        });
    }

}
