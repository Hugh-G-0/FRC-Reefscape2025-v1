package frc.robot.drive;

import com.gmail.frcteam1758.lib.swervedrive.control.SwerveDriveState;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.multi.LaserTargeting;

public class AlignCommand extends Command {

    public AlignCommand() {
        this.addRequirements(DriveSubSystem.INSTANCE);
    }
    
    @Override
    public void execute() {
        DriveSubSystem.INSTANCE.chassis.run(
            new SwerveDriveState(
                new ChassisSpeeds(0, 0.3, 0)
            )
        );
    }

    @Override
    public boolean isFinished() {
        return LaserTargeting.get();
    }

    @Override
    public void end(boolean wasInterupted) {
        DriveSubSystem.INSTANCE.chassis.run(SwerveDriveState.LOCKED);
    }
}
