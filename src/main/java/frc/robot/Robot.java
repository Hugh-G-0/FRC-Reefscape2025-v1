// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.cmd.CmdScheduler;
import frc.config.Cfg;
import frc.robot.algae.AlgaeSubSystem;
import frc.robot.climb.ClimbSubSystem;
import frc.robot.coral.CoralSubSystem;
import frc.robot.drive.DriveSubSystem;
import frc.robot.elevt.ElevtSubSystem;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {

    private final CmdScheduler scheduler = new CmdScheduler(
        CmdPhase.values(),
        CmdPriority.values()
    );

    private final AlgaeSubSystem algaeSS = new AlgaeSubSystem(scheduler);

    private final ClimbSubSystem climbSS = new ClimbSubSystem(scheduler);

    private final CoralSubSystem coralSS = new CoralSubSystem(scheduler);

    private final DriveSubSystem driveSS = new DriveSubSystem(scheduler);

    private final ElevtSubSystem elevtSS = new ElevtSubSystem(scheduler);

    public Robot() {
        Cfg.load();
    }

    @Override
    public void robotPeriodic() {
        //scheduler.run();
    }

    @Override
    public void autonomousInit() {}

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {}

    @Override
    public void teleopPeriodic() {
        this.driveSS.chassis.performTeleop();
    }

    @Override
    public void disabledInit() {
        Cfg.refresh();
    }

    @Override
    public void disabledPeriodic() {}

    @Override
    public void testInit() {}

    @Override
    public void testPeriodic() {}

    @Override
    public void simulationInit() {}

    @Override
    public void simulationPeriodic() {}

    public static enum CmdPhase {}

    public static enum CmdPriority {}
}
