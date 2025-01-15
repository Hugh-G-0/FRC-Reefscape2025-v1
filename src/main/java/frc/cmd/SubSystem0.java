package frc.cmd;

public abstract class SubSystem0 {
    
    protected SubSystem0(CmdScheduler s) {
        s.add(this);
    }
}
