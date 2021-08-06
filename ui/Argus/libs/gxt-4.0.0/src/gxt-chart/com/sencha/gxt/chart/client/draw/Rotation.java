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
 * Represents a two dimensional rotation.
 */
public class Rotation extends Translation {

  protected double degrees;

  /**
   * Creates a zeroed out rotation.
   */
  public Rotation() {
  }

  /**
   * Creates a rotation using the given degrees.
   * 
   * @param degrees the degree of rotation
   */
  public Rotation(double degrees) {
    x = 0;
    y = 0;
    this.degrees = degrees;
  }

  /**
   * Creates a rotation using the given axis and degrees.
   * 
   * @param x the x-coordinate of the axis of rotation
   * @param y the y-coordinate of the axis of rotation
   * @param degrees the degree of rotation
   */
  public Rotation(double x, double y, double degrees) {
    super(x, y);
    this.degrees = degrees;
  }

  /**
   * Creates a copy of the given rotation.
   * 
   * @param rotation the rotation to be copied
   */
  public Rotation(Rotation rotation) {
    super(rotation);
    if (rotation != null) {
      this.degrees = rotation.degrees;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Rotation)) {
      return false;
    }
    Rotation rotation = (Rotation) obj;
    return (this.x == rotation.x) && (this.y == rotation.y) && (this.degrees == rotation.degrees);
  }

  /**
   * Returns the degree of rotation.
   * 
   * @return the degree of rotation
   */
  public double getDegrees() {
    return degrees;
  }

  /**
   * Sets the degree of rotation.
   * 
   * @param degrees the degree of rotation
   */
  public void setDegrees(double degrees) {
    this.degrees = degrees;
  }

}
