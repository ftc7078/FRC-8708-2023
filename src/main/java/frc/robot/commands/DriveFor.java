package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;

public class DriveFor extends CommandBase {

    private final double m_distance;
    private final double m_targetHeading;
    private final double m_speed;
    private final Drivetrain m_drive;
    private final double cmPerRot;
    private final boolean m_brake;
    private double delta_heading;
    private final double kDecelrationRate = 1.0; //motor power per second 
    private double m_rampUpDistance;
    private double m_rampDownDistance;

    private double start_pos;

    public DriveFor(double heading, double distance_in, double unsigned_speed, Drivetrain drive, boolean brake, double rampUpDistance, double rampDownDistance) {
        m_targetHeading = heading;
        m_distance = distance_in;
        if (distance_in < 0) {
            m_speed = -unsigned_speed;
        } else {
            m_speed = unsigned_speed;
        }
        m_drive = drive;
        m_brake = brake;
        m_rampUpDistance = rampUpDistance;
        m_rampDownDistance = rampDownDistance;
        cmPerRot = DriveConstants.kSlowRevPerRot * DriveConstants.kWheelCircumference;
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        start_pos = m_drive.getAvgEncoder();
        m_drive.tankDriveRaw(0, 0, false);
        // System.out.println("DISTANCE TO GO: " + m_distance);
        m_drive.setBrakeMode(m_brake);
        m_drive.setRampRate(0.5);
    }

    @Override
    public void execute() {
        double currentHeading = m_drive.gyro.getAngle() % 360;
        double leftTurnDifference = (currentHeading - m_targetHeading);
        double rightTurnDifference = (m_targetHeading - currentHeading);
        if (leftTurnDifference < 0) {
            leftTurnDifference += 360;
        }
        if (rightTurnDifference < 0) {
            rightTurnDifference += 360;
        }
       
        double distanceTraveled = Math.abs(m_drive.getAvgEncoder()-start_pos) * cmPerRot;
        double distanceRemaining = Math.abs(m_distance) - distanceTraveled;

        double targetSpeed = accelerationCurve(m_speed, distanceTraveled, distanceRemaining );
        if (Math.abs(leftTurnDifference) < Math.abs(rightTurnDifference)) {
            delta_heading = leftTurnDifference;
            m_drive.tankDriveRaw((delta_heading * -DriveConstants.kCorrectionAggression) - m_speed, (delta_heading * DriveConstants.kCorrectionAggression) - m_speed, false);
        } else {
            delta_heading = rightTurnDifference;
            m_drive.tankDriveRaw((delta_heading * DriveConstants.kCorrectionAggression) - m_speed, (delta_heading * -DriveConstants.kCorrectionAggression) - m_speed, false);
        }
        
    }

    
    private double accelerationCurve(double speed, double distance_traveled, double distance_remaining) {

        if (distance_remaining < m_rampDownDistance) {
            return(m_speed * distance_remaining / m_rampDownDistance);
        } else if (distance_traveled < m_rampUpDistance) {
            double speedToScale = Math.abs(m_speed) - DriveConstants.kMotorStallSpeed;
            double targetSpeed = (speedToScale * (distance_traveled / m_rampUpDistance) ) + DriveConstants.kMotorStallSpeed;
            return(Math.copySign(targetSpeed, m_speed));
        } else {
            return m_speed;
        }
    }

    @Override
    public boolean isFinished() {
        double avgDistance = Math.abs((m_drive.getAvgEncoder()-start_pos) * cmPerRot);
        // System.out.println("GONE: " + avgDistance);
        // System.out.println("DEGREES OFF COURSE: " + Math.abs(delta_heading));
        return (avgDistance >= Math.abs(m_distance));
    }

    @Override
    public void end(boolean interrupted) {
        m_drive.setRampRate(0);
        m_drive.tankDriveRaw(0, 0, false);
        System.out.println("DONE, DEGREES OFF COURSE: " + Math.abs(delta_heading));
    }
}
