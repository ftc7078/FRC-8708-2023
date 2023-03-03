package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elbow;
import frc.robot.subsystems.Intake;

public class AutonSimple extends SequentialCommandGroup {

    public AutonSimple(
        Drivetrain drive,
        Arm arm,
        Elbow elbow,
        Intake intake
    ) {
        addCommands(
            new RunCommand(() -> elbow.setElbowExtended(true),arm),
            new WaitCommand(3),
            new RunCommand(intake::intakeOut,intake),
            new WaitCommand(1),
            new RunCommand(() -> elbow.setElbowExtended(false),arm),
            new TurnFor(180, 0.7, drive),
            new DriveFor(100, 1, drive)
        );

    }
}
