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
package com.sencha.gxt.core.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * Determines information about the current platform the application is running on.
 */
public class Supports {

  static {
    Element div = Document.get().createDivElement();
    
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    sb.appendHtmlConstant("<div style='height:30px; width:50px;'>");
    sb.appendHtmlConstant("<div style='height:20px; width:20px;'></div>");
    sb.appendHtmlConstant("</div>");
    sb.appendHtmlConstant("<div style='float:left; background-color:transparent;'></div>");
    
    div.setInnerSafeHtml(sb.toSafeHtml());

    css3BorderRadius = hasCss3BorderRadiusTest();
    css3LinearGradient = hasCss3LinearGradientInternal(div);
  }

  private static boolean css3BorderRadius;
  private static boolean css3LinearGradient;

  /**
   * Returns true if the device supports CSS3 linear gradients.
   * 
   * @return true for CSS3 linear gradients
   */
  public static boolean hasCss3BorderRadius() {
    return css3BorderRadius;
  }

  /**
   * Returns true if the device supports CSS3 border radius.
   * 
   * @return true for CSS3 border radius
   */
  public static boolean hasCss3LinearGradient() {
    return css3LinearGradient;
  }

  private static native boolean hasCss3BorderRadiusTest() /*-{
		var domPrefixes = [ 'borderRadius', 'BorderRadius', 'MozBorderRadius',
				'WebkitBorderRadius', 'OBorderRadius', 'KhtmlBorderRadius' ], pass = false, i;

		for (i = 0; i < domPrefixes.length; i++) {
			if (document.body.style[domPrefixes[i]] !== undefined) {
				return true;
			}
		}
		return pass;
  }-*/;

  private static native boolean hasCss3LinearGradientInternal(Element div) /*-{
		var property = 'background-image:', webkit = '-webkit-gradient(linear, left top, right bottom, from(black), to(white))', w3c = 'linear-gradient(left top, black, white)', moz = '-moz-'
				+ w3c, options = [ property + webkit, property + w3c,
				property + moz ];

		div.style.cssText = options.join(';');

		return ("" + div.style.backgroundImage).indexOf('gradient') !== -1;

  }-*/;
}
