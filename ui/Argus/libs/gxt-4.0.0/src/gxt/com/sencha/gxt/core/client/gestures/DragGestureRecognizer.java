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

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.dom.XElement;

public class DragGestureRecognizer extends AbstractGestureRecognizer {

  public abstract static class CellDragGestureRecognizer<T> extends CellGestureAdapter<DragGestureRecognizer, T> {
    private final ExtraTouchBehavior extraTouchBehavior;
    private final int minMoveDistance;

    public CellDragGestureRecognizer() {
      this(TouchConstants.INSTANCE.touchMaxDistance(), ExtraTouchBehavior.IGNORE);
    }

    public CellDragGestureRecognizer(int minMoveDistance, ExtraTouchBehavior extraTouchBehavior) {
      this.minMoveDistance = minMoveDistance;
      this.extraTouchBehavior = extraTouchBehavior;
    }

    @Override
    protected DragGestureRecognizer createRecognizer() {
      return new DragGestureRecognizer(minMoveDistance, extraTouchBehavior) {
        @Override
        protected void onMove(List<TouchData> touches) {
          if (dragging) {
            super.onMove(touches);
            //only one touch
            TouchData t = touches.get(0);
            onTouchMove(t, getContext(this), getParent(this), getValue(this), getValueUpdater(this));
          } else {
            super.onMove(touches);
          }
        }

        @Override
        protected void onEnd(List<TouchData> touches) {
          if (dragging) {
            // calling this before if(dragging) will immediately set drag to false & onTouchMoveEnd will never be called
            super.onEnd(touches);
            //only one touch
            TouchData t = touches.get(0);
            onTouchMoveEnd(t, getContext(this), getParent(this), getValue(this), getValueUpdater(this));
          } else {
            super.onEnd(touches);
          }
          release(this);
        }

        @Override
        protected void onCancel(List<TouchData> touches) {
          super.onCancel(touches);
          release(this);
        }

        @Override
        protected boolean handleMousePointers() {
          return CellDragGestureRecognizer.this.handleMousePointers();
        }
      };
    }

    protected boolean handleMousePointers() {
      return false;
    }

    protected abstract void onTouchMove(TouchData touch, Context context, Element parent, T value, ValueUpdater<T> valueUpdater);

    protected abstract void onTouchMoveEnd(TouchData touch, Context context, Element parent, T value, ValueUpdater<T> valueUpdater);
  }

  private final int distanceSquared;
  protected boolean dragging = false;

  public DragGestureRecognizer() {
    this(TouchConstants.INSTANCE.touchMaxDistance(), ExtraTouchBehavior.IGNORE);
  }

  public DragGestureRecognizer(int minMoveDistance) {
    this(minMoveDistance, ExtraTouchBehavior.IGNORE);
  }

  public DragGestureRecognizer(int minMoveDistance, ExtraTouchBehavior extraTouchBehavior) {
    super(extraTouchBehavior, 1);

    distanceSquared = minMoveDistance * minMoveDistance;
  }

  @Override
  public boolean handle(NativeEvent event) {
    if (!handleMousePointers()) {
      // if we're not handling mouse pointers, return immediately if this is not a touch event
      if (pointerEventsSupport.isPointerEvent(event) && !pointerEventsSupport.isPointerTouchEvent(event)) {
        return true;
      }
    }
    return super.handle(event);
  }

  /**
   * Returns true if the DragGR can handle mouse pointers.  By default DragGR only operates on touch pointers, this
   * method can be overridden to allow widgets to drag on mouse pointers as well.
   *
   * @return true if the DragGR can handle mouse pointers.
   */
  protected boolean handleMousePointers() {
    return false;
  }

  @Override
  protected boolean onStart(TouchData startedTouch) {
    // TODO should event return a value?
    fireEvent(new DragGestureStartEvent(startedTouch, this));
    return true;
  }

  @Override
  protected void onMove(List<TouchData> touches) {
    TouchData first = touches.get(0);
    if (!dragging) {
      int x1 = first.getStartPosition().getX();
      int y1 = first.getStartPosition().getY();
      int x2 = first.getLastPosition().getX();
      int y2 = first.getLastPosition().getY();
      if (Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) < distanceSquared) {
        return;
      }
      dragging = true;
      pointerEventsSupport.setPointerCapture(first.getStartElement().asElement().<XElement>cast(), first.getLastNativeEvent());
      fireEvent(new DragGestureStartEvent(first, this));
    } else {
      fireEvent(new DragGestureMoveEvent(first, this));
    }
  }

  @Override
  protected void onEnd(List<TouchData> touches) {
    if (dragging) {
      fireEvent(new DragGestureEndEvent(touches.get(0), this));
      dragging = false;
    }
  }

  @Override
  protected void onCancel(List<TouchData> touches) {
    if (dragging) {
      fireEvent(new DragGestureCancelEvent(touches.get(0), this));
      dragging = false;
    }
  }

  public static class DragGestureStartEvent extends AbstractGestureEvent<DragGestureStartEvent.DragGestureStartHandler> {
    private static final Type<DragGestureStartHandler> TYPE = new Type<DragGestureStartHandler>();
    private final TouchData touchData;

    public DragGestureStartEvent(TouchData touchData, GestureRecognizer gesture) {
      super(gesture);
      this.touchData = touchData;
    }

    public TouchData getTouchData() {
      return touchData;
    }

    @Override
    public Type<DragGestureStartHandler> getAssociatedType() {
      return TYPE;
    }

    public static Type<DragGestureStartHandler> getType() {
      return TYPE;
    }

    @Override
    public void dispatch(DragGestureStartHandler handler) {
      handler.onDragGestureStart(this);
    }

    public interface DragGestureStartHandler extends EventHandler {
      void onDragGestureStart(DragGestureStartEvent event);
    }

    public interface HasDragGestureStartHandlers {
      HandlerRegistration addDragGestureStartHandler(DragGestureStartHandler handler);
    }
  }

  public static class DragGestureMoveEvent extends AbstractGestureEvent<DragGestureMoveEvent.DragGestureMoveHandler> {
    private static final Type<DragGestureMoveHandler> TYPE = new Type<DragGestureMoveHandler>();
    private final TouchData touchData;

    public static Type<DragGestureMoveHandler> getType() {
      return TYPE;
    }

    public DragGestureMoveEvent(TouchData touchData, DragGestureRecognizer gesture) {
      super(gesture);
      this.touchData = touchData;
    }

    public TouchData getTouchData() {
      return touchData;
    }

    @Override
    public Type<DragGestureMoveHandler> getAssociatedType() {
      return TYPE;
    }

    @Override
    public void dispatch(DragGestureMoveHandler handler) {
      handler.onDragGestureMove(this);
    }

    public interface DragGestureMoveHandler extends EventHandler {
      void onDragGestureMove(DragGestureMoveEvent event);
    }

    public interface HasDragGestureMoveHandlers {
      HandlerRegistration addDragGestureMoveHandler(DragGestureMoveHandler handler);
    }
  }

  public static class DragGestureCancelEvent extends AbstractGestureEvent<DragGestureCancelEvent.DragGestureCancelHandler> {
    private static final Type<DragGestureCancelHandler> TYPE = new Type<DragGestureCancelHandler>();
    private final TouchData touchData;

    public static Type<DragGestureCancelHandler> getType() {
      return TYPE;
    }

    public DragGestureCancelEvent(TouchData touchData, DragGestureRecognizer gesture) {
      super(gesture);
      this.touchData = touchData;
    }

    public TouchData getTouchData() {
      return touchData;
    }

    @Override
    public Type<DragGestureCancelHandler> getAssociatedType() {
      return TYPE;
    }

    @Override
    public void dispatch(DragGestureCancelHandler handler) {
      handler.onDragGestureCancel(this);
    }

    public interface DragGestureCancelHandler extends EventHandler {
      void onDragGestureCancel(DragGestureCancelEvent event);
    }

    public interface HasDragGestureCancelHandlers {
      HandlerRegistration addDragGestureCancelHandler(DragGestureCancelHandler handler);
    }
  }

  public static class DragGestureEndEvent extends AbstractGestureEvent<DragGestureEndEvent.DragGestureEndHandler> {
    private static final Type<DragGestureEndHandler> TYPE = new Type<DragGestureEndHandler>();
    private final TouchData touchData;

    public static Type<DragGestureEndHandler> getType() {
      return TYPE;
    }

    public DragGestureEndEvent(TouchData touchData, DragGestureRecognizer gesture) {
      super(gesture);
      this.touchData = touchData;
    }

    public TouchData getTouchData() {
      return touchData;
    }

    @Override
    public Type<DragGestureEndHandler> getAssociatedType() {
      return TYPE;
    }

    @Override
    public void dispatch(DragGestureEndHandler handler) {
      handler.onDragGestureEnd(this);
    }

    public interface DragGestureEndHandler extends EventHandler {
      void onDragGestureEnd(DragGestureEndEvent event);
    }

    public interface HasDragGestureEndHandlers {
      HandlerRegistration addDragGestureEndHandler(DragGestureEndHandler handler);
    }
  }

}
