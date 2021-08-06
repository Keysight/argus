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

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XDOM;

/**
 * Provides access to package protected methods of widget and convenience methods for easier
 * widget bookkeeping.
 */
public class ComponentHelper {
  
  /**
   * Find a the nearest widget that contains the given element.
   * 
   * @param element the element to start at
   * @return the containing element, or null if there is no widget
   */
  public static Widget getWidgetWithElement(Element element) {
    assert element != null : "Cannot find a widget for a null element";
    do {
      EventListener eventListener = DOM.getEventListener(element);
      if (eventListener instanceof Widget) {
        return (Widget) eventListener;
      }
      element = element.getParentElement();
    } while (element != null);
    return null;
  }

  /**
   * Attach a widget
   *
   * @param widget the widget to attach
   */
  public static native void doAttach(Widget widget)/*-{
    if (!!widget
        && !widget.@com.google.gwt.user.client.ui.Widget::isAttached()()) {
      widget.@com.google.gwt.user.client.ui.Widget::onAttach()();
    }
  }-*/;

  /**
   * Detaches a widget
   *
   * @param widget the widget to detach
   */
  public static native void doDetach(Widget widget) /*-{
    if (!!widget
        && widget.@com.google.gwt.user.client.ui.Widget::isAttached()()) {
      widget.@com.google.gwt.user.client.ui.Widget::onDetach()();
    }
  }-*/;

  /**
   * Returns the handler manager for the specified widget, creating it if
   * necessary.
   *
   * @param widget the widget
   * @return the handler manager
   */
  public static native HandlerManager ensureHandlers(Widget widget) /*-{
    if (!!widget) {
      return widget.@com.google.gwt.user.client.ui.Widget::ensureHandlers()();
    }
    return null;
  }-*/;

  /**
   * Retrieves the ID that has been set on a widget's element. If the widget's
   * element does not already have an ID, one will be automatically generated,
   * and that generated ID will be returned.
   *
   * @param widget The widget whose element's ID is to be retrieved. Must not be
   *          {@code null}.
   * @return The ID on the widget's element
   */
  public static String getWidgetId(Widget widget) {
    if (widget == null) {
      throw new IllegalArgumentException("Target widget must not be null.");
    }
    String id;
    if (widget instanceof Component) {
      id = ((Component) widget).getId();
    } else {
      // Non-Ext-GWT widget
      Element element = widget.getElement();
      id = element.getId();
      if (id.isEmpty()) {
        id = XDOM.getUniqueId();
        element.setId(id);
      }
    }
    return id;
  }

  /**
   * Determines if the widget has an Element
   *
   * @param widget the target Element
   * @return true if has an Element
   */
  public static native boolean hasElement(Widget widget) /*-{
    if (!!widget) {
      return widget.@com.google.gwt.user.client.ui.UIObject::element != null;
    }
    return false;
  }-*/;

  /**
   * Removes a handler from the widget
   *
   * @param widget the target widget
   * @param type the type
   * @param handler the handler to remove
   */
  public static native <H extends EventHandler> void removeHandler(Widget widget, Type<H> type, H handler) /*-{
    if (!!widget) {
      var h = widget.@com.google.gwt.user.client.ui.Widget::ensureHandlers()();
      h.@com.google.gwt.event.shared.HandlerManager::removeHandler(Lcom/google/gwt/event/shared/GwtEvent$Type;Lcom/google/gwt/event/shared/EventHandler;)(type,handler);
    }
  }-*/;

  /**
   * Sets the child to the parent widget
   *
   * @param parent the parent widget
   * @param child the child widget
   */
  public static native void setParent(Widget parent, Widget child) /*-{
    if (!!child) {
      child.@com.google.gwt.user.client.ui.Widget::setParent(Lcom/google/gwt/user/client/ui/Widget;)(parent);
    }
  }-*/;

}
