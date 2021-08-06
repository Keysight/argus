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
package com.sencha.gxt.core.client.resources;

import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.client.CssResource;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility methods for injecting styles. These methods, like those in {@link StyleInjector}, bypass the use of {@link
 * com.google.gwt.resources.client.CssResource#ensureInjected()} to allow for immediate injection. These methods will
 * inject CssResources only once as long as all injections are performed using this helper class.
 */
public class StyleInjectorHelper {

  private static Set<Class<?>> injected = new HashSet<Class<?>>();

  /**
   * Ensures that the given CssResource is injected into the document. If the given CssResource has already been
   * injected into the document, this method does nothing.
   *
   * @param style       The CssResource to be injected
   * @param immediately Whether to inject the the CssResource immediately. See {@link StyleInjector#inject(boolean)}.
   * @return {@code true} if the CssResource has been injected successfully, {@code false} otherwise. If the style
   *         resource has already been injected, this method returns {@code false}.
   */
  public static boolean ensureInjected(CssResource style, boolean immediately) {
    if (!injected.contains(style.getClass())) {
      injected.add(style.getClass());
      StyleInjector.inject(style.getText(), immediately);
      return true;
    }
    return false;
  }

  /**
   * Determines whether the current CssResource has already been injected by this {@code StyleInjectorHelper}.
   *
   * @param style The CssResource for which to determine whether injection has occurred
   * @return {@code true} if the CssResource has been injected, {@code false} otherwise.
   */
  public static boolean isInjected(CssResource style) {
    return injected.contains(style.getClass());
  }

}
