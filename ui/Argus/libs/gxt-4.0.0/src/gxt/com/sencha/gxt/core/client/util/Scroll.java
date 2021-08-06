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
 * Represents the left and top scroll values.
 */
public class Scroll {

  private int scrollLeft;
  private int scrollTop;

  public Scroll(int scrollLeft, int scrollTop) {
    this.scrollLeft = scrollLeft;
    this.scrollTop = scrollTop;
  }

  /**
   * Returns the scroll left value.
   * 
   * @return the scroll left value
   */
  public int getScrollLeft() {
    return scrollLeft;
  }

  /**
   * Returns the scroll top value.
   * 
   * @return the scroll top value
   */
  public int getScrollTop() {
    return scrollTop;
  }

  /**
   * Sets the scroll left value.
   * 
   * @param scrollLeft the scroll left value
   */
  public void setScrollLeft(int scrollLeft) {
    this.scrollLeft = scrollLeft;
  }

  /**
   * Sets the scroll top value.
   * 
   * @param scrollTop the scroll top value
   */
  public void setScrollTop(int scrollTop) {
    this.scrollTop = scrollTop;
  }
}
