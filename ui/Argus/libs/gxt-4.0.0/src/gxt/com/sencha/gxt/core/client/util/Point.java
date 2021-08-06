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
package com.sencha.gxt.core.client.util;


/**
 * Instances of this class represent places on the (x, y) coordinate plane.
 * 
 * @see Rectangle
 */
public class Point {

  private int x;
  private int y;

  /**
   * Constructs a new point with the given x and y coordinates.
   * 
   * @param x the x coordinate of the new point
   * @param y the y coordinate of the new point
   */
  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(this instanceof Point)) return false;
    Point other = (Point) obj;
    if (x != other.x) return false;
    if (y != other.y) return false;
    return true;
  }

  /**
   * Returns the x coordinate of the point.
   * 
   * @return the x coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Returns the y coordinate of the point.
   * 
   * @return the y coordinate
   */
  public int getY() {
    return y;
  }

  /**
   * Sets the x-coordinate.
   * 
   * @param x the x-coordinate
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Sets the y-coordinate
   * 
   * @param y the y-coordinate
   */
  public void setY(int y) {
    this.y = y;
  }

  public String toString() {
    return ("x: " + x + ", y: " + y);
  }

}
