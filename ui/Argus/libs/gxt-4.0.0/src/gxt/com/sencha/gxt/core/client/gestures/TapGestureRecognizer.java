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

public class TapGestureRecognizer extends AbstractGestureRecognizer {

  public abstract static class CellTapGestureRecognizer<T> extends CellGestureAdapter<TapGestureRecognizer, T> {
    private final int maxMoveDistance;

    protected CellTapGestureRecognizer() {
      this(TouchConstants.INSTANCE.touchMaxDistance());
    }

    protected CellTapGestureRecognizer(int maxMoveDistance) {
      this.maxMoveDistance = maxMoveDistance;
    }

    @Override
    public TapGestureRecognizer createRecognizer() {
      return new TapGestureRecognizer(maxMoveDistance) {
        @Override
        protected boolean onStart(TouchData t) {
          onTapStart(t, getContext(this), getParent(this), getValue(this), getValueUpdater(this));
          return super.onStart(t);
        }

        @Override
        protected void onEnd(List<TouchData> touches) {
          super.onEnd(touches);
        }

        @Override
        protected void onTap(TouchData touchData) {
          CellTapGestureRecognizer.this.onTap(touchData, getContext(this), getParent(this), getValue(this), getValueUpdater(this));
          release(this);
        }

        @Override
        protected void onCancel(List<TouchData> touches) {
          super.onCancel(touches);
          release(this);
        }

        @Override
        protected void handlePreventDefault(NativeEvent event) {
          CellTapGestureRecognizer.this.handlePreventDefault(event);
        }
      };
    }
    protected void onTapStart(TouchData tap, Context context, Element parent, T value, ValueUpdater<T> valueUpdater) {
    }

    protected void handlePreventDefault(NativeEvent event) {
      event.preventDefault();
    }

    protected abstract void onTap(TouchData tap, Context context, Element parent, T value, ValueUpdater<T> valueUpdater);
  }

  private final int distanceSqr;

  public TapGestureRecognizer() {
    this(TouchConstants.INSTANCE.touchMaxDistance());
  }

  public TapGestureRecognizer(int distance) {
    super(ExtraTouchBehavior.IGNORE, 1);

    distanceSqr = distance * distance;
  }

  @Override
  protected boolean onStart(TouchData startedTouch) {
    fireEvent(new TapGestureStartEvent(startedTouch, this));
    return true;
  }

  @Override
  protected void onMove(List<TouchData> touches) {
    TouchData first = touches.get(0);

    int x1 = first.getStartPosition().getX();
    int y1 = first.getStartPosition().getY();
    int x2 = first.getLastPosition().getX();
    int y2 = first.getLastPosition().getY();

    if (Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) > distanceSqr) {
      cancel();
    }
  }

  protected void onTap(TouchData touchData) {
    fireEvent(new TapGestureEvent(touchData, this));
  }

  @Override
  protected void onEnd(List<TouchData> touches) {
    TouchData touchData = touches.get(0);
    onTap(touchData);
  }

  @Override
  protected void onCancel(List<TouchData> touches) {
    fireEvent(new TapCancelEvent(touches.get(0), this));
  }

  public static class TapGestureStartEvent extends AbstractGestureEvent<TapGestureStartEvent.TapGestureStartHandler> {
    public static final Type<TapGestureStartHandler> TYPE = new Type<TapGestureStartHandler>();
    private final TouchData touchData;

    public TapGestureStartEvent(TouchData touchData, TapGestureRecognizer gestureRecognizer) {
      super(gestureRecognizer);
      this.touchData = touchData;
    }

    public TouchData getTouchData() {
      return touchData;
    }

    @Override
    public Type<TapGestureStartHandler> getAssociatedType() {
      return TYPE;
    }

    @Override
    public void dispatch(TapGestureStartHandler handler) {
      handler.onTapGestureStart(this);
    }

    public interface TapGestureStartHandler extends EventHandler {
      void onTapGestureStart(TapGestureStartEvent event);
    }

    public interface HasTapGestureStartHandlers {
      HandlerRegistration addTapGestureStartHandler(TapGestureStartHandler handler);
    }
  }

  public static class TapGestureEvent extends AbstractGestureEvent<TapGestureEvent.TapGestureHandler> {
    public static final Type<TapGestureHandler> TYPE = new Type<TapGestureHandler>();
    private final TouchData touchData;

    public TapGestureEvent(TouchData touchData, TapGestureRecognizer gesture) {
      super(gesture);
      this.touchData = touchData;
    }

    public static Type<TapGestureHandler> getType() {
      return TYPE;
    }

    public TouchData getTouchData() {
      return touchData;
    }

    @Override
    public Type<TapGestureHandler> getAssociatedType() {
      return TYPE;
    }

    @Override
    public void dispatch(TapGestureHandler handler) {
      handler.onTapGesture(this);
    }

    public interface TapGestureHandler extends EventHandler {
      void onTapGesture(TapGestureEvent event);
    }

    public interface HasTapGestureHandlers {
      HandlerRegistration addTapGestureHandler(TapGestureHandler handler);
    }
  }

  @Override
  protected void handlePreventDefault(NativeEvent event) {
    event.preventDefault();
  }

  public static class TapCancelEvent extends AbstractGestureEvent<TapCancelEvent.TapCancelHandler> {
    private static final Type<TapCancelHandler> TYPE = new Type<TapCancelHandler>();
    private final TouchData touchData;

    public TapCancelEvent(TouchData touchData, TapGestureRecognizer gesture) {
      super(gesture);
      this.touchData = touchData;
    }

    public TouchData getTouchData() {
      return touchData;
    }

    @Override
    public Type<TapCancelHandler> getAssociatedType() {
      return TYPE;
    }

    @Override
    public void dispatch(TapCancelHandler handler) {
      handler.onTapCancel(this);
    }

    public interface TapCancelHandler extends EventHandler {
      void onTapCancel(TapCancelEvent event);
    }

    public interface HasTapCancelHandlers {
      HandlerRegistration addTapCancelHandler(TapCancelHandler handler);
    }
  }

}
