package frc.robot.drive;

import frc.config.Cfg;

import com.gmail.frcteam1758.lib.swervedrive.MaxSwerveConfig;
import com.gmail.frcteam1758.lib.swervedrive.MaxSwerveConstants;
import com.gmail.frcteam1758.lib.swervedrive.MaxSwerveModule;
import com.gmail.frcteam1758.lib.swervedrive.SwerveChassis;
import com.gmail.frcteam1758.lib.swervedrive.control.SwerveDriveInput;
import frc.robot.multi.GlobalResources;
import frc.robot.multi.Controls;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * A {@link SubSystem} that manages the drivetrain
 */
public class DriveSubSystem extends SubsystemBase {

    public static final DriveSubSystem INSTANCE = new DriveSubSystem();

    private DriveSubSystem() {

        this.setDefaultCommand(
            this.run(this.chassis::performTeleop)
        );
    }

    private MaxSwerveModule[] modules = {
        new MaxSwerveModule(
            12, MaxSwerveConfig.MAXSwerveModule.drivingConfig,
            13, MaxSwerveConfig.MAXSwerveModule.turningConfig,
            new Translation2d( Cfg.k.WHEEL_BASE_SIZE, -Cfg.k.WHEEL_BASE_SIZE)
        ),
        new MaxSwerveModule(
            10, MaxSwerveConfig.MAXSwerveModule.drivingConfig,
            11, MaxSwerveConfig.MAXSwerveModule.turningConfig,
            new Translation2d(-Cfg.k.WHEEL_BASE_SIZE, -Cfg.k.WHEEL_BASE_SIZE)
        ),
        new MaxSwerveModule(
            16, MaxSwerveConfig.MAXSwerveModule.drivingConfig,
            17, MaxSwerveConfig.MAXSwerveModule.turningConfig,
            new Translation2d(-Cfg.k.WHEEL_BASE_SIZE,  Cfg.k.WHEEL_BASE_SIZE)
        ),
        new MaxSwerveModule(
            14, MaxSwerveConfig.MAXSwerveModule.drivingConfig,
            15, MaxSwerveConfig.MAXSwerveModule.turningConfig,
            new Translation2d(Cfg.k.WHEEL_BASE_SIZE,  Cfg.k.WHEEL_BASE_SIZE)
        )
    };

    public SwerveChassis chassis = new SwerveChassis(
        Controls.swerveControls,
        SwerveDriveInput.NO_INPUT,
        modules,
        MaxSwerveConstants.DriveConstants.kMaxSpeedMetersPerSecond,
        () -> Rotation2d.fromDegrees(-GlobalResources.GYRO.getAngle())
    );
}