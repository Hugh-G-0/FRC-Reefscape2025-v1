package frc.config;

import java.io.File;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

/**
 * class that stores numeric values for reference in code.
 * 
 * call {@link #refresh()} or {@link #load()} to mkake the values stored in {@code Cfg.k}
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
        Cfg.k = new Cfg();
    }

    public static final double NOT_YET_DEFINED = 0;

    /*
     * ConfigurationBase.getOr(String key, double default_) attempts to find and return a value in
     * the configuration JSON file that matches the key provided. If there is an error, the "default_"
     * parameter is returned instead. Value are syncronized with ShuffleBoard and the config file is updated
     * every time the robot is disbaled from the FRC Driver Station
     * 
     * Currently, it does not work and the "default_" parameter is ALWAYS returned. To modify a 
     * configuration value, change the "default_" parameter (the second one).
     */


    // constants which require a restart for changes to take effect (static.*)

    public final double // ...

    WHEEL_BASE_SIZE = ConfigurationBase.getOr("static.wheelBaseSize", Units.inchesToMeters(12.5));

    // dynamic PIDF+ constants (pidf.*)
    public final double // ...

    // PIDF gains for the swerve steering motors (Neo550s)
    // currently unused; defined in MaxSwerveConfig.java/.class as part of the "1758stuff" vendordep/library
    SWERVE_DIR_P = ConfigurationBase.getOr("pidf.swvDirection-kP", NOT_YET_DEFINED),
    SWERVE_DIR_I = ConfigurationBase.getOr("pidf.swvDirection-kI", NOT_YET_DEFINED),
    SWERVE_DIR_D = ConfigurationBase.getOr("pidf.swvDirection-kD", NOT_YET_DEFINED),
    SWERVE_DIR_F = ConfigurationBase.getOr("pidf.swvDirection-kF", NOT_YET_DEFINED),

    // PIDF gains for the swerve driving motors (Neos)
    // currently unused; defined in MaxSwerveConfig.java/.class as part of the "1758stuff" vendordep/library
    SWERVE_MAG_P = ConfigurationBase.getOr("pidf.swvMagnitude-kP", NOT_YET_DEFINED),
    SWERVE_MAG_I = ConfigurationBase.getOr("pidf.swvMagnitude-kI", NOT_YET_DEFINED),
    SWERVE_MAG_D = ConfigurationBase.getOr("pidf.swvMagnitude-kD", NOT_YET_DEFINED),
    SWERVE_MAG_F = ConfigurationBase.getOr("pidf.swvMagnitude-kF", NOT_YET_DEFINED),


    // FeedForward gains for the elevator when it DOES NOT contain a "coral" (aka PVC pipe)
    // The "kA" (acceleration) gain is currently unused
    ELEV_UL_kG = ConfigurationBase.getOr("pidf.elevator-ul-kG", 0.4),
    ELEV_UL_kS = ConfigurationBase.getOr("pidf.elevator-ul-kS", 0.15),
    ELEV_UL_kA = ConfigurationBase.getOr("pidf.elevator-ul-kA", NOT_YET_DEFINED),
    ELEV_UL_kV = ConfigurationBase.getOr("pidf.elevator-ul-kV", 11),

    // FeedForward gains for the elevator when it DOES contain a "coral" (aka PVC pipe)
    // the "kA" (acceleration) gain is currently unused
    ELEV_L_kG = ConfigurationBase.getOr("pidf.elevator-l-kG", 0.4),
    ELEV_L_kS = ConfigurationBase.getOr("pidf.elevator-l-kS", 0.15),
    ELEV_L_kA = ConfigurationBase.getOr("pidf.elevator-l-kA", NOT_YET_DEFINED),
    ELEV_L_kV = ConfigurationBase.getOr("pidf.elevator-l-kV", 11),

    // PID gains for the elevator when it DOES NOT contain a "coral" (aka PVC pipe)
    // currently unused
    ELEV_UL_kP = ConfigurationBase.getOr("pidf.elevator-ul-kP", NOT_YET_DEFINED),
    ELEV_UL_kI = ConfigurationBase.getOr("pidf.elevator-ul-kI", NOT_YET_DEFINED),
    ELEV_UL_kD = ConfigurationBase.getOr("pidf.elevator-ul-kD", NOT_YET_DEFINED),

    // PID gains for the elevator when it DOES contain a "coral" (aka PVC pipe)
    // currently unused
    ELEV_L_kP = ConfigurationBase.getOr("pidf.elevator-l-kP", NOT_YET_DEFINED),
    ELEV_L_kI = ConfigurationBase.getOr("pidf.elevator-l-kI", NOT_YET_DEFINED),
    ELEV_L_kD = ConfigurationBase.getOr("pidf.elevator-l-kD", NOT_YET_DEFINED),

    // velocity and acceleration contraints for the elevator in m/s and m/s^2
    // currently unused
    ELEV_MAXV = ConfigurationBase.getOr("pidf.elevator-maxv", 0.2),
    ELEV_ACCL = ConfigurationBase.getOr("pidf.elevator-accl", 0.2),

    // maximum error allowed in the elevator for it to stop moving
    // lower values cause greater accuracy; higher values prevent occilation
    ELEV_ALLOW_ERR = ConfigurationBase.getOr("pidf.elev-allow-err", 0.015),

    // the voltages to be applied while dumping a "coral" (aka PVC pipe)
    // higher values result in faster movement/more force
    DUMP_U_VOLT = ConfigurationBase.getOr("pidf.dump-u-volt", 2),   // going up
    DUMP_H_VOLT = ConfigurationBase.getOr("pidf.dump-h-volt", 0.3), // staying/holding up
    DUMP_D_VOLT = ConfigurationBase.getOr("pidf.dump-d-volt", -2.5),         // going back down

    // timings for the lengths of the 3 phases of dumping a coral, in 20ms intervals (ex. value of 10 == 200ms == 0.2ses)
    DUMP_U_TIME = ConfigurationBase.getOr("pidf.dump-u-time", 40),
    DUMP_H_TIME = ConfigurationBase.getOr("pidf.dump-h-time", 40),
    DUMP_D_TIME = ConfigurationBase.getOr("pidf.dump-d-time", 30),

    WHEEL_VOLT = ConfigurationBase.getOr("pidf.wheel-volt", 3),

    // FeedForward gains for the (currently nonexistant) climber
    // currently unused
    CLIMB_kG = ConfigurationBase.getOr("pidf.climber-kG", NOT_YET_DEFINED),
    CLIMB_kS = ConfigurationBase.getOr("pidf.climber-kS", NOT_YET_DEFINED),
    CLIMB_kA = ConfigurationBase.getOr("pidf.climber-kA", NOT_YET_DEFINED),
    CLIMB_kV = ConfigurationBase.getOr("pidf.climber-kV", NOT_YET_DEFINED),

    // PID gains for the (currently nonexistant) climber
    // currently unused
    CLIMB_kP = ConfigurationBase.getOr("pidf.climber-kP", NOT_YET_DEFINED),
    CLIMB_kI = ConfigurationBase.getOr("pidf.climber-kI", NOT_YET_DEFINED),
    CLIMB_kD = ConfigurationBase.getOr("pidf.climber-kD", NOT_YET_DEFINED);

    // targets for moving parts (target.*)
    public final double // ...

    TARGET_L4 = ConfigurationBase.getOr("target.l-L4", 1.15), // L4 pole
    TARGET_L3 = ConfigurationBase.getOr("target.l-L3", 0.72), // L3 pole
    TARGET_L2 = ConfigurationBase.getOr("target.l-L2", 0.3), // L2 pole
    TARGET_CH = ConfigurationBase.getOr("target.l-CH", 0.1), // chute/human player station

    LASER_DIST = ConfigurationBase.getOr("target.laser-dist", 500);
}
