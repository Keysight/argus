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
package com.sencha.gxt.widget.core.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;

/**
 * A framed panel that is managed by a {@link PortalLayoutContainer}.
 */
public class Portlet extends FramedPanel {

  private boolean pinned = false;

  /**
   * Creates a portlet with the default appearance.
   */
  public Portlet() {
    this(GWT.<FramedPanelAppearance> create(FramedPanelAppearance.class));
  }

  /**
   * Creates a portlet with the specified appearance.
   * 
   * @param appearance the portlet appearance
   */
  public Portlet(FramedPanelAppearance appearance) {
    super(appearance);
    setPinned(false);
  }

  /**
   * Returns true if the portal is pinned.
   * 
   * @return true if pinned
   */
  public boolean isPinned() {
    return pinned;
  }

  /**
   * Sets the pinned state of a portlet (whether the portlet is draggable).
   * 
   * @param pinned true to pin the portlet so that it is not draggable.
   */
  public void setPinned(boolean pinned) {
    this.pinned = pinned;
    Cursor c = pinned ? Cursor.DEFAULT : Cursor.MOVE;
    getHeader().getElement().getStyle().setCursor(c);

    if (getData("gxt.draggable") != null) {
      Draggable d = (Draggable) getData("gxt.draggable");
      d.setEnabled(!pinned);
    }
  }
}
