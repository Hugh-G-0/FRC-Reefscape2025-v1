// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.gmail.frcteam1758.lib.swervedrive.control.SwerveDriveState;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import au.grapplerobotics.CanBridge;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.config.Cfg;
import frc.robot.coral.CoralSubSystem;
import frc.robot.drive.DriveSubSystem;
import frc.robot.elevt.ElevCommand;
import frc.robot.elevt.ElevtSubSystem;
import frc.robot.multi.Controls;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {

    private SendableChooser<Command> autoChooser;

    public Robot() {
        Cfg.load();

        CanBridge.runTCP();

        CameraServer.startAutomaticCapture(1);

        DriveSubSystem.INSTANCE.register();
        CoralSubSystem.INSTANCE.register();
        ElevtSubSystem.INSTANCE.register();

        try {
            AutoBuilder.configure(
                DriveSubSystem.INSTANCE.chassis::getPose,
                DriveSubSystem.INSTANCE.chassis::resetPose,
                DriveSubSystem.INSTANCE.chassis::getCurrentSpeeds,
                (speeds, ffs) -> DriveSubSystem.INSTANCE.chassis.run(new SwerveDriveState(speeds)),
                new PPHolonomicDriveController(
                new PIDConstants(5), new PIDConstants(5)
                ),
                RobotConfig.fromGUISettings(),
                () -> DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red,
                DriveSubSystem.INSTANCE
            );

            SmartDashboard.putData("autos", autoChooser = AutoBuilder.buildAutoChooser());
        }
        catch (Exception e) {
            e.printStackTrace();
    
            SmartDashboard.putData("autos", autoChooser = new SendableChooser<Command>());
        }
        autoChooser.setDefaultOption("NONE", new InstantCommand());

        NamedCommands.registerCommand("elev-L4", new ElevCommand(Cfg.k.TARGET_L4));
        NamedCommands.registerCommand("elev-L3", new ElevCommand(Cfg.k.TARGET_L3));
        NamedCommands.registerCommand("elev-L2", new ElevCommand(Cfg.k.TARGET_L2));
        NamedCommands.registerCommand("elev-CH", new ElevCommand(Cfg.k.TARGET_CH));

        NamedCommands.registerCommand("dump", new CoralSubSystem.DumpCommand());
    }

    @Override
    public void robotPeriodic() {
        Controls.scheduleCommands();
        CommandScheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {
        this.autoChooser.getSelected().schedule();
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {}

    @Override
    public void teleopPeriodic() {}

    @Override
    public void disabledInit() {
        CommandScheduler.getInstance().cancelAll();
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
}
