package frc.robot.multi;

import au.grapplerobotics.LaserCan;
import edu.wpi.first.wpilibj.ADIS16470_IMU;

public class GlobalResources {
    
    public static final ADIS16470_IMU GYRO = new ADIS16470_IMU();

    public static final LaserCan elvLidar = new LaserCan(23);
}
