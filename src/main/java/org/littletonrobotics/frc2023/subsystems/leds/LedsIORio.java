// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.littletonrobotics.frc2023.subsystems.leds;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;

/** Add your docs here. */
public class LedsIORio implements LedsIO {
  private static final int length = 10;
  private static final int centerLed = 5;
  private static final int halfLength = (int) Math.ceil(length / 2.0);
  private static final int batteryStartIndex = 72;
  private static final int batteryEndIndex = 118;
  private static final double strobeDuration = 0.2; // How long is each flash
  private static final double rainbowFastFullLength = 40.0; // How many LEDs for a full cycle
  private static final double rainbowFastDuration = 0.25; // How long until the cycle repeats
  private static final double rainbowSlowFullLength = 80.0; // How many LEDs for a full cycle
  private static final double rainbowSlowDuration = 4.0; // How long until the cycle repeats
  private static final double breathDuration = 2.0; // How long until the cycle repeats
  private static final double waveExponent = 0.4; // Controls the length of the transition
  private static final double waveFastFullLength = 40.0; // How many LEDs for a full cycle
  private static final double waveFastDuration = 0.25; // How long until the cycle repeats
  private static final double waveAllianceFullLength = 15.0; // How many LEDs for a full cycle
  private static final double waveAllianceDuration = 2.0; // How many LEDs for a full cycle
  private static final double waveSlowFullLength = 40.0; // How many LEDs for a full cycle
  private static final double waveSlowDuration = 3.0; // How long until the cycle repeats

  private final AddressableLED leds;
  private final AddressableLEDBuffer buffer;

  public LedsIORio()
  {
    leds = new AddressableLED(0);
    buffer = new AddressableLEDBuffer(length);

    leds.setLength(length);
    leds.setData(buffer);
    leds.start();
  }

  @Override
  public void setMode(LedMode mode, boolean sameBattery)
  {
    switch (mode) {
      case DEMO_CHROMA:
        break;
      case DEMO_TEAM:
        break;
      case DISABLED_NEUTRAL:
        break;
      case DISABLED_RED:
        break;
      case DISABLED_BLUE:
        break;
      case INTAKING_CUBE:
        break;
      case INTAKING_CONE:
        break;
      default:
        break;
    }
    if (sameBattery) {
      boolean on = ((Timer.getFPGATimestamp() % strobeDuration) / strobeDuration) > 0.5;
      for (int i = batteryStartIndex; i < batteryEndIndex; i++) {
        buffer.setLED(i, on ? Color.kRed : Color.kBlack);
      }
    }
    leds.setData(buffer);
  }
  
  private void solid(Color color)
  {
    for (int led = 0; led < length; led++) {
      buffer.setLED(led, color);
    }
  }

  private void strobe(Color color)
  {
    boolean on =
        ((Timer.getFPGATimestamp() % strobeDuration) / strobeDuration) > 0.5;
    solid(on ? color : Color.kBlack);
  }
  private void breath(Color c1, Color c2) {
    double x = ((Timer.getFPGATimestamp() % breathDuration) / breathDuration)
        * 2.0 * Math.PI;
    double ratio = (Math.sin(x) + 1.0) / 2.0;
    double red = (c1.red * (1 - ratio)) + (c2.red * ratio);
    double green = (c1.green * (1 - ratio)) + (c2.green * ratio);
    double blue = (c1.blue * (1 - ratio)) + (c2.blue * ratio);
    solid(new Color(red, green, blue));
  }

  private void rainbow(double fullLength, double duration) {
    double x = (1 - ((Timer.getFPGATimestamp() / duration) % 1.0)) * 180.0;
    double xDiffPerLed = 180.0 / fullLength;
    for (int i = 0; i < halfLength; i++) {
      x += xDiffPerLed;
      x %= 180.0;
      setLedsSymmetrical(i, Color.fromHSV((int) x, 255, 255));
    }
  }

  private void wave(Color c1, Color c2, double fullLength, double duration) {
    double x = (1 - ((Timer.getFPGATimestamp() % duration) / duration)) * 2.0
        * Math.PI;
    double xDiffPerLed = (2.0 * Math.PI) / fullLength;
    for (int i = 0; i < halfLength; i++) {
      x += xDiffPerLed;
      double ratio = (Math.pow(Math.sin(x), waveExponent) + 1.0) / 2.0;
      if (Double.isNaN(ratio)) {
        ratio = (-Math.pow(Math.sin(x + Math.PI), waveExponent) + 1.0) / 2.0;
      }
      if (Double.isNaN(ratio)) {
        ratio = 0.5;
      }
      double red = (c1.red * (1 - ratio)) + (c2.red * ratio);
      double green = (c1.green * (1 - ratio)) + (c2.green * ratio);
      double blue = (c1.blue * (1 - ratio)) + (c2.blue * ratio);
      setLedsSymmetrical(i, new Color(red, green, blue));
    }
  }

  private void setLedsSymmetrical(int index, Color color) {
    buffer.setLED((centerLed + index) % length, color);
    buffer.setLED(centerLed - index, color);
  }
}
