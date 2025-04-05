// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.gmail.frcteam1758.lib.swervedrive.control.SwerveDriveState;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathPlannerPath;

import au.grapplerobotics.CanBridge;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.config.Cfg;
import frc.robot.coral.DumpSubSystem;
import frc.robot.drive.AlignCommand;
import frc.robot.drive.AlignSenseCommand;
import frc.robot.drive.DriveSubSystem;
import frc.robot.elevt.ElevCommand;
import frc.robot.elevt.ElevtSubSystem;
import frc.robot.elevt.HoldCommand;
import frc.robot.multi.JSMainControls;
import frc.robot.multi.LaserTargeting;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {

    private SendableChooser<Command> autoChooser;

    public Robot() {}

    @Override
    public void robotInit() {
        CanBridge.runTCP();

        CameraServer.startAutomaticCapture();

        DriveSubSystem.INSTANCE.register();
        DumpSubSystem.INSTANCE.register();
        ElevtSubSystem.INSTANCE.register();

        NamedCommands.registerCommand("elev-L4", new ElevCommand(Cfg.k.TARGET_L4));
        NamedCommands.registerCommand("elev-L3", new ElevCommand(Cfg.k.TARGET_L3));
        NamedCommands.registerCommand("elev-L2", new ElevCommand(Cfg.k.TARGET_L2));
        NamedCommands.registerCommand("elev-CH", new ElevCommand(Cfg.k.TARGET_CH));

        NamedCommands.registerCommand("dump", new DumpSubSystem.DumpCommand(50));
        NamedCommands.registerCommand("pull", new DumpSubSystem.DumpCommand(300));

        NamedCommands.registerCommand("hold", new HoldCommand(false, false));

        NamedCommands.registerCommand("align-sense", new AlignSenseCommand());

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

            SmartDashboard.putData("Autos", autoChooser = AutoBuilder.buildAutoChooser());
        }
        catch (Exception e) {
            e.printStackTrace();
    
            SmartDashboard.putData("autos", autoChooser = new SendableChooser<Command>());
        }
        autoChooser.setDefaultOption("NONE", new InstantCommand());

        try { LaserTargeting.init(); } catch (Exception notAProblem) {}

        Command midAuto = Commands.none();

        try {

        midAuto = new SequentialCommandGroup(
            AutoBuilder.resetOdom(
                PathPlannerPath.fromPathFile("mid to far (dangle alt)").getStartingHolonomicPose().get()),
            new ParallelRaceGroup(
                new WaitCommand(5),
                AutoBuilder.followPath(PathPlannerPath.fromPathFile("mid to far (dangle alt)"))
            ),
            new ElevCommand(Cfg.k.TARGET_L4),
            new AlignCommand(),
            new ParallelRaceGroup(
                new WaitCommand(0.15),
                new RunCommand(
                    () -> DriveSubSystem.INSTANCE.chassis.run(
                        new SwerveDriveState(
                            new ChassisSpeeds(0.1, 0.3, 0)
                        )
                    ),
                    DriveSubSystem.INSTANCE
                )
            ),
            //changed to fix auto
            new RunOnceCommand(
                () -> DriveSubSystem.INSTANCE.chassis.run(
                    new SwerveDriveState(
                        new ChassisSpeeds(0, 0, 0)
                    )
                ),
                DriveSubSystem.INSTANCE
            ),
            new DumpSubSystem.DumpCommand(50)
        );

        } catch (Exception e) {}

        Command midTestAuto = new SequentialCommandGroup(
            //AutoBuilder.buildAuto("mid2"),
            new ElevCommand(Cfg.k.TARGET_L4),
            new AlignCommand(),
            new ParallelRaceGroup(
                new WaitCommand(0.2),
                new RunCommand(
                    () -> DriveSubSystem.INSTANCE.chassis.run(
                        new SwerveDriveState(
                            new ChassisSpeeds(0, 0.3, 0)
                        )
                    ),
                    DriveSubSystem.INSTANCE
                )
            ),
            new InstantCommand(
                () -> DriveSubSystem.INSTANCE.chassis.run(SwerveDriveState.LOCKED),
                DriveSubSystem.INSTANCE
            ),
            new DumpSubSystem.DumpCommand(50)
        );

        autoChooser.addOption("mid-good", midAuto);
        autoChooser.addOption("mid-test", midTestAuto);
    }

    @Override
    public void robotPeriodic() {
        JSMainControls.scheduleCommands();
        LaserTargeting.run();
        CommandScheduler.getInstance().run();

        SmartDashboard.putNumber("odoX", DriveSubSystem.INSTANCE.chassis.getPose().getX());
        SmartDashboard.putNumber("odoY", DriveSubSystem.INSTANCE.chassis.getPose().getY());
        SmartDashboard.putNumber("odoR", DriveSubSystem.INSTANCE.chassis.getPose().getRotation().getDegrees());

        SmartDashboard.putBoolean("align[.get()]", LaserTargeting.get());
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
