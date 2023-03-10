package frc.robot.subsystems;

import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.PneumaticsConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Arm extends SubsystemBase {

    private final DoubleSolenoid m_piston = new DoubleSolenoid(PneumaticsConstants.kPneumaticsHubPort,
            PneumaticsModuleType.REVPH, ArmConstants.kArmRaiseChannel, ArmConstants.kArmLowerChannel);

    private final CANSparkMax m_elevator1 = new CANSparkMax(ArmConstants.kElevatorMotor1Port, MotorType.kBrushless);
    private final CANSparkMax m_elevator2 = new CANSparkMax(ArmConstants.kElevatorMotor2Port, MotorType.kBrushless);
    private final MotorControllerGroup m_elevator = new MotorControllerGroup(m_elevator1, m_elevator2);
    private final RelativeEncoder m_elevatorEncoder = m_elevator1.getEncoder();

    private final PIDController pid = new PIDController(0.3, 0, 0);

    private double desiredPos = 0;

    private double lastPrintTime = 0;

    private boolean armEncoderResetting;

    private double armEncoderResetStartTime;

    public Arm() {

        m_elevator1.setInverted(ArmConstants.kElevatorMotor1Inverted);
        m_elevator1.setInverted(ArmConstants.kElevatorMotor2Inverted);

        m_elevator1.setIdleMode(IdleMode.kBrake);
        m_elevator2.setIdleMode(IdleMode.kBrake);

        m_elevatorEncoder.setPosition(0);

        pid.setTolerance(ArmConstants.kElevatorStopTolerance);

    }

    public boolean getElevatorExtended() {
        if (m_elevatorEncoder.getPosition() > ArmConstants.kElevatorStopTolerance) {
            return true;
        } else {
            return false;
        }
    }

    public void setElevatorExtended(boolean isExtended) {
        if (getPistonRaised() && isExtended) {
            desiredPos = (ArmConstants.kElevatorExtendRotations);
        } else {
            desiredPos = (ArmConstants.kElevatorIdleRotations);
        }
    }

    public boolean getPistonRaised() {
        if (m_piston.get() == PneumaticsConstants.kArmRaise) {
            return true;
        } else {
            return false;
        }
    }

    public void setPistonRaised(boolean isUp) {

        if (!isUp) {
            if (getElevatorExtended()) {
                setElevatorExtended(false);
                }
            m_piston.set(PneumaticsConstants.kArmLower);
        } else if (isUp) {
            m_piston.set(PneumaticsConstants.kArmRaise);
        }
    }

    @Override
    public void periodic() {
        double output;
        if (!armEncoderResetting) {
            output = pid.calculate(m_elevatorEncoder.getPosition(), desiredPos);

            output = Math.min(output, 1);
            output = Math.max(output, -1);
            output = output * ArmConstants.kMaximumElevatorSpeed;

        } else {
            double elapsed = System.currentTimeMillis() - armEncoderResetStartTime;
            if (elapsed  > 3000) {
                System.out.println("Timeout reached");
                armEncoderResetting=false;
                m_elevatorEncoder.setPosition(0);
                output=0;
            } else if (elapsed > 100) {
                double speed = Math.abs(m_elevatorEncoder.getVelocity());
                System.out.printf("Speed: %.2f\n", speed);
                if (speed < 15) {
                    output = 0;
                    armEncoderResetting = false;
                    m_elevatorEncoder.setPosition(0);
                } else {
                    output = -0.15;
                }
            } else {
                output = -0.15;
                System.out.println("waiting for movement");
            }

        }
        m_elevator.set(output);
        
        if (System.currentTimeMillis() - lastPrintTime > 100) {
            // System.out.println(output);
            // System.out.println(armEncoderResetting);
            lastPrintTime = System.currentTimeMillis();
        }
    }


    public void teleopInit() {
        armEncoderResetting = true;
        armEncoderResetStartTime = System.currentTimeMillis();
    }
}
