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

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.HasHTML;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.CellGestureAdapter;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer.TapGestureEvent;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer.TapGestureEvent.HasTapGestureHandlers;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer.TapGestureEvent.TapGestureHandler;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.shared.ExpandedHtmlSanitizer;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.widget.core.client.HasIcon;
import com.sencha.gxt.widget.core.client.event.ArrowSelectEvent;
import com.sencha.gxt.widget.core.client.event.ArrowSelectEvent.ArrowSelectHandler;
import com.sencha.gxt.widget.core.client.event.ArrowSelectEvent.HasArrowSelectHandlers;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent.BeforeSelectHandler;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent.HasBeforeSelectHandlers;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;

public class ButtonCell<C> extends ResizeCell<C> implements HasBeforeSelectHandlers, HasSelectHandlers,
    HasArrowSelectHandlers, HasHTML, HasIcon, HasSafeHtml, FocusableCell, DisableCell, HasTapGestureHandlers {

  /**
   * Button arrow alignment enum.
   */
  public enum ButtonArrowAlign {
    /**
     * Arrow is aligned to the <b>right</b>
     */
    RIGHT,
    /**
     * Arrow is aligned to the <b>bottom</b>
     */
    BOTTOM
  }

  public interface ButtonCellAppearance<C> {

    XElement getButtonElement(XElement parent);

    XElement getFocusElement(XElement parent);

    void onFocus(XElement parent, boolean focused);

    void onOver(XElement parent, boolean over);

    void onPress(XElement parent, boolean pressed);

    void onToggle(XElement parent, boolean pressed);

    void render(ButtonCell<C> cell, Context context, C value, SafeHtmlBuilder sb);

  }

  /**
   * ButtonScale enum.
   */
  public enum ButtonScale {
    NONE, SMALL, MEDIUM, LARGE
  }

  /**
   * Icon alignment enum.
   */
  public enum IconAlign {
    /**
     * Icons are aligned to the <b>right</b>.
     */
    RIGHT,
    /**
     * Icons are aligned to the <b>bottom</b>.
     */
    BOTTOM,
    /**
     * Icons are aligned to the <b>top</b>.
     */
    TOP,
    /**
     * Icons are aligned to the <b>left</b>.
     */
    LEFT
  }

  private class UnpushHandler implements NativePreviewHandler {

    private final XElement parent;
    private final HandlerRegistration reg;

    public UnpushHandler(XElement parent) {
      this.parent = parent;
      this.reg = Event.addNativePreviewHandler(this);
    }

    public void onPreviewNativeEvent(NativePreviewEvent event) {
      if ("mouseup".equals(event.getNativeEvent().getType())) {
        // Unregister self.
        reg.removeHandler();

        // Unpush the element.
        appearance.onOver(parent, false);
        appearance.onPress(parent, false);
      }
    }
  }

  //TODO consider concrete subclasses of these cell adapters for each gesture class, possibly as an inner class?
  private CellGestureAdapter<TapGestureRecognizer, C> tapRecognizer = new TapGestureRecognizer.CellTapGestureRecognizer<C>() {
    @Override
    protected void onTap(TouchData tap, Context context, Element parent, C value, ValueUpdater<C> valueUpdater) {
      ButtonCell.this.onTap(tap, context, (XElement)parent.cast(), value, valueUpdater);
    }

    @Override
    protected void onTapStart(TouchData tap, Context context, Element parent, C value, ValueUpdater<C> valueUpdater) {
      ButtonCell.this.getFocusElement(parent.<XElement>cast()).focus();
    }
  };

  protected ImageResource icon;
  protected Menu menu;
  protected SafeHtml html = SafeHtmlUtils.EMPTY_SAFE_HTML;
  private final ButtonCellAppearance<C> appearance;

  private IconAlign iconAlign = IconAlign.LEFT;
  private ButtonArrowAlign arrowAlign = ButtonArrowAlign.RIGHT;
  private ButtonScale scale = ButtonScale.SMALL;
  private AnchorAlignment menuAlign = new AnchorAlignment(Anchor.TOP_LEFT, Anchor.BOTTOM_LEFT, true);
  private boolean handleMouseEvents = true;
  private int minWidth = -1;
  private XElement menuParent;
  private HideHandler hideHandler;
  private HandlerRegistration hideHandlerRegistration;

  public ButtonCell() {
    this(GWT.<ButtonCellAppearance<C>>create(ButtonCellAppearance.class));
  }

  public ButtonCell(ButtonCellAppearance<C> appearance) {
    super("click", "keydown", "mousedown", "mouseup", "mouseover", "mouseout", "focus", "blur");
    addCellGestureAdapter(tapRecognizer);
    this.appearance = appearance;
  }

  @Override
  public HandlerRegistration addArrowSelectHandler(ArrowSelectHandler handler) {
    return addHandler(handler, ArrowSelectEvent.getType());
  }

  @Override
  public HandlerRegistration addBeforeSelectHandler(BeforeSelectHandler handler) {
    return addHandler(handler, BeforeSelectEvent.getType());
  }

  @Override
  public HandlerRegistration addSelectHandler(SelectHandler handler) {
    return addHandler(handler, SelectEvent.getType());
  }

  @Override
  public HandlerRegistration addTapGestureHandler(TapGestureHandler handler) {
    return addHandler(handler, TapGestureEvent.getType());
  }

  @Override
  public void disable(com.google.gwt.cell.client.Cell.Context context, Element parent) {
    appearance.onOver(parent.<XElement>cast(), false);
    appearance.onFocus(parent.<XElement>cast(), false);
  }

  @Override
  public void enable(com.google.gwt.cell.client.Cell.Context context, Element parent) {
    appearance.onOver(parent.<XElement>cast(), false);
  }

  /**
   * Returns the item's text.
   *
   * If text was set that contained reserved html characters, the return value will be html escaped.
   * If html was set instead, the return value will be html.
   *
   * @return the text or html, depending on what was set
   * @see #getHTML()
   */
  @Override
  public String getText() {
    return getHTML();
  }

  /**
   * Sets the item's text.
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the text
   */
  @Override
  public void setText(String text) {
    setHTML(SafeHtmlUtils.fromString(text));
  }

  /**
   * Returns the item's html.
   *
   * @return the html
   */
  public SafeHtml getSafeHtml() {
    return html;
  }

  /**
   * Returns the item's html.
   *
   * @return the html
   */
  @Override
  public String getHTML() {
    return html.asString();
  }

  /**
   * Sets the item's html.
   *
   * @param html the html
   */
  @Override
  public void setHTML(SafeHtml html) {
    this.html = html;
  }

  /**
   * Sets the item's html.
   *
   * Untrusted html will be sanitized before use to protect against XSS.
   *
   * @param html the html
   */
  @Override
  public void setHTML(String html) {
    setHTML(ExpandedHtmlSanitizer.sanitizeHtml(html));
  }

  /**
   * Returns the button's appearance.
   *
   * @return the appearance
   */
  public ButtonCellAppearance<C> getAppearance() {
    return appearance;
  }

  /**
   * Returns the button's arrow alignment.
   *
   * @return the arrow alignment
   */
  public ButtonArrowAlign getArrowAlign() {
    return arrowAlign;
  }

  @Override
  public XElement getFocusElement(XElement parent) {
    return appearance.getFocusElement(parent);
  }

  @Override
  public ImageResource getIcon() {
    return icon;
  }

  /**
   * Returns the button's icon alignment.
   *
   * @return the icon alignment
   */
  public IconAlign getIconAlign() {
    return iconAlign;
  }

  /**
   * Returns the button's menu (if it has one).
   *
   * @return the menu
   */
  public Menu getMenu() {
    return menu;
  }

  /**
   * Returns the button's menu alignment.
   *
   * @return the menu alignment
   */
  public AnchorAlignment getMenuAlign() {
    return menuAlign;
  }

  /**
   * Returns the button's minimum width.
   *
   * @return the minWidth the minimum width
   */
  public int getMinWidth() {
    return minWidth;
  }

  /**
   * Returns false if mouse over effect is disabled.
   *
   * @return false if mouse effects disabled
   */
  public boolean getMouseEvents() {
    return handleMouseEvents;
  }

  /**
   * Returns the button's scale.
   *
   * @return the button scale
   */
  public ButtonScale getScale() {
    return scale;
  }

  /**
   * Hide this button's menu (if it has one).
   */
  public void hideMenu() {
    if (menu != null) {
      menu.hide();
    }
    if (menuParent != null) {
      menuParent = null;
    }
  }

  @Override
  public void onBrowserEvent(Cell.Context context, Element parent, C value, NativeEvent event,
                             ValueUpdater<C> valueUpdater) {
    // Ensure that any GestureRecognizers get invoked
    super.onBrowserEvent(context, parent, value, event, valueUpdater);
    Element target = event.getEventTarget().cast();
    // ignore the parent element
    if (isDisableEvents() || !parent.getFirstChildElement().isOrHasChild(target)) {
      return;
    }

    XElement p = parent.cast();

    String eventType = event.getType();
    if ("click".equals(eventType)) {
      onClick(context, p, value, event, valueUpdater);
    } else if ("mouseover".equals(eventType)) {
      onMouseOver(p, event);
    } else if ("mouseout".equals(eventType)) {
      onMouseOut(p, event);
    } else if ("mousedown".equals(eventType)) {
      onMouseDown(p, event);
    } else if ("mouseup".equals(eventType)) {
      onMouseUp(p, event);
    } else if ("focus".equals(eventType)) {
      onFocus(p, event);
    } else if ("blur".equals(eventType)) {
      onBlur(p, event);
    } else if ("keydown".equals(eventType)) {
      if (KeyNav.getKeyEvent() == Event.ONKEYDOWN) {
        onNavigationKey(context, parent, value, event, valueUpdater);
      }
    } else if ("keypress".equals(eventType)) {
      if (KeyNav.getKeyEvent() == Event.ONKEYPRESS) {
        onNavigationKey(context, parent, value, event, valueUpdater);
      }
    }
  }

  @Override
  public boolean redrawOnResize() {
    return true;
  }

  @Override
  public void render(Context context, C value, SafeHtmlBuilder sb) {
    appearance.render(this, context, value, sb);
  }

  /**
   * Sets the arrow alignment (defaults to RIGHT).
   *
   * @param arrowAlign the arrow alignment
   */
  public void setArrowAlign(ButtonArrowAlign arrowAlign) {
    this.arrowAlign = arrowAlign;
  }

  @Override
  public void setIcon(ImageResource icon) {
    this.icon = icon;
  }

  /**
   * Sets the icon alignment (defaults to LEFT).
   *
   * @param iconAlign the icon alignment
   */
  public void setIconAlign(IconAlign iconAlign) {
    this.iconAlign = iconAlign;
  }

  /**
   * Sets the button's menu.
   *
   * @param menu the menu
   */
  public void setMenu(Menu menu) {
    if (this.menu != null && hideHandlerRegistration != null) {
      hideHandlerRegistration.removeHandler();
    }
    this.menu = menu;

    if (menu != null) {
      if (hideHandler == null) {
        hideHandler = new HideHandler() {

          @Override
          public void onHide(HideEvent event) {
            hideMenu();
          }
        };
      }

      hideHandlerRegistration = this.menu.addHideHandler(hideHandler);
    }
  }

  /**
   * Sets the position to align the menu to, see {@link XElement#alignTo} for
   * more details (defaults to 'tl-bl?', pre-render).
   *
   * @param menuAlign the menu alignment
   */
  public void setMenuAlign(AnchorAlignment menuAlign) {
    this.menuAlign = menuAlign;
  }

  /**
   * Sets he minimum width for this button (used to give a set of buttons a
   * common width)
   *
   * @param minWidth the minimum width
   */
  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }

  /**
   * False to disable visual cues on mouseover, mouseout and mousedown (defaults
   * to true).
   *
   * @param handleMouseEvents false to disable mouse over changes
   */
  public void setMouseEvents(boolean handleMouseEvents) {
    this.handleMouseEvents = handleMouseEvents;
  }

  /**
   * Sets the button's scale.
   *
   * @param scale the button scale
   */
  public void setScale(ButtonScale scale) {
    this.scale = scale;
  }

  /**
   * Show this button's menu (if it has one).
   *
   * @param target the element to align to
   */
  public void showMenu(Element target) {
    menu.setOnHideFocusElement(getFocusElement(XElement.as(target)));
    menu.show(target, menuAlign);
  }

  protected void onBlur(XElement p, NativeEvent event) {
    appearance.onFocus(p, false);
  }

  protected void onClick(Context context, XElement p, C value, NativeEvent event, ValueUpdater<C> valueUpdater) {
    if (!isDisableEvents() && fireCancellableEvent(context, new BeforeSelectEvent(context))) {
      if (menu != null) {
        menuParent = p;
        showMenu(p);
      }
      appearance.onOver(p, false);
      fireEvent(context, new SelectEvent(context));
    }
  }

  protected void onFocus(XElement p, NativeEvent event) {
    appearance.onFocus(p, true);
  }

  protected void onMouseDown(XElement parent, NativeEvent event) {
    if (handleMouseEvents) {
      Element target = event.getEventTarget().cast();
      // stop images from being dragged in firefox
      if ("IMG".equals(target.getTagName())) {
        event.preventDefault();
        // we need to explicitly focus, since we are preventing the event
        getFocusElement(parent).focus();
      }
      appearance.onPress(parent, true);

      new UnpushHandler(parent);
    }
  }

  protected void onMouseOut(XElement p, NativeEvent event) {
    appearance.onOver(p, false);
  }

  protected void onMouseOver(XElement p, NativeEvent event) {
    appearance.onOver(p, true);
  }

  protected void onMouseUp(XElement p, NativeEvent event) {
    appearance.onPress(p, false);
  }

  protected void onNavigationKey(com.google.gwt.cell.client.Cell.Context context, Element parent, C value,
                                 NativeEvent event, ValueUpdater<C> valueUpdater) {
    int key = event.getKeyCode();

    if (key == KeyCodes.KEY_DOWN && menu != null) {
      onClick(context, parent.<XElement>cast(), value, event, valueUpdater);
    }

    if (key == KeyCodes.KEY_ENTER || key == 32) {
      onClick(context, parent.<XElement>cast(), value, event, valueUpdater);
    }
  }

  protected void onTap(TouchData touch, Context context, XElement parent, C value, ValueUpdater<C> valueUpdater) {
    Element target = touch.getLastNativeEvent().getEventTarget().cast();
    if (parent.getFirstChildElement().isOrHasChild(target)) {
      onClick(context, parent, value, touch.getLastNativeEvent(), valueUpdater);
    }
  }
}
