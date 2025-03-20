package frc.robot.multi;

import com.gmail.frcteam1758.lib.enums.SwerveDriveMode;
import com.gmail.frcteam1758.lib.swervedrive.MaxSwerveConstants;
import com.gmail.frcteam1758.lib.swervedrive.control.SwerveDriveControls2023;
import com.gmail.frcteam1758.lib.swervedrive.control.SwerveDriveInput;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Joystick;
import frc.config.Cfg;
import frc.robot.coral.DumpSubSystem;
import frc.robot.elevt.ElevCommand;
import frc.robot.elevt.ElevtSubSystem;
import frc.robot.elevt.HoldCommand;

public class JSMainControls {
    
    private static final Joystick lstick = new Joystick(1), rstick = new Joystick(0);

    public static final SwerveDriveInput swerveControls = new SwerveDriveControls2023(
        rstick.getPort(),
        lstick.getPort(),
        () -> Rotation2d.fromDegrees(-GlobalResources.GYRO.getAngle()),
        SwerveDriveMode.FIELD_ORIENTED,
        MaxSwerveConstants.DriveConstants.kMaxSpeedMetersPerSecond,
        MaxSwerveConstants.DriveConstants.kMaxAngularSpeed
    );

    private static ElvPosition prevLPos = ElvPosition.NEUTRAL;

    public static void scheduleCommands() {

        if (rstick.getRawButtonPressed(2)) {
            ElevtSubSystem.INSTANCE.setLoaded(false);

            System.out.println("set LoadedStatus to 'unloaded'");
        }

        if (lstick.getTriggerPressed()) {
            new DumpSubSystem.DumpCommand().schedule();
        }
        else if (lstick.getRawButtonPressed(3)) {
            new DumpSubSystem.DumpCommand(150).schedule();
        }
        else if (lstick.getRawButtonPressed(2)) {
            new DumpSubSystem.ResetCommand().schedule();
        }

        ElvPosition currentLPos = ElvPosition.ofPOV(rstick.getPOV());

        if (currentLPos == ElvPosition.NEUTRAL || currentLPos == prevLPos) {
            // do nothing (guard clause)
        }
        else if (currentLPos == ElvPosition.CH) {
            new ElevCommand(Cfg.k.TARGET_CH).andThen(new HoldCommand(true, true)).schedule();
        }
        else {
            new ElevCommand(currentLPos.target).andThen(new HoldCommand(false, false)).schedule();
        }

        prevLPos = currentLPos;
    }

    private static enum ElvPosition {
        L4(Cfg.k.TARGET_L4),
        L3(Cfg.k.TARGET_L3),
        L2(Cfg.k.TARGET_L2),
        CH(Cfg.k.TARGET_CH),

        NEUTRAL(Double.NaN);

        public final double target;

        private ElvPosition(double target) {
            this.target = target;
        }

        public static ElvPosition ofPOV(int pov) {
            switch (pov) {
                case 0:
                    return L4;
                
                case 90:
                    return L3;
                
                case 180:
                    return CH;
                
                case 270:
                    return L2;
            
                default:
                    return NEUTRAL;
            }
        }
    }
}