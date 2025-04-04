package frc.robot.multi;

import au.grapplerobotics.LaserCan;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.ADIS16470_IMU;

public class GlobalResources {
    
    public static final ADIS16470_IMU GYRO = new ADIS16470_IMU();

    private static Rotation2d gyroOffset = Rotation2d.kZero;

    public static final LaserCan elvLidar = new LaserCan(23);

    public static void setGyroAngle(Rotation2d angle) {
        gyroOffset = angle.minus(Rotation2d.fromDegrees(-GYRO.getAngle()));
    }

    public static Rotation2d getGyroAngle() {
        return Rotation2d.fromDegrees(-GYRO.getAngle()).plus(gyroOffset);
    }
}
