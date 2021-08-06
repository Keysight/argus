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

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

public class XWindow {

  private static boolean lastResizeOrientationChange;

  static {
    Window.addResizeHandler(new ResizeHandler() {
      int lastWindowWidth;
      int lastWindowHeight;

      @Override
      public void onResize(ResizeEvent resizeEvent) {
        int width = resizeEvent.getWidth();
        int height = resizeEvent.getHeight();
        // check orientation change by seeing if height/width values swapped
        lastResizeOrientationChange = width == lastWindowHeight && height == lastWindowWidth;
        lastWindowWidth = width;
        lastWindowHeight = height;
      }
    });
  }

  private XWindow() {
  }

  /**
   * Returns true if the last resize was an orientation change. This is currently used by Android devices to determine if
   * the last window resize was caused by an orientation change or a virtual keyboard.
   *
   * @return true if the last resize was an orientation change.
   */
  public static boolean isLastResizeOrientationChange() {
    return lastResizeOrientationChange;
  }
}
