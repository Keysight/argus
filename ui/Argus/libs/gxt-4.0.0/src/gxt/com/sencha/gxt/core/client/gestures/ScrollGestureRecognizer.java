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
import java.util.Stack;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;

/**
 * GestureRecognizer to handle scrolling gestures
 */
public class ScrollGestureRecognizer extends DragGestureRecognizer {

  private static int FPS = 60;
  private static int FRAMES_PER_MS = 1000 / FPS;

  /**
   * Deceleration rate.
   * <p/>
   * With the animation interval of 16.7 ms (from 60 fps),
   * this automatic scrolling effectively reduces the scrolling
   * velocity by a factor of 0.95, which is Math.exp(-16.7 / 325),
   * until it barely moves and therefore the scrolling is stopped completely.
   */
  private static double DECELERATION_RATE = 325; //ms
  /**
   * Used to tweak how "heavy" the scrolling is.
   * <p/>
   * If you want to make the list feel "heavy" reduce the number.
   * Consequently, a higher factor will give the illusion of a smooth and frictionless list.
   */
  protected double heavyFactor = 0.8d;

  /**
   * Stack keeping track of all TemporalPoints in the Gesture
   */
  protected final Stack<TemporalPoint> lastTouchPoints = new Stack<TemporalPoint>();
  /**
   * Command used to animate the scrolling after gesture ends
   */
  protected MomentumCommand momentumCommand;

  public enum ScrollDirection {
    HORIZONTAL, VERTICAL, BOTH
  }

  protected Point initialPosition;
  protected ExtraTouchBehavior extraTouchBehavior;

  private final Element scrollElement;
  private final ScrollDirection direction;

  private HasHandlers eventDelegate;

  public ScrollGestureRecognizer(Element scrollElement) {
    this(scrollElement, ScrollDirection.VERTICAL);
  }

  public ScrollGestureRecognizer(Element scrollElement, ScrollDirection direction) {
    this(scrollElement, direction, ExtraTouchBehavior.IGNORE);
  }

  public ScrollGestureRecognizer(Element scrollElement, ScrollDirection direction, ExtraTouchBehavior extraTouchBehavior) {
    this.scrollElement = scrollElement;
    this.direction = direction;
    this.extraTouchBehavior = extraTouchBehavior;
    pointerEventsSupport.sinkPointerEvents(scrollElement.<XElement>cast());
  }

  @Override
  public void cancel() {
    super.cancel();
    cancelMomentum();
  }

  protected ScrollDirection getDirection() {
    return direction;
  }

  @Override
  public void setDelegate(HasHandlers eventDelegate) {
    this.eventDelegate = eventDelegate;
  }

  /**
   * Calculates the velocity at the end of touchmove events
   *
   * @return Velocity used for scroll
   */
  protected Velocity calculateEndVelocity() {
    if (lastTouchPoints.isEmpty() || lastTouchPoints.size() < 2) {
      return null;
    }

    TemporalPoint to = lastTouchPoints.pop();
    TemporalPoint from = lastTouchPoints.pop();

    double time = to.time - from.time;
    if (time <= 0) {
      return null;
    }

    int xDist = from.point.getX() - to.point.getX();
    int yDist = from.point.getY() - to.point.getY();

    double vX = 1000 * xDist / time;
    double vY = 1000 * yDist / time;

    return new Velocity(0.8 * vX, 0.8 * vY);
  }

  @Override
  protected boolean onStart(TouchData startedTouch) {
    cancelMomentum();
    return super.onStart(startedTouch);
  }

  @Override
  protected void onMove(List<TouchData> touches) {
    super.onMove(touches);

    if (dragging) {
      NativeEvent moveEvent = touches.get(0).getLastNativeEvent();
      moveEvent.preventDefault();
      moveEvent.stopPropagation();

      onScrollMove(touches);
    }
  }

  @Override
  protected void onEnd(List<TouchData> touches) {
    boolean wasDragging = dragging;
    super.onEnd(touches);

    if (wasDragging) {
      NativeEvent endEvent = touches.get(0).getLastNativeEvent();
      endEvent.preventDefault();
      endEvent.stopPropagation();

      onScrollEnd(touches);
    }
  }

  @Override
  protected void onCancel(List<TouchData> touches) {
    super.onCancel(touches);

    onScrollCancel(touches);
  }

  protected void fireEvent(GwtEvent<?> event) {
    if (eventDelegate != null) {
      eventDelegate.fireEvent(event);
    }
  }

  protected void onScrollMove(List<TouchData> touches) {
    TouchData touch = touches.get(0);

    if (initialPosition == null) {
      onInitialPosition(scrollElement);
    }

    ScrollDirection scrollDirection = getDirection();
    lastTouchPoints.push(new TemporalPoint(touch.getLastPosition(), Duration.currentTimeMillis()));

    if (scrollDirection != ScrollDirection.HORIZONTAL) {
      onScrollMoveVertical(scrollElement, touch);
    }
    if (scrollDirection != ScrollDirection.VERTICAL) {
      onScrollMoveHorizontal(scrollElement, touch);
    }

    fireEvent(new ScrollGestureMoveEvent(this));
  }

  protected void onInitialPosition(Element scrollElement) {
    initialPosition = new Point(scrollElement.getScrollLeft(), scrollElement.getScrollTop());
    cancelMomentum();
  }

  protected void onScrollMoveVertical(Element scrollElement, TouchData touch) {
    //vertical scroll or both
    scrollElement.setScrollTop(initialPosition.getY() + (touch.getStartPosition().getY() - touch.getLastPosition().getY()));
  }

  protected void onScrollMoveHorizontal(Element scrollElement, TouchData touch) {
    //horizontal scroll or both
    scrollElement.setScrollLeft(initialPosition.getX() + (touch.getStartPosition().getX() - touch.getLastPosition().getX()));
  }

  protected void onScrollEnd(List<TouchData> event) {
    Velocity velocity = calculateEndVelocity();
    if (velocity != null) {
      // be completely sure momentum is canceled before starting up a new one
      cancelMomentum();

      momentumCommand = new MomentumCommand(velocity);
      Scheduler.get().scheduleFixedDelay(momentumCommand, FRAMES_PER_MS);
    }
    initialPosition = null;
    lastTouchPoints.clear();
    fireEvent(new ScrollGestureEndEvent(this));
  }

  protected void onScrollCancel(List<TouchData> event) {
    initialPosition = null;
    lastTouchPoints.clear();
    cancelMomentum();

    fireEvent(new ScrollGestureCancelEvent(this));
  }

  private void cancelMomentum() {
    if (momentumCommand != null) {
      momentumCommand.cancel();
      momentumCommand = null;
    }
  }


  /*
   * Executes the scroll based upon given velocity
   * This class is meant to be a use-once.  No values
   * are reset for multiple runs
   */
  private class MomentumCommand implements RepeatingCommand {
    double amplitudeX;
    double amplitudeY;
    double targetX;
    double targetY;
    private double timestamp;
    private boolean canceled = false;
    private boolean xNeeded = true;
    private boolean yNeeded = true;

    public MomentumCommand(Velocity velocity) {
      timestamp = Duration.currentTimeMillis();

      amplitudeX = heavyFactor * velocity.x;
      targetX = Math.round(scrollElement.getScrollLeft() + amplitudeX);
      amplitudeY = heavyFactor * velocity.y;
      targetY = Math.round(scrollElement.getScrollTop() + amplitudeY);
    }

    @Override
    public boolean execute() {
      if (canceled) {
        return false;
      }
      double elapsed = Duration.currentTimeMillis() - timestamp;
      // if either is true, then keep repeating
      // uses bitwise OR to make sure both are executed
      return scrollHorizontal(elapsed) | scrollVertical(elapsed);
    }

    public void cancel() {
      this.canceled = true;
    }

    private boolean scrollHorizontal(double elapsed) {
      if (xNeeded && getDirection() != ScrollDirection.VERTICAL) {
        double delta = -amplitudeX * Math.exp(-elapsed / DECELERATION_RATE);
        if (delta > 0.5 || delta < -0.5) {
          scrollElement.setScrollLeft((int) (targetX + delta));
          return true;
        }

        scrollElement.setScrollLeft((int) targetX);
        xNeeded = false;
      }

      return false;
    }

    private boolean scrollVertical(double elapsed) {
      if (yNeeded && getDirection() != ScrollDirection.HORIZONTAL) {
        double delta = -amplitudeY * Math.exp(-elapsed / DECELERATION_RATE);
        if (delta > 0.5 || delta < -0.5) {
          scrollElement.setScrollTop((int) (targetY + delta));
          return true;
        }
        scrollElement.setScrollTop((int) targetY);
        yNeeded = false;
      }

      return false;
    }
  }


  public static class ScrollGestureMoveEvent extends AbstractGestureEvent<ScrollGestureMoveEvent.ScrollGestureMoveHandler> {
    private static final Type<ScrollGestureMoveHandler> TYPE = new Type<ScrollGestureMoveHandler>();

    public ScrollGestureMoveEvent(ScrollGestureRecognizer gesture) {
      super(gesture);
    }

    @Override
    public Type<ScrollGestureMoveHandler> getAssociatedType() {
      return TYPE;
    }

    public static Type<ScrollGestureMoveHandler> getType() {
      return TYPE;
    }

    @Override
    public void dispatch(ScrollGestureMoveHandler handler) {
      handler.onScrollGestureMove(this);
    }

    public interface ScrollGestureMoveHandler extends EventHandler {
      void onScrollGestureMove(ScrollGestureMoveEvent event);
    }

    public interface HasScrollGestureMoveHandlers {
      HandlerRegistration addScrollGestureMoveHandler(ScrollGestureMoveHandler handler);
    }
  }

  public static class ScrollGestureEndEvent extends AbstractGestureEvent<ScrollGestureEndEvent.ScrollGestureEndHandler> {
    private static final Type<ScrollGestureEndHandler> TYPE = new Type<ScrollGestureEndHandler>();

    public ScrollGestureEndEvent(ScrollGestureRecognizer gesture) {
      super(gesture);
    }

    @Override
    public Type<ScrollGestureEndHandler> getAssociatedType() {
      return TYPE;
    }

    public static Type<ScrollGestureEndHandler> getType() {
      return TYPE;
    }

    @Override
    public void dispatch(ScrollGestureEndHandler handler) {
      handler.onScrollGestureEnd(this);
    }

    public interface ScrollGestureEndHandler extends EventHandler {
      void onScrollGestureEnd(ScrollGestureEndEvent event);
    }

    public interface HasScrollGestureEndHandlers {
      HandlerRegistration addScrollGestureEndHandler(ScrollGestureEndHandler handler);
    }
  }

  public static class ScrollGestureCancelEvent extends AbstractGestureEvent<ScrollGestureCancelEvent.ScrollGestureCancelHandler> {
    private static final Type<ScrollGestureCancelHandler> TYPE = new Type<ScrollGestureCancelHandler>();

    public ScrollGestureCancelEvent(ScrollGestureRecognizer gesture) {
      super(gesture);
    }

    @Override
    public Type<ScrollGestureCancelHandler> getAssociatedType() {
      return TYPE;
    }

    public static Type<ScrollGestureCancelHandler> getType() {
      return TYPE;
    }

    @Override
    public void dispatch(ScrollGestureCancelHandler handler) {
      handler.onScrollGestureCancel(this);
    }

    public interface ScrollGestureCancelHandler extends EventHandler {
      void onScrollGestureCancel(ScrollGestureCancelEvent event);
    }

    public interface HasScrollGestureCancelHandlers {
      HandlerRegistration addScrollGestureCancelHandler(ScrollGestureCancelHandler handler);
    }
  }

  private static final class TemporalPoint {
    private Point point;
    private double time;

    public TemporalPoint(Point point, double time) {
      this.point = point;
      this.time = time;
    }
  }

  private static final class Velocity {
    private final double x;
    private final double y;

    public Velocity(double x, double y) {
      this.x = x;
      this.y = y;
    }
  }
}
