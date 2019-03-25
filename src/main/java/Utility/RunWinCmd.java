package Utility;

import Service.ServiceInstance;

public class RunWinCmd extends RunCmd {
    public RunWinCmd(ServiceInstance serviceInstance) {
        super(serviceInstance);
    }

    @Override
    public ExecResult start() {
        return null;
    }

    @Override
    public ExecResult stop() {
        return null;
    }

    @Override
    public ExecResult check() {
        return null;
    }

    @Override
    public ExecResult restart() {
        return null;
    }
}
