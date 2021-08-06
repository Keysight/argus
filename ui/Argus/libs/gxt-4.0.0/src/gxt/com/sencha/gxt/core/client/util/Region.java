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
 * Represents a region in the coordinate system.
 */
public class Region {

  private int left;
  private int top;
  private int right;
  private int bottom;

  public Region() {

  }

  public Region(int spacing) {
    this(spacing, spacing, spacing, spacing);
  }

  public Region(int top, int right, int bottom, int left) {
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  /**
   * Returns the bottom value.
   *
   * @return the bottom value
   */
  public int getBottom() {
    return bottom;
  }

  /**
   * Returns the left value.
   *
   * @return the left value
   */
  public int getLeft() {
    return left;
  }

  /**
   * Returns the right value.
   *
   * @return the right value
   */
  public int getRight() {
    return right;
  }

  /**
   * Returns the top value.
   *
   * @return the top value
   */
  public int getTop() {
    return top;
  }

  /**
   * Sets the bottom value.
   *
   * @param bottom the bottom value
   */
  public void setBottom(int bottom) {
    this.bottom = bottom;
  }

  /**
   * Sets the left value.
   *
   * @param left the left value
   */
  public void setLeft(int left) {
    this.left = left;
  }

  /**
   * Sets the right value.
   *
   * @param right the right value
   */
  public void setRight(int right) {
    this.right = right;
  }

  /**
   * Sets the top value.
   *
   * @param top the top value
   */
  public void setTop(int top) {
    this.top = top;
  }

  /**
   * Tests if the given point is within the region.
   *
   * @param x the global coordinate x value
   * @param y the global coordinate y value
   * @return true if point is with region
   */
  public boolean contains(int x, int y) {
    if ((x >= x && x <= right)
        && (y >= top && y <= bottom)) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return left + ":" + top + ":" + right + ":" + bottom;
  }

}
