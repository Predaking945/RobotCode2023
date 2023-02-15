// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.littletonrobotics.frc2023.subsystems.leds;

import java.util.HashMap;
import java.util.Map;

import org.littletonrobotics.frc2023.util.BlinkinLedDriver;
import org.littletonrobotics.frc2023.util.BlinkinLedDriver.BlinkinLedMode;

/** Add your docs here. */
public class LedsIOBlinkin implements LedsIO {
  private static final Map<LedMode, BlinkinLedMode> modeLookup = new HashMap<>();

  static {
    modeLookup.put(LedMode.INTAKING, BlinkinLedMode.SOLID_BLUE);
  }

  private final BlinkinLedDriver blinkin;

  public LedsIOBlinkin() {
    blinkin = new BlinkinLedDriver(0);
  }

  @Override
  public void setMode(LedMode mode, boolean sameBattery) {
    if (modeLookup.containsKey(mode)) {
      blinkin.setMode(modeLookup.get(mode));
    } else {
      blinkin.setMode(BlinkinLedMode.SOLID_BLACK);
    }
  }
}
