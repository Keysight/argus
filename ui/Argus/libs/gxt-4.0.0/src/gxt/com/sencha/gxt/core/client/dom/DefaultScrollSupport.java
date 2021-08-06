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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.UIObject;

public class DefaultScrollSupport implements ScrollSupport {

  private XElement scrollElement;

  private ScrollMode scrollMode = ScrollMode.NONE;

  public DefaultScrollSupport(Element scrollElement) {
    this.scrollElement = scrollElement.cast();
  }

  @Override
  public void ensureVisible(UIObject item) {
    Element element = item.getElement();
    ensureVisibleImpl(scrollElement, element);
  }

  @Override
  public int getHorizontalScrollPosition() {
    return scrollElement.getScrollLeft();
  }

  @Override
  public int getMaximumHorizontalScrollPosition() {
    return ScrollImplHelper.getMaximumHorizontalScrollPosition(scrollElement);
  }

  @Override
  public int getMaximumVerticalScrollPosition() {
    return scrollElement.getScrollHeight() - scrollElement.getClientHeight();
  }

  @Override
  public int getMinimumHorizontalScrollPosition() {
    return ScrollImplHelper.getMinimumHorizontalScrollPosition(scrollElement);
  }

  @Override
  public int getMinimumVerticalScrollPosition() {
    return 0;
  }

  public XElement getScrollElement() {
    return scrollElement;
  }

  @Override
  public ScrollMode getScrollMode() {
    return scrollMode;
  }

  @Override
  public int getVerticalScrollPosition() {
    return scrollElement.getScrollTop();
  }

  @Override
  public void scrollToBottom() {
    setVerticalScrollPosition(getMaximumVerticalScrollPosition());
  }

  @Override
  public void scrollToLeft() {
    setHorizontalScrollPosition(getMinimumHorizontalScrollPosition());
  }

  @Override
  public void scrollToRight() {
    setHorizontalScrollPosition(getMaximumHorizontalScrollPosition());
  }

  @Override
  public void scrollToTop() {
    setVerticalScrollPosition(getMinimumVerticalScrollPosition());
  }

  @Override
  public void setHorizontalScrollPosition(int position) {
    scrollElement.setScrollLeft(position);
  }

  @Override
  public void setScrollMode(ScrollMode scrollMode) {
    this.scrollMode = scrollMode;
    switch (scrollMode) {
      case AUTO:
      case ALWAYS:
      case NONE:
        scrollElement.getStyle().setProperty("overflowX", scrollMode.value().toLowerCase());
        scrollElement.getStyle().setProperty("overflowY", scrollMode.value().toLowerCase());
        break;
      case AUTOX:
        scrollElement.getStyle().setProperty("overflowX", scrollMode.value().toLowerCase());
        scrollElement.getStyle().setProperty("overflowY", "hidden");
        break;
      case AUTOY:
        scrollElement.getStyle().setProperty("overflowY", scrollMode.value().toLowerCase());
        scrollElement.getStyle().setProperty("overflowX", "hidden");
        break;
    }
  }
  
  @Override
  public void setVerticalScrollPosition(int position) {
    scrollElement.setScrollTop(position);
  }

  private native void ensureVisibleImpl(Element scroll, Element e) /*-{
		if (!e)
			return;

		var item = e;
		var realOffset = 0;
		while (item && (item != scroll)) {
			realOffset += item.offsetTop;
			item = item.offsetParent;
		}

		scroll.scrollTop = realOffset - scroll.offsetHeight / 2;
  }-*/;

}
