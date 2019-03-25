package Utility;

import Service.ServiceInstance;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;

public class RunLinuxCmd extends RunCmd {
    private static final String execStr = "sh ";
    public RunLinuxCmd(ServiceInstance serviceInstance) {
        super(serviceInstance);
    }
    @Override
    public ExecResult check() {
        String checkPath = serviceInstance.getCheckPath();
        File parentDir = Utility.getParent(checkPath);
        String cmd = execStr +checkPath;
        ExecResult execResult = execCmd(cmd,parentDir);
        return execResult;
    }

    @Override
    public ExecResult start() {
        String startPath = serviceInstance.getStartPath();
        File parentDir = Utility.getParent(startPath);
        String cmd = execStr + startPath;
        ExecResult execResult = execCmd(cmd,parentDir);
        return execResult;
    }

    @Override
    public ExecResult stop() {
        String stopPath = serviceInstance.getStopPath();
        File parentDir = Utility.getParent(stopPath);
        String cmd = execStr + stopPath;
        ExecResult execResult = execCmd(cmd,parentDir);
        return execResult;
    }


    @Override
    public ExecResult restart() {
        ExecResult result = check();
        if(result.isRunning()){
            result = stop();
            if(!result.isSuccess()){
                System.out.println("停止失败");
            }else{
                result =start();
                if(!result.isSuccess()){
                    System.out.println("启动失败");
                }else{
                    System.out.println("启动成功");
                }
            }

        }else{
            result = start();
        }
        return result;
    }


    /**
     * 执行系统命令, 返回执行结果
     *
     * @param cmd 需要执行的命令
     * @param dir 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
     */
    public static ExecResult execCmd(String cmd, File dir) {
        ExecResult execResult = new ExecResult();
        StringBuilder normalResult = new StringBuilder();
        StringBuilder errorResult = new StringBuilder();

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;

        try {
            String[] commond = {"/bin/sh", "-c", cmd};
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(commond, null, dir);

            // 方法阻塞, 等待命令执行完成（成功会返回0）
            int resultCode = process.waitFor();
            execResult.setResultCode(resultCode);

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                normalResult.append(line).append("<br/>");   //在网页中进行换行，可以考虑用‘\n’
                execResult.setResultStr(normalResult.toString());

            }
            while ((line = bufrError.readLine()) != null) {
                errorResult.append(line).append("<br/>");
                execResult.setResultStr(errorResult.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);
            if (process != null) {
                process.destroy();
            }
        }
        return execResult;
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
            }
        }
    }

}
