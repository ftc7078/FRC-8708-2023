package frc.robot.subsystems;

import frc.robot.Constants.ArmConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elbow extends SubsystemBase {

    private final CANSparkMax m_elbow = new CANSparkMax(ArmConstants.kElbowMotorPort, MotorType.kBrushless);
    private final RelativeEncoder m_elbowEncoder = m_elbow.getEncoder();
    private Arm m_arm;

    private double target;

    private final PIDController pid = new PIDController(0.1,0,0);

    public Elbow(Arm arm) {
        m_arm = arm;
        m_elbow.setInverted(true);
        m_elbow.setIdleMode(IdleMode.kBrake);
    }

    public void setElbowExtended(boolean isExtended) {
        if (isExtended) {
            if (m_arm.getPistonRaised()) {
                target = (ArmConstants.kHighElbowExtendRotations);
            } else {
                target = (ArmConstants.kLowElbowExtendRotations);
            }
        } else {
            target = (0);
        }
    }

    @Override
    public void periodic() {
        m_elbow.set(pid.calculate(m_elbowEncoder.getPosition(),target));
    }
}
