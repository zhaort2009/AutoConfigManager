package ZkClient;

import Service.ServiceInstance;
import Service.ServiceXmlParse;
import Utility.ExecResult;
import Utility.PropertiesConfig;
import Utility.RunCmd;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.utils.ZKPaths;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ZkClientNormal extends ZkClient {

    private List<ServiceInstance> serviceList = null;
    private String ipPath;

    public ZkClientNormal() {
        super();
        serviceList = ServiceXmlParse.getInstance().parseXml();

    }

    public void start() {
        if (serviceList == null || serviceList.size() == 0) {
            return;
        }
        createIpFile();
        for (ServiceInstance service : serviceList) {
            createService(service);
        }
        checkEveryMinute();
    }


    private void createIpFile() {
        String ip = serviceList.get(0).getAddress();
        ipPath = ZKPaths.makePath(ROOTPATH, ip);
        createAndSetData(ipPath, null);
    }

    private void createService(ServiceInstance service) {
        String UUID = service.getId();
        String servicePath = ZKPaths.makePath(ROOTPATH, UUID);
        byte[] serviceByteArray = SerializationUtils.serialize(service);
        createAndSetData(servicePath, serviceByteArray);

        createCmdResult(servicePath, service);
        createConfigFile(servicePath, service);
        createConfigUpdate(servicePath, service);
        createCmd(servicePath, service);
        createStatus(servicePath, service);


    }


    private void createConfigFile(String parentPath, ServiceInstance service) {
        String configFilePath = ZKPaths.makePath(parentPath, "configFile");
        File configFile = new File(service.getConfigPath());
        try {
            byte[] fileContent = Files.readAllBytes(configFile.toPath());
            createAndSetData(configFilePath, fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createConfigUpdate(String parentPath, ServiceInstance service) {
        String configUpdatePath = ZKPaths.makePath(parentPath, "configUpdate");
        NodeCache cache = new NodeCache(client, configUpdatePath);
        NodeCacheListener listener = new NodeCacheListener() {

            @Override
            public void nodeChanged() throws Exception {
                if (cache.getCurrentData() != null) {
                    if (cache.getCurrentData().getData() == null) {
                        return;
                    }
                    System.out.println("Node changed: " + cache.getCurrentData().getPath() + ", value: " + new String(cache.getCurrentData().getData()));
                    PropertiesConfig propertiesConfig = new PropertiesConfig(service.getConfigPath());
                    propertiesConfig.Update(new String(cache.getCurrentData().getData(), "UTF-8"));
                    File configFile = new File(service.getConfigPath());
                    createAndSetData(ZKPaths.makePath(parentPath,"configFile"),Files.readAllBytes(configFile.toPath()));
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

    private void createCmd(String parentPath, ServiceInstance service) {
        String cmdPath = ZKPaths.makePath(parentPath, "cmd");
        NodeCache cache = new NodeCache(client, cmdPath);
        NodeCacheListener listener = new NodeCacheListener() {

            @Override
            public void nodeChanged() throws Exception {
                if (cache.getCurrentData() != null) {
                    if (cache.getCurrentData().getData() == null) {
                        return;
                    }
                    System.out.println("Node changed: " + cache.getCurrentData().getPath() + ", value: " + new String(cache.getCurrentData().getData()));
                    ExecResult result = RunCmd.RunCmdByOs(service).execute(new String(cache.getCurrentData().getData(), "UTF-8"));

                    ZKPaths.PathAndNode pathAndNode = ZKPaths.getPathAndNode(cache.getCurrentData().getPath());
                    String cmdResultPath = ZKPaths.makePath(pathAndNode.getPath(),"cmdResult");
                    createAndSetData(cmdResultPath,result.toJson().getBytes("UTF-8"));
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

    private void createStatus(String parentPath, ServiceInstance service) {
        String statusPath = ZKPaths.makePath(parentPath, "status");
        int status = checkStatus(service);
        try {
            createAndSetData(statusPath,String.valueOf(status).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void createCmdResult(String parentPath, ServiceInstance service) {
        String cmdResultPath = ZKPaths.makePath(parentPath, "cmdResult");
        createAndSetData(cmdResultPath,null);
    }

    private int checkStatus(ServiceInstance service) {
        ExecResult checkResult = RunCmd.RunCmdByOs(service).check();
        if(!checkResult.isSuccess()){
            System.out.println("check 命令执行失败："+checkResult.getErrorStr());
            return checkResult.getResultCode();
        }
        if(checkResult.isRunning()){
            return 1;
        }else{
            return 0;
        }

    }



    private void checkEveryMinute(){
        ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
        es.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                for(ServiceInstance service:serviceList){
                    int status =  checkStatus(service);
                    String statusPath = ZKPaths.makePath(ipPath,service.getId());
                    statusPath = ZKPaths.makePath(statusPath,"status");
                    try {
                        createAndSetData(statusPath,String.valueOf(status).getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }


            }
        },60,60, TimeUnit.SECONDS);
    }



    private void stop(){
        for (ServiceInstance service:serviceList){
            RunCmd.RunCmdByOs(service).stop();
        }
        deleteNode(ipPath);
    }




    public static void main(String [] args) {
        ZkClientNormal zkClientNormal = new ZkClientNormal();
        zkClientNormal.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("normal byebye");

                zkClientNormal.stop();
                zkClientNormal.close();

            }
        });
    }


}
