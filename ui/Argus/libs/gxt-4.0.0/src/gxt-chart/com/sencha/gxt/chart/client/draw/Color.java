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
 * Base color class that represents a String color value.
 */
public class Color {

  protected String color;

  /**
   * Represents no color value;
   */
  public static final Color NONE = new Color("none");

  /**
   * Constructs an empty color object.
   */
  public Color() {
  }

  /**
   * Constructs a color using the given literal string.
   * 
   * @param color the color string
   */
  public Color(String color) {
    this.color = color;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof Color)) return false;
    Color other = (Color) obj;
    if (color == null) {
      if (other.color != null) return false;
    } else if (!color.equals(other.color)) return false;
    return true;
  }

  /**
   * Returns the literal string of the color.
   * 
   * @return the literal string of the color
   */
  public String getColor() {
    return color;
  }

  /**
   * Manually sets the color string of the color object. Should not be called in most subclasses of Color.
   * 
   * @param color the new literal color string to set
   */
  public void setColor(String color) {
    this.color = color;
  }

  /**
   * Return the color in String format.
   * 
   * @return string value
   */
  @Override
  public String toString() {
    return color;
  }

}
