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
import com.google.gwt.user.client.Timer;

public class LongPressOrTapGestureRecognizer extends TapGestureRecognizer {
  public abstract static class CellLongPressOrTapGestureRecognizer<T> extends CellGestureAdapter<LongPressOrTapGestureRecognizer, T> {
    private final int minPressTimeMs;
    private final int maxMoveDistance;

    protected CellLongPressOrTapGestureRecognizer() {
      this(TouchConstants.INSTANCE.longPressMs(), TouchConstants.INSTANCE.touchMaxDistance());
    }

    public CellLongPressOrTapGestureRecognizer(int minPressTimeMs, int maxMoveDistance) {
      this.minPressTimeMs = minPressTimeMs;
      this.maxMoveDistance = maxMoveDistance;
    }

    @Override
    protected LongPressOrTapGestureRecognizer createRecognizer() {
      return new LongPressOrTapGestureRecognizer(minPressTimeMs, maxMoveDistance) {
        @Override
        protected void onLongPress(TouchData touchData) {
          // save variables from recognizer instance before super.onLongPress() removes this recognizer
          Context context = getContext(this);
          Element parent = getParent(this);
          T value = getValue(this);
          ValueUpdater<T> valueUpdater = getValueUpdater(this);
          super.onLongPress(touchData);
          CellLongPressOrTapGestureRecognizer.this.onLongPress(touchData, context, parent, value, valueUpdater);
          release(this);
        }

        @Override
        protected void onEnd(List<TouchData> touches) {
          // save variables from recognizer instance in case super.onEnd() removes this recognizer
          Context context = getContext(this);
          Element parent = getParent(this);
          T value = getValue(this);
          ValueUpdater<T> valueUpdater = getValueUpdater(this);
          super.onEnd(touches);
          CellLongPressOrTapGestureRecognizer.this.onTap(touches.get(0), context, parent, value, valueUpdater);
          release(this);
        }

        @Override
        protected void onTap(TouchData touchData) {
          CellLongPressOrTapGestureRecognizer.this.onTap(touchData, getContext(this), getParent(this), getValue(this), getValueUpdater(this));
        }

        @Override
        protected void onCancel(List<TouchData> touches) {
          super.onCancel(touches);
          release(this);
        }
      };
    }

    protected abstract void onLongPress(TouchData touch, Context context, Element parent, T value, ValueUpdater<T> updater);

    protected abstract void onTap(TouchData touch, Context context, Element parent, T value, ValueUpdater<T> updater);
  }

  private final int minPressTimeMs;
  private final Timer timer = new Timer() {
    @Override
    public void run() {
      onLongPress(getTouches().get(0));
    }
  };

  public LongPressOrTapGestureRecognizer() {
    this(TouchConstants.INSTANCE.longPressMs());
  }

  public LongPressOrTapGestureRecognizer(int minPressTimeMs) {
    this(minPressTimeMs, TouchConstants.INSTANCE.touchMaxDistance());
  }

  public LongPressOrTapGestureRecognizer(int minPressTimeMs, int distance) {
    super(distance);
    this.minPressTimeMs = minPressTimeMs;
  }

  @Override
  protected boolean onStart(TouchData startedTouch) {
    timer.schedule(minPressTimeMs);
    return super.onStart(startedTouch);
  }

  @Override
  protected void onEnd(List<TouchData> touches) {
    //TODO consider firing longPressEnd event here and not calling super (and not canceling in onLongPress)
    timer.cancel();
    super.onEnd(touches);
  }

  @Override
  protected void onTap(TouchData touchData) {
    super.onTap(touchData);
  }

  @Override
  protected void onCancel(List<TouchData> touches) {
    timer.cancel();
    super.onCancel(touches);
  }

  protected void onLongPress(TouchData touchData) {
    cancel();
    fireEvent(new LongPressEvent(touchData, this));
  }

  public static class LongPressEvent extends AbstractGestureEvent<LongPressEvent.LongPressHandler> {
    private static final Type<LongPressHandler> TYPE = new Type<LongPressHandler>();
    private final TouchData touchData;

    public LongPressEvent(TouchData touchData, LongPressOrTapGestureRecognizer gesture) {
      super(gesture);
      this.touchData = touchData;
    }

    public TouchData getTouchData() {
      return touchData;
    }

    @Override
    public Type<LongPressHandler> getAssociatedType() {
      return TYPE;
    }

    public static Type<LongPressHandler> getType() {
      return TYPE;
    }

    @Override
    public void dispatch(LongPressHandler handler) {
      handler.onLongPress(this);
    }

    public interface LongPressHandler extends EventHandler {
      void onLongPress(LongPressEvent event);
    }

    public interface HasLongPressHandlers {
      HandlerRegistration addLongPressHandler(LongPressHandler handler);
    }
  }
}
