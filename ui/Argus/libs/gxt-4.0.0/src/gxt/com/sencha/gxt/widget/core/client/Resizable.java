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
package com.sencha.gxt.widget.core.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.fx.client.Shim;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent.HasResizeEndHandlers;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent.ResizeEndHandler;
import com.sencha.gxt.widget.core.client.event.ResizeStartEvent;
import com.sencha.gxt.widget.core.client.event.ResizeStartEvent.HasResizeStartHandlers;
import com.sencha.gxt.widget.core.client.event.ResizeStartEvent.ResizeStartHandler;
import com.sencha.gxt.widget.core.client.event.XEvent;

/**
 * Applies drag handles to a widget to make it resizable. The drag handles are inserted into the widget and positioned
 * absolute.
 * 
 * <p />
 * Only <code>Component</code> instances can be used with <code>Resizable</code> as <code>Widget</code>'s do not fire
 * resize events. Widgets can be wrapped in a {@link WidgetComponent} instance to use with <code>Resizable</code>.
 * 
 * <p>
 * Here is the list of valid resize handles:
 * </p>
 * 
 * <pre>
 * Value   Description
 * ------  -------------------
 * 'n'     north
 * 's'     south
 * 'e'     east
 * 'w'     west
 * 'nw'    northwest
 * 'sw'    southwest
 * 'se'    southeast
 * 'ne'    northeast
 * 'all'   all
 * </pre>
 */
public class Resizable implements HasResizeStartHandlers, HasResizeEndHandlers {

  /**
   * The location of the resize handle in standard compass points.
   */
  @SuppressWarnings("javadoc")
  public enum Dir {
    E, N, NE, NW, S, SE, SW, W
  }

  @SuppressWarnings("javadoc")
  public interface ResizableAppearance {

    Element createProxy();

    String getHandleStyles(Dir dir);
  }

  private class Handler implements ResizeHandler, AttachEvent.Handler {

    @Override
    public void onAttachOrDetach(AttachEvent event) {
      if (event.isAttached()) {
        onAttach();
      } else {
        onDetach();
      }
    }

    @Override
    public void onResize(ResizeEvent event) {
      onComponentResize();
    }

  }

  private class ResizeHandle extends Component {

    public Dir dir;

    private ResizeHandle() {
      setElement(Document.get().createDivElement());
      sinkEvents(Event.MOUSEEVENTS | Event.TOUCHEVENTS);
    }

    @Override
    public void onBrowserEvent(Event event) {
      switch (DOM.eventGetType(event)) {
        case Event.ONTOUCHSTART:
        case Event.ONMOUSEDOWN:
          DOM.eventCancelBubble(event, true);
          event.preventDefault();
          handleMouseDown(event, this);
          break;
      }
    }

  }

  private Dir dir;
  private boolean dynamic;
  private boolean enabled = true;
  private List<ResizeHandle> handleList;
  private Dir[] handles;
  private int maxHeight = 2000;
  private int maxWidth = 2000;
  private int minHeight = 50;
  private int minWidth = 50;
  private boolean preserveRatio = false;
  private BaseEventPreview preview;
  private XElement proxyEl;
  private Component resize;
  private boolean resizing;
  private Rectangle startBox;
  private Point startPoint;
  private SimpleEventBus eventBus;

  private GroupingHandlerRegistration registration;
  private final ResizableAppearance appearance;

  /**
   * Creates a new resizable instance with 8-way resizing.
   * 
   * @param resize the resize widget
   */
  public Resizable(Component resize) {
    this(resize, Dir.values());
  }

  /**
   * Creates a new resizable instance with the default appearance.
   * 
   * @param resize the resize widget
   * @param handles the resize handle locations
   */
  public Resizable(final Component resize, Dir... handles) {
    this(GWT.<ResizableAppearance> create(ResizableAppearance.class), resize, handles);
  }

  /**
   * Creates a new resizable instance with the specified appearance.
   * 
   * @param appearance the appearance of the resizable
   * @param resize the resize widget
   * @param handles the resize handle locations
   */
  public Resizable(ResizableAppearance appearance, final Component resize, Dir... handles) {
    this.resize = resize;
    this.handles = handles;
    this.appearance = appearance;

    resize.getElement().getStyle().setOverflow(Style.Overflow.VISIBLE);

    registration = new GroupingHandlerRegistration();
    Handler handler = new Handler();
    registration.add(resize.addAttachHandler(handler));
    registration.add(resize.addResizeHandler(handler));

    init();

    if (resize.isAttached()) {
      onAttach();
      onComponentResize();
    }
  }

  @Override
  public HandlerRegistration addResizeEndHandler(ResizeEndHandler handler) {
    return ensureHandlers().addHandler(ResizeEndEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addResizeStartHandler(ResizeStartHandler handler) {
    return ensureHandlers().addHandler(ResizeStartEvent.getType(), handler);
  }

  public ResizableAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the max height
   * 
   * @return the max height
   */
  public int getMaxHeight() {
    return maxHeight;
  }

  /**
   * Returns the max width.
   * 
   * @return the max width
   */
  public int getMaxWidth() {
    return maxWidth;
  }

  /**
   * Returns the minimum height.
   * 
   * @return the minimum height
   */
  public int getMinHeight() {
    return minHeight;
  }

  /**
   * Returns the minimum width.
   * 
   * @return the minimum width
   */
  public int getMinWidth() {
    return minWidth;
  }

  /**
   * Returns true if widget is being resized directly.
   * 
   * @return the dynamic state
   */
  public boolean isDynamic() {
    return dynamic;
  }

  /**
   * Returns true if the aspect ratio is being preserved.
   * 
   * @return true if the aspect ratio is being preserved
   */
  public boolean isPreserveRatio() {
    return preserveRatio;
  }

  /**
   * Returns <code>true</code> if if resizing.
   * 
   * @return the resize state
   */
  public boolean isResizing() {
    return resizing;
  }

  /**
   * Removes the drag handles.
   */
  public void release() {
    registration.removeHandler();

    if (handleList != null) {
      for (ResizeHandle handle : handleList) {
        handle.getElement().removeFromParent();
      }
    }
    
    onDetach();
  }

  /**
   * True to resize the widget directly instead of using a proxy (defaults to false).
   * 
   * @param dynamic true to resize directly
   */
  public void setDynamic(boolean dynamic) {
    this.dynamic = dynamic;
  }

  /**
   * Enables or disables the drag handles.
   * 
   * @param enable <code>true</code> to enable
   */
  public void setEnabled(boolean enable) {
    if (enabled != enable && handleList != null) {
      for (ResizeHandle handle : handleList) {
        handle.getElement().setVisibility(enable);
      }
      if (enable) {
        syncHandleHeight();
      }
    }
    this.enabled = enable;
  }

  /**
   * Sets the max height (defaults to 2000).
   * 
   * @param maxHeight the max height
   */
  public void setMaxHeight(int maxHeight) {
    this.maxHeight = maxHeight;
  }

  /**
   * Sets the max width (defaults to 2000).
   * 
   * @param maxWidth the max width
   */
  public void setMaxWidth(int maxWidth) {
    this.maxWidth = maxWidth;
  }

  /**
   * Sets the minimum height (default to 50).
   * 
   * @param minHeight the minimum height
   */
  public void setMinHeight(int minHeight) {
    this.minHeight = minHeight;
  }

  /**
   * Sets the minimum width (defaults to 50).
   * 
   * @param minWidth the minimum width
   */
  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }

  /**
   * True to preserve the original ratio between height and width during resize (defaults to false).
   * 
   * @param preserveRatio true to preserve the original aspect ratio
   */
  public void setPreserveRatio(boolean preserveRatio) {
    this.preserveRatio = preserveRatio;
  }

  protected Element createProxy() {
    return appearance.createProxy();
  }

  protected void init() {
    resize.getElement().makePositionable();
    if (handleList == null) {
      handleList = new ArrayList<ResizeHandle>();
      for (Dir handle : handles) {
        create(handle);
      }

      preview = new BaseEventPreview() {

        @Override
        public boolean onPreview(NativePreviewEvent event) {
          event.getNativeEvent().preventDefault();
          switch (event.getTypeInt()) {
            case Event.ONTOUCHMOVE:
            case Event.ONMOUSEMOVE:
              XEvent xEvent = event.getNativeEvent().cast();
              Point xy = xEvent.getXY();
              int x = xy.getX();
              int y = xy.getY();
              handleMouseMove(x, y);
              break;
            case Event.ONTOUCHEND:
            case Event.ONMOUSEUP:
              handleMouseUp(event.getNativeEvent().<Event> cast());
              break;
          }
          return true;
        }

      };
      preview.setAutoHide(false);
    }

    syncHandleHeight();
    setEnabled(enabled);
  }

  protected void onAttach() {
    if (handleList != null) {
      for (ResizeHandle handle : handleList) {
        ComponentHelper.doAttach(handle);
      }
    }
  }

  protected void onComponentResize() {
    syncHandleHeight();
  }

  protected void onDetach() {
    if (handleList != null) {
      for (ResizeHandle handle : handleList) {
        ComponentHelper.doDetach(handle);
      }
    }
  }

  protected void syncHandleHeight() {
  }

  SimpleEventBus ensureHandlers() {
    return eventBus == null ? eventBus = new SimpleEventBus() : eventBus;
  }

  private int constrain(int v, int diff, int m, int mx) {
    if (v - diff < m) {
      diff = v - m;
    } else if (v - diff > mx) {
      diff = (diff < 0 ? -1 : 1) * (mx - v);
    }
    return diff;
  }

  private ResizeHandle create(Dir dir) {
    ResizeHandle rh = new ResizeHandle();
    rh.setStyleName(appearance.getHandleStyles(dir));
    rh.dir = dir;
    resize.getElement().appendChild(rh.getElement());
    handleList.add(rh);
    return rh;
  }

  private void handleMouseDown(Event event, ResizeHandle handle) {
    ResizeStartEvent evt = new ResizeStartEvent(handle, event);
    ensureHandlers().fireEventFromSource(evt, this);

    if (!enabled || evt.isCancelled()) {
      return;
    }

    dir = handle.dir;

    startBox = resize.getElement().getBounds(false);
    startPoint = event.<XEvent>cast().getXY();

    resizing = true;

    if (dynamic) {
      if (proxyEl != null) {
        proxyEl.setVisible(false);
      }
    } else {
      if (proxyEl == null) {
        proxyEl = XElement.as(createProxy());
      }
      Element body = RootPanel.getBodyElement();
      DOM.appendChild(body, proxyEl);

      proxyEl.makePositionable(true);
      proxyEl.setLeft(startBox.getX());
      proxyEl.setTop(startBox.getY());
      proxyEl.setSize(startBox.getWidth(), startBox.getHeight(), true);
      proxyEl.setVisible(true);
      proxyEl.updateZIndex(5);
    }

    preview.add();

    Shim.get().cover(false);
    Shim.get().setStyleAttribute("cursor", handle.getElement().getStyle().getCursor());
  }

  private void handleMouseMove(int xin, int yin) {
    if (resizing) {
      int x = startBox.getX();
      int y = startBox.getY();
      float w = startBox.getWidth();
      float h = startBox.getHeight();
      float ow = w, oh = h;
      int mw = minWidth;
      int mh = minHeight;
      int mxw = maxWidth;
      int mxh = maxHeight;

      Point eventXY = new Point(xin, yin);

      int diffX = -(startPoint.getX() - Math.max(2, eventXY.getX()));
      int diffY = -(startPoint.getY() - Math.max(2, eventXY.getY()));

      switch (dir) {
        case E:
          w += diffX;
          w = Math.min(Math.max(mw, w), mxw);
          break;
        case S:
          h += diffY;
          h = Math.min(Math.max(mh, h), mxh);
          break;
        case SE:
          w += diffX;
          h += diffY;
          w = Math.min(Math.max(mw, w), mxw);
          h = Math.min(Math.max(mh, h), mxh);
          break;
        case N:
          diffY = constrain((int) h, diffY, mh, mxh);
          y += diffY;
          h -= diffY;
          break;
        case W:
          diffX = constrain((int) w, diffX, mw, mxw);
          x += diffX;
          w -= diffX;
          break;
        case NE:
          w += diffX;
          w = Math.min(Math.max(mw, w), mxw);
          diffY = constrain((int) h, diffY, mh, mxh);
          y += diffY;
          h -= diffY;
          break;
        case NW:
          diffX = constrain((int) w, diffX, mw, mxw);
          diffY = constrain((int) h, diffY, mh, mxh);
          y += diffY;
          h -= diffY;
          x += diffX;
          w -= diffX;
          break;
        case SW:
          diffX = constrain((int) w, diffX, mw, mxw);
          h += diffY;
          h = Math.min(Math.max(mh, h), mxh);
          x += diffX;
          w -= diffX;
          break;
      }
      if (preserveRatio) {
        switch (dir) {
          case SE:
          case E:
            h = oh * (w / ow);
            h = Math.min(Math.max(mh, h), mxh);
            w = ow * (h / oh);
            break;
          case S:
            w = ow * (h / oh);
            w = Math.min(Math.max(mw, w), mxw);
            h = oh * (w / ow);
            break;
          case NE:
            w = ow * (h / oh);
            w = Math.min(Math.max(mw, w), mxw);
            h = oh * (w / ow);
            break;
          case N: {
            float tw = w;
            w = ow * (h / oh);
            w = Math.min(Math.max(mw, w), mxw);
            h = oh * (w / ow);
            x += (tw - w) / 2;
          }
            break;
          case SW: {
            h = oh * (w / ow);
            h = Math.min(Math.max(mh, h), mxh);
            float tw = w;
            w = ow * (h / oh);
            x += tw - w;
            break;
          }
          case W: {
            float th = h;
            h = oh * (w / ow);
            h = Math.min(Math.max(mh, h), mxh);
            y += (th - h) / 2;
            float tw = w;
            w = ow * (h / oh);
            x += tw - w;
            break;
          }
          case NW: {
            float tw = w;
            float th = h;
            h = oh * (w / ow);
            h = Math.min(Math.max(mh, h), mxh);
            w = ow * (h / oh);
            y += th - h;
            x += tw - w;
            break;
          }
        }
      }

      if (dynamic) {
        resize.setPagePosition(x, y);
        resize.setPixelSize((int) w, (int) h);
      } else {
        proxyEl.setLeftTop(x, y);
        proxyEl.setSize((int) w, (int) h, true);
      }

    }
  }

  private void handleMouseUp(Event event) {
    resizing = false;
    preview.remove();
    Shim.get().uncover();

    if (!dynamic) {
      Rectangle rect = dynamic ? resize.getElement().getBounds() : proxyEl.getBounds();

      rect.setWidth(Math.min(rect.getWidth(), maxWidth));
      rect.setHeight(Math.min(rect.getHeight(), maxHeight));

      proxyEl.disableTextSelection(false);
      proxyEl.setVisible(false);
      proxyEl.removeFromParent();
      resize.setBounds(rect);
    }

    syncHandleHeight();

    ensureHandlers().fireEventFromSource(new ResizeEndEvent(resize, event), this);
  }

}
