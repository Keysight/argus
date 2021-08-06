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

import com.google.gwt.core.client.Callback;

/**
 * A <code>DataProxy</code> implementation that simply passes the data specified
 * in the constructor to the reader when its load method is called.
 */
public class MemoryProxy<C, D> implements DataProxy<C, D> {

  private D data;

  /**
   * Creates new memory proxy.
   * 
   * @param data the local data
   */
  public MemoryProxy(D data) {
    this.data = data;
  }

  /**
   * Returns the proxy data.
   * 
   * @return the data
   */
  public D getData() {
    return data;
  }

  @Override
  public void load(C loadConfig, Callback<D, Throwable> callback) {
    try {
      callback.onSuccess(data);
    } catch (Exception e) {
      callback.onFailure(e);
    }
  }

  /**
   * Sets the proxy data.
   * 
   * @param data the data
   */
  public void setData(D data) {
    this.data = data;
  }

}
