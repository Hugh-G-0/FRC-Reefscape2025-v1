
package frc.robot.multi;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;
import au.grapplerobotics.interfaces.LaserCanInterface.Measurement;
import au.grapplerobotics.interfaces.LaserCanInterface.RangingMode;
import au.grapplerobotics.interfaces.LaserCanInterface.RegionOfInterest;
import au.grapplerobotics.interfaces.LaserCanInterface.TimingBudget;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.config.Cfg;

public class LaserTargeting {
    
    private static final LaserCan targetingLaser = new LaserCan(26);

    private static PWM basicPWM = new PWM(0);

    private static int count = 0;

    public static final int LED_NORMAL = 1995;

    public static final int LED_ALIGNED = 1885;

    private static boolean flag = false;

    public static void init() throws ConfigurationFailedException {
        targetingLaser.setRangingMode(RangingMode.SHORT);

        targetingLaser.setRegionOfInterest(new RegionOfInterest(0, 0, 4, 4));

        targetingLaser.setTimingBudget(TimingBudget.TIMING_BUDGET_100MS);
    }

    public static void run() {

        if (++count % 50 == 0) {
            basicPWM.setPulseTimeMicroseconds(2145);
            return;
        }
        
        Measurement range = targetingLaser.getMeasurement();

        if (range == null) {
            return;
        }

        SmartDashboard.putNumber("targeting-dist (mm)", range.distance_mm);

        if (range.distance_mm < Cfg.k.LASER_DIST && range.distance_mm > 1) {
            basicPWM.setPulseTimeMicroseconds(LED_ALIGNED);
            SmartDashboard.putBoolean("align", true);
            flag  = true;
            return;
        }
        basicPWM.setPulseTimeMicroseconds(LED_NORMAL);
        SmartDashboard.putBoolean("align", false);
        flag = false;
    }

    public static boolean get() {
        return flag;
    }
}
