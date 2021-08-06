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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.CompositeElement;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent;
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent.AfterAnimateHandler;
import com.sencha.gxt.fx.client.animation.FadeIn;
import com.sencha.gxt.fx.client.animation.FadeOut;
import com.sencha.gxt.fx.client.animation.Fx;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;

/**
 * A panel that can be displayed over other widgets.
 */
public class Popup extends SimpleContainer {

  private int yOffset = 15;
  private int xOffset = 10;
  private boolean animate;
  private boolean autoFocus = true;
  private boolean autoHide = true;
  private boolean constrainViewport = true;
  private AnchorAlignment defaultAlign = new AnchorAlignment(Anchor.TOP_LEFT, Anchor.BOTTOM_LEFT, true);

  private Element alignElem;
  private AnchorAlignment alignPos;
  private int alignOffsetX = 0, alignOffsetY = 2;
  private Point alignPoint;
  private BaseEventPreview preview = new BaseEventPreview() {
    @Override
    protected boolean onAutoHide(NativePreviewEvent ce) {
      if (Popup.this.onAutoHide(ce.getNativeEvent().<Event> cast())) {
        hide();
        return true;
      }
      return false;
    }
  };

  /**
   * Creates a new popup panel.
   */
  public Popup() {
    shim = true;
    getElement().getStyle().setOverflow(Style.Overflow.VISIBLE);
  }

  /**
   * Centers the panel within the viewport.
   */
  public void center() {
    getElement().center();
  }

  /**
   * Returns the default alignment.
   * 
   * @return the default align
   */
  public AnchorAlignment getDefaultAlign() {
    return defaultAlign;
  }

  /**
   * Any elements added to this list will be ignored when auto close is enabled.
   * 
   * @return the list of ignored elements
   */
  public CompositeElement getIgnoreList() {
    return preview.getIgnoreList();
  }

  /**
   * Returns the x offset.
   * 
   * @return the offset
   */
  public int getXOffset() {
    return xOffset;
  }

  /**
   * Returns the y offsets.
   * 
   * @return the offset
   */
  public int getYOffset() {
    return yOffset;
  }

  /**
   * Hides the popup.
   */
  public void hide() {
    if (!fireCancellableEvent(new BeforeHideEvent())) {
      return;
    }

    if (autoHide) {
      preview.remove();
    }
    if (isAnimate()) {
      Fx fx = new Fx();
      fx.addAfterAnimateHandler(new AfterAnimateHandler() {
        @Override
        public void onAfterAnimate(AfterAnimateEvent event) {
          afterHide();
        }
      });

      fx.run(new FadeOut(getElement()));
    } else {
      afterHide();
    }
  }

  /**
   * Returns true if animations are enabled.
   * 
   * @return the animation state
   */
  public boolean isAnimate() {
    return animate;
  }

  /**
   * Returns true if auto focus is enabled.
   * 
   * @return the auto focus state
   */
  public boolean isAutoFocus() {
    return autoFocus;
  }

  /**
   * Returns true if auto hide is enabled.
   * 
   * @return the auto hide state
   */
  public boolean isAutoHide() {
    return autoHide;
  }

  /**
   * Returns true if constrain to viewport is enabled.
   * 
   * @return the constrain viewport state
   */
  public boolean isConstrainViewport() {
    return constrainViewport;
  }

  /**
   * True to enable animations when showing and hiding (defaults to false).
   * 
   * @param animate true to enable animations
   */
  public void setAnimate(boolean animate) {
    this.animate = animate;
  }

  /**
   * True to move focus to the popup when being opened (defaults to true).
   * 
   * @param autoFocus true for auto focus
   */
  public void setAutoFocus(boolean autoFocus) {
    this.autoFocus = autoFocus;
  }

  /**
   * True to close the popup when the user clicks outside of the menu (default to true).
   * 
   * @param autoHide true for auto hide
   */
  public void setAutoHide(boolean autoHide) {
    this.autoHide = autoHide;
  }

  /**
   * True to ensure popup is displayed within the browser's viewport.
   * 
   * @param constrainViewport true to constrain
   */
  public void setConstrainViewport(boolean constrainViewport) {
    this.constrainViewport = constrainViewport;
  }

  /**
   * The default {@link XElement#alignTo} anchor position value for this menu relative to its element of origin
   * (defaults to "tl-bl?").
   * 
   * @param defaultAlign the default alignment
   */
  public void setDefaultAlign(AnchorAlignment defaultAlign) {
    this.defaultAlign = defaultAlign;
  }

  /**
   * Sets the xOffset when constrainViewport == true (defaults to 10).
   * 
   * @param xOffset the x offset
   */
  public void setXOffset(int xOffset) {
    this.xOffset = xOffset;
  }

  /**
   * Sets the yOffset when constrainViewport == true (defaults to 15).
   * 
   * @param yOffset the offset
   */
  public void setYOffset(int yOffset) {
    this.yOffset = yOffset;
  }

  /**
   * Displays the popup.
   */
  public void show() {
    if (!fireCancellableEvent(new BeforeShowEvent())) {
      return;
    }
    Point p = new Point((int) Window.getClientWidth() / 2, (int) Window.getClientHeight() / 2);
    showAt(p.getX(), p.getY());
  }

  /**
   * Displays the popup aligned to the bottom left of the widget. For exact control of popup position see
   * {@link #show(com.google.gwt.dom.client.Element, com.sencha.gxt.core.client.Style.AnchorAlignment, int, int)}.
   * 
   * @param widget the widget to use for alignment
   */
  public void show(Component widget) {
    if (!fireCancellableEvent(new BeforeShowEvent())) {
      return;
    }
    alignElem = widget.getElement();
    onShowPopup();
  }

  /**
   * Displays the popup.
   * 
   * @param elem the element to align to
   * @param pos the position
   */
  public void show(Element elem, AnchorAlignment pos) {
    if (!fireCancellableEvent(new BeforeShowEvent())) {
      return;
    }
    alignElem = elem;
    alignPos = pos;
    onShowPopup();
  }

  /**
   * Displays the popup.
   * 
   * @param elem the element to align to
   * @param pos the position
   * @param offsetX X offset
   * @param offsetY Y offset
   */
  public void show(Element elem, AnchorAlignment pos, int offsetX, int offsetY) {
    if (!fireCancellableEvent(new BeforeShowEvent())) {
      return;
    }
    alignElem = elem;
    alignPos = pos;
    alignOffsetX = offsetX;
    alignOffsetY = offsetY;
    onShowPopup();
  }

  /**
   * Shows the popup at the specified location.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void showAt(int x, int y) {
    if (!fireCancellableEvent(new BeforeShowEvent())) {
      return;
    }
    alignPoint = new Point(x, y);
    onShowPopup();
  }

  protected void afterHide() {
    RootPanel.get().remove(this);
    hidden = true;
    hideShadow();
    fireEvent(new HideEvent());
  }

  protected void afterShow() {
    if (layer != null) {
      layer.sync(true);
    }
    if (isAutoFocus()) {
      focus();
    }

    getElement().setZIndex(XDOM.getTopZIndex());
  }

  protected void onAfterFirstAttach() {
    getElement().makePositionable(true);

    preview.getIgnoreList().add(getElement());
  }

  /**
   * Subclasses may override to cancel the hide from an auto hide.
   * 
   * @param event the current event
   * @return true to close, false to cancel
   */
  protected boolean onAutoHide(Event event) {
    return true;
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    if (preview != null) {
      preview.remove();
    }
  }

  protected Popup onShowPopup() {
    RootPanel.get().add(this);

    hidden = false;
    Point p = null;

    if (alignElem != null) {
      alignPos = alignPos != null ? alignPos : getDefaultAlign();
      p = getElement().getAlignToXY(alignElem, alignPos, alignOffsetX, alignOffsetY);
    } else if (alignPoint != null) {
      p = alignPoint;
    }
    getElement().makePositionable(true);
    getElement().setVisibility(false);

    getElement().setLeftTop(p.getX(), p.getY());

    alignElem = null;
    alignPos = null;
    alignPoint = null;

    if (constrainViewport) {
      int clientHeight = Window.getClientHeight() + XDOM.getBodyScrollTop();
      int clientWidth = Window.getClientWidth() + XDOM.getBodyScrollLeft();

      Rectangle r = getElement().getBounds();

      int x = r.getX();
      int y = r.getY();

      if (y + r.getHeight() > clientHeight) {
        y = clientHeight - r.getHeight() - getYOffset();
        getElement().setTop(y);
      }
      if (x + r.getWidth() > clientWidth) {
        x = clientWidth - r.getWidth() - getXOffset();
        getElement().setLeft(x);
      }
    }

    getElement().setVisibility(true);

    if (autoHide) {
      preview.add();
    }

    if (animate) {
      Fx fx = new Fx();
      fx.addAfterAnimateHandler(new AfterAnimateHandler() {

        @Override
        public void onAfterAnimate(AfterAnimateEvent event) {
          afterShow();
        }
      });

      fx.run(new FadeIn(getElement()));
    } else {
      afterShow();
    }

    return this;
  }

}
