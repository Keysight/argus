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
 * Represents a gradient stop.
 */
public class Stop {

  private final int offset;
  private final Color color;
  private final double opacity;

  /**
   * Creates a stop with the default values.
   */
  public Stop() {
    this(10, new Color("#fff"));
  }

  /**
   * Creates a stop using the given offset and color.
   * 
   * @param offset the offset of the stop
   * @param color the color of the stop
   */
  public Stop(int offset, Color color) {
    this(offset, color, 1);
  }

  /**
   * Creates a stop using the given offset, color and opacity.
   * 
   * @param offset the offset of the stop
   * @param color the color of the stop
   * @param opacity the opacity of the stop
   */
  public Stop(int offset, Color color, double opacity) {
    assert offset >= 0 && offset <= 100 : "Gradient stop offset must be within [0,100]: " + offset;
    this.offset = offset;
    this.color = color;
    this.opacity = opacity;
  }

  /**
   * Returns the color of the stop.
   * 
   * @return the color of the stop
   */
  public Color getColor() {
    return color;
  }

  /**
   * Returns the offset of the stop.
   * 
   * @return the offset of the stop
   */
  public int getOffset() {
    return offset;
  }

  /**
   * Returns the opacity of the stop.
   * 
   * @return the opacity of the stop
   */
  public double getOpacity() {
    return opacity;
  }
}
