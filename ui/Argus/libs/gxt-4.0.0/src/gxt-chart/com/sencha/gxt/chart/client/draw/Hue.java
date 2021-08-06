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
 * Abstract color representing hue and saturation values.
 */
public abstract class Hue extends Color {

  private double hue = 0;
  private double saturation = 0;

  /**
   * Returns the hue value.
   * 
   * @return the hue value
   */
  public double getHue() {
    return hue;
  }

  /**
   * Returns the saturation value.
   * 
   * @return the saturation value
   */
  public double getSaturation() {
    return saturation;
  }

  /**
   * Sets the hue value, a value in degrees. The value passed in will be adjusted to the range [0.0, 360.0).
   * 
   * @param hue the hue value
   */
  public void setHue(double hue) {
    double tmp = hue % 360.0;

    this.hue = tmp >= 0 ? tmp : (tmp + 360.0);
    color = null;
  }

  /**
   * Sets the saturation value, a double value between 0.0 and 1.0.
   * 
   * @param saturation the saturation value
   */
  public void setSaturation(double saturation) {
    this.saturation = Math.min(1.0, Math.max(0.0, saturation));
    color = null;
  }

}
