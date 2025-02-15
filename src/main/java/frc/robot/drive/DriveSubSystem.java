package frc.robot.drive;

import frc.cmd.CmdScheduler;
import frc.cmd.SubSystem0;
import frc.config.Cfg;

import com.gmail.frcteam1758.lib.enums.SwerveDriveMode;
import com.gmail.frcteam1758.lib.swervedrive.MaxSwerveConfig;
import com.gmail.frcteam1758.lib.swervedrive.MaxSwerveConstants;
import com.gmail.frcteam1758.lib.swervedrive.MaxSwerveModule;
import com.gmail.frcteam1758.lib.swervedrive.SwerveChassis;
import com.gmail.frcteam1758.lib.swervedrive.control.SwerveDriveControls2023;
import com.gmail.frcteam1758.lib.swervedrive.control.SwerveDriveInput;
import frc.robot.multi.Communal;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

/**
 * A {@link SubSystem0} that manages the drivetrain
 */
public class DriveSubSystem extends SubSystem0 {
    
    public DriveSubSystem(CmdScheduler scheduler) {
        super(scheduler);
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

    // TODO: proper drive controls
    private SwerveDriveInput ctrl_TEMP = new SwerveDriveControls2023(
        0, 1,
        () -> Rotation2d.fromDegrees(-Communal.GYRO.getAngle()),
        SwerveDriveMode.FIELD_ORIENTED,
        MaxSwerveConstants.DriveConstants.kMaxSpeedMetersPerSecond,
        MaxSwerveConstants.DriveConstants.kMaxAngularSpeed
    );

    // TODO: make chassis private when CMD stuff is done
    public SwerveChassis chassis = new SwerveChassis(
        ctrl_TEMP,
        SwerveDriveInput.NO_INPUT,
        modules,
        MaxSwerveConstants.DriveConstants.kMaxSpeedMetersPerSecond,
        () -> Rotation2d.fromDegrees(-Communal.GYRO.getAngle())
    );
}