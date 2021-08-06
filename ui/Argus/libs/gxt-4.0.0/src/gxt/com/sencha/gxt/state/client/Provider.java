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
package com.sencha.gxt.state.client;

import com.google.gwt.core.client.Callback;

/**
 * Abstract base class for state provider implementations. Providers are stores
 * of String->String pairs, so that a given String key may be used to persist a
 * String value, and that same key later can be used to load that value again.
 * 
 * An empty string should be considered no value at all, so setValue(key, "") is
 * equivalent to setValue(key, null), as to clear(key).
 */
public abstract class Provider {

  protected StateManager manager = StateManager.get();

  /**
   * Clears the named value.
   * 
   * @param name the property name
   */
  public void clear(String name) {
    setValue(name, "");
  }

  /**
   * Returns the value asynchronously.
   * 
   * @param name the property name
   * @param callback the callback
   */
  public abstract void getValue(String name, Callback<String, Throwable> callback);

  /**
   * Sets the value.
   * 
   * @param name the property name
   * @param value the value
   */
  public abstract void setValue(String name, String value);

  protected void bind(StateManager manager) {
    this.manager = manager;
  }

}
