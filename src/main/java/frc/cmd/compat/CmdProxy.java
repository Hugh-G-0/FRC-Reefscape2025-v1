package frc.cmd.compat;

import frc.cmd.Cmd;
import frc.cmd.CmdScheduler;
import frc.cmd.SubSystem0;

import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * dummy {@link Cmd} to "use" {@link SubSystem0}s that are in use by the WPILib {@link CommandScheduler}
 */
public class CmdProxy extends Cmd {

    private boolean finishFlag = false;
    
    public CmdProxy(CmdScheduler scheduler, Object phase, Object priority, SubSystem0[] reqs) {
        super(scheduler, phase, priority, reqs);
    }

    @Override
    public void init() {}

    @Override
    public boolean exec() {
        return this.finishFlag;
    }

    @Override
    public void end(boolean x) {}

    public void registerEnd() {
        this.finishFlag = true;
    }
}
