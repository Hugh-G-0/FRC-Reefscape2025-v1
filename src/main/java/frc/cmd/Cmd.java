package frc.cmd;

import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import frc.cmd.compat.CmdAdapter;
import frc.cmd.compat.CmdProxy;

/** new verson of {@link Command} */
public abstract class Cmd {

    public final Set<SubSystem0> reqs;

    public final CmdScheduler scheduler;

    public final Object phase, priority;

    /* package-private */ CmdStatus status = CmdStatus.UNSCHEDULED;

    protected Cmd(CmdScheduler scheduler, Object phase, Object priority, SubSystem0... reqs) {
        this.reqs = Set.of(reqs);
        this.scheduler = scheduler;
        this.phase = phase;
        this.priority = priority;
    }

    public CmdStatus getStatus() {
        return this.status;
    }

    public boolean schedule() {
        return this.scheduler.add(this);
    }

    public void cancel() {
        this.scheduler.cancel(this);
    }

    public Command getWPILibCommand() {
        return new CmdAdapter(
            this,
            new CmdProxy(scheduler, phase, priority, (SubSystem0[])reqs.toArray())
        );
    }

    /** same as {@link Command#initialize()} */
    public abstract void init();

    /** same as {@link Command#execute()} then {@link Command#isFinished()} */
    public abstract boolean exec();

    /** same as {@link Command#end(boolean)} */
    public abstract void end(boolean wasInterupted);
    
    public static enum CmdStatus {QUEUED, GROUPED, RUNNING, FINALIZING, UNSCHEDULED}
}
