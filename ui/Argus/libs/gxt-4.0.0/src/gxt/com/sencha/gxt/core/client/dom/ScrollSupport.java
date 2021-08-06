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
package com.sencha.gxt.core.client.dom;

import com.google.gwt.user.client.ui.UIObject;

/**
 * Interface for objects that support scrolling.
 */
public interface ScrollSupport {

  /**
   * Scroll enumeration.
   */
  public enum ScrollMode {
    AUTO("auto"), AUTOX("auto"), AUTOY("auto"), ALWAYS("scroll"), NONE("hidden");
    private final String value;

    private ScrollMode(String value) {
      this.value = value;
    }

    public String value() {
      return value;
    }
  }
  
  public void ensureVisible(UIObject item);
  
  public int getHorizontalScrollPosition();

  public int getMaximumHorizontalScrollPosition();

  public int getMaximumVerticalScrollPosition();

  public int getMinimumHorizontalScrollPosition();

  public int getMinimumVerticalScrollPosition();

  public ScrollMode getScrollMode();

  public int getVerticalScrollPosition();

  public void scrollToBottom();

  public void scrollToLeft();

  public void scrollToRight();

  public void scrollToTop();

  public void setHorizontalScrollPosition(int position);

  public void setScrollMode(ScrollMode scroll);

  public void setVerticalScrollPosition(int position);

}
