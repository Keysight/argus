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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent;
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent.AfterAnimateHandler;
import com.sencha.gxt.fx.client.animation.Fx;
import com.sencha.gxt.fx.client.animation.SlideIn;
import com.sencha.gxt.fx.client.animation.SlideOut;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.Header.HeaderAppearance;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.BeforeCollapseEvent;
import com.sencha.gxt.widget.core.client.event.BeforeCollapseEvent.BeforeCollapseHandler;
import com.sencha.gxt.widget.core.client.event.BeforeCollapseEvent.HasBeforeCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent.BeforeExpandHandler;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent.HasBeforeExpandHandlers;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.HasCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.HasExpandHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * ContentPanel is a component container that has specific functionality and structural components that make it the
 * perfect building block for application-oriented user interfaces. A content panel contains separate header, footer and
 * body sections. The header may contain an icon, text and a tool area that can be wired up to provide customized
 * behavior. The footer contains buttons added using {@link #addButton(Widget)}. The body contains a single widget,
 * added using {@link #add}. The widget is resized to match the size of the container. A content panel provides built-in
 * expandable and collapsible behavior.
 *
 * Code snippet:
 *
 * <pre>
 * public void onModuleLoad() {
 *   ContentPanel cp = new ContentPanel();
 *   cp.setHeading("Content Panel");
 *   cp.setPixelSize(250, 140);
 *   cp.setPosition(10, 10);
 *   cp.setCollapsible(true);
 *   cp.addTool(new ToolButton(ToolButton.GEAR));
 *   cp.addTool(new ToolButton(ToolButton.CLOSE));
 *   cp.setWidget(new HTML("This is an HTML Widget in a ContentPanel."));
 *   cp.addButton(new TextButton("Ok"));
 *   RootPanel.get().add(cp);
 * }
 * </pre>
 */
public class ContentPanel extends SimpleContainer implements HasBeforeExpandHandlers, HasExpandHandlers,
    HasBeforeCollapseHandlers, HasCollapseHandlers, Collapsible {

  private final ContentPanelAppearance appearance;
  protected Header header;
  protected ButtonBar buttonBar;
  protected boolean secondPassRequired;
  private ContentPanelMessages messages;
  private boolean animating;
  private boolean animCollapse = true;
  private int animationDuration = 500;
  private ToolButton collapseBtn;
  private boolean collapsed, hideCollapseTool;
  private boolean collapsible;
  private boolean titleCollapse;
  private boolean headerVisible = true;
  private boolean layoutOnExpand;
  /**
   * Creates a content panel with default appearance.
   */
  public ContentPanel() {
    this((ContentPanelAppearance) GWT.create(ContentPanelAppearance.class));
  }
  /**
   * Creates a content panel with the specified appearance.
   *
   * @param appearance the appearance of the content panel.
   */
  public ContentPanel(ContentPanelAppearance appearance) {
    super(true);
    this.appearance = appearance;

    setDeferHeight(true);

    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    appearance.render(sb);

    setElement((Element) XDOM.create(sb.toSafeHtml()));

    header = new Header(appearance.getHeaderAppearance());
    ComponentHelper.setParent(this, header);

    XElement headerElem = appearance.getHeaderElem(getElement());
    headerElem.appendChild(header.getElement());

    buttonBar = new ButtonBar();
    buttonBar.setMinButtonWidth(75);
    buttonBar.setPack(BoxLayoutPack.END);
    buttonBar.setVisible(false);
    buttonBar.getElement().getStyle().setProperty("minHeight", "5px");
    appearance.getFooterElem(getElement()).appendChild(buttonBar.getElement());
  }

  @Override
  public HandlerRegistration addBeforeCollapseHandler(BeforeCollapseHandler handler) {
    return addHandler(handler, BeforeCollapseEvent.getType());
  }

  @Override
  public HandlerRegistration addBeforeExpandHandler(BeforeExpandHandler handler) {
    return addHandler(handler, BeforeExpandEvent.getType());
  }

  /**
   * Adds a widget the the button bar.
   *
   * @param widget the widget to add
   */
  @UiChild
  public void addButton(Widget widget) {
    buttonBar.add(widget);
    if (isOrWasAttached() && !buttonBar.isVisible()) {
      buttonBar.setVisible(true);
      if (isAttached()) {
        forceLayout();
      }
    }
  }

  @Override
  public HandlerRegistration addCollapseHandler(CollapseHandler handler) {
    return addHandler(handler, CollapseEvent.getType());
  }

  @Override
  public HandlerRegistration addExpandHandler(ExpandHandler handler) {
    return addHandler(handler, ExpandEvent.getType());
  }

  /**
   * Adds a Tool to Header
   *
   * @param tool the tool to add
   */
  @UiChild
  public void addTool(Widget tool) {
    header.addTool(tool);
  }

  @Override
  public void collapse() {
    if (!collapsed && !animating && fireCancellableEvent(new BeforeCollapseEvent())) {
      hideShadow();
      onCollapse();
    }
  }

  @Override
  public void expand() {
    if (collapsed && !animating && fireCancellableEvent(new BeforeExpandEvent())) {
      hideShadow();
      onExpand();
    }
  }

  @Override
  public void forceLayout() {
    if (collapsed) {
      doLayout();
    } else {
      super.forceLayout();
    }
  }

  /**
   * Gets the duration for the expand/collapse animations
   *
   * @return the duration for the expand/collapse animations in milliseconds.
   */
  public int getAnimationDuration() {
    return animationDuration;
  }

  /**
   * Sets the duration for the expand/collapse animations.
   *
   * @param animationDuration the duration of the expand/collapse animations in milliseconds
   */
  public void setAnimationDuration(int animationDuration) {
    this.animationDuration = animationDuration;
  }

  /**
   * Gets a reference to the appearance this object was instantiated with
   *
   * @return the appearance impl used by this component
   */
  public ContentPanelAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the panel's body element.
   *
   * @return the body
   */
  public XElement getBody() {
    return getAppearance().getContentElem(getElement());
  }

  /**
   * Returns the panel's button alignment.
   *
   * @return the button alignment
   */
  public BoxLayoutPack getButtonAlign() {
    return buttonBar.getPack();
  }

  /**
   * Sets the button alignment of any buttons added to this panel (defaults to RIGHT, pre-render).
   *
   * @param buttonAlign the button alignment
   */
  public void setButtonAlign(BoxLayoutPack buttonAlign) {
    assertPreRender();
    buttonBar.setPack(buttonAlign);
  }

  /**
   * Returns the content panel button bar. In the default implementation, the button bar is displayed in the content
   * panel's footer.
   *
   * @return the button bar
   */
  public ButtonBar getButtonBar() {
    return buttonBar;
  }

  /**
   * Returns the content panel messages.
   *
   * @return the content panel messages
   */
  public ContentPanelMessages getMessages() {
    if (messages == null) {
      messages = new DefaultContentPanelMessages();
    }
    return messages;
  }

  /**
   * Sets the content panel messages.
   *
   * @param messages the messages
   */
  public void setMessages(ContentPanelMessages messages) {
    this.messages = messages;
  }

  /**
   * Returns the minimum button width.
   *
   * @return the minimum button width
   */
  public int getMinButtonWidth() {
    return buttonBar.getMinButtonWidth();
  }

  /**
   * Sets the minimum button width.
   *
   * @param width the button width
   */
  public void setMinButtonWidth(int width) {
    buttonBar.setMinButtonWidth(width);
  }

  /**
   * Returns true if animated collapsing is enabled.
   *
   * @return true if animating
   */
  public boolean isAnimCollapse() {
    return animCollapse;
  }

  /**
   * Sets whether expand and collapse is animating (defaults to true).
   *
   * @param animCollapse true to enable animations
   */
  public void setAnimCollapse(boolean animCollapse) {
    this.animCollapse = animCollapse;
  }

  /**
   * Returns true if the panel is collapsed.
   *
   * @return the collapsed state
   */
  public boolean isCollapsed() {
    return collapsed;
  }

  /**
   * Returns true if the panel is collapsible.
   *
   * @return the collapsible state
   */
  public boolean isCollapsible() {
    return collapsible;
  }

  /**
   * True to make the panel collapsible and have the expand/collapse toggle button automatically rendered into the
   * header tool button area (defaults to false, pre-render).
   *
   * @param collapsible the collapsible state
   */
  public void setCollapsible(boolean collapsible) {
    assertPreRender();
    this.collapsible = collapsible;
  }

  @Override
  public boolean isExpanded() {
    return !isCollapsed();
  }

  /**
   * Sets the panel's expand state.
   *
   * @param expanded <code>true<code> true to expand
   */
  public void setExpanded(boolean expanded) {
    if (expanded) {
      expand();
    } else {
      collapse();
    }
  }

  /**
   * Returns true if the collapse tool is hidden.
   *
   * @return the hide collapse tool state
   */
  public boolean isHideCollapseTool() {
    return hideCollapseTool;
  }

  /**
   * Sets whether the collapse tool should be displayed (when {@link #setCollapsible(boolean)} = true) (defaults to false, pre-render).
   *
   * @param hideCollapseTool true if the tool is hidden
   */
  public void setHideCollapseTool(boolean hideCollapseTool) {
    assertPreRender();
    this.hideCollapseTool = hideCollapseTool;
  }

  /**
   * Returns true if title collapsing has been enabled.
   *
   * @return true for title collapse
   */
  public boolean isTitleCollapse() {
    return titleCollapse;
  }

  /**
   * True to allow expanding and collapsing the panel (when {@link #setCollapsible(boolean)} = true) by clicking
   * anywhere in the header bar, false to allow it only by clicking to tool button (defaults to false).
   *
   * @param titleCollapse the titleCollapse to set
   */
  public void setTitleCollapse(boolean titleCollapse) {
    this.titleCollapse = titleCollapse;
    if (titleCollapse) {
      header.getElement().getStyle().setCursor(Cursor.POINTER);
      sinkEvents(Event.ONCLICK);
    } else {
      header.getElement().getStyle().setCursor(Cursor.DEFAULT);
    }
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    if (event.getTypeInt() == Event.ONCLICK && event.getEventTarget() != null) {
      onClick(event);
    }
  }

  /**
   * Displays or hides the body border.
   *
   * @param border true to display the border
   */
  public void setBodyBorder(boolean border) {
    getAppearance().onBodyBorder(getElement(), border);
  }

  /**
   * Sets multiple style properties on the body element. Style attribute names must be in lower camel case, e.g.
   * "backgroundColor:white; color:red;"
   *
   * @param style the style(s) to set
   */
  public void setBodyStyle(String style) {
    getAppearance().getContentElem(getElement()).applyStyles(style);
  }

  /**
   * Adds a style class name to the body element.
   *
   * @param style the style class name
   */
  public void setBodyStyleName(String style) {
    getAppearance().getContentElem(getElement()).addClassName(style);
  }

  /**
   * Returns the content panel header.
   *
   * @return the header
   */
  public Header getHeader() {
    return header;
  }

  /**
   * Shows or hides the content panel header.
   *
   * @param visible true to show the header.
   */
  public void setHeaderVisible(boolean visible) {
    this.headerVisible = visible;
    getAppearance().onHideHeader(getElement(), !visible);
  }

  /**
   * Returns the heading html.
   *
   * @return the heading html
   */
  public SafeHtml getHeading() {
    return header.getSafeHtml();
  }

  /**
   * Sets the heading html.
   *
   * @param html the heading html
   */
  public void setHeading(SafeHtml html) {
    header.setHTML(html);
  }

  /**
   * Sets the heading text.
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the text
   */
  public void setHeading(String text) {
    header.setHTML(SafeHtmlUtils.fromString(text));
  }

  protected Size adjustBodySize() {
    return new Size(0, 0);
  }

  protected void afterCollapse() {
    collapsed = true;
    animating = false;

    getAppearance().getBodyWrap(getElement()).hide();
    for (int i = 0; i < getWidgetCount(); i++) {
      Widget w = getWidget(i);
      if (w.isVisible() && w instanceof Component) {
        ((Component) w).notifyHide();
      }
    }

    if (buttonBar != null && buttonBar.isAttached()) {
      buttonBar.notifyHide();
    }

    sync(true);

    // Re-enable the toggle tool after an animated collapse
    if (animCollapse && collapseBtn != null) {
      collapseBtn.enable();
    }

    if (collapseBtn != null) {
      collapseBtn.changeStyle(getAppearance().expandIcon());
    }

    fireEvent(new CollapseEvent());
  }

  protected void afterExpand() {
    collapsed = false;
    animating = false;

    for (int i = 0; i < getWidgetCount(); i++) {
      Widget w = getWidget(i);
      if (w.isVisible() && w instanceof Component) {
        ((Component) w).notifyShow();
      }
    }

    if (buttonBar != null && buttonBar.isAttached()) {
      buttonBar.notifyShow();
    }

    sync(true);

    // Re-enable the toggle tool after an animated collapse
    if (animCollapse && collapseBtn != null) {
      collapseBtn.enable();
    }

    if (collapseBtn != null && !hideCollapseTool) {
      collapseBtn.changeStyle(getAppearance().collapseIcon());
    }

    if (layoutOnExpand) {
      layoutOnExpand = false;
      super.forceLayout();
    }

    fireEvent(new ExpandEvent());
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(header);
    ComponentHelper.doAttach(buttonBar);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(header);
    ComponentHelper.doDetach(buttonBar);
  }

  @Override
  protected void doLayout() {
    if (collapsed && widget != null && resize) {
      layoutOnExpand = true;
      return;
    }

    // EXTGWT-3414 - When the button is autosized, a width is not given to it, this will give it a width.
    doLayoutButtonBar();

    super.doLayout();
  }

  protected void doLayoutButtonBar() {
    if (buttonBar != null) {
      int offsetWidth = getContainerTarget().getOffsetWidth();
      buttonBar.setWidth(offsetWidth);
    }
  }

  @Override
  protected XElement getContainerTarget() {
    return getAppearance().getContentElem(getElement());
  }

  protected Size getFrameSize() {
    return new Size(getAppearance().getFrameWidth(getElement()), getAppearance().getFrameHeight(getElement()));
  }

  protected void initTools() {
    if (collapsible && !hideCollapseTool) {
      collapseBtn = new ToolButton(collapsed ? getAppearance().expandIcon() : getAppearance().collapseIcon());
      collapseBtn.addSelectHandler(new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          setExpanded(!isExpanded());
        }
      });
      header.addTool(collapseBtn);
    }
  }

  protected boolean layoutBars() {
    if (buttonBar != null && buttonBar.getWidgetCount() > 0) {

      boolean hlr = hadLayoutRunning;
      if (!hadLayoutRunning) {

        hadLayoutRunning = true;
      }

      // first call to layoutBars will happen before the panel has been sized
      // button bar width = 0
      boolean overflow = buttonBar.isEnableOverflow();
      buttonBar.setEnableOverflow(false);
      buttonBar.forceLayout();
      buttonBar.setEnableOverflow(overflow);
      hadLayoutRunning = hlr;
      sync(true);
      return true;
    }
    return false;
  }

  @Override
  protected void onAfterFirstAttach() {
    super.onAfterFirstAttach();
    if (buttonBar.getWidgetCount() > 0) {
      buttonBar.setVisible(true);
    }

    initTools();
    layoutBars();

    if (!headerVisible) {
      header.setVisible(false);
    }
  }

  protected void onClick(Event ce) {
    if (collapsible && titleCollapse && header.getElement().isOrHasChild(ce.getEventTarget().<Element> cast())) {
      setExpanded(!isExpanded());
    }
  }

  protected void onCollapse() {
    if (isAttached() && animCollapse) {
      animating = true;
      // Disable layout adjustment during animation

      // Disable toggle tool during animated collapse
      if (collapseBtn != null) {
        collapseBtn.disable();
      }

      Fx fx = new Fx(getAnimationDuration());
      fx.addAfterAnimateHandler(new AfterAnimateHandler() {
        @Override
        public void onAfterAnimate(AfterAnimateEvent event) {
          afterCollapse();
        }
      });
      fx.run(new SlideOut(getAppearance().getBodyWrap(getElement()), Direction.UP));

      addStyleDependentName("animated");
    } else {
      getAppearance().getBodyWrap(getElement()).hide();
      afterCollapse();
    }
  }

  @Override
  protected void onDisable() {
    mask();
    super.onDisable();
  }

  @Override
  protected void onEnable() {
    unmask();
    super.onEnable();
  }

  protected void onExpand() {
    if (isAttached() && animCollapse) {
      animating = true;
      // Show the body before animating
      getAppearance().getBodyWrap(getElement()).show();

      addStyleDependentName("animated");

      Fx fx = new Fx(getAnimationDuration());
      fx.addAfterAnimateHandler(new AfterAnimateHandler() {
        @Override
        public void onAfterAnimate(AfterAnimateEvent event) {
          afterExpand();
        }
      });
      fx.run(new SlideIn(getAppearance().getBodyWrap(getElement()), Direction.DOWN));

      // Disable toggle tool during animated expand
      if (collapseBtn != null) {
        collapseBtn.disable();
      }
    } else {
      getAppearance().getBodyWrap(getElement()).show();
      afterExpand();
    }
  }

  @Override
  protected void onResize(int width, int height) {
    Size frameSize = getFrameSize();
    Size adjustBodySize = adjustBodySize();

    if (isAutoWidth()) {
      getContainerTarget().getStyle().clearWidth();
    } else {
      width -= frameSize.getWidth();

      if (header != null) {
        int aw = width - appearance.getHeaderElem(getElement()).getFrameWidth(Side.LEFT, Side.RIGHT);
        header.setWidth(aw);
      }
      getContainerTarget().setWidth(width - adjustBodySize.getWidth(), true);
    }

    layoutBars();

    // EXTGWT-2773 - Button bar inner frame height
    if (buttonBar != null) {
      appearance.getFooterElem(getElement()).setHeight(buttonBar.getOffsetHeight());
    }

    if (isAutoHeight()) {
      getContainerTarget().getStyle().clearHeight();
    } else {
      height -= frameSize.getHeight();
      height -= headerVisible ? getAppearance().getHeaderSize(getElement()).getHeight() : 0;
      height -= getAppearance().getFooterElem(getElement()).getHeight(false);

      getContainerTarget().setHeight(height - adjustBodySize.getHeight(), true);
    }

    super.onResize(width, height);
  }

  /**
   * The appearance of a content panel. A content panel has a header, body and footer. The header includes a button that
   * can be used to collapse or expand the body. The button has an icon that changes to indicate whether a collapse or
   * expand is possible. The body contains a single widget, added using {@link ContentPanel#add}. The widget is resized
   * to match the size of the container. The footer contains a button bar with optional buttons.
   */
  public interface ContentPanelAppearance {

    /**
     * Returns the button icon that indicates a collapse is possible.
     *
     * @return the collapse icon
     */
    IconConfig collapseIcon();

    /**
     * Returns the button icon that indicates an expand is possible.
     *
     * @return the expand icon
     */
    IconConfig expandIcon();

    /**
     * Returns the element that wraps the content panel body. In the default implementation, this wraps the body widget
     * and footer.
     *
     * @param parent the content panel root element
     * @return the element that wraps the body
     */
    XElement getBodyWrap(XElement parent);

    /**
     * Returns the content panel body element.
     *
     * @param parent the content panel root element
     * @return the body element
     */
    XElement getContentElem(XElement parent);

    /**
     * Returns the content panel footer element.
     *
     * @param parent the content panel root element
     * @return the body element
     */
    XElement getFooterElem(XElement parent);

    /**
     * Returns the total height of the content panel frame elements.
     *
     * @param parent the content panel root element
     * @return the total height of the frame elements
     */
    int getFrameHeight(XElement parent);

    /**
     * Returns the total width of the content panel frame elements.
     *
     * @param parent the content panel root element
     * @return the total width of the frame elements
     */
    int getFrameWidth(XElement parent);

    /**
     * Returns the content panel header's appearance
     *
     * @return the header appearance
     */
    HeaderAppearance getHeaderAppearance();

    /**
     * Returns the content panel header element.
     *
     * @param parent the content panel root element
     * @return the content panel header element
     */
    XElement getHeaderElem(XElement parent);

    /**
     * Returns the header size excluding any framing.
     *
     * @return the header size
     */
    Size getHeaderSize(XElement parent);

    /**
     * Handles a change in the visibility of the body border.
     *
     * @param parent content panel root element
     * @param border true to display the border
     */
    void onBodyBorder(XElement parent, boolean border);

    /**
     * Hides or shows the header.
     *
     * @param parent content panel root element
     * @param hide true to hide the header
     */
    void onHideHeader(XElement parent, boolean hide);

    /**
     * Renders the appearance of a content panel as HTML into a {@link SafeHtmlBuilder}, suitable for passing to
     * {@link Element#setInnerSafeHtml(SafeHtml)} on a container element.
     *
     * @param sb receives the rendered appearance
     */
    void render(SafeHtmlBuilder sb);

  }

  /**
   * Provides access to content panel messages.
   */
  public interface ContentPanelMessages {

    /**
     * Returns the content panel collapse message.
     *
     * @return the content panel collapse message
     */
    String panelCollapse();

    /**
     * Returns the content panel expand message.
     *
     * @return the content panel expand message
     */
    String panelExpand();

  }

  /**
   * Provides support for deferred binding for the panel header appearance.
   */
  public interface PanelHeaderAppearance extends HeaderAppearance {

  }

  protected class DefaultContentPanelMessages implements ContentPanelMessages {

    public String panelCollapse() {
      return DefaultMessages.getMessages().panel_collapsePanel();
    }

    public String panelExpand() {
      return DefaultMessages.getMessages().panel_expandPanel();
    }

  }
}
