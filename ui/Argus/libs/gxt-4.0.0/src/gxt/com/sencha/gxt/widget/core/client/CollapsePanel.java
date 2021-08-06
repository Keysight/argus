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
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.core.client.Style.LayoutRegion;
import com.sencha.gxt.core.client.dom.CompositeElement;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent;
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent.AfterAnimateHandler;
import com.sencha.gxt.fx.client.animation.Fx;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.HasExpandHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Acts as a stand-in for a collapsed {@link ContentPanel}. Used by
 * {@link com.sencha.gxt.widget.core.client.container.BorderLayoutContainer}.
 */
public class CollapsePanel extends Component implements HasExpandHandlers {

  /**
   * Provides the appearance of a {@link CollapsePanel}.
   */
  public interface CollapsePanelAppearance {
    /**
     * Renders the {@link CollapsePanel} appearance for the given region into
     * safe HTML.
     *
     * @param sb receives the rendered appearance
     * @param region the region
     * @param header true to include header text
     */
    public void render(SafeHtmlBuilder sb, LayoutRegion region, boolean header);

    /**
     * Returns the element that wraps the icon for the {@link CollapsePanel}.
     * 
     * @param parent the parent of the icon wrapper (generally
     *          {@link #getElement}).
     * @return the icon wrapper
     */
    XElement iconWrap(XElement parent);

    /**
     * Returns the element that wraps the text for the {@link CollapsePanel}.
     *
     * @param parent the parent of the text wrapper (generally
     *          {@link #getElement}).
     * @return the text wrapper
     */
    XElement textWrap(XElement parent);
  }

  private ContentPanel panel;
  private LayoutRegion region;
  private ToolButton expandBtn;
  private boolean expanded;
  private final CollapsePanelAppearance appearance;
  private boolean animate = false;
  private boolean disableAnimations;
  private BorderLayoutData panelData;
  private BaseEventPreview preview;

  /**
   * Creates a {@link CollapsePanel} that acts as a stand-in for the given panel
   * when it is collapsed.
   * 
   * @param panel the content panel
   * @param data layout data describing a region in a border panel
   * @param region the region this panel occupies in the border panel
   */
  public CollapsePanel(ContentPanel panel, BorderLayoutData data, LayoutRegion region) {
    this(GWT.<CollapsePanelAppearance> create(CollapsePanelAppearance.class), panel, data, region);
  }

  /**
   * Creates a {@link CollapsePanel} that acts as a stand-in for the given panel
   * when it is collapsed.
   * 
   * @param appearance the appearance instance to use when rendering and updating the dom
   * @param panel the content panel
   * @param data layout data describing a region in a border panel
   * @param region the region this panel occupies in the border panel
   */
  public CollapsePanel(CollapsePanelAppearance appearance, ContentPanel panel, BorderLayoutData data, LayoutRegion region) {
    this.panel = panel;
    this.panelData = data;
    this.region = region;
    this.appearance = appearance;
    monitorWindowResize = true;

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    this.appearance.render(builder, region, data.isCollapseHeaderVisible());

    setElement((Element) XDOM.create(builder.toSafeHtml()));
    sinkEvents(Event.ONCLICK);

    IconConfig config = ToolButton.DOUBLERIGHT;
    switch (region) {
      case EAST:
        config = ToolButton.DOUBLELEFT;
        break;
      case NORTH:
        config = ToolButton.DOUBLEDOWN;
        break;
      case SOUTH:
        config = ToolButton.DOUBLEUP;
        break;
      default:
        // do nothing
    }

    expandBtn = new ToolButton(config);
    expandBtn.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        if (expanded) {
          disableAnimations = false;
          collapse();
          disableAnimations = true;
        }
        onExpandButton();
      }
    });

    appearance.iconWrap(getElement()).appendChild(expandBtn.getElement());
    appearance.textWrap(getElement()).setInnerSafeHtml(panel.getHeading());
  }

  @Override
  public HandlerRegistration addExpandHandler(ExpandHandler handler) {
    return addHandler(handler, ExpandEvent.getType());
  }

  /**
   * Collapses an expanded {@link CollapsePanel}, optionally animating it so
   * that it appears to collapse toward the direction of its associated region.
   * Has no effect if the panel is not expanded.
   */
  public void collapse() {
    if (expanded) {
      if (animate) {
        Fx fx = new Fx();
        fx.addAfterAnimateHandler(new AfterAnimateHandler() {
          @Override
          public void onAfterAnimate(AfterAnimateEvent event) {
            afterCollapse();
          }
        });
        Direction d = Direction.LEFT;
        switch (region) {
          case EAST:
            d = Direction.RIGHT;
            break;
          case SOUTH:
            d = Direction.DOWN;
            break;
          case NORTH:
            d = Direction.UP;
            break;
          default:
            // do nothing
        }

        panel.getElement().<FxElement> cast().slideOut(d, fx);
      } else {
        afterCollapse();
      }
    }
  }

  private SplitBar splitBar;

  private SplitBar createSplitBar(LayoutRegion region) {
    switch (region) {
      case WEST:
        return new SplitBar(LayoutRegion.EAST, this);
      case EAST:
        return new SplitBar(LayoutRegion.WEST, this);
      case NORTH:
        return new SplitBar(LayoutRegion.SOUTH, this);
      case SOUTH:
        return new SplitBar(LayoutRegion.NORTH, this);
      case CENTER:
        // do nothing
    }

    return null;
  }

  /**
   * Returns the split bar, creating it if necessary. Shows a mini-collapse tool
   * in the split bar if the collapse panel was created with
   * {@link BorderLayoutData#isCollapseMini()} true.
   * 
   * @return the split bar
   */
  public SplitBar getSplitBar() {
    if (splitBar == null) {
      splitBar = createSplitBar(region);
      splitBar.disableDragging();
      if (panelData.isCollapseMini()) {
        splitBar.setCollapsible(true);
        switch (region) {
          case EAST:
            splitBar.updateMini(Direction.LEFT);
            break;
          case WEST:
            splitBar.updateMini(Direction.RIGHT);
            break;
          case NORTH:
            splitBar.updateMini(Direction.DOWN);
            break;
          case SOUTH:
            splitBar.updateMini(Direction.UP);
            break;
          case CENTER:
            // do nothing
        }
      }
    }

    return splitBar;
  }

  /**
   * Collapses a {@link CollapsePanel} so that it is hidden.
   */
  public void collapseHidden() {
    XElement wrap = appearance.iconWrap(getElement());
    if (wrap != null) {
      wrap.removeChildren();
    }

    getElement().getStyle().setProperty("border", "none");

    SplitBar bar = getData("splitBar");
    if (bar != null) {
      switch (region) {
        case EAST:
          bar.setXOffset(bar.getHandleWidth());
          break;
        case WEST:
          bar.setXOffset(-bar.getHandleWidth());
          break;
        case NORTH:
          bar.setYOffset(-bar.getHandleWidth());
          break;
        case SOUTH:
          bar.setYOffset(bar.getHandleWidth());
          break;
        case CENTER:
          // do nothing
      }

      bar.disableDragging();
    }
  }

  /**
   * Expands a collapsed {@link CollapsePanel}, optionally animating it so that
   * it appears to expand from the direction of its associated region. Has no
   * effect if the panel is not collapsed.
   */
  public void expand() {
    if (panelData.isFloatable() && !expanded) {
      SplitBar bar = panel.getData("splitBar");
      if (bar != null) {
        bar.getElement().getStyle().setDisplay(Display.NONE);
      }
  

      for (Widget w : panel.getHeader().getTools()) {
        w.getElement().getStyle().setVisibility(Visibility.HIDDEN);
      }
      panel.getElement().updateZIndex(0);
      panel.getElement().setVisible(true);
      RootPanel.get().add(panel);

      SplitBar collapseBar = getData("splitBar");
      if (bar != null) {
        collapseBar.getElement().getStyle().setDisplay(Display.NONE);
      }

      Direction d = Direction.RIGHT;
      AnchorAlignment align = new AnchorAlignment(Anchor.TOP_LEFT, Anchor.TOP_RIGHT);
      switch (region) {
        case EAST:
          d = Direction.LEFT;
          align = new AnchorAlignment(Anchor.TOP_RIGHT, Anchor.TOP_LEFT);
          break;
        case SOUTH:
          align = new AnchorAlignment(Anchor.BOTTOM_LEFT, Anchor.TOP_LEFT);
          d = Direction.UP;
          break;
        case NORTH:
          d = Direction.DOWN;
          align = new AnchorAlignment(Anchor.TOP_LEFT, Anchor.BOTTOM_LEFT);
          break;
        case WEST:
        case CENTER:
          // do nothing
      }
      panel.getElement().alignTo(getElement(), align, 0, 0);

      if (animate && !disableAnimations) {
        Fx fx = new Fx();
        fx.addAfterAnimateHandler(new AfterAnimateHandler() {
          @Override
          public void onAfterAnimate(AfterAnimateEvent event) {
            expanded = true;
          }
        });
        panel.getElement().<FxElement> cast().slideIn(d, fx);
      } else {
        expanded = true;
      }

      final TapGestureRecognizer previewTapGestureRecognizer = new TapGestureRecognizer() {
        @Override
        public boolean handleEnd(NativeEvent endEvent) {
          super.handleEnd(endEvent);
          XElement target = endEvent.getEventTarget().cast();
          if (!panel.getElement().isOrHasChild(target) && !(getElement().isOrHasChild(target))) {
            collapse();
            return false;
          }
          return true;
        }
      };

      preview = new BaseEventPreview() {
        @Override
        protected boolean onPreview(NativePreviewEvent pe) {
          switch (pe.getTypeInt()) {
            case Event.ONCLICK:
            case Event.ONMOUSEDOWN:
              XElement target = pe.getNativeEvent().getEventTarget().cast();
              if (!panel.getElement().isOrHasChild(target) && !(getElement().isOrHasChild(target))) {
                collapse();
                remove();
              }

              break;
            case Event.ONTOUCHSTART:
            case Event.ONTOUCHMOVE:
            case Event.ONTOUCHCANCEL:
              previewTapGestureRecognizer.handle(pe.getNativeEvent());
              break;
            case Event.ONTOUCHEND:
              if (!previewTapGestureRecognizer.handle(pe.getNativeEvent())) {
                remove();
              }
          }
          return true;
        }
      };
      CompositeElement comp = new CompositeElement();
      comp.add(panel.getElement());
      preview.setIgnoreList(comp);
      preview.add();
    }
  }

  public CollapsePanelAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the {@link ContentPanel} associated with this {@link CollapsePanel}
   * .
   * 
   * @return the content panel
   */
  public ContentPanel getContentPanel() {
    return panel;
  }

  /**
   * Returns the {@link LayoutRegion} associated with this {@link CollapsePanel}
   * .
   * 
   * @return the layout region
   */
  public LayoutRegion getRegion() {
    return region;
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);

    if (event.getTypeInt() == Event.ONCLICK) {
      event.stopPropagation();
      XElement target = event.getEventTarget().cast();
      if (expandBtn.getElement().isOrHasChild(target)) {
        disableAnimations = false;
        collapse();
        disableAnimations = true;
        onExpandButton();
      } else {
        onBarClick(event);
      }
    }

  }

  @Override
  protected void onWindowResize(int width, int height) {
    if (expanded) {
      if (preview != null) {
        preview.remove();
      }
      collapse();
    }
  }

  public void setIconConfig(IconConfig config) {
    expandBtn.changeStyle(config);
  }

  /**
   * Displays the collapsed form of the {@link CollapsePanel}
   */
  protected void afterCollapse() {
    RootPanel.get().remove(panel);
    SplitBar bar = panel.getData("splitBar");
    if (bar != null) {
      bar.getElement().getStyle().setDisplay(Display.BLOCK);
    }

    SplitBar collapseBar = getData("splitBar");
    if (collapseBar != null) {
      collapseBar.getElement().getStyle().setDisplay(Display.BLOCK);
    }

    for (Widget w : panel.getHeader().getTools()) {
      w.getElement().getStyle().setVisibility(Visibility.VISIBLE);
    }
    panel.getElement().getStyle().setDisplay(Display.BLOCK);
    expanded = false;
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(expandBtn);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(expandBtn);
  }

  protected void onBarClick(Event event) {
    if (!expanded) {
      expand();
    } else {
      collapse();
    }
  }

  protected void onExpandButton() {

  }

}
