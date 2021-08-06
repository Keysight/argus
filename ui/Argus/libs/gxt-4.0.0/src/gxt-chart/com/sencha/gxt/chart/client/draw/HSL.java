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
 * Represent an HSL color.
 */
public class HSL extends Hue {

  private double lightness = 0;

  /**
   * Creates an HSL instance with default values.
   */
  public HSL() {
  }

  /**
   * Creates an HSV instance using the given hue, saturation and lightness.
   * 
   * @param hue the hue value
   * @param saturation the saturation value
   * @param lightness the lightness value
   */
  public HSL(double hue, double saturation, double lightness) {
    setHue(hue);
    setSaturation(saturation);
    setLightness(lightness);
  }

  /**
   * Creates an HSV instance from the given {@link RGB} color.
   * 
   * @param rgb the RGB color
   */
  public HSL(RGB rgb) {
    double r = rgb.getRed();
    double g = rgb.getGreen();
    double b = rgb.getBlue();
    double max = Math.max(r, Math.max(g, b));
    double min = Math.min(r, Math.min(g, b));
    setLightness((max + min) / 2.0 / 255.0);

    // min==max means achromatic (hue is undefined, saturation is zero)
    if (min != max) {
      double delta = max - min;
      setSaturation((getLightness() < 0.5) ? delta / (max + min) : delta / (2 * 255.0 - max - min));
      if (r == max) {
        setHue(60 * (g - b) / delta);
      } else if (g == max) {
        setHue(120 + 60 * (b - r) / delta);
      } else {
        assert b == max : "No color matched max value, shouldn't be possible";
        setHue(240 + 60 * (r - g) / delta);
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof HSL)) return false;
    HSL other = (HSL) obj;
    //hue may not match and still be equal if lightness is zero or saturation is 0 or 1
    if (getHue() != other.getHue() && (getLightness() != 0 && getSaturation() != 0 && getSaturation() != 1)) return false;
    if (getSaturation() != other.getSaturation()) return false;
    if (getLightness() != other.getLightness()) return false;
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
   * Returns the lightness value.
   * 
   * @return the lightness value
   */
  public double getLightness() {
    return lightness;
  }

  /**
   * Sets the lightness value, a double value between 0.0 and 1.0.
   * 
   * @param lightness the lightness value
   */
  public void setLightness(double lightness) {
    this.lightness = Math.min(1.0, Math.max(0.0, lightness));
    color = null;
  }

  @Override
  public String toString() {
    return new StringBuilder().append("hsl(").append(getHue()).append(", ").append(getSaturation()).append(", ").append(lightness).append(
        ")").toString();
  }

}
