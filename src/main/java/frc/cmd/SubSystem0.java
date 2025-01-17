package frc.cmd;

import java.util.function.Supplier;

public abstract class SubSystem0 {

    public final CmdScheduler scheduler;

    public final Supplier<IdleCmd> gIdle;
    
    /**
     * @param scheduler the {@link CmdScheduler} which will manage this Object
     * @param gIdle 
     */
    protected SubSystem0(CmdScheduler scheduler, Supplier<IdleCmd> gIdle) {
        this.scheduler = scheduler;
        this.gIdle = gIdle;
        scheduler.add(this);
    }

    protected SubSystem0(CmdScheduler scheduler) {
        this(scheduler, null);
    }
}
