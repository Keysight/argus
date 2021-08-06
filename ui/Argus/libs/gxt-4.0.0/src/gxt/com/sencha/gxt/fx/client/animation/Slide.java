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

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Rectangle;

public abstract class Slide extends BaseEffect {

  protected Direction dir;
  protected double from, to;
  protected Rectangle oBounds;
  protected Overflow overflow;
  protected XElement wrapEl;
  protected int marginTop;
  protected int marginLeft;

  public Slide(XElement el, Direction dir) {
    super(el);
    this.dir = dir;
  }

  public abstract void increase(int v);

  @Override
  public void onComplete() {
    wrapEl.unwrap(element, oBounds);
    element.getStyle().setMarginTop(marginTop, Unit.PX);
    element.getStyle().setMarginLeft(marginLeft, Unit.PX);
    if (overflow != null) {
      element.getStyle().setOverflow(overflow);
    }
    super.onComplete();
  }

  @Override
  public void onUpdate(double progress) {
    int v = (int) getValue(from, to, progress);
    increase(v);
  }

}
