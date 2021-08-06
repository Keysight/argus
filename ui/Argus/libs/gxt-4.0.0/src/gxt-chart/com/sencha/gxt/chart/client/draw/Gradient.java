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

import java.util.ArrayList;
import java.util.List;

/**
 * A color representing a linear gradient.
 */
public class Gradient extends Color {

  @Deprecated
  private String id = "";
  private double angle = 0;
  private List<Stop> stops = new ArrayList<Stop>();

  /**
   * Creates a gradient using the given id and angle.
   *
   * @deprecated use {@link Gradient#Gradient(double)} instead, id is unused and will be removed in a future version
   * @param id the gradient id
   * @param angle the angle of the gradient
   */
  @Deprecated
  public Gradient(String id, double angle) {
    super("url(#" + id + ")");
    this.id = id;
    this.angle = angle;
  }

  /**
   * Creates a linear gradient using the given angle.
   *
   * @param angle the angle of the gradient
   */
  public Gradient(double angle) {
    this.angle = angle;
  }

  /**
   * Adds a stop to the gradient using the given offset and color.
   * 
   * @param offset the offset of the stop
   * @param color the color of the stop
   */
  public void addStop(int offset, Color color) {
    stops.add(new Stop(offset, color));
  }

  /**
   * Adds the given stop to the gradient.
   * 
   * @param stop the stop to be added
   */
  public void addStop(Stop stop) {
    stops.add(stop);
  }

  /**
   * Removes all stops from the gradient.
   */
  public void clearStops() {
    stops.clear();
  }

  /**
   * Returns the angle of the gradient.
   * 
   * @return the angle of the gradient
   */
  public double getAngle() {
    return angle;
  }

  /**
   * Returns the id of the gradient.
   *
   * @deprecated the id property is unused and will be removed in a future release
   * @return the id of the gradient
   */
  @Deprecated
  public String getId() {
    return id;
  }

  /**
   * Returns the stops of the gradient.
   *
   * @return the stops of the gradient
   */
  public List<Stop> getStops() {
    return stops;
  }

  /**
   * Sets the angle of the gradient.
   * 
   * @param angle the new angle of the gradient
   */
  public void setAngle(double angle) {
    this.angle = angle;
  }

  /**
   * Sets the id of the gradient. Updates the color string with the new id.
   *
   * @deprecated the id property is unused and will be removed in a future release
   * @param id the new id of the gradient
   */
  @Deprecated
  public void setId(String id) {
    this.id = id;
    setColor("url(#" + id + ")");
  }

}
