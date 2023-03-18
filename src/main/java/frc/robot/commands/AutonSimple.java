package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

public class AutonSimple extends SequentialCommandGroup {

    public AutonSimple(
            Drivetrain drive,
            Arm arm,
            Intake intake) {
        addCommands(
            // new RunCommand(() -> arm.setElbowExtended(true),arm),
            // new WaitCommand(3),
            //new InstantCommand(() -> intake.intakeOut(1.0), intake),
            //new WaitCommand(1),
            //new InstantCommand(intake::intakeStop, intake),
            // new RunCommand(() -> arm.setElbowExtended(false),arm),
            // new TurnFor(180, 0.7, drive),
            new DriveFor(10, 0.1, drive)//.withTimeout(2),
            //new InstantCommand(() -> drive.tankDriveRaw(0, 0, false), drive)
        );

    }
}