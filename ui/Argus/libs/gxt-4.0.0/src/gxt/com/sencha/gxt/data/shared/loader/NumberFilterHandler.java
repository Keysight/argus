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
package com.sencha.gxt.data.shared.loader;

import com.google.gwt.text.shared.Parser;

/**
 * A {@link FilterHandler} that provides support for <code>Number</code> values
 * and uses a property editor to convert a string representation to a number.
 */
public class NumberFilterHandler<V extends Number> extends FilterHandler<V> {

  /**
   * The {@link Parser} (probably a <code>NumberPropertyEditor</code>) this 
   * <code>NumberFilterHandler</code> uses to perform the conversion.
   */
  protected Parser<V> propertyEditor;

  /**
   * Creates a number filter handler that uses the given property editor to
   * convert a string representation to a number.
   * 
   * @param propertyEditor the property editor to use to do the conversion
   */
  public NumberFilterHandler(Parser<V> propertyEditor) {
    this.propertyEditor = propertyEditor;
  }

  @Override
  public V convertToObject(String value) {
    try {
      return propertyEditor.parse(value);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String convertToString(V object) {
    return object.toString();
  }
}
