package Utility;

import Service.ServiceInstance;

import java.util.regex.Pattern;

public abstract class RunCmd {
    protected ServiceInstance serviceInstance = null;

    public RunCmd(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public static RunCmd RunCmdByOs(ServiceInstance service){
        String osName = System.getProperty("os.name");
        if (Pattern.matches("Linux.*", osName)) {
            return new RunLinuxCmd(service);
        } else if (Pattern.matches("Windows.*", osName)) {
            return new RunWinCmd(service);
        } else if (Pattern.matches("Mac.*", osName)) {
            return null;

        }
        return null;

    }

    public ExecResult execute(String cmd){
        ExecResult result = null;
        switch (cmd){
            case "start":
                result = start();
                break;
            case "stop":
                result = stop();
                break;
            case "check":
                result =  check();
                break;
            case "restart":
                result =  restart();
                break;
            default:

        }
        return  result;

    }


    public abstract ExecResult start();

    public abstract ExecResult stop();

    public abstract ExecResult check();

    public abstract ExecResult restart();
}
