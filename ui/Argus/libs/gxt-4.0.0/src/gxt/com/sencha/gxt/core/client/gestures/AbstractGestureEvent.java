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

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Gesture events are logical events - they describe as a gesture occurs, and how it ends
 * (failure/success, or 'end' vs 'cancel'). A gesture canceled event does not necessarily
 * mean that a touch was canceled nor vice versa - a touch cancel event means only that
 * the OS/Browser decided that the touch could not continue or end normally, and a gesture
 * cancel means that the entire gesture did not complete normally.
 * @param <H>
 */
public abstract class AbstractGestureEvent<H extends EventHandler> extends GwtEvent<H> {
  private final GestureRecognizer gesture;

  public AbstractGestureEvent(GestureRecognizer gesture) {
    this.gesture = gesture;
  }

  /**
   * Stops the gesture recognition process, allowing the move events to propagate.
   */
  public void cancel() {
    gesture.cancel();
  }

  public List<TouchData> getActiveTouches() {
    return gesture.getTouches();
  }

}