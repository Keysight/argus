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
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.shared.ExpandedHtmlSanitizer;

/**
 * A widget that displays a status message and icon, typically used in a tool
 * bar.
 */
public class Status extends Component implements HasText, HasHTML, HasIcon, HasSafeHtml {

  @SuppressWarnings("javadoc")
  public interface StatusAppearance {

    ImageResource getBusyIcon();

    XElement getHtmlElement(XElement parent);

    void onUpdateIcon(XElement parent, ImageResource icon);

    void render(SafeHtmlBuilder sb);

  }
  
  public interface BoxStatusAppearance extends StatusAppearance {
    
  }

  private final StatusAppearance appearance;

  private SafeHtml html = SafeHtmlUtils.EMPTY_SAFE_HTML;
  private ImageResource icon;

  /**
   * Creates a status component with the default appearance.
   */
  public Status() {
    this(GWT.<StatusAppearance> create(StatusAppearance.class));
  }

  /**
   * Creates a status component with the specified appearance.
   * 
   * @param appearance the appearance of the status widget.
   */
  public Status(StatusAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    appearance.render(sb);

    setElement((Element) XDOM.create(sb.toSafeHtml()));
  }

  /**
   * Clears the current status by removing the current icon and change the text.
   * 
   * @param text the new text value
   */
  public void clearStatus(String text) {
    setIcon(null);
    setText(text);
  }

  /**
   * Clears the current status by removing the current icon and change the html.
   *
   * @param html the new html value
   */
  public void clearStatus(SafeHtml html) {
    setIcon(null);
    setHTML(html);
  }

  public StatusAppearance getAppearance() {
    return appearance;
  }

  /**
   * Enables a busy icon and displays the given text.
   * 
   * @param text the text to display
   */
  public void setBusy(String text) {
    setIcon(appearance.getBusyIcon());
    setText(text);
  }

  /**
   * Enables a busy icon and displays the given html.
   *
   * @param html the html to display
   */
  public void setBusy(SafeHtml html) {
    setIcon(appearance.getBusyIcon());
    setHTML(html);
  }

  @Override
  public ImageResource getIcon() {
    return icon;
  }

  @Override
  public void setIcon(ImageResource icon) {
    this.icon = icon;
    appearance.onUpdateIcon(getElement(), icon);
  }

  /**
   * Returns the status text.
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
   * Sets the status text.
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
   * Returns the status html.
   *
   * @return the html
   */
  public SafeHtml getSafeHtml() {
    return html;
  }

  /**
   * Returns the status html.
   *
   * @return the html
   */
  @Override
  public String getHTML() {
    return html.asString();
  }

  /**
   * Sets the status html.
   *
   * @param html the html
   */
  @Override
  public void setHTML(SafeHtml html) {
    this.html = html;
    getAppearance().getHtmlElement(getElement()).setInnerSafeHtml(html);
  }

  /**
   * Sets the status html.
   *
   * Untrusted html will be sanitized before use to protect against XSS.
   *
   * @param html the html
   */
  @Override
  public void setHTML(String html) {
    setHTML(ExpandedHtmlSanitizer.sanitizeHtml(html));
  }

}
