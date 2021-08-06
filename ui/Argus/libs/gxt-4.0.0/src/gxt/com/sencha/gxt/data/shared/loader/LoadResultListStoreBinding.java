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

import com.sencha.gxt.data.shared.ListStore;

/**
 * Event handler for the {@link LoadEvent} fired when a {@link Loader} has
 * finished loading data. This handler takes a {@link ListStore} and uses the
 * contents of the {@link ListLoadResult} provided by the Loader to re-populate
 * the store.
 * 
 * @param <C> load config object type
 * @param <M> model objects that populate the store
 * @param <D> load result passed from the loader
 */
public class LoadResultListStoreBinding<C, M, D extends ListLoadResult<M>> implements LoadHandler<C, D> {
  private final ListStore<M> store;

  /**
   * Creates a load event handler that re-populates the given list store using
   * the list load result provided by the loader via the event.
   * 
   * @param store the list store
   */
  public LoadResultListStoreBinding(ListStore<M> store) {
    this.store = store;
  }

  @Override
  public void onLoad(LoadEvent<C, D> event) {
    ListLoadResult<M> loaded = event.getLoadResult();

    store.replaceAll(loaded.getData());
  }
}
