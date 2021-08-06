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
package com.sencha.gxt.fx.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.DragGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.client.gestures.TouchEventToGestureAdapter;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.resources.CommonStyles.Styles;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.fx.client.DragCancelEvent.DragCancelHandler;
import com.sencha.gxt.fx.client.DragCancelEvent.HasDragCancelHandlers;
import com.sencha.gxt.fx.client.DragEndEvent.DragEndHandler;
import com.sencha.gxt.fx.client.DragEndEvent.HasDragEndHandlers;
import com.sencha.gxt.fx.client.DragHandler.HasDragHandlers;
import com.sencha.gxt.fx.client.DragMoveEvent.DragMoveHandler;
import com.sencha.gxt.fx.client.DragMoveEvent.HasDragMoveHandlers;
import com.sencha.gxt.fx.client.DragStartEvent.DragStartHandler;
import com.sencha.gxt.fx.client.DragStartEvent.HasDragStartHandlers;

/**
 * Adds drag behavior to any widget. Drag operations can be initiated from the
 * widget itself, or another widget, such as the header in a dialog.
 *
 * <p/>
 * It is possible to specify event targets that will be ignored. If the target
 * element has the {@link Styles#nodrag()} style (as returned by
 * {@link CommonStyles#get()}) it will not trigger a drag operation.
 */
public class Draggable implements HasDragStartHandlers, HasDragEndHandlers, HasDragMoveHandlers, HasDragCancelHandlers,
    HasDragHandlers {

  public interface DraggableAppearance {
    void addUnselectableStyle(Element element);

    Element createProxy();

    void removeUnselectableStyle(Element element);

    void setProxyStyle(String proxyClass);
  }

  protected int conX, conY, conWidth, conHeight;
  protected int dragStartX, dragStartY;
  protected int lastX, lastY;
  protected XElement proxyEl;
  protected Rectangle startBounds;
  protected DragGestureRecognizer dragGestureRecognizer;

  private final DraggableAppearance appearance;

  private int clientWidth, clientHeight;
  private boolean constrainClient = true;
  private boolean constrainHorizontal;
  private boolean constrainVertical;
  private Widget container;
  private boolean dragging;
  private Widget dragWidget;
  private XElement dragWidgetElement;
  private boolean enabled = true;
  private Widget handleWidget;
  private GroupingHandlerRegistration handlerRegistrations;
  private boolean moveAfterProxyDrag = true;
  private BaseEventPreview eventPreview;
  private boolean sizeProxyToSource = true;
  private int startDragDistance = 2;
  private Element startElement;
  // config
  private boolean updateZIndex = true;
  private boolean useProxy = true;
  private int xLeft = Style.DEFAULT, xRight = Style.DEFAULT;
  private int xTop = Style.DEFAULT, xBottom = Style.DEFAULT;
  private SimpleEventBus eventBus;

  /**
   * Creates a new draggable instance.
   *
   * @param dragWidget the widget to be dragged
   */
  public Draggable(Widget dragWidget) {
    this(dragWidget, dragWidget, GWT.<DraggableAppearance> create(DraggableAppearance.class));
  }

  /**
   * Creates a new draggable instance.
   *
   * @param dragWidget the widget to be dragged
   * @param appearance the appearance with which to render the component
   */
  public Draggable(Widget dragWidget, DraggableAppearance appearance) {
    this(dragWidget, dragWidget, appearance);
  }

  /**
   * Create a new draggable instance.
   *
   * @param dragWidget the widget to be dragged
   * @param handleWidget the widget drags will be initiated from
   */
  public Draggable(final Widget dragWidget, final Widget handleWidget) {
    this(dragWidget, handleWidget, GWT.<DraggableAppearance>create(DraggableAppearance.class));
  }

  /**
   * Create a new draggable instance.
   *
   * @param dragWidget the widget to be dragged
   * @param handleWidget the widget drags will be initiated from
   * @param appearance the appearance with which to render the component
   */
  public Draggable(final Widget dragWidget, final Widget handleWidget, DraggableAppearance appearance) {
    this.dragWidget = dragWidget;
    this.handleWidget = handleWidget;
    this.appearance = appearance;

    handleWidget.getElement().getStyle().setProperty("touchAction", "none");
    handleWidget.getElement().getStyle().setProperty("msTouchAction", "none");

    dragWidgetElement = dragWidget.getElement().cast();

    // Determines where the drop target is for mouse
    // Mouse over provides drop over element target
    eventPreview = new BaseEventPreview() {
      @Override
      public boolean onPreview(NativePreviewEvent event) {
        Event e = event.getNativeEvent().cast();
        e.preventDefault();
        switch (event.getTypeInt()) {
          case Event.ONKEYDOWN:
            if (dragging && e.getKeyCode() == KeyCodes.KEY_ESCAPE) {
              cancelDrag();
            }
            break;
          case Event.ONMOUSEMOVE:
            onMouseMove(e);
            break;
          case Event.ONMOUSEUP:
            stopDrag(e);
            break;
        }
        return true;
      }
    };
    eventPreview.setAutoHide(false);

    MouseDownHandler mouseDownHandler = new MouseDownHandler() {
      @Override
      public void onMouseDown(MouseDownEvent event) {
        Draggable.this.onMouseDown(event);
      }
    };

    // Register mouse and touch listeners
    handlerRegistrations = new GroupingHandlerRegistration();
    handlerRegistrations.add(handleWidget.addDomHandler(mouseDownHandler, MouseDownEvent.getType()));

    dragGestureRecognizer = getDragGestureRecognizer();
  }

  /**
   * Crates the drag gesture recognizer and passes along the touch events to Draggable.
   *
   * @return the grag gesture recognizer
   */
  protected DragGestureRecognizer getDragGestureRecognizer() {
    if (dragGestureRecognizer == null) {
      dragGestureRecognizer = new DragGestureRecognizer() {
        @Override
        protected boolean onStart(TouchData startedTouch) {
          super.onStart(startedTouch);

          return onTouchStart(startedTouch);
        }

        @Override
        protected void onMove(List<TouchData> touches) {
          super.onMove(touches);

          onTouchMove(touches);
        }

        @Override
        protected void onCancel(List<TouchData> touches) {
          super.onCancel(touches);

          onTouchEnd(touches);
        }

        @Override
        protected void onEnd(List<TouchData> touches) {
          super.onEnd(touches);

          onTouchEnd(touches);
        }
      };

      TouchEventToGestureAdapter touchEventsGestureAdapter = new TouchEventToGestureAdapter(handleWidget, dragGestureRecognizer);

      handlerRegistrations.add(touchEventsGestureAdapter.getHandlerRegistration());
    }

    return dragGestureRecognizer;
  }

  protected void onTouchEnd(List<TouchData> touches) {
    stopDrag(touches.get(0).getLastNativeEvent().<Event>cast());
  }

  protected void onTouchMove(List<TouchData> touches) {
    TouchData touch = touches.get(0);
    Point pos = touch.getLastPosition();
    handleMove(pos.getX(), pos.getY(), touch.getLastNativeEvent().<Event>cast());
  }

  protected boolean onTouchStart(TouchData startedTouch) {
    if (!enabled) {
      return false;
    }

    Element target = startedTouch.getStartElement().asElement();
    if (hasClassName(target, CommonStyles.get().nodrag())) {
      return false;
    }

    if (!handleWidget.getElement().isOrHasChild(target)) {
      return false;
    }

    Point position = startedTouch.getStartPosition();
    Element start = startedTouch.getStartElement() != null ? startedTouch.getStartElement().asElement() : null;
    handleStart(position.getX(), position.getY(), startedTouch.getLastNativeEvent().<Event>cast(), start);
    return true;
  }

  @Override
  public HandlerRegistration addDragCancelHandler(DragCancelHandler handler) {
    return ensureHandlers().addHandler(DragCancelEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDragEndHandler(DragEndHandler handler) {
    return ensureHandlers().addHandler(DragEndEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDragHandler(DragHandler handler) {
    GroupingHandlerRegistration reg = new GroupingHandlerRegistration();
    reg.add(ensureHandlers().addHandler(DragStartEvent.getType(), handler));
    reg.add(ensureHandlers().addHandler(DragEndEvent.getType(), handler));
    reg.add(ensureHandlers().addHandler(DragMoveEvent.getType(), handler));
    reg.add(ensureHandlers().addHandler(DragCancelEvent.getType(), handler));
    return reg;
  }

  @Override
  public HandlerRegistration addDragMoveHandler(DragMoveHandler handler) {
    return ensureHandlers().addHandler(DragMoveEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDragStartHandler(DragStartHandler handler) {
    return ensureHandlers().addHandler(DragStartEvent.getType(), handler);
  }

  /**
   * Cancels the drag if running.
   */
  public void cancelDrag() {
    eventPreview.remove();
    if (dragging) {
      dragging = false;
      if (isUseProxy()) {
        proxyEl.disableTextSelection(false);
        proxyEl.getStyle().setVisibility(Visibility.HIDDEN);
        proxyEl.removeFromParent();
      } else {
        dragWidgetElement.setXY(startBounds.getX(), startBounds.getY());
      }

      ensureHandlers().fireEventFromSource(new DragCancelEvent(dragWidget, startElement), this);
      afterDrag();
    }
    startElement = null;
  }

  /**
   * Returns the drag container.
   *
   * @return the drag container
   */
  public Widget getContainer() {
    return container;
  }

  /**
   * Specifies a container to which the drag widget is constrained.
   *
   * @param container the container
   */
  public void setContainer(Widget container) {
    this.container = container;
  }

  /**
   * Returns the drag handleWidget.
   *
   * @return the drag handleWidget
   */
  public Widget getDragHandle() {
    return handleWidget;
  }

  /**
   * Returns the widget being dragged.
   *
   * @return the drag widget
   */
  public Widget getDragWidget() {
    return dragWidget;
  }

  /**
   * Returns the number of pixels the cursor must move before dragging begins.
   *
   * @return the distance in pixels
   */
  public int getStartDragDistance() {
    return startDragDistance;
  }

  /**
   * Specifies how far the cursor must move after mousedown to start dragging
   * (defaults to 2).
   *
   * @param startDragDistance the start distance in pixels
   */
  public void setStartDragDistance(int startDragDistance) {
    this.startDragDistance = startDragDistance;
  }

  /**
   * Returns true if drag is constrained to the viewport.
   *
   * @return the constrain client state
   */
  public boolean isConstrainClient() {
    return constrainClient;
  }

  /**
   * True to set constrain movement to the viewport (defaults to true).
   *
   * @param constrainClient true to constrain to viewport
   */
  public void setConstrainClient(boolean constrainClient) {
    this.constrainClient = constrainClient;
  }

  /**
   * Returns true if horizontal movement is constrained.
   *
   * @return the horizontal constrain state
   */
  public boolean isConstrainHorizontal() {
    return constrainHorizontal;
  }

  /**
   * True to stop horizontal movement (defaults to false).
   *
   * @param constrainHorizontal true to stop horizontal movement
   */
  public void setConstrainHorizontal(boolean constrainHorizontal) {
    this.constrainHorizontal = constrainHorizontal;
  }

  /**
   * Returns true if vertical movement is constrained.
   *
   * @return true if vertical movement is constrained
   */
  public boolean isConstrainVertical() {
    return constrainVertical;
  }

  /**
   * True to stop vertical movement (defaults to false).
   *
   * @param constrainVertical true to stop vertical movement
   */
  public void setConstrainVertical(boolean constrainVertical) {
    this.constrainVertical = constrainVertical;
  }

  /**
   * Returns <code>true</code> if a drag is in progress.
   *
   * @return the drag state
   */
  public boolean isDragging() {
    return dragging;
  }

  /**
   * Returns <code>true</code> if enabled.
   *
   * @return the enable state
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Enables dragging if the argument is <code>true</code>, and disables it
   * otherwise.
   *
   * @param enabled the new enabled state
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Returns true if the drag widget is moved after a proxy drag.
   *
   * @return the move after proxy state
   */
  public boolean isMoveAfterProxyDrag() {
    return moveAfterProxyDrag;
  }

  /**
   * True to move source widget after a proxy drag (defaults to true).
   *
   * @param moveAfterProxyDrag true to move after a proxy drag
   */
  public void setMoveAfterProxyDrag(boolean moveAfterProxyDrag) {
    this.moveAfterProxyDrag = moveAfterProxyDrag;
  }

  /**
   * Returns true if the proxy element is sized to match the drag widget.
   *
   * @return the size proxy to source state
   */
  public boolean isSizeProxyToSource() {
    return sizeProxyToSource;
  }

  /**
   * True to set proxy dimensions the same as the drag widget (defaults to
   * true).
   *
   * @param sizeProxyToSource true to update proxy size
   */
  public void setSizeProxyToSource(boolean sizeProxyToSource) {
    this.sizeProxyToSource = sizeProxyToSource;
  }

  /**
   * Returns true if the z-index is updated after a drag.
   *
   * @return the update z-index state
   */
  public boolean isUpdateZIndex() {
    return updateZIndex;
  }

  /**
   * True if the CSS z-index should be updated on the widget being dragged.
   * Setting this value to <code>true</code> will ensure that the dragged
   * element is always displayed over all other widgets (defaults to true).
   *
   * @param updateZIndex true update the z-index
   */
  public void setUpdateZIndex(boolean updateZIndex) {
    this.updateZIndex = updateZIndex;
  }

  /**
   * Returns true if proxy element is enabled.
   *
   * @return the use proxy state
   */
  public boolean isUseProxy() {
    return useProxy;
  }

  /**
   * True to use a proxy widget during drag operation (defaults to true).
   *
   * @param useProxy true use a proxy
   */
  public void setUseProxy(boolean useProxy) {
    this.useProxy = useProxy;
  }

  /**
   * Removes the drag handles.
   */
  public void release() {
    cancelDrag();
    handlerRegistrations.removeHandler();
  }

  /**
   * Sets the proxy element.
   *
   * @param element the proxy element
   */
  public void setProxy(XElement element) {
    proxyEl = element;
  }

  public void setProxyStyle(String proxyClass) {
    appearance.setProxyStyle(proxyClass);
  }

  /**
   * Constrains the horizontal travel.
   *
   * @param left the number of pixels the element can move to the left
   * @param right the number of pixels the element can move to the right
   */
  public void setXConstraint(int left, int right) {
    xLeft = left;
    xRight = right;
  }

  /**
   * Constrains the vertical travel.
   *
   * @param top the number of pixels the element can move to the up
   * @param bottom the number of pixels the element can move to the down
   */
  public void setYConstraint(int top, int bottom) {
    xTop = top;
    xBottom = bottom;
  }

  protected void afterDrag() {
    appearance.removeUnselectableStyle(Document.get().getBody());
    Shim.get().uncover();
  }

  protected XElement createProxy() {
    return proxyEl = appearance.createProxy().cast();
  }

  protected void handleStart(int x, int y, Event event, Element target) {
    startBounds = dragWidgetElement.getBounds();

    startElement = target;

    dragStartX = x;
    dragStartY = y;

    eventPreview.add();

    clientWidth = Window.getClientWidth() + XDOM.getBodyScrollLeft();
    clientHeight = Window.getClientHeight() + XDOM.getBodyScrollTop();

    if (container != null) {
      conX = container.getAbsoluteLeft();
      conY = container.getAbsoluteTop();
      conWidth = container.getOffsetWidth();
      conHeight = container.getOffsetHeight();
    }

    if (startDragDistance == 0) {
      startDrag(event);
    }
  }

  protected void onMouseDown(MouseDownEvent e) {
    if (!enabled || e.getNativeEvent().getButton() != Event.BUTTON_LEFT) {
      return;
    }
    Element target = e.getNativeEvent().getEventTarget().cast();
    if (hasClassName(target, CommonStyles.get().nodrag())) {
      return;
    }

    // still allow text selection, prevent drag of other elements
    if ((!"input".equalsIgnoreCase(target.getTagName()) && !"textarea".equalsIgnoreCase(target.getTagName()))
        || target.getPropertyBoolean("disabled")) {
      e.getNativeEvent().preventDefault();
    }

    handleStart(e.getClientX(), e.getClientY(), e.getNativeEvent().<Event> cast(), target);
  }

  protected void handleMove(int x, int y, Event event) {
    if (!dragging && (Math.abs(dragStartX - x) > startDragDistance || Math.abs(dragStartY - y) > startDragDistance)) {
      startDrag(event);
    }

    if (dragging) {
      int left = constrainHorizontal ? startBounds.getX() : startBounds.getX() + (x - dragStartX);
      int top = constrainVertical ? startBounds.getY() : startBounds.getY() + (y - dragStartY);

      if (constrainClient) {
        if (!constrainHorizontal) {
          int width = startBounds.getWidth();
          left = Math.max(left, 0);
          left = Math.max(0, Math.min(clientWidth - width, left));
        }
        if (!constrainVertical) {
          top = Math.max(top, 0);
          int height = startBounds.getHeight();
          if (Math.min(clientHeight - height, top) > 0) {
            top = Math.max(2, Math.min(clientHeight - height, top));
          }
        }
      }

      if (container != null) {
        int width = startBounds.getWidth();
        int height = startBounds.getHeight();
        if (!constrainHorizontal) {
          left = Math.max(left, conX);
          left = Math.min(conX + conWidth - width, left);
        }
        if (!constrainVertical) {
          top = Math.min(conY + conHeight - height, top);
          top = Math.max(top, conY);
        }
      }
      if (!constrainHorizontal) {
        if (xLeft != Style.DEFAULT) {
          left = Math.max(startBounds.getX() - xLeft, left);
        }
        if (xRight != Style.DEFAULT) {
          left = Math.min(startBounds.getX() + xRight, left);
        }
      }

      if (!constrainVertical) {
        if (xTop != Style.DEFAULT) {
          top = Math.max(startBounds.getY() - xTop, top);
        }
        if (xBottom != Style.DEFAULT) {
          top = Math.min(startBounds.getY() + xBottom, top);
        }
      }

      lastX = left;
      lastY = top;

      DragMoveEvent evt = new DragMoveEvent(dragWidget, startElement, lastX, lastY, event);
      ensureHandlers().fireEventFromSource(evt, this);

      if (evt.isCancelled()) {
        cancelDrag();
        return;
      }

      int tl = evt.getX() != lastX ? evt.getX() : lastX;
      int tt = evt.getY() != lastY ? evt.getY() : lastY;
      if (useProxy) {
        proxyEl.setXY(tl, tt);
      } else {
        dragWidgetElement.setXY(tl, tt);
      }
    }
  }

  protected void onMouseMove(Event event) {
    Element elem = event.getEventTarget().cast();
    // elem.getClassName throwing GWT exception when dragged widget is over SVG / VML
    if (hasClassName(elem, "x-insert")) {
      return;
    }
    handleMove(event.getClientX(), event.getClientY(), event);
  }

  protected void startDrag(Event event) {
    DragStartEvent de = new DragStartEvent(dragWidget, startElement, startBounds.getX(), startBounds.getY(), event);
    ensureHandlers().fireEventFromSource(de, this);

    if (de.isCancelled()) {
      cancelDrag();
      return;
    }

    dragging = true;
    appearance.addUnselectableStyle(Document.get().getBody());

    if (!useProxy) {
      dragWidget.getElement().<XElement> cast().makePositionable();
    }

    if (event != null) {
      event.preventDefault();
    }
    Shim.get().cover(true);

    lastX = startBounds.getX();
    lastY = startBounds.getY();

    if (useProxy) {
      if (proxyEl == null) {
        createProxy();
      }
      if (container == null) {
        Document.get().getBody().appendChild(proxyEl);
      } else {
        container.getElement().appendChild(proxyEl);
      }
      proxyEl.setVisibility(true);
      proxyEl.setZIndex(XDOM.getTopZIndex());
      proxyEl.makePositionable(true);

      if (sizeProxyToSource) {
        proxyEl.setBounds(startBounds);
      } else {
        proxyEl.setXY(startBounds.getX() + 50, startBounds.getY() + 50);
      }

      // did listeners change size?
      if (de.getHeight() > 0 && de.getWidth() > 0) {
        proxyEl.setSize(de.getWidth(), de.getHeight(), true);
      } else if (de.getHeight() > 0) {
        proxyEl.setHeight(de.getHeight(), true);
      } else if (de.getWidth() > 0) {
        proxyEl.setWidth(de.getWidth(), true);
      }
    } else if (updateZIndex) {
      dragWidget.getElement().<XElement> cast().setZIndex(XDOM.getTopZIndex());
    }
  }

  protected void stopDrag(Event event) {
    eventPreview.remove();
    if (dragging) {
      dragging = false;
      if (isUseProxy()) {
        if (isMoveAfterProxyDrag()) {
          Rectangle rect = proxyEl.getBounds();
          dragWidget.getElement().<XElement> cast().setXY(rect.getX(), rect.getY());
        }
        proxyEl.setVisibility(false);
        proxyEl.disableTextSelection(false);
        proxyEl.removeFromParent();
      }
      DragEndEvent de = new DragEndEvent(dragWidget, startElement, lastX, lastY, event);
      ensureHandlers().fireEventFromSource(de, this);
      afterDrag();
    }
    startElement = null;
  }

  SimpleEventBus ensureHandlers() {
    return eventBus == null ? eventBus = new SimpleEventBus() : eventBus;
  }

  private native boolean hasClassName(Element elem, String className) /*-{
    return !!elem.hasAttribute && elem.hasAttribute("class") && elem.getAttribute("class").indexOf(className) != -1;
  }-*/;

}
