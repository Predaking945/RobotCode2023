// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.littletonrobotics.frc2023.subsystems.leds;

import java.util.function.Supplier;

import org.littletonrobotics.frc2023.subsystems.drive.Drive;
import org.littletonrobotics.frc2023.subsystems.leds.LedsIO.LedMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Leds extends SubsystemBase {
  private final LedsIO io;
  private Supplier<String> demoModeSupplier = () -> "";
  private Alliance alliance = Alliance.Invalid;

  private boolean intaking = false;
  private boolean sameBattery = false;
  

  /** Creates a new Leds. */
  public Leds(LedsIO io) 
  {
    this.io = io;
  }

  public void setDemoModeSupplier(Supplier<String> demoModeSupplier)
  {
    this.demoModeSupplier = demoModeSupplier;
  }

  public void update()
  {
    if (DriverStation.isFMSAttached()) {
      alliance = DriverStation.getAlliance();
    }

    // get demo mode
    boolean demoTeam = false;
    boolean demoChroma = false;
    switch (demoModeSupplier.get()) {
      case "Team Colors":
        demoTeam = true;
        break;
      case "Rainbow":
        demoChroma = true;
        break;
    }

    // Select LED mode
    LedMode mode = LedMode.DISABLED_NEUTRAL;
    if (demoTeam) {
      mode = LedMode.DEMO_TEAM;
    } else if (demoChroma) {
      mode = LedMode.DEMO_CHROMA;
    } else if (DriverStation.isDisabled()) {
      switch (alliance) {
        case Red:
          mode = LedMode.DISABLED_RED;
          break;
        case Blue:
          mode = LedMode.DISABLED_BLUE;
          break;
        default:
          mode = LedMode.DISABLED_NEUTRAL;
          break;
      }
    } else if (intaking) {
      mode = LedMode.INTAKING;
    }
  }
  
  public void setIntaking(boolean active) {
    intaking = active;
  }

  public void setSameBetteryAlert(boolean active) {
    sameBattery = active;
  }
}
