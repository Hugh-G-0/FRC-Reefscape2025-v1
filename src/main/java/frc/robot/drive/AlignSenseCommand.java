package frc.robot.drive;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.multi.LaserTargeting;

public class AlignSenseCommand extends Command {

    @Override
    public boolean isFinished() {
        return LaserTargeting.get();
    }
    
}
