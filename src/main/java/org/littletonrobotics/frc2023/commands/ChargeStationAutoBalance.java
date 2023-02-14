// Copyright (c) 2023 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package org.littletonrobotics.frc2023.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.littletonrobotics.frc2023.Constants;
import org.littletonrobotics.frc2023.FieldConstants;
import org.littletonrobotics.frc2023.subsystems.drive.Drive;
import org.littletonrobotics.frc2023.util.LoggedTunableNumber;

public class ChargeStationAutoBalance extends CommandBase {
  private final Drive drive;
  private Rotation2d driveYaw;
  private Rotation2d drivePitch;
  private Rotation2d driveRoll;

  private static final LoggedTunableNumber AutoBalanceKP =
      new LoggedTunableNumber("ChargeStationAutoBalance/AutoBalanceKP");
  private static final LoggedTunableNumber AutoBalanceKI =
      new LoggedTunableNumber("ChargeStationAutoBalance/AutoBalanceKI");
  private static final LoggedTunableNumber AutoBalanceKD =
      new LoggedTunableNumber("ChargeStationAutoBalance/AutoBalanceKD");
  double driveSpeedMetersPerSec = 5.0;

  private final PIDController pidController =
      new PIDController(AutoBalanceKP.get(), AutoBalanceKI.get(), AutoBalanceKD.get());

  /** Creates a new ChargeStationAutoBalance. */
  public ChargeStationAutoBalance(Drive drive) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.drive = drive;
  }

  static {
    switch (Constants.getRobot()) {
      case ROBOT_2023P:
      case ROBOT_SIMBOT:
        AutoBalanceKP.initDefault(1.0);
        AutoBalanceKI.initDefault(0.0);
        AutoBalanceKD.initDefault(1.0);
        break;
      default:
        break;
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // update tunnable numbers if changed
    if (AutoBalanceKD.hasChanged(hashCode())
        || AutoBalanceKI.hasChanged(hashCode())
        || AutoBalanceKP.hasChanged(hashCode())) {
      pidController.setP(AutoBalanceKP.get());
      pidController.setI(AutoBalanceKI.get());
      pidController.setD(AutoBalanceKD.get());
    }

    // get rotation of robot
    driveRoll = drive.getRollRotation();
    drivePitch = drive.getPitchRotation();
    driveYaw = drive.getRotation();

    double chargeStationAngle =
        driveRoll.getRadians() * driveYaw.getSin() + drivePitch.getRadians() * driveYaw.getCos();

    // P controller
    double driveSpeedAfterPID = pidController.calculate(chargeStationAngle);

    // check boundaries for left of charge station
    if (drive.getPose().getX() <= FieldConstants.Community.chargingStationLeftY
        && driveSpeedAfterPID < 0) {
      driveSpeedAfterPID = 0;
    }

    // check boundaries for right of charge station
    if (drive.getPose().getX() <= FieldConstants.Community.chargingStationRightY
        && driveSpeedAfterPID > 0) {
      driveSpeedAfterPID = 0;
    }

    // command drive subsystem
    drive.runVelocity(ChassisSpeeds.fromFieldRelativeSpeeds(driveSpeedAfterPID, 0, 0, driveYaw));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
