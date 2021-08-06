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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.sencha.gxt.core.client.dom.XElement;

/**
 * Adapter to support Pointer event registration
 */
public class PointerEventsSupport {

  public static final PointerEventsSupport impl = GWT.create(PointerEventsSupport.class);

  /**
   * Sink pointer events to given Element.
   * <p/>
   * Makes the assumption that the DOMImpl has already initialized the EventSystem.
   *
   * @param element
   */
  public void sinkPointerEvents(XElement element) {
  }

  /**
   * Determines if the provided event is Pointer originated
   *
   * @param event
   * @return true if Pointer event
   */
  public boolean isPointerEvent(NativeEvent event) {
    return PointerEvents.isPointerEvent(event.getType());
  }

  /**
   * Assigns a pointer (by pointerId) to a specific Element.
   * <p>
   * Unlike Touch events, the event.target changes with pointer events
   * </p>
   *
   * @param element
   * @param event
   */
  public void setPointerCapture(XElement element, NativeEvent event) {
  }

  /**
   * Determines if the incoming NativeEvent is a pointer event with type "touch" or "pen"
   *
   * @param event
   * @return true if event is "pointer" AND one of "touch" or "pen" type
   */
  public boolean isPointerTouchEvent(NativeEvent event) {
    if (!isPointerEvent(event)) {
      return false;
    }
    final PointerType pointerType = PointerType.getPointerType(getPointerType(event));
    return PointerType.TOUCH == pointerType || PointerType.PEN == pointerType;
  }

  /**
   * Identifies whether pointer events are supported in the current running browser.
   *
   * @return true if pointers are supported
   */
  public boolean isSupported() {
    return false;
  }

  /**
   * Converts data in the event to Touch data structure
   *
   * @param event
   * @return JsArray of Touch objects - empty if pointers are not supported
   */
  public JsArray<Touch> getChangedTouches(NativeEvent event) {
    return JsArray.createArray().cast();
  }

  private static native String getPointerType(NativeEvent event) /*-{
    return event.pointerType;
  }-*/;
}
