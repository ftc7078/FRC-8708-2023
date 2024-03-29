package frc.robot.commands.experimental;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.math.controller.PIDController;

public class DriveForTrap extends CommandBase {
    private final double m_distance;
    private final double m_maxSpeed;
    private final Drivetrain m_drivetrain;
    private final ADIS16470_IMU m_gyro;
    private final boolean m_brake;

    private double m_startingPosition;
    private double m_heading;
    private boolean headingIsSpecified = false;
    //private double m_lastError;
    //private double m_integral;

    private static final double kPL = 0.04;
    private static final double kPA = 0.1;
    private static final double kI = 0.0;
    private static final double kD = 0.0;

    //private final SimpleMotorFeedforward m_feedforward = new SimpleMotorFeedforward(
    //        kS, kV, kA);

    private final PIDController m_drivePID = new PIDController(kPL, kI, kD);

    private final PIDController m_anglePID = new PIDController(kPA, kI, kD);

    public DriveForTrap(double distance_in, double maxSpeed, Drivetrain drivetrain, boolean brake) {
        /**
        * Uses ramp-up and ramp-down
        * If brake then ramp down
        * else coast (don't ramp down)
        * 
        * 
        */
        
        m_distance = distance_in;
        m_maxSpeed = maxSpeed;
        m_drivetrain = drivetrain;
        m_gyro = drivetrain.gyro;
        m_brake = brake;

        addRequirements(drivetrain);
    }

    public DriveForTrap(double targetHeading, double distance_in, double maxSpeed, Drivetrain drivetrain, boolean brake) {
        /**
        * Uses ramp-up and ramp-down
        * If brake then ramp down
        * else coast (don't ramp down)
        * 
        * 
        */
        
        m_distance = distance_in;
        m_maxSpeed = maxSpeed;
        m_drivetrain = drivetrain;
        m_gyro = drivetrain.gyro;
        m_brake = brake;

        m_heading = targetHeading;
        headingIsSpecified = true;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        m_startingPosition = m_drivetrain.getAvgEncoder();
        if (!headingIsSpecified) {
            m_heading = m_drivetrain.targetHeading;
        }
        System.out.println("Starting" + m_distance);
        
        //m_lastError = 0;
        //m_integral = 0;
        m_drivetrain.setRampRate(0.4);

        m_anglePID.setTolerance(DriveConstants.kAllowableHeadingOffset);
        m_drivePID.setTolerance(DriveConstants.kDriveDistanceTolerance);
        m_drivetrain.setBrakeMode(m_brake);
    }

    public double clamp(double in, double m) {
        return (in>m?m:(in<-m?-m:in));
    }

    @Override
    public void execute() {
        double distanceTraveled = -(m_drivetrain.getAvgEncoder() - m_startingPosition) * DriveConstants.kSlowRevPerRot * DriveConstants.kWheelCircumference;
        double error = m_distance - distanceTraveled; 

        //System.out.println(error);
        
        //double velocity = m_maxSpeed;

        double headingError = m_gyro.getAngle() - m_heading;
        //m_integral += headingError;

        double forwardPower = kPL*error;//m_drivePID.calculate(error,0);
        double turnPower = m_anglePID.calculate(headingError, 0) * DriveConstants.kTurnAggression;
        //turnPower += m_pidController.calculate(m_integral, 0) * DriveConstants.kTurnIntegral; // what is this

        double leftPower = forwardPower - turnPower;
        double rightPower = forwardPower + turnPower;

        //velocity *= Math.min(Math.abs(error) / DriveConstants.kDriveDistanceTolerance, 1);

        leftPower = clamp(leftPower, m_maxSpeed);
        rightPower = clamp(rightPower, m_maxSpeed);

        System.out.println("DIST: "+distanceTraveled+" -> E: "+error);

        //leftPower *= velocity;
        //rightPower *= velocity;

        if (!m_brake) {
            m_drivetrain.tankDriveRawCorrectDirection(Math.copySign(m_maxSpeed,m_distance), Math.copySign(m_maxSpeed,m_distance), false);
        } else {
            m_drivetrain.tankDriveRawCorrectDirection(leftPower, rightPower, false);
        }
        //System.out.println(error);
    }

    @Override
    public boolean isFinished() {
        double distanceTraveled = Math.abs(m_drivetrain.getAvgEncoder() - m_startingPosition) * DriveConstants.kSlowRevPerRot * DriveConstants.kWheelCircumference;
        return Math.abs(Math.abs(m_distance) - distanceTraveled) < DriveConstants.kDriveDistanceTolerance;
    }

    @Override
    public void end(boolean interrupted) {
        
        m_drivetrain.tankDriveRaw(0,0,false);
        System.out.println("done");
    }
}