package frc.config;

import java.io.File;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

/**
 * class that stores numeric values for reference in code.
 * 
 * call {@link #refresh()} or {@link #load()} to mkake the values stored in {@code DynamicConfiguration.k}
 * match those on {@link Shuffleboard}
 * and a JSON file on the roboRIO
 */
public class Cfg {
    
    public static Cfg k = new Cfg();

    private Cfg() {}

    public static void load(File f) {
        ConfigurationBase.load(f);
    }

    public static void load() {
        ConfigurationBase.load(ConfigurationBase.DEFAULT_FILE);
    }

    public static void refresh() {
        ConfigurationBase.refresh();
        k = new Cfg();
    }

    public static final double NOT_YET_DEFINED = Double.NaN;

    // constants which require a restart for changes to take effect (static.*)

    public final double // ...

    WHEEL_BASE_SIZE = ConfigurationBase.getOr("static.wheelBaseSize", Units.inchesToMeters(12.5));

    // dynamic PIDF+ constants (pidf.*)
    public final double // ...

    SWERVE_DIR_P = ConfigurationBase.getOr("pidf.swvDirection-kP", NOT_YET_DEFINED),
    SWERVE_DIR_I = ConfigurationBase.getOr("pidf.swvDirection-kI", NOT_YET_DEFINED),
    SWERVE_DIR_D = ConfigurationBase.getOr("pidf.swvDirection-kD", NOT_YET_DEFINED),
    SWERVE_DIR_F = ConfigurationBase.getOr("pidf.swvDirection-kF", NOT_YET_DEFINED),

    SWERVE_MAG_P = ConfigurationBase.getOr("pidf.swvMagnitude-kP", NOT_YET_DEFINED),
    SWERVE_MAG_I = ConfigurationBase.getOr("pidf.swvMagnitude-kI", NOT_YET_DEFINED),
    SWERVE_MAG_D = ConfigurationBase.getOr("pidf.swvMagnitude-kD", NOT_YET_DEFINED),
    SWERVE_MAG_F = ConfigurationBase.getOr("pidf.swvMagnitude-kF", NOT_YET_DEFINED),


    ELEV_kG = ConfigurationBase.getOr("pidf.elevator-kG", NOT_YET_DEFINED),
    ELEV_kS = ConfigurationBase.getOr("pidf.elevator-kS", NOT_YET_DEFINED),
    ELEV_kA = ConfigurationBase.getOr("pidf.elevator-kA", NOT_YET_DEFINED),
    ELEV_kV = ConfigurationBase.getOr("pidf.elevator-kV", NOT_YET_DEFINED),

    ELEV_kP = ConfigurationBase.getOr("pidf.elevator-kP", NOT_YET_DEFINED),
    ELEV_kI = ConfigurationBase.getOr("pidf.elevator-kI", NOT_YET_DEFINED),
    ELEV_kD = ConfigurationBase.getOr("pidf.elevator-kD", NOT_YET_DEFINED),


    CLIMB_kG = ConfigurationBase.getOr("pidf.climber-kG", NOT_YET_DEFINED),
    CLIMB_kS = ConfigurationBase.getOr("pidf.climber-kS", NOT_YET_DEFINED),
    CLIMB_kA = ConfigurationBase.getOr("pidf.climber-kA", NOT_YET_DEFINED),
    CLIMB_kV = ConfigurationBase.getOr("pidf.climber-kV", NOT_YET_DEFINED),

    CLIMB_kP = ConfigurationBase.getOr("pidf.climber-kP", NOT_YET_DEFINED),
    CLIMB_kI = ConfigurationBase.getOr("pidf.climber-kI", NOT_YET_DEFINED),
    CLIMB_kD = ConfigurationBase.getOr("pidf.climber-kD", NOT_YET_DEFINED);

    // targets for moving parts (target.*)
}
