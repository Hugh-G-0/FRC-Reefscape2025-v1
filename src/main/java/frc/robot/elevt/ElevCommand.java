package frc.robot.elevt;

import edu.wpi.first.wpilibj2.command.Command;
import frc.config.Cfg;

public class ElevCommand extends Command {
    
    private final double target;

    public ElevCommand(double target) {

        this.addRequirements(ElevtSubSystem.INSTANCE);

        this.target = target;
    }

    @Override
    public void initialize() {
        System.out.println("ElevCommand.initialize()");
    }

    @Override
    public void execute() {
        ElevtSubSystem.INSTANCE.approach(this.target);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(this.target - ElevtSubSystem.INSTANCE.getPos()) <  Cfg.k.ELEV_ALLOW_ERR;
    }

    @Override
    public void end (boolean wasInterupted) {
        System.out.println("ElevCommand.end(boolean)");
    }
}
