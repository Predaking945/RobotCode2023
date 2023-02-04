// Copyright (c) 2023 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package org.littletonrobotics.frc2023;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;

public class LedTesting {
  AddressableLED led = new AddressableLED(0); // PWM port 0
  AddressableLEDBuffer ledBuffer = new AddressableLEDBuffer(60);

  private void rainbow(int rainbowFirstPixelHue) {
    for (var i = 0; i < ledBuffer.getLength(); i++) {
      final var hue = rainbowFirstPixelHue + (i * 180 / ledBuffer.getLength()) % 180;
      ledBuffer.setHSV(i, hue, 255, 128);
    }
    rainbowFirstPixelHue += 3;
    rainbowFirstPixelHue %= 180;
    led.setLength(ledBuffer.getLength());
    led.start();
    led.setData(ledBuffer);
  }

  private void yellow(int time) {
    var Time = time % 60;
    var time2 = Time;
    if (Time < 5) {
      time2 += 55;
    } else {
      time2 -= 5;
    }
    for (var i = 0; i < ledBuffer.getLength(); i++) {
      ledBuffer.setRGB(Time, 255, 230, 0);
      ledBuffer.setRGB(time2, 0, 0, 0);
    }
    led.setLength(ledBuffer.getLength());
    led.start();
    led.setData(ledBuffer);
  }

  public void periodic() {
    // rainbow((int) (Timer.getFPGATimestamp() * 20));
    yellow((int) (Timer.getFPGATimestamp() * 20));
  }
}
