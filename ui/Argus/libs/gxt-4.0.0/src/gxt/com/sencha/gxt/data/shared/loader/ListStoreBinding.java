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

import java.util.List;

import com.sencha.gxt.data.shared.ListStore;

/**
 * Event handler for the {@link LoadEvent} fired when a {@link Loader} has
 * finished loading data. This handler takes a {@link ListStore} and uses the
 * List provided by the Loader to re-populate the store.
 * 
 * @param <C> load config object type
 * @param <M> model objects that populate the store
 * @param <D> collection passed from the loader
 */
public class ListStoreBinding<C, M, D extends List<M>> implements LoadHandler<C, D> {
  private ListStore<M> listStore;

  /**
   * Creates a {@link LoadEvent} handler for the given {@link ListStore}. It
   * expects the load result to be a list, which it uses to re-populate the
   * store.
   * 
   * @param listStore the list store
   */
  public ListStoreBinding(ListStore<M> listStore) {
    this.listStore = listStore;
  }

  @Override
  public void onLoad(LoadEvent<C, D> event) {
    List<M> data = event.getLoadResult();

    listStore.replaceAll(data);
  }

}
