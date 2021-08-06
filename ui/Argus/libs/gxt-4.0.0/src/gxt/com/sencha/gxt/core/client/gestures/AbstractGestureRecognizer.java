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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.sencha.gxt.core.client.gestures.TouchData.Type;
import com.sencha.gxt.core.client.util.Point;

/**
 * Abstract starting point for building your own gesture recognition.
 */
public abstract class AbstractGestureRecognizer implements GestureRecognizer {

  /**
   * Enum specifying how the gesture should treat 'extra' touches that occur, either to ignore them, or
   * cancel the whole gesture.
   */
  public enum ExtraTouchBehavior {
    IGNORE, CANCEL
  }

  /**
   * indicates touches that this gesture wants to see when they <b>end/cancel</b>, but not move or preventDefault/stopPropagation
   */
  private final Map<Integer, TouchData> interested = new HashMap<Integer, TouchData>();
  /**
   * indicates touches that this gesture owns, and wants all info about, and keeps to itself
   */
  private final Map<Integer, TouchData> captured = new HashMap<Integer, TouchData>();

  private final int touchCount;
  private final ExtraTouchBehavior extraTouchBehavior;
  private HasHandlers eventDelegate;
  protected PointerEventsSupport pointerEventsSupport = PointerEventsSupport.impl;

  public AbstractGestureRecognizer(ExtraTouchBehavior extraTouchBehavior, int touchCount) {
    this.extraTouchBehavior = extraTouchBehavior;
    this.touchCount = touchCount;
  }

  @Override
  public void cancel() {
    if (!captured.isEmpty()) {
      ArrayList<TouchData> activeTouches = new ArrayList<TouchData>(interested.values());
      onCancel(activeTouches);

      for (int i = 0; i < activeTouches.size(); i++) {
        TouchData touch = activeTouches.get(i);
        setInterest(touch, false);
      }
    }
  }

  @Override
  public List<TouchData> getTouches() {
    return new ArrayList<TouchData>(captured.values());
  }

  @Override
  public boolean handle(NativeEvent event) {
    String eventType = event.getType();
    if (eventType.equals(BrowserEvents.TOUCHSTART) || eventType.equals(PointerEvents.POINTERDOWN.getEventName())) {
      return handleStart(event);
    } else if (eventType.equals(BrowserEvents.TOUCHMOVE) || eventType.equals(PointerEvents.POINTERMOVE.getEventName())) {
      return handleMove(event);
    } else if (eventType.equals(BrowserEvents.TOUCHEND) || eventType.equals(PointerEvents.POINTERUP.getEventName())) {
      return handleEnd(event);
    } else if (eventType.equals(BrowserEvents.TOUCHCANCEL) || eventType.equals(PointerEvents.POINTERCANCEL.getEventName())) {
      return handleCancel(event);
    } else if (eventType.equals(PointerEvents.POINTERENTER.getEventName())
        || eventType.equals(PointerEvents.POINTERLEAVE.getEventName())
        || eventType.equals(PointerEvents.POINTEROVER.getEventName())) {
      event.preventDefault();
      event.stopPropagation();
    } //else, skip, we aren't interested
    return true;
  }

  /**
   * Handles the start of gesture.  Typically associated by a touchstart or pointerdown event
   *
   * @param startEvent
   * @return true if the gesture is not handling the event and can allow it to be propagated, false to indicate that
   * it has been handled and should not be given to other handlers. Should always return true for any start
   * event.
   */
  public boolean handleStart(NativeEvent startEvent) {
    JsArray<Touch> touches = pointerEventsSupport.isSupported() ?
        pointerEventsSupport.getChangedTouches(startEvent) : startEvent.getChangedTouches();

    List<TouchData> startedTouches = new ArrayList<TouchData>();
    for (int i = 0; i < touches.length(); i++) {
      Touch t = touches.get(i);
      TouchData data = new TouchData(new Point(t.getPageX(), t.getPageY()), t.getIdentifier(), startEvent);
      startedTouches.add(data);
    }

    start(startedTouches);

    return true;
  }

  /**
   * Handles gesture move events
   *
   * @param moveEvent
   * @return true if the gesture is not handling the event and can allow it to be propagated, false to indicate that
   * it has been handled and should not be given to other handlers. Should always return true for any start
   * event.
   */
  public boolean handleMove(NativeEvent moveEvent) {
    JsArray<Touch> touches = pointerEventsSupport.isSupported() ?
        pointerEventsSupport.getChangedTouches(moveEvent) : moveEvent.getChangedTouches();

    List<TouchData> allData = new ArrayList<TouchData>();
    int unhandledTouches = 0;
    for (int i = 0; i < touches.length(); i++) {
      Touch t = touches.get(i);
      if (captured.containsKey(t.getIdentifier())) {
        TouchData data = captured.get(t.getIdentifier());
        data.setLastChange(Type.Move);
        data.setLastPosition(new Point(t.getPageX(), t.getPageY()));
        data.setLastNativeEvent(moveEvent);
        allData.add(data);
      } else {
        unhandledTouches++;
      }
    }
    if (!allData.isEmpty()) {
      handlePreventDefault(moveEvent);
      onMove(allData);
    }
    if (unhandledTouches == 0) {
      moveEvent.stopPropagation();
      return false;
    }
    return true;
  }

  /**
   * Handles gesture cancel events
   *
   * @param cancelEvent
   * @return true if the gesture is not handling the event and can allow it to be propagated, false to indicate that
   * it has been handled and should not be given to other handlers. Should always return true for any start
   * event.
   */
  public boolean handleCancel(NativeEvent cancelEvent) {
    JsArray<Touch> empty = JsArray.createArray().cast();
    JsArray<Touch> touches = pointerEventsSupport.isSupported() ?
        empty : cancelEvent.getTouches();

    //list of items that we are interested in that were canceled by this dom event
    List<TouchData> canceledData = new ArrayList<TouchData>(interested.values());

    for (int i = 0; i < touches.length(); i++) {
      Touch t = touches.get(i);
      if (interested.containsKey(t.getIdentifier())) {
        canceledData.remove(interested.get(t.getIdentifier()));
      }
    }

    for (TouchData data : canceledData) {
      data.setLastChange(Type.Cancel);
      data.setLastNativeEvent(cancelEvent);
      setInterest(data, false);
    }

    if (!canceledData.isEmpty()) {
      handlePreventDefault(cancelEvent);
      onCancel(canceledData);
    }
    //never stopPropagation the cancel event, same as stop

    return true;
  }

  /**
   * Handles gesture end events
   *
   * @param endEvent
   * @return true if the gesture is not handling the event and can allow it to be propagated, false to indicate that
   * it has been handled and should not be given to other handlers. Should always return true for any start
   * event.
   */
  public boolean handleEnd(NativeEvent endEvent) {
    // TODO: For pointers, it may not be as easy to determine what current touches are on the surface
    JsArray<Touch> empty = JsArray.createArray().cast();
    JsArray<Touch> touches = pointerEventsSupport.isSupported() ?
        empty : endEvent.getTouches();

    //list of items that we are interested that were ended by this dom event
    List<TouchData> endedData = new ArrayList<TouchData>(interested.values());

    for (int i = 0; i < touches.length(); i++) {
      Touch t = touches.get(i);
      if (interested.containsKey(t.getIdentifier())) {
        endedData.remove(interested.get(t.getIdentifier()));
      }
    }

    for (TouchData data : endedData) {
      data.setLastChange(Type.End);
      data.setLastNativeEvent(endEvent);
      setInterest(data, false);
    }

    if (!endedData.isEmpty()) {
      handlePreventDefault(endEvent);
      onEnd(endedData);
    }
    return true;
  }

  /**
   * Indicates whether or not the gesture is interested in all events about the given touch, and that any event
   * about that touch should be preventDefault'd and stopPropagation'd. Returning {@code true} from #onStart
   * automatically captures the started touch.
   * <p/>
   * Note that if the second argument is true, setInterest(touch, true) is implicit.
   *
   * @param touch
   * @param capture
   */
  public void setCaptured(TouchData touch, boolean capture) {
    assert touch != null : "Cannot capture a null touch";
    if (capture) {
      //if we capture this touch, we are interested in it too
      setInterest(touch, true);
      captured.put(touch.getIdentifier(), touch);
    } else {
      captured.remove(touch.getIdentifier());
    }
  }

  @Override
  public void setDelegate(HasHandlers eventDelegate) {
    this.eventDelegate = eventDelegate;
  }

  /**
   * Indicates whether or not the gesture are interested in end/cancel events about the given touch. Designed
   * mostly for use in multi-touch gestures. If interest is declared, onCancel and onEnd will be invoked,
   * but onMove will not.
   * <p/>
   * Note that if the second argument is false, setCaptured(touch, false) is implicit.
   *
   * @param touch the touch to be interested in, but not capture
   * @param interest whether to add or remove interest on the given touch
   */
  public void setInterest(TouchData touch, boolean interest) {
    assert touch != null : "Cannot interest a null touch";
    if (interest) {
      interested.put(touch.getIdentifier(), touch);
    } else {
      //if we aren't interested, we can un-capture as well
      setCaptured(touch, false);

      interested.remove(touch.getIdentifier());
    }
  }

  @Override
  public void start(List<TouchData> touches) {
    for (int i = 0; i < touches.size(); i++) {
      TouchData data = touches.get(i);
      if (captured.size() < touchCount) {
        //we can actually handle another one, go ahead and process it
        if (onStart(data)) {
          setCaptured(data, true);
        }
      } else {
        //otherwise, figure out how we handle an extra
        if (extraTouchBehavior == ExtraTouchBehavior.CANCEL) {
          cancel();
        } else {
          assert extraTouchBehavior == ExtraTouchBehavior.IGNORE;
        }
      }
    }
  }

  protected void fireEvent(GwtEvent<?> event) {
    // Without something to fire the events through, no one is listening, just ignore
    if (eventDelegate != null) {
      eventDelegate.fireEvent(event);
    }
  }

  /**
   * Intended to allow subclasses to determine if preventDefault should be called
   * <p/>
   * Default behavior is to NOT preventDefault
   *
   * @param event
   */
  protected void handlePreventDefault(NativeEvent event) {
  }

  /**
   * Indicates that a touch has started, giving the gesture the chance to exclusively handle this touch as it moves.
   *
   * @param startedTouch the touch that has begun
   * @return false to not capture the touch passed in, true to capture it so that later gestures do not hear about it.
   * Returning true is equivelent to calling setCaptured(startedTouch, true)
   */
  protected abstract boolean onStart(TouchData startedTouch);

  /**
   * Indicates that on or more touches are moving
   *
   * @param touches
   */
  protected abstract void onMove(List<TouchData> touches);

  /**
   * Indicates that one or more touches have ended.
   *
   * @param touches data for the now-ended touches (as opposed to #getTouches which no longer has those entries)
   */
  protected abstract void onEnd(List<TouchData> touches);

  /**
   * Indicates that one or more touches have been canceled by the browser or by the gesture itself being canceled
   * (see #cancel).
   *
   * @param touches data for the now-canceled touches (as opposed to #getTouches which no longer has those entries)
   */
  protected abstract void onCancel(List<TouchData> touches);

}
