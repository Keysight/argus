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
package com.sencha.gxt.cell.core.client;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.sencha.gxt.cell.core.client.form.FieldCell;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.CellGestureAdapter;
import com.sencha.gxt.core.client.gestures.DragGestureRecognizer;
import com.sencha.gxt.core.client.gestures.DragGestureRecognizer.CellDragGestureRecognizer;
import com.sencha.gxt.core.client.gestures.PointerEventsSupport;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer.CellTapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.google.gwt.dom.client.BrowserEvents.KEYDOWN;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEDOWN;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;
import static com.google.gwt.dom.client.BrowserEvents.TOUCHCANCEL;
import static com.google.gwt.dom.client.BrowserEvents.TOUCHEND;
import static com.google.gwt.dom.client.BrowserEvents.TOUCHMOVE;
import static com.google.gwt.dom.client.BrowserEvents.TOUCHSTART;
import static com.sencha.gxt.core.client.gestures.PointerEvents.POINTERCANCEL;
import static com.sencha.gxt.core.client.gestures.PointerEvents.POINTERDOWN;
import static com.sencha.gxt.core.client.gestures.PointerEvents.POINTERMOVE;
import static com.sencha.gxt.core.client.gestures.PointerEvents.POINTERUP;



public class SliderCell extends FieldCell<Integer> {

  public interface HorizontalSliderAppearance extends SliderAppearance {
  }

  public interface SliderAppearance extends FieldAppearance {

    int getClickedValue(Context context, Element parent, Point location);

    int getSliderLength(XElement parent);

    Element getThumb(Element parent);

    boolean isVertical();

    void onMouseDown(Context context, Element parent);

    void onMouseOut(Context context, Element parent);

    void onMouseOver(Context context, Element parent);

    void onMouseUp(Context context, Element parent);

    void render(double fractionalValue, int width, int height, SafeHtmlBuilder sb);

    void setThumbPosition(Element parent, int left);

  }

  public interface VerticalSliderAppearance extends SliderAppearance {
  }

  protected class MouseDragPreview extends BaseEventPreview {

    private final Context context;
    private final Element parent;
    private int thumbWidth;
    private int thumbHeight;

    private final ValueUpdater<Integer> valueUpdater;

    public MouseDragPreview(Context context, Element parent, ValueUpdater<Integer> valueUpdater, NativeEvent e) {
      super();
      this.context = context;
      this.parent = parent;
      this.valueUpdater = valueUpdater;

      XElement t = getAppearance().getThumb(parent).cast();
      thumbWidth = t.getOffsetWidth();
      thumbHeight = t.getOffsetHeight();

      positionTip(e);
    }

    @Override
    protected boolean onPreview(NativePreviewEvent event) {
      boolean allow = super.onPreview(event);

      switch (event.getTypeInt()) {
        case Event.ONMOUSEMOVE:
          positionTip(event.getNativeEvent());
          break;
        case Event.ONMOUSEUP:
          this.remove();
          XElement p = XElement.as(parent);
          Point location = event.getNativeEvent().<XEvent>cast().getXY();
          int v = setValue(p, reverseValue(p, getAppearance().getClickedValue(context, p, location)));
          valueUpdater.update(v);
          getAppearance().onMouseUp(context, parent);
          getAppearance().onMouseOut(context, parent);
          tip.hide();
          break;
      }

      return allow;
    }

    private void positionTip(NativeEvent event) {
      if (!showMessage) {
        return;
      }

      XElement p = XElement.as(parent);
      Point location = event.<XEvent> cast().getXY();
      int v = setValue(p, reverseValue(p, getAppearance().getClickedValue(context, p, location)));
      Element thumb = getAppearance().getThumb(parent);

      tip.setBody(onFormatValue(v));

      tip.onMouseMove(thumbWidth, thumbHeight, thumb);
    }

  }

  private class ToolTipExt extends ToolTip {
    public ToolTipExt(ToolTipConfig config) {
      super(config);
    }

    public void setBody(String text) {
      body = SafeHtmlUtils.fromString(text);
    }

    public void onMouseMove(int thumbWidth, int thumbHeight, Element target) {
      this.target = target;
      Side origAnchor = toolTipConfig.getAnchor();
      Point p = getTargetXY(0);
      p.setX(p.getX() - (thumbWidth / 2));
      p.setY(p.getY() - (thumbHeight / 2));
      super.showAt(p.getX(), p.getY());
      toolTipConfig.setAnchor(origAnchor);
    }
  }

  //TODO consider concrete subclasses of these cell adapters for each gesture class, possibly as an inner class?
  private CellGestureAdapter<TapGestureRecognizer, Integer> tapRecognizer = new CellTapGestureRecognizer<Integer>() {
    @Override
    protected void onTap(TouchData tap, Context context, Element parent, Integer value, ValueUpdater<Integer> valueUpdater) {
      SliderCell.this.onTap(tap, context, parent, value, valueUpdater);
    }
  };

  private CellGestureAdapter<DragGestureRecognizer, Integer> dragGestureRecognizer = new CellDragGestureRecognizer<Integer>() {
    @Override
    protected void onTouchMove(TouchData touch, Context context, Element parent, Integer value, ValueUpdater<Integer> valueUpdater) {
      SliderCell.this.onTouchMove(touch, context, parent, value, valueUpdater);
    }

    @Override
    protected void onTouchMoveEnd(TouchData touch, Context context, Element parent, Integer value, ValueUpdater<Integer> valueUpdater) {
      SliderCell.this.onTouchMoveEnd(touch, context, parent, value, valueUpdater);
    }

    @Override
    protected boolean handleMousePointers() {
      return true;
    }
  };

  private static final PointerEventsSupport POINTER_EVENTS_SUPPORT = PointerEventsSupport.impl;
  private static final Set<String> CONSUMED_EVENTS;
  static {
    Set<String> consumedEvents = new HashSet<String>(Arrays.asList(MOUSEDOWN, MOUSEOVER, MOUSEOUT, KEYDOWN,
            TOUCHSTART, TOUCHMOVE, TOUCHEND, TOUCHCANCEL));
    if (POINTER_EVENTS_SUPPORT.isSupported()) {
      consumedEvents.addAll(Arrays.asList(POINTERDOWN.getEventName(), POINTERMOVE.getEventName(),
              POINTERUP.getEventName(), POINTERCANCEL.getEventName()));
    }
    CONSUMED_EVENTS = Collections.unmodifiableSet(consumedEvents);
  }

  private String message = "{0}";
  private boolean showMessage = true;
  private boolean vertical = false;
  private int maxValue = 100;
  private int minValue = 0;
  private ToolTipExt tip;
  private ToolTipConfig toolTipConfig;
  private int increment = 10;

  public SliderCell() {
    this(GWT.<SliderAppearance>create(SliderAppearance.class));
  }

  public SliderCell(SliderAppearance appearance) {
    super(appearance, CONSUMED_EVENTS);
    addCellGestureAdapter(tapRecognizer);
    addCellGestureAdapter(dragGestureRecognizer);

    vertical = appearance.isVertical();

    toolTipConfig = new ToolTipConfig();
    toolTipConfig.setAnchorArrow(false);
    toolTipConfig.setMinWidth(25);
    toolTipConfig.setAutoHide(true);
    toolTipConfig.setDismissDelay(1000);
    if (vertical) {
      toolTipConfig.setAnchor(Side.LEFT);
      toolTipConfig.setMouseOffsetX(25);
      toolTipConfig.setMouseOffsetY(0);
    } else {
      toolTipConfig.setAnchor(Side.TOP);
      toolTipConfig.setMouseOffsetX(0);
      toolTipConfig.setMouseOffsetY(25);
    }

    tip = new ToolTipExt(toolTipConfig);
  }

  @Override
  public SliderAppearance getAppearance() {
    return (SliderAppearance) super.getAppearance();
  }

  /**
   * Returns the increment.
   *
   * @return the increment
   */
  public int getIncrement() {
    return increment;
  }

  /**
   * Returns the max value (defaults to 100).
   *
   * @return the max value
   */
  public int getMaxValue() {
    return maxValue;
  }

  /**
   * Returns the tool tip message.
   *
   * @return the tool tip message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Returns the minimum value (defaults to 0).
   *
   * @return the minimum value
   */
  public int getMinValue() {
    return minValue;
  }

  /**
   * Returns true if the tool tip message is shown
   *
   * @return the showMessage state
   */
  public boolean isShowMessage() {
    return showMessage;
  }

  @Override
  public void onBrowserEvent(Context context, Element parent, Integer value, NativeEvent event,
                             ValueUpdater<Integer> valueUpdater) {
    Element target = event.getEventTarget().cast();
    if (!parent.isOrHasChild(target) || isDisabled()) {
      return;
    }
    super.onBrowserEvent(context, parent, value, event, valueUpdater);

    // too late in the game to handle pointers in the super call, do the onTouch for pointers right here
    if (POINTER_EVENTS_SUPPORT.isSupported()) {
      if (POINTER_EVENTS_SUPPORT.isPointerEvent(event)) {
        onTouch(context, parent, value, event, valueUpdater);
      }
      // since we've delegated pointer events to the appropriate GRs, return immediately so mouse events don't interfere
      return;
    }

    String eventType = event.getType();

    if ("mousedown".equals(eventType)) {
      onMouseDown(context, parent, event, valueUpdater);
    } else if ("mouseover".equals(eventType)) {
      getAppearance().onMouseOver(context, parent);
    } else if ("mouseout".equals(eventType)) {
      getAppearance().onMouseOut(context, parent);
    } else if ("keydown".equals(eventType)) {
      int key = event.getKeyCode();
      if (!vertical) {
        switch (key) {
          case KeyCodes.KEY_LEFT:
            int v = setValue(parent, value - increment);
            valueUpdater.update(v);
            positionTip(parent, v);
            break;
          case KeyCodes.KEY_RIGHT:
            v = setValue(parent, value + increment);
            valueUpdater.update(v);
            positionTip(parent, v);
            break;
        }
      } else {
        switch (key) {
          case KeyCodes.KEY_DOWN:
            int v = setValue(parent, value - increment);
            valueUpdater.update(v);
            positionTip(parent, v);
            break;
          case KeyCodes.KEY_UP:
            v = setValue(parent, value + increment);
            valueUpdater.update(v);
            positionTip(parent, v);
            break;
        }
      }
    }
  }

  private void positionTip(Element parent, int v) {
    if (!showMessage) {
      return;
    }
    Element thumb = getAppearance().getThumb(parent);

    XElement t = thumb.cast();
    int thumbWidth = t.getOffsetWidth();
    int thumbHeight = t.getOffsetHeight();

    tip.setBody(onFormatValue(v));

    tip.onMouseMove(thumbWidth, thumbHeight, thumb);
  }

  @Override
  public void onEmpty(XElement parent, boolean empty) {
    getAppearance().onEmpty(parent, empty);
  }

  @Override
  public boolean redrawOnResize() {
    return true;
  }

  @Override
  public void render(Context context, Integer value, SafeHtmlBuilder sb) {
    double fractionalValue;
    if (value == null) {
      fractionalValue = 0.5;
    } else {
      fractionalValue = 1.0 * (value - minValue) / (maxValue - minValue);
    }
    getAppearance().render(fractionalValue, getWidth(), getHeight(), sb);
  }

  /**
   * How many units to change the slider when adjusting by drag and drop. Use this option to enable 'snapping' (default
   * to 10).
   *
   * @param increment the increment
   */
  public void setIncrement(int increment) {
    this.increment = increment;
  }

  /**
   * Sets the max value (defaults to 100).
   *
   * @param maxValue the max value
   */
  public void setMaxValue(int maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Sets the tool tip message (defaults to '{0}'). "{0} will be substituted with the current slider value.
   *
   * @param message the tool tip message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Sets the minimum value (defaults to 0).
   *
   * @param minValue the minimum value
   */
  public void setMinValue(int minValue) {
    this.minValue = minValue;
  }

  /**
   * Sets if the tool tip message should be displayed (defaults to true, pre-render).
   *
   * @param showMessage true to show tool tip message
   */
  public void setShowMessage(boolean showMessage) {
    this.showMessage = showMessage;
  }

  /**
   * Set the tooltip config. This is the tooltip for the message configuration. Set {@link #setShowMessage(boolean)} to
   * true to use this feature.
   * <p/>
   * {@link ToolTipConfig#setAnchor(com.sencha.gxt.core.client.Style.Side)} is a required setting for toolTipConfig.
   *
   * @param toolTipConfig is the tooltip configuration.
   */
  public void setToolTipConfig(ToolTipConfig toolTipConfig) {
    assert toolTipConfig != null : "The toolTipConfig parameter is null and it is required.";
    assert toolTipConfig.getAnchor() != null : "The toolTipConfig must have an anchor Side set. "
      + "Like toolTipConfig.setAnchor(Side.RIGHT);";
    this.toolTipConfig = toolTipConfig;
  }

  protected int constrain(int value) {
    return Util.constrain(value, minValue, maxValue);
  }

  protected int doSnap(int v) {
    if (increment == 1) {
      return v;
    }
    int m = v % increment;
    if (m != 0) {
      v -= m;
      if (m * 2 > increment) {
        v += increment;
      } else if (m * 2 < -increment) {
        v -= increment;
      }
    }
    return v;
  }

  protected double getRatio(XElement parent) {
    int v = maxValue - minValue;
    int length = getAppearance().getSliderLength(parent);
    return v == 0 ? length : ((double) length / v);
  }

  protected int normalizeValue(int value) {
    value = doSnap(value);
    value = constrain(value);
    return value;
  }

  protected String onFormatValue(int value) {
    return Format.substitute(getMessage(), value);
  }

  protected void onMouseDown(final Context context, final Element parent, NativeEvent event,
                             final ValueUpdater<Integer> valueUpdater) {
    Element target = Element.as(event.getEventTarget());
    if (!getAppearance().getThumb(parent).isOrHasChild(target)) {
      Point location = event.<XEvent> cast().getXY();
      int value = getAppearance().getClickedValue(context, parent, location);
      value = reverseValue(parent.<XElement>cast(), value);
      value = normalizeValue(value);
      valueUpdater.update(value);
      int position = translateValue(parent.<XElement> cast(), value);
      getAppearance().setThumbPosition(parent, position);
      return;
    }

    BaseEventPreview preview = new MouseDragPreview(context, parent, valueUpdater, event);
    getAppearance().onMouseDown(context, parent);
    preview.add();
  }

  protected void onTap(TouchData t, Context context, Element parent, Integer value, ValueUpdater<Integer> valueUpdater) {
    int v = getAppearance().getClickedValue(context, parent, t.getLastPosition());
    v = reverseValue(parent.<XElement>cast(), v);
    v = normalizeValue(v);
    valueUpdater.update(v);

    // move thumb
    int pos = translateValue(parent.<XElement> cast(), v);
    getAppearance().setThumbPosition(parent, pos);
  }

  protected void onTouchMove(TouchData t, Context context, Element parent, Integer value, ValueUpdater<Integer> valueUpdater) {
    int v = getAppearance().getClickedValue(context, parent, t.getLastPosition());
    v = reverseValue(parent.<XElement>cast(), v);
    v = normalizeValue(v);

    // move thumb
    int pos = translateValue(parent.<XElement> cast(), v);
    getAppearance().setThumbPosition(parent, pos);


    tip.setBody(onFormatValue(v));


    XElement thumb = getAppearance().getThumb(parent).cast();
    int thumbWidth = thumb.getOffsetWidth();
    int thumbHeight = thumb.getOffsetHeight();

    tip.onMouseMove(thumbWidth, thumbHeight, thumb);
  }
  protected void onTouchMoveEnd(TouchData t, Context context, Element parent, Integer value, ValueUpdater<Integer> valueUpdater) {
    int v = getAppearance().getClickedValue(context, parent, t.getLastPosition());
    v = reverseValue(parent.<XElement>cast(), v);
    v = normalizeValue(v);
    valueUpdater.update(v);
  }

  protected int reverseValue(XElement parent, int pos) {
    double ratio = getRatio(parent);
    if (vertical) {
      int length = getAppearance().getSliderLength(parent);
      return (int) (((minValue * ratio) + length - pos) / ratio);
    } else {
      int halfThumb = getAppearance().getThumb(parent).getOffsetWidth() / 2;
      return (int) ((pos + halfThumb + (minValue * ratio)) / ratio);
    }
  }

  protected int translateValue(XElement parent, int v) {
    int halfThumb;
    if (vertical) {
      halfThumb = getAppearance().getThumb(parent).getOffsetHeight() / 2;
    } else {
      halfThumb = getAppearance().getThumb(parent).getOffsetWidth() / 2;
    }

    double ratio = getRatio(parent);

    return (int) ((v * ratio) - (minValue * ratio) - halfThumb);
  }

  private int setValue(Element parent, int value) {
    value = normalizeValue(value);

    // move thumb
    int pos = translateValue(parent.<XElement> cast(), value);
    getAppearance().setThumbPosition(parent, pos);

    return value;
  }

}
