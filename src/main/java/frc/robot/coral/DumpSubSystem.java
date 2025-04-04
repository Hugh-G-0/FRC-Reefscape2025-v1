package frc.robot.coral;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.config.Cfg;
import frc.robot.elevt.ElevtSubSystem;

/**
 * A {@link SubSystem} that manages the Coral dropper
 */
public class DumpSubSystem extends SubsystemBase {

    public static final DumpSubSystem INSTANCE = new DumpSubSystem();

    private final SparkMax dumpMtr = new SparkMax(22, MotorType.kBrushless);

    private final SparkMax wheelMtr = new SparkMax(24, MotorType.kBrushless);

    public final Servo servo = new Servo(6);

    private DumpSubSystem() {
        this.wheelMtr.setInverted(true);

        this.setDefaultCommand(this.run(() -> {this.wheelMtr.setVoltage(1);}));
    }

    public static class DumpCommand extends Command {

        private static int time = 0;

        private final int wheelTime;

        public DumpCommand() {
            this.addRequirements(DumpSubSystem.INSTANCE);

            this.wheelTime = 0;


        }

        public DumpCommand(int wheelTime) {
            this.addRequirements(DumpSubSystem.INSTANCE);

            this.wheelTime = wheelTime;
        }

        @Override
        public void initialize() {
            System.out.println("DumpCommand.initialize()");
        }

        @Override
        public void execute() {
            if (this.wheelTime == 0) {
                this.dExecute();
                return;
            }
            else {
                this.wExecute();
            }
        }
        
        @Override
        public boolean isFinished() {
            if (this.wheelTime == 0) {
                return time > Cfg.k.DUMP_U_TIME + Cfg.k.DUMP_H_TIME;
            }
            return time > Cfg.k.DUMP_U_TIME + this.wheelTime + Cfg.k.DUMP_D_TIME;
        }

        @Override
        public void end(boolean wasInterupted) {
            ElevtSubSystem.INSTANCE.setLoaded(false);

            INSTANCE.dumpMtr.setVoltage(0);

            INSTANCE.wheelMtr.setVoltage(0);

            System.out.println("DumbCommand.end(boolean)");

            if (!wasInterupted) {
                time = 0; // if interupted it should continue where it left off
            }
        }

        private void dExecute() {
            ++time;

            if (time < Cfg.k.DUMP_U_TIME) {
                INSTANCE.dumpMtr.setVoltage(Cfg.k.DUMP_U_VOLT);
            }
            else if (time < Cfg.k.DUMP_H_TIME + Cfg.k.DUMP_U_TIME) {
                INSTANCE.dumpMtr.setVoltage(Cfg.k.DUMP_H_VOLT);
            }
            else {
                INSTANCE.dumpMtr.setVoltage(Cfg.k.DUMP_D_VOLT);
            }
        }

        private void wExecute() {
            ++time;

            INSTANCE.wheelMtr.setVoltage(Cfg.k.WHEEL_VOLT);

            if (time < Cfg.k.DUMP_U_TIME) {
                INSTANCE.dumpMtr.setVoltage(Cfg.k.DUMP_U_VOLT);
            }
            else if (time < this.wheelTime + Cfg.k.DUMP_U_TIME) {
                INSTANCE.dumpMtr.setVoltage(Cfg.k.DUMP_H_VOLT);
            }
            else {
                INSTANCE.dumpMtr.setVoltage(Cfg.k.DUMP_D_VOLT);
            }
        }
    }

    public static class ResetCommand extends Command {

        private static int time = 0;

        @Override
        public void execute() {
            ++time;
            INSTANCE.dumpMtr.setVoltage(Cfg.k.DUMP_D_VOLT);
        }

        @Override
        public boolean isFinished() {
            return time > Cfg.k.DUMP_D_TIME;
        }

        @Override
        public void end (boolean wasInterupted) {
            INSTANCE.dumpMtr.setVoltage(0);

            if (!wasInterupted) {
                time = 0;
            }
        }
    }

    public static class EjectAlgaeCommand extends Command {
    
        private int time = 0;

        public EjectAlgaeCommand() {
            this.addRequirements(INSTANCE);
        }

        @Override
        public void initialize() {
            this.time = 0;
        }

        @Override public void execute() {
            ++time;
            INSTANCE.wheelMtr.setVoltage(-8);
        }

        @Override
        public boolean isFinished() {
            return this.time > 50;
        }

        @Override
        public void end(boolean wasInterupted) {
            INSTANCE.wheelMtr.setVoltage(0);
        }
    }
}
