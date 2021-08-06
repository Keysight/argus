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
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.sencha.gxt.core.client.gestures.impl.IsElementImpl;
import com.sencha.gxt.core.client.util.Point;

public class TouchData {
  public enum Type {
    Start, Move, End, Cancel
  }


  private final Point startPosition;
  private final int identifier;
  private Point lastPosition;
  private IsElement startElement;
  private Type lastChange = Type.Start;
  private NativeEvent lastNativeEvent;

  public TouchData(Point startPosition, int identifier, IsElement startElement) {
    this.startPosition = startPosition;
    this.lastPosition = startPosition;
    this.identifier = identifier;
    this.startElement = startElement;
  }

  public TouchData(Point startPosition, int identifier, NativeEvent startEvent) {
    this(startPosition, identifier, startEvent.getEventTarget());
    this.lastNativeEvent = startEvent;
  }

  public TouchData(Point startPosition, int identifier, EventTarget eventTarget) {
    this.startPosition = startPosition;
    this.lastPosition = startPosition;
    this.identifier = identifier;
    if (eventTarget != null && GWT.isClient() && Element.is(eventTarget)) {
      this.startElement = eventTarget.<IsElementImpl>cast();
    }
  }

  public IsElement getStartElement() {
    return startElement;
  }

  public Point getStartPosition() {
    return startPosition;
  }

  public int getIdentifier() {
    return identifier;
  }

  public NativeEvent getLastNativeEvent() {
    return lastNativeEvent;
  }

  public void setLastNativeEvent(NativeEvent lastNativeEvent) {
    this.lastNativeEvent = lastNativeEvent;
  }

  public Point getLastPosition() {
    return lastPosition;
  }

  public void setLastPosition(Point lastPosition) {
    this.lastPosition = lastPosition;
  }

  public Type getLastChange() {
    return lastChange;
  }

  public void setLastChange(Type lastChange) {
    this.lastChange = lastChange;
  }
}
