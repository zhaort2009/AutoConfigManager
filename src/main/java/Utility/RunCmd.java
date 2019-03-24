package Utility;

import java.util.regex.Pattern;

public abstract class RunCmd {
    public static RunCmd RunCmdByOs(){
        String osName = System.getProperty("os.name");
        if (Pattern.matches("Linux.*", osName)) {
            return new RunLinuxCmd();
        } else if (Pattern.matches("Windows.*", osName)) {
            return new RunWinCmd();
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
            default:

        }
        return  result;

    }


    public abstract ExecResult start();

    public abstract ExecResult stop();

    public abstract ExecResult check();
}
