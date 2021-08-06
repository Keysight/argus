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
package com.sencha.gxt.fx.client.animation;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Util;

public class SlideIn extends Slide {

  public SlideIn(XElement el, Direction dir) {
    super(el, dir);
  }

  @Override
  public void increase(int now) {
    int v = now;
    switch (dir) {
      case LEFT:
        wrapEl.setLeft(oBounds.getWidth() - v);
        wrapEl.setWidth(v);
        break;
      case UP:
        wrapEl.setTop((oBounds.getHeight() - v));
        wrapEl.setHeight(v);
        break;
      case DOWN:
        element.getStyle().setMarginTop(-(oBounds.getHeight() - v), Unit.PX);
        wrapEl.setHeight(v);
        break;
      case RIGHT:
        element.getStyle().setMarginLeft(-(oBounds.getWidth() - v), Unit.PX);
        wrapEl.setWidth(v);
        break;
    }
  }

  @Override
  public void onStart() {
    overflow = Util.parseOverflow(element.getStyle().getOverflow());
    marginTop = Util.parseInt(element.getStyle().getMarginTop(), 0);
    marginLeft = Util.parseInt(element.getStyle().getMarginLeft(), 0);

    wrapEl = Document.get().createDivElement().cast();
    oBounds = element.wrap(wrapEl);

    int h = oBounds.getHeight();
    int w = oBounds.getWidth();

    wrapEl.setSize(w, h);
    element.setVisible(true);
    wrapEl.setVisible(true);

    switch (dir) {
      case DOWN:
        wrapEl.setHeight(1);
        from = 1;
        to = oBounds.getHeight();
        break;
      case RIGHT:
        wrapEl.setWidth(1);
        from = 1;
        to = oBounds.getWidth();
        break;
      case LEFT:
        wrapEl.setWidth(1);
        from = 1;
        to = oBounds.getWidth();
        break;
      case UP:
        wrapEl.setHeight(1);
        from = 1;
        to = oBounds.getHeight();
    }
  }
}
