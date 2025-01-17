package frc.cmd.compat;

import frc.cmd.Cmd;
import frc.cmd.Cmd.CmdStatus;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * allows the WPILib {@link CommandScheduler} to run {@link Cmd}s
 */
public class CmdAdapter extends Command {

    private final Cmd base;

    private final CmdProxy proxy;

    private boolean finishFlag = false;
    
    public CmdAdapter(Cmd base, CmdProxy proxy) {

        this.base = base;
        this.proxy = proxy;
    }

    @Override
    public void initialize() {

        if (this.proxy.schedule()) {
            this.base.init();
        }
        else {
            this.finishFlag = true;
        }
    }

    @Override
    public void execute() {

        if (this.finishFlag) {
            return;
        }

        this.finishFlag = this.base.exec();
    }

    @Override
    public boolean isFinished() {
        return (
            this.finishFlag
            || 
            this.base.getStatus() == CmdStatus.FINALIZING
            ||
            this.base.getStatus() == CmdStatus.UNSCHEDULED
        );
    }

    @Override
    public void end(boolean wasInterupted) {
        base.end(wasInterupted);
        this.proxy.registerEnd();
    }
}
