package frc.robot.elevt;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.config.Cfg;
import frc.robot.multi.GlobalResources;

/**
 * A {@link SubSystem} that manages the elevator
 */
public class ElevtSubSystem extends SubsystemBase {

    public static final ElevtSubSystem INSTANCE = new ElevtSubSystem();

    private final SparkMax liftMtr;

    private final RelativeEncoder encoder;

    private ElevatorFeedforward unloadedFF, loadedFF, ff;

    public ElevtSubSystem() {
        
        this.liftMtr = new SparkMax(21, MotorType.kBrushless);

        this.encoder = this.liftMtr.getEncoder();

        this.cfgFF();

        this.ff = this.loadedFF;

        this.setDefaultCommand(this.run(this::hold));

        Shuffleboard.getTab("target").addDouble("elev-pos", this::getPos);
        Shuffleboard.getTab("target").addDouble("elev-vel", this::getVel);
    }

    public void setLoaded(boolean x) {
        this.ff = x? this.loadedFF : this.unloadedFF;
    }

    public void approach(double target) {

        double voltage = this.ff.calculate(
            0.5 * Math.signum(target - this.getPos())
        );

        if (Math.abs(target - this.getPos()) < 0.1) {
            voltage *= 0.4;
        }

        voltage = Math.min(voltage, 6);
        voltage = Math.max(voltage, -6);

        SmartDashboard.putNumber("elev voltage", voltage);
        
        liftMtr.setVoltage(voltage);
    }

    public void hold() {
        this.liftMtr.setVoltage(this.ff.calculate(0));
    }

    public void drop() {
        this.liftMtr.setVoltage(0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("elev pos (m)", this.getPos());
        SmartDashboard.putNumber("elev vel (m/s)", this.getVel());
        SmartDashboard.putNumber("elev vel (RPM)", this.getVel());
    }

    private void cfgFF() {

        this.loadedFF = new ElevatorFeedforward(
            Cfg.k.ELEV_L_kS,
            Cfg.k.ELEV_L_kG,
            Cfg.k.ELEV_L_kV,
            Cfg.k.ELEV_L_kA
        );

        this.unloadedFF = new ElevatorFeedforward(
            Cfg.k.ELEV_UL_kS,
            Cfg.k.ELEV_UL_kG,
            Cfg.k.ELEV_UL_kV,
            Cfg.k.ELEV_UL_kA
        );
    }

    public double getPos() {
        return GlobalResources.elvLidar.getMeasurement().distance_mm / 1000.0;
    }

    public double getVel() {
        return this.encoder.getVelocity() * (1 / 60) * (1 / 20) * (0.0444 / 2); // RPM:RPS -> 20:1 gearbox -> ~0.0444m sprocket
    }
}