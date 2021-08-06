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
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.core.client.util.Point;

import java.util.ArrayList;
import java.util.List;

public class DoubleTapGestureRecognizer extends TapGestureRecognizer {

  /** The default interval threshold in millis */
  private static final int DEFAULT_INTERVAL_THRESHOLD = 300;
  /** The default allowable position difference threshold */
  private static final int DEFAULT_POSITION_DIFFERENCE_THRESHOLD = 10;

  /** The maximum interval in millis between taps */
  private final int intervalThreshold;
  /** Some position difference is allowable (and expected) between two taps. */
  private final int positionDifferenceThreshold;

  /** All the taps that have happened within the interval */
  // NOTE: we can't rely on getTouches() - need to keep track of touches locally
  private final List<TouchData> touches;

  /**
   * Timer set to call single tap at the end of the interval. When a tap comes in, we don't know if it's actually a
   * single tap or if it's just the first tap in a double tap.  Rather than firing the tap event immediately, we start
   * a timer on the first tap and cancel it if a second tap comes in.  If the timer finishes without being cancel, we
   * know it's not a double tap.
   */
  private final Timer intervalTimer;

  public DoubleTapGestureRecognizer() {
    this(DEFAULT_INTERVAL_THRESHOLD, DEFAULT_POSITION_DIFFERENCE_THRESHOLD);
  }

  public DoubleTapGestureRecognizer(int intervalThreshold, int positionDifferenceThreshold) {
    this.intervalThreshold = intervalThreshold;
    this.positionDifferenceThreshold = positionDifferenceThreshold;
    touches = new ArrayList<TouchData>(2);
    intervalTimer = new Timer() {
      @Override
      public void run() {
        // a double tap would cancel and a long press would start the timer without adding any touches
        if (!touches.isEmpty()) {
          handleSingleTap();
        }
      }
    };
  }

  public void onDoubleTap(TouchData touchData) {
    fireEvent(new DoubleTapGestureEvent(touchData, this));
  }

  @Override
  protected boolean onStart(TouchData startedTouch) {
    // start the timer as soon as the first start is called
    if (intervalTimer.isRunning() == false) {
      intervalTimer.schedule(intervalThreshold);
    }
    return super.onStart(startedTouch);
  }

  @Override
  protected void onEnd(List<TouchData> touches) {
    /*
     * only add to the touches list in onEnd - since we start the timer in onStart, this will guarantee that touches is
     * the list of full taps in the interval
     *
     * NOTE: do not call super.onEnd here as it will call the onTap method.
     */
    this.touches.add(touches.get(0));

    // if this is the second tap, cancel the timer and fire the double tap
    if (intervalTimer.isRunning() && isDoubleTap()) {
      handleDoubleTap();
    }
  }

  private void handleDoubleTap() {
    intervalTimer.cancel();
    TouchData touchData = touches.get(touches.size() - 1);
    onDoubleTap(touchData);
    touches.clear();
  }

  private void handleSingleTap() {
    super.onEnd(touches);
    touches.clear();
  }

  private boolean isDoubleTap() {
    if (touches.size() < 2) {
      return false;
    }

    for (int i = 0; i < touches.size() - 1; i++) {
      Point p1 = touches.get(i).getLastPosition();
      Point p2 = touches.get(i + 1).getLastPosition();
      if (Math.abs(p1.getX() - p2.getX()) > positionDifferenceThreshold
              || Math.abs(p1.getY() - p2.getY()) > positionDifferenceThreshold) {
        return false;
      }
    }
    return true;
  }

  public static class DoubleTapGestureEvent extends AbstractGestureEvent<DoubleTapGestureEvent.DoubleTapGestureEventHandler> {

    private static final Type<DoubleTapGestureEventHandler> TYPE = new Type<DoubleTapGestureEventHandler>();

    private final TouchData touchData;

    public DoubleTapGestureEvent(TouchData touchData, GestureRecognizer gesture) {
      super(gesture);
      this.touchData = touchData;
    }

    public static Type<DoubleTapGestureEventHandler> getType() {
      return TYPE;
    }

    public TouchData getTouchData() {
      return touchData;
    }

    @Override
    public Type<DoubleTapGestureEventHandler> getAssociatedType() {
      return TYPE;
    }

    @Override
    protected void dispatch(DoubleTapGestureEventHandler eventHandler) {
      eventHandler.onDoubleTapGesture(this);
    }

    public interface DoubleTapGestureEventHandler extends EventHandler {
      void onDoubleTapGesture(DoubleTapGestureEvent event);
    }
  }
}
