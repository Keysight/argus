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
package com.sencha.gxt.cell.core.client;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.widget.core.client.event.ArrowSelectEvent;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.XEvent;

public class SplitButtonCell extends TextButtonCell {

  public SplitButtonCell() {
    this(GWT.<ButtonCellAppearance<String>> create(ButtonCellAppearance.class));
  }

  public SplitButtonCell(ButtonCellAppearance<String> appearance) {
    super(appearance);
  }

  @Override
  protected void onClick(Context context, XElement p, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
    event.preventDefault();

    if (!isDisableEvents() && fireCancellableEvent(context, new BeforeSelectEvent(context))) {

      if (isClickOnArrow(p, event)) {
        if (menu != null && !menu.isVisible()) {
          showMenu(p);
        }
        fireEvent(context, new ArrowSelectEvent(context, menu));
      } else {
        fireEvent(context, new SelectEvent(context));
      }
    }
  }

  public boolean isClickOnArrow(XElement p, NativeEvent e) {
    Point eventXY = e.<XEvent>cast().getXY();

    Point elementXY = p.getPosition(false);

    int buttonX = elementXY.getX();
    int buttonY = elementXY.getY();
    int width = p.getOffsetWidth();
    int height = p.getOffsetHeight();

    int x = eventXY.getX();
    int y = eventXY.getY();

    return (getArrowAlign() == ButtonArrowAlign.BOTTOM) ? y > (buttonY + height  - 14)
        : x > (buttonX + width - 14);
  }
}
