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
 * Represents a two dimensional translation.
 */
public class Translation {
  protected double x;
  protected double y;

  /**
   * Creates a zeroed out translation.
   */
  public Translation() {
  }

  /**
   * Creates a translation using the given x and y values.
   * 
   * @param x translation on the x axis
   * @param y translation on the y axis
   */
  public Translation(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Creates a copy of the given translation.
   * 
   * @param translation the translation to be copied
   */
  public Translation(Translation translation) {
    if (translation != null) {
      this.x = translation.x;
      this.y = translation.y;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Translation)) {
      return false;
    }
    Translation translation = (Translation) obj;
    return (this.x == translation.x) && (this.y == translation.y);
  }

  /**
   * Returns the translation on the x axis.
   * 
   * @return the translation on the x axis
   */
  public double getX() {
    return x;
  }

  /**
   * Returns the translation on the y axis.
   * 
   * @return the translation on the y axis
   */
  public double getY() {
    return y;
  }

  /**
   * Sets the translation on the x axis.
   * 
   * @param x the translation on the x axis
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * Sets the translation on the y axis.
   * 
   * @param y the translation on the y axis
   */
  public void setY(double y) {
    this.y = y;
  }

  @Override
  public String toString() {
    return new StringBuilder().append("x:").append(x).append(", y:").append(y).toString();
  }

}
