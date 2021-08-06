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

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * External api for mangling touch events into gestures (events or callbacks). The handle() method accepts
 * incoming dom events, getTouches() returns the current set of touches this gesture is handling, and cancel()
 * releases the current touches so that other handlers can deal with them.
 * <p/>
 * Various implementations of this can either support on- methods or events to indicate that touch events or
 * gestures are ongoing or have completed. Events are generally expected to be fired from any provided delegate
 * rather than this object itself
 */
public interface GestureRecognizer {
  /**
   * Ends recognition of the gesture, and fires any cancel events that apply. Any touch events
   * being recognized by this object will now be ignored.
   */
  void cancel();

  /**
   * Gets all actively recognized touches that are being recognized by this gesture.
   *
   * @return
   */
  List<TouchData> getTouches();

  /**
   * Takes a browser event, and checks to see if it should decipher a gesture from it.
   * <p/>
   * Callers of this are expected to do their own vetting to decide if the given event should be handled by this
   * gesture - for example, if a cell accepts a tap on one part of it and a longpress on another, the cell should
   * determine where the touch events occur and send to the right recognizer accordingly.
   *
   * @param event the browser event to read for a gesture.
   * @return true if the gesture is not handling the event and can allow it to be propagated, false to indicate that
   * it has been handled and should not be given to other handlers. Should always return true for any start
   * event.
   */
  boolean handle(NativeEvent event);

  /**
   * Sets delegate to receive Gesture events
   *
   * @param eventDelegate
   */
  void setDelegate(HasHandlers eventDelegate);

  /**
   * Artificially starts a gesture
   *
   * @param touches
   */
  void start(List<TouchData> touches);
}