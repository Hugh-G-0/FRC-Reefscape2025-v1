package frc.cmd;

public abstract class IdleCmd extends Cmd {
    
    public IdleCmd(SubSystem0 req) {
        super(req.scheduler, null, null, req);
    }
}
