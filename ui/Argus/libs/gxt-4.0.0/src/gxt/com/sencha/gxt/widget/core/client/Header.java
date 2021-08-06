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
import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.shared.ExpandedHtmlSanitizer;
import com.sencha.gxt.core.client.util.Util;

/**
 * A custom widget that supports an icon, text, and tool area.
 */
public class Header extends Component implements HasIcon, HasText, HasHTML, HasSafeHtml {

  /**
   * The appearance of a header. A header has a bar that contains an icon and
   * text.
   */
  public interface HeaderAppearance {

    /**
     * Returns the bar element for the specified header.
     * 
     * @param parent the header root element
     * @return the bar element
     */
    XElement getBarElem(XElement parent);

    /**
     * Returns the heading element for the specified header.
     * 
     * @param parent the header root element
     * @return the heading element
     */
    XElement getHeadingElem(XElement parent);

    /**
     * Renders the appearance of a header as HTML into a {@link SafeHtmlBuilder}
     * suitable for passing to {@link Element#setInnerSafeHtml(SafeHtml)} on a
     * container element.
     * 
     * @param sb receives the rendered appearance
     */
    void render(SafeHtmlBuilder sb);

    /**
     * Sets the icon for the specified header.
     * 
     * @param parent the header root element
     * @param icon the icon to display in the header
     */
    void setIcon(XElement parent, ImageResource icon);

  }

  protected ImageResource icon;

  private List<Widget> tools = new ArrayList<Widget>();
  private HorizontalPanel widgetPanel;
  private SafeHtml heading = SafeHtmlUtils.EMPTY_SAFE_HTML;
  private String altIconText;
  private final HeaderAppearance appearance;

  /**
   * Creates a header with the default appearance which includes a header bar,
   * text and an icon.
   */
  public Header() {
    this(GWT.<HeaderAppearance> create(HeaderAppearance.class));
  }

  /**
   * Creates a header with the specified appearance.
   * 
   * @param appearance the appearance of the header
   */
  public Header(HeaderAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    this.appearance.render(sb);

    setElement((Element) XDOM.create(sb.toSafeHtml()));

    addStyleName("x-small-editor");

    widgetPanel = new HorizontalPanel();
    widgetPanel.addStyleName("x-panel-toolbar");

    XElement barElem = appearance.getBarElem(getElement());
    barElem.appendChild(widgetPanel.getElement());

    if (tools.size() > 0) {
      for (int i = 0; i < tools.size(); i++) {
        widgetPanel.add(tools.get(i));
      }
    } else {
      widgetPanel.setVisible(false);
    }

    ComponentHelper.setParent(this, widgetPanel);

    appearance.getHeadingElem(getElement()).setId(getId() + "-label");
      setHeading(heading);

    if (icon != null) {
      setIcon(icon);
    }

    getFocusSupport().setIgnore(true);
  }

  /**
   * Adds a tool.
   * 
   * @param tool the tool to be inserted
   */
  public void addTool(Widget tool) {
    insertTool(tool, getToolCount());
  }

  /**
   * Returns the appearance used to render the header.
   * 
   * @return the header appearance
   */
  public HeaderAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the icon image
   * 
   * @return the icon
   */
  public ImageResource getIcon() {
    return icon;
  }

  /**
   * Returns the icon's alt text.
   * 
   * @return the alt text
   */
  public String getIconAltText() {
    return altIconText;
  }

  /**
   * Returns the tool at the given index.
   * 
   * @param index the index
   * @return the tool
   */
  public Widget getTool(int index) {
    return tools.get(index);
  }

  /**
   * Returns the number of tool items.
   * 
   * @return the count
   */
  public int getToolCount() {
    return tools.size();
  }

  /**
   * Returns the tool's.
   * 
   * @return the tools
   */
  public List<Widget> getTools() {
    return Collections.unmodifiableList(tools);
  }

  /**
   * Returns the index of the given tool.
   * 
   * @param tool the tool
   * @return the index or -1 if no match
   */
  public int indexOf(Widget tool) {
    return tools.indexOf(tool);
  }

  /**
   * Inserts a tool.
   * 
   * @param tool the tool to insert
   * @param index the insert location
   */
  public void insertTool(Widget tool, int index) {
    tools.add(index, tool);
    widgetPanel.setVisible(true);
    widgetPanel.insert(tool, index);
  }

  /**
   * Removes a tool.
   * 
   * @param tool the tool to remove
   */
  public void removeTool(Widget tool) {
    tools.remove(tool);
    widgetPanel.remove(tool);
  }

  /**
   * Returns the heading html.
   *
   * @return the heading html
   */
  public SafeHtml getHeading() {
    return heading;
  }

  /**
   * Sets the heading html.
   *
   * @param html the heading html
   */
  public void setHeading(SafeHtml html) {
    this.heading = html;
    if (html == SafeHtmlUtils.EMPTY_SAFE_HTML) {
      getAppearance().getHeadingElem(getElement()).setInnerSafeHtml(Util.NBSP_SAFE_HTML);
    } else {
      getAppearance().getHeadingElem(getElement()).setInnerSafeHtml(html);
    }
  }

  /**
   * Sets the heading text.
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the text
   */
  public void setHeading(String text) {
    setHeading(SafeHtmlUtils.fromString(text));
  }

  /**
   * Returns the header's html.
   *
   * @return the html
   */
  public SafeHtml getSafeHtml() {
    return heading;
  }

  /**
   * Returns the header's html.
   *
   * @return the html
   */
  @Override
  public String getHTML() {
    return heading.asString();
  }

  /**
   * Sets the header's html.
   *
   * @param html the html
   */
  @Override
  public void setHTML(SafeHtml html) {
    setHeading(html);
  }

  /**
   * Sets the header's html.
   *
   * Untrusted html will be sanitized before use to protect against XSS.
   *
   * @param html the html
   */
  @Override
  public void setHTML(String html) {
    setHeading(ExpandedHtmlSanitizer.sanitizeHtml(html));
  }

  /**
   * Returns the header's text.
   *
   * If text was set that contained reserved html characters, the return value will be html escaped.
   * If html was set instead, the return value will be html.
   *
   * @return the text or html, depending on what was set
   * @see #getHTML()
   */
  public String getText() {
    return heading.asString();
  }

  /**
   * Sets the header's text.
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the text
   */
  @Override
  public void setText(String text) {
    setHeading(text);
  }

  @Override
  public void setIcon(ImageResource icon) {
    this.icon = icon;
    appearance.setIcon(getElement(), icon);
  }

  /**
   * Sets the header's icon alt text (defaults to null).
   * 
   * @param altIconText the icon alt text
   */
  public void setIconAltText(String altIconText) {
    this.altIconText = altIconText;
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(widgetPanel);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(widgetPanel);
  }

}
