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

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Utility class for creating elements from HTML fragments.
 */
public class DomHelper {
  static {
    Ext.loadExt();
    Ext.loadDomHelper();
  }

  /**
   * Creates new DOM element(s) and appends them to el.
   * 
   * @param elem the context element
   * @param html the html
   * @return the new element
   */
  public static native Element append(Element elem, SafeHtml html) /*-{
    var Ext = @com.sencha.gxt.core.client.dom.Ext::ext;
		return Ext.DomHelper.append(elem, html.@com.google.gwt.safehtml.shared.SafeHtml::asString()(), false);
  }-*/;

  /**
   * Creates new DOM element(s) and inserts them after el.
   * 
   * @param elem the context element
   * @param html rthe html
   * @return the new element
   */
  public static native Element insertAfter(Element elem, SafeHtml html) /*-{
    var Ext = @com.sencha.gxt.core.client.dom.Ext::ext;
		return Ext.DomHelper.doInsert(elem, html.@com.google.gwt.safehtml.shared.SafeHtml::asString()(), false,
        "afterEnd", "nextSibling");
  }-*/;

  /**
   * Creates new DOM element(s) and inserts them before el.
   * 
   * @param elem the context element
   * @param html the html
   * @return the new element
   */
  public static native Element insertBefore(Element elem, SafeHtml html) /*-{
		var Ext = @com.sencha.gxt.core.client.dom.Ext::ext;
		return Ext.DomHelper.doInsert(elem, html.@com.google.gwt.safehtml.shared.SafeHtml::asString()(), false, "beforeBegin");
  }-*/;

  /**
   * Creates new DOM element(s) and inserts them as the first child of el.
   * 
   * @param elem the context element
   * @param html the html
   * @return the new element
   */
  public static native Element insertFirst(Element elem, SafeHtml html) /*-{
		var Ext = @com.sencha.gxt.core.client.dom.Ext::ext;
		return Ext.DomHelper.doInsert(elem, html.@com.google.gwt.safehtml.shared.SafeHtml::asString()(), false,
        "afterBegin", "firstChild");
  }-*/;

  /**
   * Inserts an HTML fragment into the DOM.
   * 
   * @param where where to insert the html in relation to el - beforeBegin,
   *          afterBegin, beforeEnd, afterEnd.
   * @param el the context element
   * @param html the html
   * @return the inserted node (or nearest related if more than 1 inserted)
   */
  public static native Element insertHtml(String where, Element el, SafeHtml html) /*-{
    var h = html.@com.google.gwt.safehtml.shared.SafeHtml::asString()();
    if (!h)
      return el;

    var Ext = @com.sencha.gxt.core.client.dom.Ext::ext;
    return Ext.DomHelper.insertHtml(where, el, h);
  }-*/;

  /**
   * Creates new DOM element(s) and overwrites the contents of el with them.
   * 
   * @param elem the context element
   * @param html the html
   * @return the first new element
   */
  public static native Element overwrite(Element elem, SafeHtml html) /*-{
    var Ext = @com.sencha.gxt.core.client.dom.Ext::ext;
		return Ext.DomHelper.overwrite(elem, html.@com.google.gwt.safehtml.shared.SafeHtml::asString()());
  }-*/;
  
}
