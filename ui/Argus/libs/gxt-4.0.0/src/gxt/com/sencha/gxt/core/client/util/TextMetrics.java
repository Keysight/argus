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
package com.sencha.gxt.core.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.dom.XElement;

/**
 * Provides precise pixel measurements for blocks of text so that you can determine exactly how high and wide, in
 * pixels, a given block of text will be.
 */
public class TextMetrics {

  private static final TextMetrics instance = GWT.create(TextMetrics.class);

  /**
   * Returns the singleton instance.
   *
   * @return the text metrics instance
   */
  public static TextMetrics get() {
    return instance;
  }

  private XElement el;
  private List<String> styleNames;

  private TextMetrics() {
    el = XElement.createElement("div");
    Document.get().getBody().appendChild(el);
    el.makePositionable(true);
    el.setLeftTop(-10000, -10000);
    el.getStyle().setVisibility(Visibility.HIDDEN);

    styleNames = new ArrayList<String>();
    styleNames.add("fontSize");
    styleNames.add("fontWeight");
    styleNames.add("fontStyle");
    styleNames.add("fontFamily");
    styleNames.add("lineHeight");
    styleNames.add("textTransform");
    styleNames.add("letterSpacing");
  }

  /**
   * Binds this TextMetrics instance to an element from which to copy existing CSS styles that can affect the size of
   * the rendered text.
   *
   * @param el the element
   */
  public void bind(Element el) {
    bind(XElement.as(el));
  }

  /**
   * Binds the TextMetrics instance using the styles from the given class name.
   *
   * @param className the class name
   */
  public void bind(String className) {
    clearStyles();

    this.el.setClassName(className);
  }

  /**
   * Binds this TextMetrics instance to an element from which to copy existing CSS styles that can affect the size of
   * the rendered text.
   *
   * @param el the element
   */
  public void bind(XElement el) {
    clearStyles();

    Map<String, String> map = el.getComputedStyle(styleNames);
    for (String key : map.keySet()) {
      String value = map.get(key);
      if (value == null) {
        value = "";
      }
      this.el.getStyle().setProperty(key, value);
    }
  }

  /**
   * Returns the measured height of the specified text. For multiline text, be sure to call {@link #setFixedWidth} if
   * necessary.
   *
   * @param text the text to be measured
   * @return the height in pixels
   */
  public int getHeight(String text) {
    return getSize(text).getHeight();
  }

  /**
   * Returns the measured height of the specified html. For multiline text, be sure to call {@link #setFixedWidth} if
   * necessary.
   *
   * @param html the text to be measured
   * @return the height in pixels
   */
  public int getHeight(SafeHtml html) {
    return getSize(html).getHeight();
  }

  /**
   * Returns the size of the specified text based on the internal element's style and width properties.
   *
   * @param text the text to measure
   * @return the size
   */
  public Size getSize(String text) {
    el.setInnerText(text);
    Size size = el.getSize();
    el.setInnerText("");
    return size;
  }

  /**
   * Returns the size of the specified text based on the internal element's style and width properties.
   *
   * @param html the html or text to measure
   * @return the size
   */
  public Size getSize(SafeHtml html) {
    Size size;
    el.setInnerSafeHtml(html);
      size = el.getSize();
    el.setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
    return size;
  }

  /**
   * Returns the measured width of the specified text.
   *
   * @param text the text to measure
   * @return the width in pixels
   */
  public int getWidth(String text) {
    el.getStyle().setProperty("width", "auto");
    return getSize(text).getWidth();
  }

  /**
   * Returns the measured width of the specified html.
   *
   * @param html the text to measure
   * @return the width in pixels
   */
  public int getWidth(SafeHtml html) {
    el.getStyle().setProperty("width", "auto");
    return getSize(html).getWidth();
  }

  /**
   * Sets a fixed width on the internal measurement element. If the text will be multiline, you have to set a fixed
   * width in order to accurately measure the text height.
   *
   * @param width the width to set on the element
   */
  public void setFixedWidth(int width) {
    el.setWidth(width);
  }

  private void clearStyles() {
    el.setClassName("");

    Map<String, String> map = el.getComputedStyle(styleNames);
    for (String key : map.keySet()) {
      el.getStyle().setProperty(key, "");
    }

    // needed sometimes to force a refresh
    el.repaint();
  }

}
