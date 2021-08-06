/**
 * Sencha GXT 4.0.0 - Sencha for GWT
 * Copyright (c) 2006-2015, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Open Source License
 * ================================================================================
 * This version of Sencha GXT is licensed under the terms of the Open Source GPL v3
 * license. You may use this license only if you are prepared to distribute and
 * share the source code of your application under the GPL v3 license:
 * http://www.gnu.org/licenses/gpl.html
 *
 * If you are NOT prepared to distribute and share the source code of your
 * application under the GPL v3 license, other commercial and oem licenses
 * are available for an alternate download of Sencha GXT.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package com.sencha.gxt.chart.client.draw;

/**
 * Represents an HSV color.
 */
public class HSV extends Hue {

  private double value = 0;

  /**
   * Creates an instance of HSV with default values.
   */
  public HSV() {
  }

  /**
   * Creates an instance of HSV with the given hue, saturation and value
   * 
   * @param hue the hue value
   * @param saturation the saturation value
   * @param value the value value
   */
  public HSV(double hue, double saturation, double value) {
    setHue(hue);
    setSaturation(saturation);
    setValue(value);
  }

  /**
   * Creates an instance of HSV with the given RGB color.
   * 
   * @param rgb the RGB color
   */
  public HSV(RGB rgb) {
    double r = rgb.getRed();
    double g = rgb.getGreen();
    double b = rgb.getBlue();
    double min = Math.min(r, Math.min(g, b));
    double max = Math.max(r, Math.max(g, b));
    setValue(max / 255.0);

    // min == max means achromatic (hue is undefined, saturation is zero)
    if (min != max) {
      double delta = max - min;
      setSaturation(delta / max);
      if (r == max) {
        setHue(60.0 * (g - b) / delta);
      } else if (g == max) {
        setHue(120.0 + 60.0 * (b - r) / delta);
      } else {
        assert b == max : "No color matched max value, shouldn't be possible";
        setHue(240.0 + 60.0 * (r - g) / delta);
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof HSV)) return false;
    HSV other = (HSV) obj;
    //hue may not match and still be equal if saturation is zero or value is zero
    if (getHue() != other.getHue() && (getSaturation() != 0 && getValue() != 0)) return false;
    if (getSaturation() != other.getSaturation()) return false;
    if (getValue() != other.getValue()) return false;
    return true;
  }

  @Override
  public String getColor() {
    if (color == null) {
      color = (new RGB(this)).getColor();
    }
    return color;
  }

  /**
   * Returns the value.
   * 
   * @return the value
   */
  public double getValue() {
    return value;
  }

  /**
   * Sets the value, a double value between 0.0 and 1.0.
   * 
   * @param value the value
   */
  public void setValue(double value) {
    this.value = Math.min(1.0, Math.max(0.0, value));
    color = null;
  }

  @Override
  public String toString() {
    return new StringBuilder().append("hsv(").append(getHue()).append(", ").append(getSaturation()).append(", ").append(value).append(
        ")").toString();
  }

}
