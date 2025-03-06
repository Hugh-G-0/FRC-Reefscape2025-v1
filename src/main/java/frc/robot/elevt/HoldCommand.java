package frc.robot.elevt;

import edu.wpi.first.wpilibj2.command.Command;

public class HoldCommand extends Command {

    private final boolean takeLoadFlag, dropFlag;;

    public HoldCommand(boolean takeLoadFlag, boolean dropFlag) {
        this.takeLoadFlag = takeLoadFlag;
        this.dropFlag = dropFlag;

        this.addRequirements(ElevtSubSystem.INSTANCE);
    }

    @Override
    public void execute() {

        if (this.dropFlag) {
            ElevtSubSystem.INSTANCE.drop();
            return;
        }
        ElevtSubSystem.INSTANCE.hold();
    }

    @Override
    public void end(boolean wasInterupted) {

        if (this.takeLoadFlag) {
            ElevtSubSystem.INSTANCE.setLoaded(true);
        }
    }
    
}
