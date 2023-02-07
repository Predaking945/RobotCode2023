// Copyright (c) 2023 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package org.littletonrobotics.frc2023.oi;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/** Class for controlling the robot with a single Xbox controller. */
public class SingleHandheldOI extends HandheldOI {
  private final CommandXboxController controller;

  public SingleHandheldOI(int port) {
    controller = new CommandXboxController(port);
  }

  @Override
  public double getLeftDriveX() {
    return -controller.getLeftY();
  }

  @Override
  public double getLeftDriveY() {
    return -controller.getLeftX();
  }

  @Override
  public double getRightDriveX() {
    return 0.0;
  }

  @Override
  public double getRightDriveY() {
    return (controller.getHID().getRawButton(7) ? 0.3 : 0.0)
        + (controller.getHID().getRawButton(8) ? -0.3 : 0.0);
  }

  @Override
  public Trigger getDriverAssist() {
    return controller.leftBumper();
  }

  public Trigger getArmTest1() {
    return controller.button(1);
  }

  public Trigger getArmTest2() {
    return controller.button(2);
  }

  public Trigger getArmTest3() {
    return controller.button(3);
  }

  public Trigger getArmTest4() {
    return controller.button(4);
  }

  public Trigger getArmTest5() {
    return controller.rightBumper();
  }

  public double getArmX() {
    return controller.getRawAxis(2);
  }

  public double getArmY() {
    return -controller.getRawAxis(3);
  }

  // @Override
  // public Trigger getResetGyro() {
  //   return controller.start().or(controller.back());
  // }

  @Override
  public void setDriverRumble(double percent) {
    controller.getHID().setRumble(RumbleType.kRightRumble, percent);
  }

  @Override
  public void setOperatorRumble(double percent) {
    controller.getHID().setRumble(RumbleType.kRightRumble, percent);
  }
}
