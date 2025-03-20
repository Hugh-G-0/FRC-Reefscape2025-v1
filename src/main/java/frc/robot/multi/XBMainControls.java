package frc.robot.multi;

import com.gmail.frcteam1758.lib.enums.SwerveDriveMode;
import com.gmail.frcteam1758.lib.swervedrive.MaxSwerveConstants;
import com.gmail.frcteam1758.lib.swervedrive.control.SwerveDriveInput;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.XboxController;
import frc.config.Cfg;
import frc.robot.XBSwerveCtrls;
import frc.robot.coral.DumpSubSystem;
import frc.robot.elevt.ElevCommand;
import frc.robot.elevt.ElevtSubSystem;

public class XBMainControls {
    
    private static final XboxController xbox = new XboxController(0);

    public static final SwerveDriveInput swerveControls = new XBSwerveCtrls(
        xbox,
        () -> Rotation2d.fromDegrees(-GlobalResources.GYRO.getAngle()),
        SwerveDriveMode.FIELD_ORIENTED,
        MaxSwerveConstants.DriveConstants.kMaxSpeedMetersPerSecond,
        MaxSwerveConstants.DriveConstants.kMaxAngularSpeed
    );

    private static boolean[] povFlags = new boolean[8];

    public static void scheduleCommands() {

        if (xbox.getYButtonPressed()) {
            new ElevCommand(Cfg.k.TARGET_L4).schedule();
        }
        else if (xbox.getBButtonPressed()) {
            new ElevCommand(Cfg.k.TARGET_L3).schedule();
        }
        else if (xbox.getXButtonPressed()) {
            new ElevCommand(Cfg.k.TARGET_L2).schedule();
        }
        else if (xbox.getAButtonPressed()) {
            new ElevCommand(Cfg.k.TARGET_CH).schedule();
        }

        for (int i = 0; i < 8; ++i) {
            povFlags[i] = (povFlags[i] || xbox.getPOV() == 45 * i);
        }

        if (xbox.getLeftBumperButtonPressed()) {
            new DumpSubSystem.DumpCommand().schedule();
        }
        else if (povFlags[0]) {
            povFlags[0] = false;
            new DumpSubSystem.ResetCommand().schedule();
        }
        else if (povFlags[4]) {
            povFlags[4] = false;
            new DumpSubSystem.DumpCommand(150).schedule();;
        }
        if (povFlags[6]) {
            povFlags[6] = false;
            ElevtSubSystem.INSTANCE.setLoaded(false);
        }
    }

    @SuppressWarnings("unused")
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
