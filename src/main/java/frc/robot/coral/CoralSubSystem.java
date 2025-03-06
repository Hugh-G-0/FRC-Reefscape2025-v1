package frc.robot.coral;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.config.Cfg;
import frc.robot.elevt.ElevtSubSystem;

/**
 * A {@link SubSystem} that manages the Coral dropper
 */
public class CoralSubSystem extends SubsystemBase {

    public static final CoralSubSystem INSTANCE = new CoralSubSystem();

    private final SparkMax mtr = new SparkMax(22, MotorType.kBrushless);

    private CoralSubSystem() {}

    public static class DumpCommand extends Command {

        private int time = 0;

        public DumpCommand() {
            this.addRequirements(CoralSubSystem.INSTANCE);
        }

        @Override
        public void initialize() {
            this.time = 0;

            System.out.println("DumpCommand.initialize()");
        }

        @Override
        public void execute() {
            ++this.time;

            if (this.time < Cfg.k.DUMP_U_TIME) {
                INSTANCE.mtr.setVoltage(Cfg.k.DUMP_U_VOLT);
            }
            else if (this.time < Cfg.k.DUMP_H_TIME) {
                INSTANCE.mtr.setVoltage(Cfg.k.DUMP_H_VOLT);
            }
            else if (this.time < Cfg.k.DUMP_D_TIME) {
                INSTANCE.mtr.setVoltage(Cfg.k.DUMP_D_VOLT);
            }
        }

        @Override
        public boolean isFinished() {
            return this.time > Cfg.k.DUMP_D_TIME;
        }

        @Override
        public void end(boolean wasInterupted) {
            ElevtSubSystem.INSTANCE.setLoaded(false);

            INSTANCE.mtr.setVoltage(0);

            System.out.println("DumbCommand.end(boolean)");
        }
    }

}
