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
package com.sencha.gxt.core.client.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.Style.ScrollDirection;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.impl.ComputedStyleImpl;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.core.client.util.Region;
import com.sencha.gxt.core.client.util.Scroll;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.core.client.util.TextMetrics;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.core.shared.FastMap;

/**
 * Adds additional features and functionality to the GWT <code>Element</code> class.
 * 
 * <p />
 * XElement's cannot be directly instantiated. New XElements can be created using the static
 * {@link #createElement(String)} method.
 */
@SuppressWarnings("deprecation")
public class XElement extends com.google.gwt.user.client.Element {

  /**
   * VisMode enumeration. Specifies the the element should hidden using the CSS display or visibility style.
   * 
   * @see #setVisible(boolean)
   */
  public enum VisMode {
    DISPLAY, VISIBILITY
  }

  /**
   * Workaround for http://code.google.com/p/google-web-toolkit/issues/detail?id=3192, breaking the XElement static
   * fields into their own class so they can be created, accessed through a 'real' Java type, not a JSO.
   */
  private static class FieldHolder {
    private static Map<String, Boolean> borderBoxMap = new FastMap<Boolean>();

    private static ComputedStyleImpl computedStyle = GWT.create(ComputedStyleImpl.class);

    private static int adjustXYOffset;

    private static RegExp leftOrRight = RegExp.compile("Left|Right");

    static {
      XElement elem = XElement.createElement("div");
      elem.setXY(100, 100);

      Document.get().getBody().appendChild(elem);

      Point p = elem.getXY();
      if (p.getX() != 100) {
        adjustXYOffset = p.getX() - 100;
      }

      elem.removeFromParent();
    }
  }

  /**
   * Tests to see if the value has units, otherwise appends the default (px).
   * 
   * @param v the value
   * @return the value with units
   */
  public native static String addUnits(String v, String defaultUnit) /*-{
    if (v === "" || v == "auto") {
        return v;
    }
    if (v === undefined) {
        return '';
    }
    if (typeof v == "number"
            || !/\d+(px|em|%|en|ex|pt|in|cm|mm|pc)$/i.test(v)) {
        return v + (defaultUnit || 'px');
    }
    return v;
  }-*/;

  /**
   * Assert that the given {@link Node} is an {@link Element} and automatically typecast it.
   */
  public static XElement as(Node node) {
    assert Element.is(node);
    return (XElement) node;
  }

  /**
   * Creates a new element based on the specified HTML tag name.
   * 
   * @param tagName the element tag name
   * @return the new element
   */
  public static final XElement createElement(String tagName) {
    return (XElement) Document.get().createElement(tagName);
  }

  /**
   * Returns true if the passed element has a border box.
   * 
   * @param element the element to test
   * @return true if the passed element has a border box
   */
  public static boolean isBorderBox(Element element) {
    assert element != null : "Element may not be null";
    String tag = element.getTagName().toLowerCase();
    Boolean r = FieldHolder.borderBoxMap.get(tag);
    if (r == null) {
      Element testElement = (Element) Document.get().createElement(tag);
      testElement.getStyle().setPropertyPx("padding", 1);
      testElement.getStyle().setPropertyPx("width", 100);
      testElement.getStyle().setProperty("visibility", "hidden");
      testElement.getStyle().setProperty("position", "absolute");
      Document.get().getBody().appendChild(testElement);
      r = testElement.getOffsetWidth() == 100;
      Document.get().getBody().removeChild(testElement);
      FieldHolder.borderBoxMap.put(tag, r);
    }
    return r;
  }

  private native static void disableTextSelectInternal(Element e, boolean disable) /*-{
    if (disable) {
        e.ondrag = function(evt) {
            var targ;
            if (!evt)
                evt = $wnd.event;
            if (evt.target)
                targ = evt.target;
            else if (evt.srcElement)
                targ = evt.srcElement;
            if (targ.nodeType == 3) // defeat Safari bug
                targ = targ.parentNode;
            if (targ.tagName == 'INPUT' || targ.tagName == 'TEXTAREA') {
                return true;
            }
            return false;
        }
        e.onselectstart = function(evt) {
            var targ;
            if (!evt)
                evt = $wnd.event;
            if (evt.target)
                targ = evt.target;
            else if (evt.srcElement)
                targ = evt.srcElement;
            if (targ.nodeType == 3) // defeat Safari bug
                targ = targ.parentNode;
            if (targ.tagName == 'INPUT' || targ.tagName == 'TEXTAREA') {
                return true;
            }
            return false;
        };
    } else {
        e.ondrag = null;
        e.onselectstart = null;
    }
  }-*/;

  /**
   * Not directly instantiable. Subclasses should also define a protected no-arg constructor to prevent client code from
   * directly instantiating the class.
   */
  protected XElement() {
  }

  /**
   * Adds the style names to the element. Duplicate styles are automatically filtered out.
   * 
   * @param classNames the new class names to add
   */
  public final void addClassName(String... classNames) {
    if (classNames != null) {
      for (String styleName : classNames) {
        if (styleName != null && !hasClassName(styleName)) {
          styleName = styleName.trim();
          setClassName(getClassName() + " " + styleName);
        }
      }
    }
  }

  /**
   * Adds the style names to the element. Duplicate styles are automatically filtered out.
   * 
   * @param className1 the first new class name to add
   * @param className2 the second new class name to add
   */
  public final void addClassName(String className1, String className2) {
    addClassName(className1);
    addClassName(className2);
  }

  /**
   * Adds the event type to the element's sunk events.
   * 
   * @param event the events to add
   */
  public final void addEventsSunk(int event) {
    int bits = DOM.getEventsSunk((Element) this.cast());
    DOM.sinkEvents((Element) this.cast(), bits | event);
  }

  /**
   * Ensures the entire element fits within the browser viewport.
   * The target point (x, y) will be adjusted inward until it fits.
   * This may result in a different position, but the size will
   * stay the same.
   * 
   * @param point the target point
   * @return the adjusted point
   */
  public final Point adjustForConstraints(Point point) {
    return adjustForConstraints(Document.get().getBody(), point);
  }

  /**
   * Ensures the entire element fits within the constraining element.
   * This may result in a different position, but the size will
   * stay the same.
   *
   * @param constraint the element to constrain within
   * @param point the target point
   * @return the adjusted point
   */
  public final Point adjustForConstraints(Element constraint, Point point) {
    // possibly move logic here and deprecate protected method
    return getConstrainToXY(constraint, point);
  }

  /**
   * Ensures the entire element fits within the browser viewport.
   * The target bounds (x, y, width, height) will be adjusted inward
   * until it fits. This may result in a both a different position
   * and a different size.
   *
   * @param bounds the target bounds
   * @return the adjusted bounds
   */
  public final Rectangle adjustForConstraints(Rectangle bounds) {
    return adjustForConstraints(Document.get().getBody(), bounds);
  }

  /**
   * Ensures the entire element fits within the constraining element.
   * The target bounds (x, y, width, height) will be adjusted inward
   * until it fits. This may result in a both a different position
   * and a different size.
   *
   * @param constraint the element to constrain within
   * @param bounds the target bounds
   * @return the adjusted bounds
   */
  public final Rectangle adjustForConstraints(Element constraint, Rectangle bounds) {
    int constraintTop = 0;
    int constraintRight = 0;
    int constraintBottom = 0;
    int constraintLeft = 0;
    if (constraint == Document.get().getBody()) {
      constraintRight = XDOM.getViewportSize().getWidth();
      constraintBottom = XDOM.getViewportSize().getHeight();
    } else {
      constraintTop = constraint.getAbsoluteTop();
      constraintRight = constraint.getAbsoluteLeft() + constraint.getOffsetWidth();
      constraintBottom = constraint.getAbsoluteTop() + constraint.getOffsetHeight();
      constraintLeft = constraint.getAbsoluteLeft();
    }

    int boundsTop = bounds.getY();
    int boundsRight = bounds.getX() + bounds.getWidth();
    int boundsBottom = bounds.getY() + bounds.getHeight();
    int boundsLeft = bounds.getX();


    if (boundsTop < constraintTop) {
      boundsTop = constraintTop;
    }
    if (boundsRight > constraintRight) {
      boundsRight = constraintRight;
    }
    if (boundsBottom > constraintBottom) {
      boundsBottom = constraintBottom;
    }
    if (boundsLeft < constraintLeft) {
      boundsLeft = constraintLeft;
    }

    return new Rectangle(boundsLeft, boundsTop, (boundsRight - boundsLeft), (boundsBottom - boundsTop));
  }

  /**
   * Aligns the element with another element relative to the specified anchor points.
   * 
   * @param elem the element to align to
   * @param alignment the position to align to
   * @param offsetX X offset
   * @param offsetY Y offset
   */
  public final void alignTo(Element elem, AnchorAlignment alignment, int offsetX, int offsetY) {
    Point p = getAlignToXY(elem, alignment, offsetX, offsetY);
    setXY(p);
  }

  /**
   * Sets multiple style properties. Style attribute names must be in lower camel case, e.g.
   * "backgroundColor:white; color:red;"
   * 
   * @param styles a style specification string
   */
  public final native void applyStyles(String styles) /*-{
    var re = /\s?([a-z\-]*)\:\s?([^;]*);?/gi;
    var matches;
    while ((matches = re.exec(styles)) != null) {
        this.style[matches[1]] = matches[2];
    }
  }-*/;

  /**
   * Centers the element in the viewport.
   */
  public final void center() {
    center(null);
  }

  /**
   * Centers the element.
   * 
   * @param constrainViewport true to constrain the element position to the viewport.
   */
  public final void center(boolean constrainViewport) {
    alignTo(Document.get().getBody(), new AnchorAlignment(Anchor.CENTER, Anchor.CENTER, constrainViewport), 0, 0);
  }

  /**
   * Centers an element.
   * 
   * @param container the container element
   */
  public final void center(Element container) {
    if (container == null) {
      container = Document.get().getBody();
    }
    alignTo(container, new AnchorAlignment(Anchor.CENTER, Anchor.CENTER, false), 0, 0);
  }

  /**
   * Selects a single child at any depth below this element based on the passed CSS selector.
   * 
   * @param selector the css selector
   * @return the child element
   */
  public final XElement child(String selector) {
    Element child = childElement(selector);
    return child == null ? null : XElement.as(child);
  }

  /**
   * Selects a single child at any depth below this element based on the passed CSS selector.
   * 
   * @param selector the css selector
   * @return the child element
   */
  public final Element childElement(String selector) {
    return DomQuery.selectNode(selector, this);
  }

  /**
   * Generators a native dom click on the element.
   */
  public final native void click() /*-{
    var dom = this;
    if (dom.click) {
        dom.click();
    } else {
        var event = $doc.createEvent("MouseEvents");
        event.initEvent('click', true, true, $wnd, 0, 0, 0, 0, 0, false,
                false, false, false, 1, dom);
        dom.dispatchEvent(event);
    }
  }-*/;

  /**
   * Creates and adds a child using the HTML fragment.
   *
   * @param html the html fragment
   * @return the new child
   */
  public final XElement createChild(SafeHtml html) {
    return appendChild(XDOM.create(html)).<XElement> cast();
  }

  /**
   * Disables the element.
   */
  public final void disable() {
    setPropertyBoolean("disabled", true);
  }

  /**
   * Enables or disables text selection for the element and all children. A circular reference will be created when disabling text
   * selection. Disabling should be cleared when the element is detached. See the <code>Component</code> source for an
   * example.
   * 
   * @param disable true to disable, false to enable
   */
  public final void disableTextSelection(boolean disable) {
    setClassName(CommonStyles.get().unselectable(), disable);
    setPropertyString("unselectable", disable ? "on" : "");
    disableTextSelectInternal(this, disable);
  }

  /**
   * Enables or disables text selection for the element. A circular reference will be created when disabling text
   * selection. Disabling should be cleared when the element is detached. See the <code>Component</code> source for an
   * example.
   *
   * @param disable true to disable, false to enable
   */
  public final void disableTextSelectionSingle(boolean disable) {
    setClassName(CommonStyles.get().unselectableSingle(), disable);
    setPropertyString("unselectable", disable ? "on" : "");
    disableTextSelectInternal(this, disable);
  }

  /**
   * Selects a single *direct* child based on the passed CSS selector (the selector should not contain an id).
   * 
   * @param selector the CSS selector
   * @return the child element
   */
  public final XElement down(String selector) {
    Element elem = DomQuery.selectNode(" > " + selector, this);
    if (elem != null) {
      return XElement.as(elem);
    }
    return null;
  }

  /**
   * Enables the element.
   */
  public final void enable() {
    setPropertyBoolean("disabled", false);
  }

  /**
   * Walks up the DOM and ensures all elements are visible. Useful when trying to calculate offsets or page coordinates.
   * 
   * @return list of meta data, see {@link #restoreVisible(List)}
   */
  public final List<FastMap<Object>> ensureVisible() {
    List<FastMap<Object>> list = new ArrayList<FastMap<Object>>();
    XElement p = this;
    XElement body = Document.get().getBody().cast();
    while (p != null && p != body) {
      if (p.isStyleAttribute("display", "none")) {
        FastMap<Object> m = new FastMap<Object>();
        m.put("element", p);
        m.put("origd", p.getStyle().getProperty("display"));

        boolean hasxhideoffset = p.hasClassName(CommonStyles.get().hideOffsets());
        m.put("hasxhideoffset", hasxhideoffset);
        if (!hasxhideoffset) {
          p.addClassName(CommonStyles.get().hideOffsets());
        }

        boolean hideDisplay = p.hasClassName(CommonStyles.get().hideDisplay());
        if (hideDisplay) {
          m.put("hasxhidedisplay", hideDisplay);
          p.removeClassName(CommonStyles.get().hideDisplay());
        }

        p.getStyle().setProperty("display", "block");
        list.add(m);
      }
      p = (XElement) p.getParentElement();
    }
    return list.size() > 0 ? list : null;
  }

  /**
   * Looks at this node and then at parent nodes for a match of the passed simple selector (e.g. div.some-class or
   * span:first-child).
   * 
   * @param selector the simple selector to test
   * @return the matching element
   */
  public final XElement findParent(String selector, int maxDepth) {
    Element elem = findParentElement(selector, maxDepth);
    if (elem == null) {
      return null;
    }
    return XElement.as(elem);
  }

  /**
   * Looks at this node and then at parent nodes for a match of the passed simple selector (e.g. div.some-class or
   * span:first-child).
   * 
   * @param selector the simple selector to test
   * @param maxDepth the max depth
   * @return the matching element
   */
  public final Element findParentElement(String selector, int maxDepth) {
    Element p = this;
    Element b = Document.get().getBody();
    int depth = 0;
    while (p != null && p.getNodeType() == 1 && (maxDepth == -1 || depth < maxDepth) && p != b) {
      if (DomQuery.is(p, selector)) {
        return p;
      }
      depth++;
      p = (Element) p.getParentElement();
    }
    return null;
  }

  /**
   * Gets the x,y coordinates to align this element with another element.
   * 
   * @param elem the element to align to
   * @param alignment the alignment
   * @param ox x offset
   * @param oy y offset
   * @return the point
   */
  public final Point getAlignToXY(Element elem, AnchorAlignment alignment, int ox, int oy) {
    XElement el = XElement.as(elem);

    boolean constrainViewport = alignment.isConstrainViewport();
    Anchor anch1 = alignment.getAlign();
    Anchor anch2 = alignment.getTargetAlign();
    // Subtract the aligned el's internal xy from the target's offset xy
    // plus custom offset to get the aligned el's new offset xy
    Point a1 = getAnchorXY(anch1, true);
    Point a2 = el.getAnchorXY(anch2, false);

    int x = a2.getX() - a1.getX() + ox;
    int y = a2.getY() - a1.getY() + oy;

    if (constrainViewport) {
      // constrain the aligned el to viewport if necessary
      int w = getOffsetWidth();
      int h = getOffsetHeight();
      Region r = el.getRegion();
      int dw = XDOM.getViewWidth(false);
      int dh = XDOM.getViewHeight(false);

      // If we are at a viewport boundary and the aligned el is anchored on a
      // target border that is
      // perpendicular to the vp border, allow the aligned el to slide on that
      // border,
      // otherwise swap the aligned el to the opposite border of the target.
      boolean swapY = (anch1.isTop() && anch2.isBottom()) || (anch1.isBottom() && anch2.isTop());
      boolean swapX = (anch1.isRight() && anch2.isLeft()) || (anch1.isLeft() && anch2.isRight());

      // EXTGWT-1730 only applying 5 when there is scrolling. 5 may be able to
      // be removed
      // but not sure why this was added in first place. it exists in 2.0
      int scrollX = XDOM.getBodyScrollLeft();

      if (scrollX > 0) {
        scrollX += 5;
      }
      int scrollY = XDOM.getBodyScrollTop();
      if (scrollY > 0) {
        scrollY += 5;
      }

      if ((x + w) > dw + scrollX) {
        x = swapX ? r.getLeft() - w : dw + scrollX - w;
      }
      if (x < scrollX) {
        x = swapX ? r.getRight() : scrollX;
      }

      if ((y + h) > (dh + scrollY)) {
        y = swapY ? r.getTop() - h : dh + scrollY - h;
      }
      if (y < scrollY) {
        y = swapY ? r.getBottom() : scrollY;
      }
    }

    return new Point(x, y);
  }

  /**
   * Returns the x,y coordinates specified by the anchor position on the element.
   * 
   * @param anchor the specified anchor position (defaults to {@code CENTER}). See {@link #alignTo} for details on
   *          supported anchor positions.
   * @param local {@code true} to get the local (element top/left-relative) anchor position instead of page coordinates
   * @return the position
   */
  public final Point getAnchorXY(Anchor anchor, boolean local) {
    if (anchor == null) {
      return null;
    }
    boolean vp = false;
    final int w;
    final int h;
    if (this.cast() == Document.get().getBody() || this == Document.get().getDocumentElement()) {
      vp = true;
      w = XDOM.getViewWidth(false);
      h = XDOM.getViewHeight(false);
    } else {
      w = getOffsetWidth();
      h = getOffsetHeight();
    }

    int x = 0, y = 0;
    switch (anchor) {
      case CENTER:
        x = (int) Math.round(w * .5);
        y = (int) Math.round(h * .5);
        break;
      case TOP:
        x = (int) Math.round(w * .5);
        y = 0;
        break;
      case LEFT:
        x = 0;
        y = (int) Math.round(h * .5);
        break;
      case RIGHT:
        x = w;
        y = (int) Math.round(h * .5);
        break;
      case BOTTOM:
        x = (int) Math.round(w * .5);
        y = h;
        break;
      case TOP_LEFT:
        x = 0;
        y = 0;
        break;
      case BOTTOM_LEFT:
        x = 0;
        y = h;
        break;
      case BOTTOM_RIGHT:
        x = w;
        y = h;
        break;
      case TOP_RIGHT:
        x = w;
        y = 0;
        break;
    }

    if (local) {
      return new Point(x, y);
    }
    if (vp) {
      Scroll sc = getScroll();
      return new Point(x + sc.getScrollLeft(), y + sc.getScrollTop());
    }
    // Add the element's offset xy

    Point o = getXY();
    return new Point(x + o.getX(), y + o.getY());
  }

  /**
   * Returns the total border size of the specified sides.
   * <p/>
   * Passing more than one side will yield the sum of the sides of those sides of the element.
   * 
   * @param side the side
   * @return the sum of the widths of the borders
   */
  public final int getBorders(Side side) {
    switch (side) {
      case LEFT:
        return Util.parseInt(getComputedStyle("borderLeft"), 0);
      case RIGHT:
        return Util.parseInt(getComputedStyle("borderRight"), 0);
      case TOP:
        return Util.parseInt(getComputedStyle("borderTop"), 0);
      case BOTTOM:
        return Util.parseInt(getComputedStyle("borderBottom"), 0);
    }
    assert false : "Unparsable Side " + side;
    return 0;
  }

  /**
   * Returns the total border size of the specified sides.
   * <p/>
   * Passing more than one side will yield the sum of the sides of those sides of the element.
   * 
   * @param sides the sides
   * @return the sum of the widths of the borders
   */
  public final int getBorders(Side... sides) {
    List<String> list = new ArrayList<String>();

    for (int i = 0; i < sides.length; i++) {
      Side side = sides[i];
      appendSideToList(list, side, "border");
    }

    return getStylePropertyTotal(list);
  }

  /**
   * Returns the total border size of the specified sides.
   * <p/>
   * Passing more than one side will yield the sum of the sides of those sides of the element.
   * 
   * @param side1 the first side
   * @param side2 the second side
   * @return the sum of the widths of the borders
   */
  public final int getBorders(Side side1, Side side2) {
    List<String> list = new ArrayList<String>();

    appendSideToList(list, side1, "border");
    appendSideToList(list, side2, "border");

    return getStylePropertyTotal(list);
  }

  /**
   * Returns the elements bounds in page coordinates.
   * 
   * @return the bounds
   */
  public final Rectangle getBounds() {
    return getBounds(false, false);
  }

  /**
   * Returns the elements bounds in page coordinates.
   * 
   * @param local if true the element's left and top are returned instead of page coordinates
   * 
   * @return the bounds
   */
  public final Rectangle getBounds(boolean local) {
    return getBounds(local, false);
  }

  /**
   * Returns the element's bounds in page coordinates.
   * 
   * @param local if true the element's left and top are returned instead of page coordinates
   * @param adjust if true sizes get adjusted
   * 
   * @return the element's bounds
   */
  public final Rectangle getBounds(boolean local, boolean adjust) {
    Size s = getSize(adjust);
    Rectangle rect = new Rectangle();
    rect.setWidth(s.getWidth());
    rect.setHeight(s.getHeight());
    if (local) {
      rect.setX(getLeft(true));
      rect.setY(getTop(true));
    } else {
      Point p = getXY();
      rect.setX(p.getX());
      rect.setY(p.getY());
    }
    return rect;
  }

  /**
   * Returns the index of the child element.
   * 
   * @return the index
   */
  public final int getChildIndex(Element child) {
    return DOM.getChildIndex((Element) this.cast(), (Element) child.cast());
  }

  /**
   * Returns either the offsetHeight or the height of this element based on it's CSS height.
   * 
   * @return the height
   */
  public final int getComputedHeight() {
    int h = getOffsetHeight();
    if (h == 0) {
      h = getStyleSize().getHeight();
    }
    return h;
  }

  /**
   * Returns a map of style values mapped by property name.
   * 
   * @param props the list of CSS property names
   * @return the map of property and values
   */
  public final FastMap<String> getComputedStyle(List<String> props) {
    return FieldHolder.computedStyle.getStyleAttribute(this, props);
  }

  /**
   * Normalizes currentStyle and computedStyle.
   * 
   * @param prop the style attribute whose value is returned.
   * @return the current value of the style attribute for this element.
   */
  public final String getComputedStyle(String prop) {
    return FieldHolder.computedStyle.getStyleAttribute(this, prop);
  }

  /**
   * Returns either the offsetWidth or the width of this element based on it's CSS width.
   * 
   * @return the width
   */
  public final int getComputedWidth() {
    int w = getOffsetWidth();
    if (w == 0) {
      w = getStyleSize().getWidth();
    }
    return w;
  }

  /**
   * Returns the sum width of the padding and borders for all "sides". See #getBorderWidth() for more information about
   * the sides.
   * 
   * @return the frame size
   */
  public final Size getFrameSize() {
    int width = 0;
    int height = 0;
    List<String> list = new ArrayList<String>();
    list.add("paddingLeft");
    list.add("borderLeftWidth");

    list.add("paddingRight");
    list.add("borderRightWidth");

    list.add("paddingTop");
    list.add("borderTopWidth");

    list.add("paddingBottom");
    list.add("borderBottomWidth");

    FastMap<String> map = getComputedStyle(list);
    for (String s : map.keySet()) {
      if (isLeftOrRight(s)) {
        width += Util.parseInt(map.get(s), 0);
      } else {
        height += Util.parseInt(map.get(s), 0);
      }
    }
    return new Size(width, height);
  }

  /**
   * Returns the sum width of the padding and borders for the passed "sides".
   * 
   * @param side the sides
   * @return the width
   */
  public final int getFrameWidth(Side... side) {
    List<String> list = new ArrayList<String>();

    for (int i = 0; i < side.length; i++) {
      Side s = side[i];
      appendSideToList(list, s, "padding");
      appendSideToList(list, s, "border");
    }

    return getStylePropertyTotal(list);
  }

  /**
   * Returns the sum width of the padding and borders for the given side.
   * 
   * @param side the side
   * @return the total width
   */
  public final int getFrameWidth(Side side) {
    List<String> list = new ArrayList<String>();

    appendSideToList(list, side, "padding");
    appendSideToList(list, side, "border");
    return getStylePropertyTotal(list);
  }

  /**
   * Returns the sum width of the padding and borders for the given side.
   * 
   * @param side1 the side
   * @param side2 the side
   * @return the total width
   */
  public final int getFrameWidth(Side side1, Side side2) {
    List<String> list = new ArrayList<String>();

    appendSideToList(list, side1, "padding");
    appendSideToList(list, side1, "border");
    appendSideToList(list, side2, "padding");
    appendSideToList(list, side2, "border");

    return getStylePropertyTotal(list);
  }

  /**
   * Returns the element's height.
   * 
   * @param content true to get the height minus borders and padding
   * @return the element's height
   */
  public final int getHeight(boolean content) {
    int h = getOffsetHeight();
    if (content) {
      h -= getFrameWidth(Side.TOP, Side.BOTTOM);
    }
    return Math.max(0, h);
  }

  /**
   * Returns the element's id.
   * 
   * @param autoGenId true to set an id if one does not exist
   * @return the id
   */
  public final String getId(boolean autoGenId) {
    String id = getId();
    if (!autoGenId || !(id == null || (id.length() == 0))) {
      return id;
    }

    id = DomIdProvider.generateId(this);
    setId(id);
    return id;
  }

  /**
   * Returns the top Y coordinate.
   * 
   * @return the top value
   */
  public final int getLeft() {
    return getLeft(true);
  }

  /**
   * Gets the left X coordinate.
   * 
   * @param local true to get the local css position instead of page coordinate
   * @return the left value
   */
  public final int getLeft(boolean local) {
    return local ? Util.parseInt(getStyle().getLeft(), 0) : getX();
  }

  /**
   * Returns the total margin size of the specified side.
   * 
   * @param side the side from which to calculate the margin.
   * @return the widths of the margin
   */
  public final int getMargins(Side side) {
    switch (side) {
      case LEFT:
        return Util.parseInt(getComputedStyle("marginLeft"), 0);
      case RIGHT:
        return Util.parseInt(getComputedStyle("marginRight"), 0);
      case TOP:
        return Util.parseInt(getComputedStyle("marginTop"), 0);
      case BOTTOM:
        return Util.parseInt(getComputedStyle("marginBottom"), 0);
    }
    assert false : "Unparsable Side " + side;
    return 0;
  }

  /**
   * Returns the total margin size of the specified sides.
   * 
   * @param sides the sides from which to calculate the margin. Passing more than one side will yield the sum of the
   *          margins of those sides of the element.
   * @return the sum of the widths of the margins
   */
  public final int getMargins(Side... sides) {
    List<String> list = new ArrayList<String>();
    for (Side side : sides) {
      appendSideToList(list, side, "margin");
    }

    return getStylePropertyTotal(list);
  }

  /**
   * Returns the total margin size of the specified sides.
   * <p/>
   * Passing more than one side will yield the sum of the margins of those sides of the element.
   * 
   * @param side1 the first side from which to calculate the margin.
   * @param side2 the second side from which to calculate the margin.
   * @return the sum of the widths of the margins
   */
  public final int getMargins(Side side1, Side side2) {
    List<String> list = new ArrayList<String>();

    appendSideToList(list, side1, "margin");
    appendSideToList(list, side2, "margin");

    return getStylePropertyTotal(list);
  }

  /**
   * Returns the offsets between two elements. Both element must be part of the DOM tree and not have display:none to
   * have page coordinates.
   * 
   * @param to the to element
   * @return the xy page offsets
   */
  public final Point getOffsetsTo(Element to) {
    Point o = getXY();

    XElement xto = (XElement) to;
    Point e = xto.getXY();
    return new Point(o.getX() - e.getX(), o.getY() - e.getY());
  }

  /**
   * Returns the width of the padding(s) for the specified side.
   * 
   * @param side the side from which to calculate the padding.
   * @return the sum of the widths of the padding
   */
  public final int getPadding(Side side) {
    switch (side) {
      case LEFT:
        return Util.parseInt(getComputedStyle("paddingLeft"), 0);
      case RIGHT:
        return Util.parseInt(getComputedStyle("paddingRight"), 0);
      case TOP:
        return Util.parseInt(getComputedStyle("paddingTop"), 0);
      case BOTTOM:
        return Util.parseInt(getComputedStyle("paddingBottom"), 0);
    }
    assert false : "Unparsable Side " + side;
    return 0;
  }

  /**
   * Returns the width of the padding(s) for the specified side(s).
   * 
   * @param sides the sides from which to calculate the padding. Passing more than one side will yield the sum of the
   *          padding of those sides of the element.
   * @return the sum of the widths of the padding
   */
  public final int getPadding(Side... sides) {
    List<String> list = new ArrayList<String>();
    for (Side side : sides) {
      appendSideToList(list, side, "padding");
    }

    return getStylePropertyTotal(list);
  }

  /**
   * Returns the widget's current position. The widget must be attached to return page coordinates.
   * 
   * @param local true to return the element's left and top rather than page coordinates
   * @return the position
   */
  public final Point getPosition(boolean local) {
    if (local) {
      return new Point(getLeft(true), getTop(true));
    }
    return getXY();
  }

  /**
   * Returns the region of the given element. The element must be part of the DOM tree to have a region.
   * 
   * @return a region containing top, left, bottom, right
   */
  public final Region getRegion() {
    Rectangle bounds = getBounds();
    Region r = new Region();
    r.setLeft(bounds.getX());
    r.setTop(bounds.getY());
    r.setRight(r.getLeft() + bounds.getWidth());
    r.setBottom(r.getTop() + bounds.getHeight());
    return r;
  }

  /**
   * Returns the right X coordinate of the element (element X position + element width).
   * 
   * @param local <code>true</code> to get the local css position instead of page coordinate
   * @return the right value
   */
  public final int getRight(boolean local) {
    return getOffsetWidth() + (local ? getLeft(true) : getX());
  }

  /**
   * Returns the body elements current scroll position.
   * 
   * @return the scroll position
   */
  public final Scroll getScroll() {
    if (this.cast() == Document.get().getBody() || this == Document.get().getDocumentElement()) {
      return new Scroll(XDOM.getBodyScrollLeft(), XDOM.getBodyScrollTop());
    } else {
      return new Scroll(getScrollLeft(), getScrollTop());
    }
  }

  /**
   * Returns the size of the element.
   * 
   * @return the size
   */
  public final Size getSize() {
    return getSize(false);
  }

  /**
   * Returns the element's size.
   * 
   * @param content true to get the size minus borders and padding
   * @return the size
   */
  public final Size getSize(boolean content) {
    int w = getOffsetWidth();
    int h = getOffsetHeight();
    if (content) {
      Size frameWidth = getFrameSize();
      w -= frameWidth.getWidth();
      h -= frameWidth.getHeight();
    }
    return new Size(Math.max(0, w), Math.max(0, h));
  }

  /**
   * Returns the element's size (excluding padding and borders), using style attribute before offsets.
   * 
   * @return the size
   */
  public final Size getStyleSize() {
    return getStyleSize(true);
  }

  /**
   * Returns the element's size, using style attribute before offsets.
   * 
   * @param contentOnly true to exclude padding and borders
   * @return the size
   */
  public final Size getStyleSize(boolean contentOnly) {
    int h = Util.parseInt(getStyle().getProperty("height"), Style.DEFAULT);
    int w = Util.parseInt(getStyle().getProperty("width"), Style.DEFAULT);

    boolean isBorderBox = isBorderBox();

    if (isBorderBox && contentOnly && w != Style.DEFAULT) {
      w -= getFrameWidth(Side.LEFT, Side.RIGHT);
      if (w < 0) {
        w = Style.DEFAULT;
      }
    } else if (!isBorderBox && !contentOnly && w != Style.DEFAULT) {
      w += getFrameWidth(Side.LEFT, Side.RIGHT);
    }
    if (isBorderBox && contentOnly && h != Style.DEFAULT) {
      h -= getFrameWidth(Side.TOP, Side.BOTTOM);
      if (h < 0) {
        h = Style.DEFAULT;
      }
    } else if (!isBorderBox && !contentOnly && h != Style.DEFAULT) {
      h += getFrameWidth(Side.TOP, Side.BOTTOM);
    }

    int offsetWidth = Style.DEFAULT;
    int offsetHeight = Style.DEFAULT;
    if (w == Style.DEFAULT && h == Style.DEFAULT) {
      Size s = getSize(contentOnly);
      offsetWidth = s.getWidth();
      offsetHeight = s.getHeight();
      if (s.getWidth() > 0) {
        w = s.getWidth();
      }
      if (s.getHeight() > 0) {
        h = s.getHeight();
      }
    } else if (w == Style.DEFAULT) {
      offsetWidth = getWidth(contentOnly);
      if (offsetWidth > 0) {
        w = offsetWidth;
      }
    } else if (h == Style.DEFAULT) {
      offsetHeight = getHeight(contentOnly);
      if (offsetHeight > 0) {
        h = offsetHeight;
      }
    }

    List<String> l = new ArrayList<String>();
    if (w == Style.DEFAULT) {
      l.add("width");
    }
    if (h == Style.DEFAULT) {
      l.add("height");
    }
    Map<String, String> map = getComputedStyle(l);
    if (map != null) {
      String wid = map.get("width");
      if (wid != null) {
        w = Util.parseInt(wid, Style.DEFAULT);
        if (offsetWidth == 0 && isBorderBox && contentOnly && w != Style.DEFAULT && !GXT.isIE()) {
          w -= getFrameWidth(Side.LEFT, Side.RIGHT);
        } else if (GXT.isIE() && isBorderBox && w != Style.DEFAULT && contentOnly) {
          w -= getFrameWidth(Side.LEFT, Side.RIGHT);
        } else if (offsetWidth == 0 && !isBorderBox && !contentOnly && w != Style.DEFAULT) {
          w += getFrameWidth(Side.LEFT, Side.RIGHT);
        }
      }
      String hei = map.get("height");
      if (hei != null) {
        h = Util.parseInt(hei, Style.DEFAULT);
        if (offsetHeight == 0 && isBorderBox && contentOnly && h != Style.DEFAULT && !GXT.isIE()) {
          h -= getFrameWidth(Side.TOP, Side.BOTTOM);
        } else if (GXT.isIE() && isBorderBox && h != Style.DEFAULT && contentOnly) {
          h -= getFrameWidth(Side.TOP, Side.BOTTOM);
        } else if (offsetHeight == 0 && !isBorderBox && !contentOnly && h != Style.DEFAULT) {
          h += getFrameWidth(Side.TOP, Side.BOTTOM);
        }
      }
    }
    if (w == Style.DEFAULT && h == Style.DEFAULT) {
      return new Size(offsetWidth, offsetHeight);
    }
    return new Size(w != Style.DEFAULT ? w : offsetWidth, h != Style.DEFAULT ? h : offsetHeight);
  }

  /**
   * Returns the element's sub child.
   * 
   * @param depth the child node depth
   * @return the child element
   */
  public final Element getSubChild(int depth) {
    Element child = this;
    while (depth-- > 0) {
      child = child.getChild(0).cast();
    }
    return child;
  }

  /**
   * Returns the measured width of the element's text.
   * 
   * @return the width
   */
  public final int getTextWidth() {
    String text = getInnerText();
    TextMetrics metrics = TextMetrics.get();
    metrics.bind(this);
    return metrics.getWidth(text);
  }

  /**
   * Returns the top Y coordinate.
   * 
   * @return the top value
   */
  public final int getTop() {
    return getTop(true);
  }

  /**
   * Gets the top Y coordinate.
   * 
   * @param local true to get the local css position instead of page coordinate
   * @return the top value
   */
  public final int getTop(boolean local) {
    return local ? Util.parseInt(getStyle().getTop(), 0) : getY();
  }

  /**
   * Returns the vis mode.
   * 
   * @return the vis mode
   */
  public final native VisMode getVisMode() /*-{
    return this.visMode;
  }-*/;

  /**
   * Returns the element's width.
   * 
   * @param content true to get the width minus borders and padding
   * @return the width
   */
  public final int getWidth(boolean content) {
    int w = getOffsetWidth();
    if (content) {
      w -= getFrameWidth(Side.LEFT, Side.RIGHT);
    }
    return Math.max(0, w);
  }

  /**
   * Gets the current X position of the element based on page coordinates. Element must be part of the DOM tree to have
   * page coordinates.
   * 
   * @return the x position of the element
   */
  public final int getX() {
    int x = getAbsoluteLeft();
    return x > 0 ? x - FieldHolder.adjustXYOffset : x;
  }

  /**
   * Gets the current position of the element based on page coordinates. Element must be part of the DOM tree to have
   * page coordinates.
   * 
   * @return the location
   */
  public final Point getXY() {
    return new Point(getX(), getY());
  }

  /**
   * Gets the current Y position of the element based on page coordinates.
   * 
   * @return the y position of the element
   */
  public final int getY() {
    int y = getAbsoluteTop();
    return y > 0 ? y - FieldHolder.adjustXYOffset : y;
  }

  /**
   * Returns the element's z-index.
   * 
   * @return the z-index
   */
  public final int getZIndex() {
    try {
      return Util.parseInt(getStyle().getZIndex(), 0);
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Hides this element.
   */
  public final void hide() {
    setVisible(false);
  }

  /**
   * Returns the index of the child.
   * 
   * @param child the child
   * @return the index or -1 of not a child
   */
  public final int indexOf(Element child) {
    int count = getChildCount();
    for (int i = 0; i < count; i++) {
      if (getChild(i) == child) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Inserts this element before the passed element.
   * 
   * @param element the element to insert before
   */
  public final void insertBefore(Element element) {
    element.getParentElement().insertBefore(this, element);
  }

  /**
   * Inserts the elements before this element.
   * 
   * @param elements the elements to insert
   */
  public final void insertBefore(NodeList<Element> elements) {
    Element parent = getParentElement();
    assert parent != null : "cannot insertBefore with null parent";
    for (int i = 0, len = elements.getLength(); i < len; i++) {
      parent.insertBefore(elements.getItem(0), this);
    }
  }

  /**
   * Inserts an element at the specified index.
   * 
   * @param child the child element
   * @param index the insert location
   */
  public final void insertChild(Element child, int index) {
    DOM.insertChild((com.google.gwt.dom.client.Element) this.cast(), child, index);
  }

  /**
   * Creates and inserts a child element.
   * 
   * @param html the HTML fragment
   * @return the new child
   */
  public final XElement insertFirst(SafeHtml html) {
    return XElement.as(DomHelper.insertFirst(this, html));
  }

  /**
   * Inserts an html fragment into this element
   * 
   * @param where where to insert the html in relation to el - beforeBegin, afterBegin, beforeEnd, afterEnd.
   * @param html the HTML fragment
   * @return the inserted node (or nearest related if more than 1 inserted)
   */
  public final XElement insertHtml(String where, SafeHtml html) {
    return XElement.as(DomHelper.insertHtml(where, this, html));
  }

  /**
   * Returns true if this element matches the passed simple selector (e.g. div.some-class or span:first-child).
   * 
   * @param selector selector
   * @return true if the element matches the selector, else false
   */
  public final boolean is(String selector) {
    return DomQuery.is(this, selector);
  }

  /**
   * Returns true if the element is a border box.
   * 
   * @return true for border box
   */
  public final boolean isBorderBox() {
    // only supported mode is w3c model as we force strict
    return false;
    // return isBorderBox(this);
  }

  /**
   * Returns true if the element is part of the browser's DOM.
   * 
   * @return the connected state
   */
  public final boolean isConnected() {
    return Document.get().getBody().isOrHasChild(this);
  }

  /**
   * Determine whether an element is equal to, or the child of, this element.<br/>
   * <br/>
   * This implementation delegates to <code>Node.isOrHasChildImpl(Node)</code>, but catches a scenario
   * in IE9 & IE10 where not all elements support the contains() method used by the deferred binding to
   * <code>DOMImplTrident.isOrHasChildImpl(Node, Node)</code>.
   *
   * @param child the potential child element
   * @return <code>true</code> if the relationship holds
   * @see com.google.gwt.dom.client.Node#isOrHasChild(Node)
   * @see com.google.gwt.dom.client.DOMImplTrident#isOrHasChildImpl(Node, Node)
   */
  public final boolean isOrHasChild(XElement child) {
    try {
      return super.isOrHasChild(child);
    } catch (JavaScriptException e) {
      // swallow the exception if it is a manifestation of the IE9 & IE10 bug
      if ((GXT.isIE9() || GXT.isIE10())
          && e.getName().equals("TypeError")
          && e.getMessage().contains("'contains'")) {
        return false;
      } else {
        throw e;
      }
    }
  }

  /**
   * Returns whether the element is scrollable (x or y).
   * 
   * @return true if scrollable
   */
  public final boolean isScrollable() {
    return isScrollableX() || isScrollableY();
  }

  /**
   * Returns whether the element is scrollable on the x-axis.
   * 
   * @return true if scrollable on the x-axis
   */
  public final boolean isScrollableX() {
    return getScrollWidth() > getClientWidth();
  }

  /**
   * Returns whether the element is scrollable on the y-axis.
   * 
   * @return true if scrollable on the y-axis
   */
  public final boolean isScrollableY() {
    return getScrollHeight() > getClientHeight();
  }

  /**
   * Tests the style for the given value.
   * 
   * @param attr the style name
   * @param value the test value
   * @return true if equal
   */
  public final boolean isStyleAttribute(String attr, String value) {
    String a = getComputedStyle(attr);
    return a != null && a.equals(value);
  }

  /**
   * Tests style property values for matches.
   * 
   * @param map the map of style property names and values
   * @param matchAll true to match all properties
   * @return true if a match is found, otherwise false
   */
  public final boolean isStyleProperty(Map<String, String> map, boolean matchAll) {
    Set<String> collection = map.keySet();
    FastMap<String> a = getComputedStyle(new ArrayList<String>(collection));
    for (String s : collection) {
      if (map.get(s).equals(a.get(s))) {
        if (!matchAll) {
          return true;
        }
      } else {
        if (matchAll) {
          return false;
        }
      }
    }
    return false;
  }

  /**
   * Returns whether the element is currently visible.
   * 
   * @return true if visible
   */
  public final boolean isVisible() {
    return isVisible(false);
  }

  /**
   * Returns whether the element is currently visible.
   * 
   * @param deep true to deep test
   * 
   * @return true if visible
   */
  public final boolean isVisible(boolean deep) {
    Map<String, String> map = new FastMap<String>();
    map.put("visibility", "hidden");
    map.put("display", "none");
    boolean vis = !isStyleProperty(map, false);
    XElement parent = (XElement) getParentElement();
    XElement p = parent != null ? parent : null;
    if (p == null) {
      return false;
    }
    if (!deep || !vis) {
      return vis;
    }

    while (p != null && p != Document.get().getDocumentElement()) {
      if (!p.isVisible()) {
        return false;
      }
      p = (XElement) p.getParentElement();
    }
    return true;
  }

  /**
   * Retrieves the data using the request builder and updates the element's contents.
   * <p/>
   * Please note that the <code>Response</code> from the <code>RequestBuilder</code> is treated as raw html
   * without any sanitizing. If is up to the caller to ensure that the call does not return unsafe html.
   * <p/>
   * This method is subject to change.
   * 
   * @param builder the request builder
   */
  public final Request load(RequestBuilder builder) {
    try {
      builder.setCallback(new RequestCallback() {

        public void onError(Request request, Throwable exception) {
          setInnerSafeHtml(exception != null && exception.getMessage() != null
              ? SafeHtmlUtils.fromString(exception.getMessage()) : SafeHtmlUtils.EMPTY_SAFE_HTML);
        }

        public void onResponseReceived(Request request, Response response) {
          setInnerSafeHtml(response != null && response.getText() != null
              ? SafeHtmlUtils.fromString(response.getText()) : SafeHtmlUtils.EMPTY_SAFE_HTML);
        }

      });
      return builder.send();
    } catch (Exception e) {
      setInnerSafeHtml(e != null && e.getMessage() != null
          ? SafeHtmlUtils.fromString(e.getMessage()) : SafeHtmlUtils.EMPTY_SAFE_HTML);
      return null;
    }
  }

  /**
   * Makes an element positionable.
   */
  public final void makePositionable() {
    makePositionable(false);
  }

  /**
   * Makes an element positionable.
   * 
   * @param absolute <code>true</code> to position absolutely
   */
  public final void makePositionable(boolean absolute) {
    if (absolute) {
      getStyle().setPosition(Position.ABSOLUTE);
    } else {
      String p = getComputedStyle("position");
      if (p == null || "".equals(p) || "static".equals(p) && (!"absolute".equals(p))) {
        getStyle().setPosition(Position.RELATIVE);
      }
    }
  }

  /**
   * Puts a mask over this element to disable user interaction.
   * 
   * @param message a message to display in the mask
   */
  public final void mask(String message) {
    Mask.mask(this, message);
  }

  /**
   * Removes all the elements children.
   */
  public final void removeChildren() {
    Element child = null;
    while ((child = getFirstChildElement()) != null) {
      removeChild(child);
    }
    String tag = getTagName().toLowerCase();
    if (!tag.equals("table") && !tag.equals("tbody") && !tag.equals("tr") && !tag.equals("td")) {
      setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
    }
  }

  /**
   * Removes the style names(s) from the element.
   * 
   * @param classNames the style names
   */
  public final void removeClassName(String... classNames) {
    for (int i = 0; i < classNames.length; i++) {
      removeClassName(classNames[i]);
    }
  }

  /**
   * Removes the style names(s) from the element.
   * 
   * @param className1 the first class name to remove
   * @param className2 the second class name to remove
   */
  public final void removeClassName(String className1, String className2) {
    removeClassName(className1);
    removeClassName(className2);
  }

  /**
   * Forces the Browser to repaint this element.
   */
  public final void repaint() {
    addClassName(CommonStyles.get().repaint());
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

      @Override
      public void execute() {
        removeClassName(CommonStyles.get().repaint());
      }
    });
  }

  /**
   * Replace one class name with another.
   * 
   * @param oldClassName the class name to be replaced
   * @param newClassName the class name to replace it
   * @param deep true to class names on child elements
   */
  public final void replaceClassName(String oldClassName, String newClassName, boolean deep) {
    removeClassName(oldClassName);
    addClassName(newClassName);
    NodeList<Element> nodes = select("." + oldClassName);
    for (int i = 0; i < nodes.getLength(); i++) {
      nodes.getItem(i).replaceClassName(oldClassName, newClassName);
    }
  }

  /**
   * Restores any elements made visible with {@link #ensureVisible()}.
   * 
   * @param list the meta data list
   */
  public final void restoreVisible(List<FastMap<Object>> list) {
    if (list != null) {
      for (FastMap<Object> m : list) {
        Element e = (Element) m.get("element");

        Boolean offset = (Boolean) m.get("hasxhideoffset");
        if (offset != null) {
          if (!offset.booleanValue()) {
            e.removeClassName(CommonStyles.get().hideOffsets());
          }
        }

        Boolean display = (Boolean) m.get("hasxhidedisplay");
        if (display != null) {
          if (display.booleanValue()) {
            e.addClassName(CommonStyles.get().hideDisplay());
          }
        }

        e.getStyle().setProperty("display", (String) m.get("origd"));
      }
    }
  }

  /**
   * Scrolls the element into view.
   * 
   * @param container the container element
   * @param hscroll <code>false</code> to disable horizontal scrolling.
   */
  public final void scrollIntoView(Element container, boolean hscroll) {
    scrollIntoView(container, hscroll, 0, 0);
  }

  /**
   * Scrolls the element into view.
   * 
   * @param container the container element
   * @param hscroll <code>false</code> to disable horizontal scrolling.
   * @param offsetX X offset
   * @param offsetY Y offset
   */
  public final void scrollIntoView(Element container, boolean hscroll, int offsetX, int offsetY) {
    Element c = container != null ? container : Document.get().getBody();

    Point o = getOffsetsTo(c);
    int l = o.getX();
    int t = o.getY();
    l = l + c.getScrollLeft();
    t = t + c.getScrollTop();
    int b = t + getOffsetHeight() + offsetY;
    int r = l + getOffsetWidth() + offsetX;

    int ch = c.getClientHeight();
    int ct = c.getScrollTop();
    int cb = ct + ch;

    if (getOffsetHeight() > ch || t < ct) {
      c.setScrollTop(t);
    } else if (b > cb) {
      c.setScrollTop(b - ch);
    }

    if (hscroll) {
      int cl = c.getScrollLeft();
      int cw = c.getClientWidth();
      int cr = cl + cw;

      if (getOffsetWidth() > cw || l < cl) {
        c.setScrollLeft(l);
      } else if (r > cr) {
        c.setScrollLeft(r - cw);
      }
    }
  }

  /**
   * Scrolls this element the specified scroll point.
   * 
   * @param side the scroll direction
   * @param value the new scroll value
   */
  public final void scrollTo(ScrollDirection side, int value) {
    if (side == ScrollDirection.LEFT) {
      setScrollLeft(value);
    } else {
      setScrollTop(value);
    }
  }

  /**
   * Selects child nodes based on the passed CSS selector (the selector should not contain an id).
   * 
   * @param selector the selector/xpath query
   * @return the matching elements
   */
  public final NodeList<Element> select(String selector) {
    return DomQuery.select(selector, this);
  }

  /**
   * Selects a single element.
   * 
   * @param selector the CSS selector
   * @return the matching element or null if no match
   */
  public final XElement selectNode(String selector) {
    Element el = DomQuery.selectNode(selector, this);
    if (el != null) {
      return XElement.as(el);
    }
    return null;
  }

  /**
   * Sets the attribute, determined by it names, using the given name space and value.
   * 
   * @param nameSpace the name space of the attribute
   * @param name the attribute name
   * @param value the value of the attribute
   */
  public final native void setAttributeNS(String nameSpace, String name, String value)/*-{
    this.setAttributeNS(nameSpace, name, value);
  }-*/;

  /**
   * Adds or removes a border.
   * 
   * @param show the show state
   */
  public final void setBorders(boolean show) {
    if (show) {
      addClassName(ThemeStyles.get().style().border());
      getStyle().setBorderWidth(1, Unit.PX);
    } else {
      removeClassName(ThemeStyles.get().style().border());
      getStyle().setBorderWidth(0, Unit.PX);
    }
  }

  /**
   * Sets the element's bounds.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   * @param width the new width
   * @param height the new height
   */
  public final void setBounds(int x, int y, int width, int height) {
    setBounds(x, y, width, height, false);
  }

  /**
   * Sets the element's bounds.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   * @param width the new width
   * @param height the new height
   * @param adjust true to adjust for box model issues
   */
  public final void setBounds(int x, int y, int width, int height, boolean adjust) {
    setXY(x, y);
    setSize(width, height, adjust);
  }

  /**
   * Sets the element's bounds.
   * 
   * @param bounds the new bounds
   */
  public final void setBounds(Rectangle bounds) {
    setBounds(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
  }

  /**
   * Sets the element's bounds.
   * 
   * @param bounds the new bounds
   * @param content <code>true</code> to adjust for box model issues
   */
  public final void setBounds(Rectangle bounds, boolean content) {
    setBounds(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), content);
  }

  /**
   * Adds or removes the class name.
   * 
   * @param cls the style name
   * @param add true to add, false to remove
   */
  public final void setClassName(String cls, boolean add) {
    if (add) {
      addClassName(cls);
    } else {
      removeClassName(cls);
    }
  }

  /**
   * Sets the CSS display property.
   * 
   * @param display true to display the element using its default display
   */
  public final void setDisplayed(boolean display) {
    getStyle().setDisplay(display ? Display.BLOCK : Display.NONE);
    if (getLayer() != null) {
      if (display) {
        getLayer().sync(true);
      } else {
        getLayer().hideUnders();
      }
    }
  }

  /**
   * Sets the elements height.
   * 
   * @param height the height
   */
  public final void setHeight(int height) {
    setHeight(height, false);
  }

  /**
   * Sets the elements height.
   * 
   * @param height the height
   * @param adjust <code>true</code> to adjust for box model issues
   */
  public final void setHeight(int height, boolean adjust) {
    if (adjust && !isBorderBox()) {
      height -= getFrameWidth(Side.TOP, Side.BOTTOM);
    }
    if (height >= 0) {
      getStyle().setPropertyPx("height", height);
    }
  }

  /**
   * Sets the elements height.
   * 
   * @param height the height
   */
  public final void setHeight(String height) {
    getStyle().setProperty("height", addUnits(height, "px"));
  }

  /**
   * Sets the element's left position directly using CSS style (instead of {@link #setX}).
   * 
   * @param left the left value in pixels
   */
  public final void setLeft(int left) {
    getStyle().setLeft(left, Unit.PX);
    if (getLayer() != null) {
      getLayer().sync(false);
    }
  }

  /**
   * Quick set left and top adding default units.
   * 
   * @param left the left value
   * @param top the top value
   */
  public final void setLeftTop(int left, int top) {
    setLeft(left);
    setTop(top);
  }

  /**
   * Sets the elements's margin.
   * 
   * @param margin the margin
   */
  public final void setMargins(int margin) {
    setMargins(new Margins(margin));
  }

  /**
   * Sets the elements's margin.
   * 
   * @param margin the margin
   */
  public final void setMargins(Margins margin) {
    if (margin != null) {
      getStyle().setMarginLeft(margin.getLeft(), Unit.PX);
      getStyle().setMarginTop(margin.getTop(), Unit.PX);
      getStyle().setMarginRight(margin.getRight(), Unit.PX);
      getStyle().setMarginBottom(margin.getBottom(), Unit.PX);
    }
  }

  /**
   * Cross-browser support for setting opacity.
   * 
   * @param opacity the opacity
   */
  public final void setOpacity(double opacity) {
    FieldHolder.computedStyle.setStyleAttribute(this, "opacity", opacity);
  }

  /**
   * Sets the elements's padding.
   * 
   * @param padding the padding
   */
  public final void setPadding(Padding padding) {
    if (padding != null) {
      getStyle().setPaddingLeft(padding.getLeft(), Unit.PX);
      getStyle().setPaddingTop(padding.getTop(), Unit.PX);
      getStyle().setPaddingRight(padding.getRight(), Unit.PX);
      getStyle().setPaddingBottom(padding.getBottom(), Unit.PX);
    }
  }

  /**
   * Sets the element's size.
   * 
   * @param width the new width
   * @param height the new height
   */
  public final void setSize(int width, int height) {
    setSize(width, height, false);
  }

  /**
   * Set the size of the element.
   * 
   * @param width the new width
   * @param height the new height
   * @param adjust <code>true</code> to adjust for box model issues
   */
  public final void setSize(int width, int height, boolean adjust) {
    if (adjust && !isBorderBox()) {
      Size frameWidth = getFrameSize();
      width -= frameWidth.getWidth();
      height -= frameWidth.getHeight();
    }
    if (width >= 0) {
      getStyle().setPropertyPx("width", width);
    }
    if (height >= 0) {
      getStyle().setPropertyPx("height", height);
    }
    if (getLayer() != null) {
      getLayer().sync(false);
    }
  }

  /**
   * Sets the element's size.
   * 
   * @param size the size
   */
  public final void setSize(Size size) {
    setSize(size.getWidth(), size.getHeight());
  }

  /**
   * Sets the element's top position directly using CSS style (instead of {@link #setY}).
   * 
   * @param top the top value in pixels
   */
  public final void setTop(int top) {
    getStyle().setTop(top, Unit.PX);
    if (getLayer() != null) {
      getLayer().sync(false);
    }
  }

  /**
   * Sets the elements CSS visibility property.
   * 
   * @param visible true for visible
   */
  public final void setVisibility(boolean visible) {
    getStyle().setVisibility(visible ? Visibility.VISIBLE : Visibility.HIDDEN);
  }

  /**
   * Sets the visibility of the element using the {@link #getVisMode()}.
   * 
   * @param visible whether the element is visible
   */
  public final void setVisible(boolean visible) {
    if (getVisMode() == null || getVisMode() == VisMode.DISPLAY) {
      setDisplayed(visible);
    } else {
      setVisibility(visible);
    }
  }

  /**
   * Sets the vis mode (defaults to {@link VisMode#DISPLAY}.
   * 
   * @param visMode the vis mode
   */
  public final native void setVisMode(VisMode visMode) /*-{
    this.visMode = visMode;
  }-*/;

  /**
   * Sets the element's width.
   * 
   * @param width the new width
   */
  public final void setWidth(int width) {
    setWidth(width, false);
  }

  /**
   * Sets the elements's width.
   * 
   * @param width the new width
   * @param adjust <code>true</code> to adjust for box model issues
   */
  public final void setWidth(int width, boolean adjust) {
    if (adjust && !isBorderBox()) {
      width -= getFrameWidth(Side.LEFT, Side.RIGHT);
    }
    if (width >= 0) {
      getStyle().setPropertyPx("width", width);
    }
  }

  /**
   * Sets the elements width.
   * 
   * @param width the width
   */
  public final void setWidth(String width) {
    getStyle().setProperty("width", addUnits(width, "px"));
  }

  /**
   * Sets the X position of the element based on page coordinates. Element must be part of the DOM tree to have page
   * coordinates.
   * 
   * @param x the x coordinate
   */
  public final void setX(int x) {
    setXY(x, Style.DEFAULT);
  }

  /**
   * Sets the elements position in page coordinates.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public final void setXY(int x, int y) {
    setXY(new Point(x, y));
  }

  /**
   * Sets the element's position in page coordinates.
   * 
   * @param p the position
   */
  public final void setXY(Point p) {
    makePositionable();
    Point pts = translatePoints(p);
    if (p.getX() != Style.DEFAULT) {
      setLeft(pts.getX());
    }
    if (p.getY() != Style.DEFAULT) {
      setTop(pts.getY());
    }
  }

  /**
   * Sets the Y position of the element based on page coordinates. Element must be part of the DOM tree to have page
   * coordinates.
   * 
   * @param y the y coordinate
   */
  public final void setY(int y) {
    setXY(Style.DEFAULT, y);
  }

  /**
   * Sets the element's z-index.
   * 
   * @param zIndex the z-index value
   */
  public final void setZIndex(int zIndex) {
    DOM.setIntStyleAttribute(this, "zIndex", Math.max(0, zIndex));
  }

  /**
   * Shows this element.
   */
  public final void show() {
    setVisible(true);
  }

  /**
   * Replace one class name with another.
   * 
   * @param oldClassName the class name to be replaced, empty strings allowed
   * @param newClassName the class name to replace it, empty strings allowed
   */
  public final void swapClassName(String oldClassName, String newClassName) {
    if (!oldClassName.equals("")) {
      removeClassName(oldClassName);
    }
    if (!newClassName.equals("")) {
      addClassName(newClassName);
    }
  }

  /**
   * Translates the passed page coordinates into left/top css values for this element.
   * 
   * @param p the coordinates
   * @return the translated coordinates
   */
  public final Point translatePoints(Point p) {
    List<String> list = new ArrayList<String>(3);
    list.add("position");
    list.add("left");
    list.add("top");

    Map<String, String> map = getComputedStyle(list);
    boolean relative = "relative".equals(map.get("position"));
    int l = Util.parseInt(map.get("left"), -11234);
    int t = Util.parseInt(map.get("top"), -11234);

    l = l != -11234 ? l : (relative ? 0 : getOffsetLeft());
    t = t != -11234 ? t : (relative ? 0 : getOffsetTop());

    Point o = getXY();
    return new Point(p.getX() - o.getX() + l, p.getY() - o.getY() + t);
  }

  /**
   * Removes a previously applied mask.
   */
  public final void unmask() {
    Mask.unmask(this);
  }

  /**
   * Unwraps the child element.
   * 
   * @param bounds the original bounds
   */
  public final void unwrap(XElement child, Rectangle bounds) {
    child.setLeftTop(bounds.getX(), bounds.getY());
    XElement p = getParentElement().cast();
    int pos = p.getChildIndex(this);
    p.insertChild(child, pos);
    p.removeChild(this);
  }

  /**
   * Sets the element's z-index using {@link XDOM#getTopZIndex()} to ensure it has the highest values.
   * 
   * @param adj the adjustment to be applied to the z-index value
   */
  public final void updateZIndex(int adj) {
    getStyle().setZIndex(XDOM.getTopZIndex() + adj);
  }

  /**
   * Wraps the element with the specified wrapper. The wrapper will have the same size and position of the element. The
   * original bounds can be used to 'unwrap' the element.
   * 
   * @param wrapper the wrapper element
   * @return the original bounds
   */
  public final Rectangle wrap(Element wrapper) {
    XElement wrap = XElement.as(wrapper);
    wrap.setVisible(false);

    String pos = getStyle().getPosition();
    wrap.getStyle().setProperty("position", pos);

    int l = getLeft();
    int t = getTop();

    setLeft(5000);
    setVisible(true);

    int h = getComputedHeight();
    int w = getComputedWidth();

    setLeft(1);
    getStyle().setOverflow(Overflow.HIDDEN);
    setVisible(false);

    wrap.insertBefore(this);
    wrap.appendChild(this);

    wrap.getStyle().setOverflow(Overflow.HIDDEN);

    wrap.setLeft(l);
    wrap.setTop(t);

    setTop(0);
    setLeft(0);

    return new Rectangle(l, t, w, h);
  }

  protected final Point getConstrainToXY(Element elem, Point proposedXY) {
    int vw, vh, vx = 0, vy = 0;
    if (elem == Document.get().getBody()) {
      vw = XDOM.getViewportSize().getWidth();
      vh = XDOM.getViewportSize().getHeight();
    } else {
      vw = elem.getOffsetWidth();
      vh = elem.getOffsetHeight();
      vx = elem.getAbsoluteLeft();
      vy = elem.getAbsoluteTop();
    }

    Point xy = proposedXY;
    int x = xy.getX();
    int y = xy.getY();

    int vr = vx + vw;
    int vb = vy + vh;

    int w = getOffsetWidth();
    int h = getOffsetHeight();

    if ((x + w) > vr) {
      x = vr - w;
    }
    if ((y + h) > vb) {
      y = vb - h;

    }

    // then make sure top/left isn't negative
    if (x < vx) {
      x = vx;
    }
    if (y < vy) {
      y = vy;
    }

    return new Point(x, y);
  }

  final native Layer getLayer() /*-{
    return this.layer;
  }-*/;

  final native void setLayer(Layer layer) /*-{
    this.layer = layer;
  }-*/;

  private void appendSideToList(List<String> list, Side side, String prop) {
    String s = side.toString();
    if (prop != null) {
      String temp = prop + s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
      if (prop.equals("border")) {
        temp += "Width";
      }
      list.add(temp);
    }
  }

  private int getStylePropertyTotal(List<String> props) {
    int total = 0;
    FastMap<String> map = getComputedStyle(props);
    for (String s : map.keySet()) {
      total += Util.parseInt(map.get(s), 0);
    }
    return total;
  }

  private final boolean isLeftOrRight(String s) {
    return FieldHolder.leftOrRight.test(s);
  }

}
