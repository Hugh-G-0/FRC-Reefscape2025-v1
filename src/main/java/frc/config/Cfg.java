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

    public static final double NOT_YET_DEFINED = 0;

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


    ELEV_UL_kG = ConfigurationBase.getOr("pidf.elevator-ul-kG", 0.4),
    ELEV_UL_kS = ConfigurationBase.getOr("pidf.elevator-ul-kS", 0.15),
    ELEV_UL_kA = ConfigurationBase.getOr("pidf.elevator-ul-kA", NOT_YET_DEFINED),
    ELEV_UL_kV = ConfigurationBase.getOr("pidf.elevator-ul-kV", 11),

    ELEV_L_kG = ConfigurationBase.getOr("pidf.elevator-l-kG", 0.4),
    ELEV_L_kS = ConfigurationBase.getOr("pidf.elevator-l-kS", 0.15),
    ELEV_L_kA = ConfigurationBase.getOr("pidf.elevator-l-kA", NOT_YET_DEFINED),
    ELEV_L_kV = ConfigurationBase.getOr("pidf.elevator-l-kV", 11),

    ELEV_UL_kP = ConfigurationBase.getOr("pidf.elevator-ul-kP", NOT_YET_DEFINED),
    ELEV_UL_kI = ConfigurationBase.getOr("pidf.elevator-ul-kI", NOT_YET_DEFINED),
    ELEV_UL_kD = ConfigurationBase.getOr("pidf.elevator-ul-kD", NOT_YET_DEFINED),

    ELEV_L_kP = ConfigurationBase.getOr("pidf.elevator-l-kP", NOT_YET_DEFINED),
    ELEV_L_kI = ConfigurationBase.getOr("pidf.elevator-l-kI", NOT_YET_DEFINED),
    ELEV_L_kD = ConfigurationBase.getOr("pidf.elevator-l-kD", NOT_YET_DEFINED),

    ELEV_MAXV = ConfigurationBase.getOr("pidf.elevator-maxv", 0.2),
    ELEV_ACCL = ConfigurationBase.getOr("pidf.elevator-accl", 0.2),

    ELEV_ALLOW_ERR = ConfigurationBase.getOr("pidf.elev-allow-err", 0.01),

    DUMP_U_VOLT = ConfigurationBase.getOr("pidf.dump-u-volt", 2),
    DUMP_H_VOLT = ConfigurationBase.getOr("pidf.dump-h-volt", 0.3),
    DUMP_D_VOLT = ConfigurationBase.getOr("pidf.dump-d-volt", -1.5),

    DUMP_U_TIME = ConfigurationBase.getOr("pidf.dump-u-time", 40),
    DUMP_H_TIME = ConfigurationBase.getOr("pidf.dump-h-time", 90),
    DUMP_D_TIME = ConfigurationBase.getOr("pidf.dump-d-time", 110),

    CLIMB_kG = ConfigurationBase.getOr("pidf.climber-kG", NOT_YET_DEFINED),
    CLIMB_kS = ConfigurationBase.getOr("pidf.climber-kS", NOT_YET_DEFINED),
    CLIMB_kA = ConfigurationBase.getOr("pidf.climber-kA", NOT_YET_DEFINED),
    CLIMB_kV = ConfigurationBase.getOr("pidf.climber-kV", NOT_YET_DEFINED),

    CLIMB_kP = ConfigurationBase.getOr("pidf.climber-kP", NOT_YET_DEFINED),
    CLIMB_kI = ConfigurationBase.getOr("pidf.climber-kI", NOT_YET_DEFINED),
    CLIMB_kD = ConfigurationBase.getOr("pidf.climber-kD", NOT_YET_DEFINED);

    // targets for moving parts (target.*)

    public final double
    TARGET_L4 = ConfigurationBase.getOr("target.l-L4", 1.1),
    TARGET_L3 = ConfigurationBase.getOr("target.l-L3", 0.7),
    TARGET_L2 = ConfigurationBase.getOr("target.l-L2", 0.1),
    TARGET_CH = ConfigurationBase.getOr("target.l-CH", 0.1);
}
