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
package com.sencha.gxt.data.client.loader;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.DataProxy;

/**
 * <code>DataProxy</code> implementation that retrieves data using GWT RPC.
 * 
 * @param <C> the type of data used to configure the load from the proxy
 * @param <D> the type of data being returned by the data proxy
 */
public abstract class RpcProxy<C, D> implements DataProxy<C, D> {

  /**
   * Retrieves data using GWT RPC.
   * 
   * @param loadConfig the load config describing the data to retrieve
   * @param callback the callback to invoke on success or failure
   */
  public abstract void load(C loadConfig, AsyncCallback<D> callback);

  @Override
  public final void load(C loadConfig, final Callback<D, Throwable> callback) {
    load(loadConfig, new AsyncCallback<D>() {
      @Override
      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }

      @Override
      public void onSuccess(D result) {
        callback.onSuccess(result);
      }
    });
  }
}
