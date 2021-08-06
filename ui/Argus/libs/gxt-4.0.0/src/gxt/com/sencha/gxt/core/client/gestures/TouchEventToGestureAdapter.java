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
package com.sencha.gxt.core.client.gestures;

import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.widget.core.client.Component;

/**
 * Adapter to add gesture support to an existing widget without extending it.
 *
 * @TODO support pointer here
 */
public class TouchEventToGestureAdapter implements TouchStartHandler, TouchMoveHandler, TouchEndHandler, TouchCancelHandler {

  private final Widget targetWidget;
  private final GestureRecognizer gestureRecognizer;
  private final GroupingHandlerRegistration handlerRegistration = new GroupingHandlerRegistration();

  /**
   * Add gesture support to an existing widget.
   * If the widget is a GXT Component then handle the events in the component otherwise handle them here.
   *
   * @param targetWidget add gesture support to this widget
   * @param gestureRecongnizer is the type of touch support to be added
   */
  public TouchEventToGestureAdapter(Widget targetWidget, GestureRecognizer gestureRecongnizer) {
    this.targetWidget = targetWidget;
    this.gestureRecognizer = gestureRecongnizer;

    gestureRecongnizer.setDelegate(targetWidget);

    // Handle events in HasGestureRecognizers widgets/components
    if (targetWidget instanceof HasGestureRecognizers) {
      ((HasGestureRecognizers) targetWidget).addGestureRecognizer(gestureRecongnizer);
    } else {
      handlerRegistration.add(targetWidget.addDomHandler(this, TouchStartEvent.getType()));
      handlerRegistration.add(targetWidget.addDomHandler(this, TouchMoveEvent.getType()));
      handlerRegistration.add(targetWidget.addDomHandler(this, TouchEndEvent.getType()));
      handlerRegistration.add(targetWidget.addDomHandler(this, TouchCancelEvent.getType()));
    }
  }

  public HandlerRegistration getHandlerRegistration() {
    return handlerRegistration;
  }

  public Widget getTargetWidget() {
    return targetWidget;
  }

  @Override
  public void onTouchCancel(TouchCancelEvent event) {
    gestureRecognizer.handle(event.getNativeEvent());
  }

  @Override
  public void onTouchEnd(TouchEndEvent event) {
    gestureRecognizer.handle(event.getNativeEvent());
  }

  @Override
  public void onTouchMove(TouchMoveEvent event) {
    gestureRecognizer.handle(event.getNativeEvent());
  }

  @Override
  public void onTouchStart(TouchStartEvent event) {
    gestureRecognizer.handle(event.getNativeEvent());
  }

}