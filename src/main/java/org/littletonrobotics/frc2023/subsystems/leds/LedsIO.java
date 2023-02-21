// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.littletonrobotics.frc2023.subsystems.leds;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** Leds hardware interface. */  
public interface LedsIO {

  /** Sets the current LED mode */
  public default void setMode(LedMode mode, boolean sameBattery) {}
  
  /**
   * Possible LED modes based on robot state, IO implementations should select an appropriate
   * pattern.
   */
  public static enum LedMode
  {
    DEMO_CHROMA,
    DEMO_TEAM,
    DISABLED_NEUTRAL,
    DISABLED_RED,
    DISABLED_BLUE,
    INTAKING_CUBE,
    INTAKING_CONE
  }
}
