// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public final class Constants {
  
  public static final class OperatorConstants {
    //HARDWARE CONFIG:
    public static final int kDriverLeftJoystickPort = 0;
    public static final int kDriverRightJoystickPort = 1;
    public static final int kManipulatorControllerPort = 2;
    //SOFTWARE CONFIG:
    public static final double kInputDeadzone = 0.1f;
    public static final double kInputLinearity = 0.0f;

    public static final int kCubicOnly = 0;
    public static final int kSnapToForward = 1;
    public static final int kTurnRateLimiting = 2;

    // The ramp limits are the amount of seconds it should take to go from 0% to 100% speed while shifted to high or low gear
    public static final double kRampLimitLowGearSeconds = 1;
    public static final double kRampLimitHighGearSeconds = 0;


    public static final int kDriveNormalizationType = kTurnRateLimiting;

    public static final double kSlowModeMultiplier = 0.3;
    public static final double kLightsTimeoutSeconds = 5;

    public static final boolean kLimitTurnAcceleration = true;
    public static final double kMaximumTurnAccelerationPerSecond = 0.5;
  }
  public static final class PneumaticsConstants{
    //HARDWARE CONFIG:
    public static final int kPneumaticsHubPort = 22;
    public static final Value kShifterHighSpeed = Value.kReverse;
    public static final Value kShifterLowSpeed = Value.kForward;

    public static final Value kArmRaise = Value.kForward;
    public static final Value kArmLower = Value.kReverse;
  }
  public static final class DriveConstants {
    //HARDWARE CONFIG:
    // Left side
    public static final int kLeftMotor1Port = 1;
    public static final int kLeftMotor2Port = 2;
    public static final boolean kLeftMotorsInverted = false;

    // Right side
    public static final int kRightMotor1Port = 3;
    public static final int kRightMotor2Port = 4;
    public static final boolean kRightMotorsInverted = true;

    // Shifter
    public static final int kShifterHighSpeedChannel = 0;
    public static final int kShifterLowSpeedChannel = 1;
    //SOFTWARE CONFIG:
    public static final double kMaximumDrivetrainSpeed = 1.0;

    public static final double kFastRevPerRot = 55d/544d; // wheel revolutions per motor rotation
    public static final double kSlowRevPerRot = 11d/288d;
    public static final double kWheelCircumference = 6 * Math.PI * 2.54;
    public static final double kTurnCircumference = 20 * Math.PI * 2.54;
  }
  public static final class ArmConstants {
    //HARDWARE CONFIG:
    // Pneumatic channels
    public static final int kArmRaiseChannel = 2;
    public static final int kArmLowerChannel = 3;

    public static final int kElbowMotorPort = 5;
    public static final int kElevatorMotor1Port = 6;
    public static final int kElevatorMotor2Port = 7;

    public static final double kElevatorExtendRotations = 10.0;
    public static final double kMaximumElevatorSpeed = 0.1;
    public static final double kElevatorStopThreshold = 0.1;

    public static final double kLowElbowExtendRotations = 14;
    public static final double kLowMaximumElbowSpeed = 0.1;
    public static final double kLowElbowStopThreshold = 0.5;

    public static final double kHighElbowExtendRotations =18;
    public static final double kHighMaximumElbowSpeed = 0.1;
    public static final double kHighElbowStopThreshold = 0.5;
  }
  public static final class IntakeConstants {
    public static final int kIntakeMotorTop = 8;
    public static final int kIntakeMotorBottom = 9;

    public static final boolean kIntakeMotorTopReversed = true;
    public static final boolean kIntakeMotorBottomReversed = true;

    public static final double kMaximumIntakeInSpeed = 0.5;
    public static final double kMaximumIntakeOutSpeed = 1.0;

    public static final int kIntakeDisabled = 0;
    public static final int kIntakeIn = 1;
    public static final int kIntakeOut = 2;
  }

  public static final class LightStripConstants {
    public static final int kLightstripPort = 0;
    public static final int kLightstripLength = 60;

    public static final int kOff = 0;
    public static final int kOrange = 1;
    public static final int kYellow = 2;
    public static final int kPurple = 3;
    public static final int kSlowChaser = 4;
    public static final int kFastChaser = 5;
  }
}
