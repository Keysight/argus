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

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.TreeStore.TreeNode;

/**
 * Event handler for the {@link LoadEvent} fired when a {@link Loader} has
 * finished loading data. It expects {@link LoadEvent#getLoadConfig} to return a
 * parent and {@link LoadEvent#getLoadResult} to return a list of new children.
 * It replaces the children of the given parent with the list of new children.
 * The children are expected to be of type {@link TreeNode}. For children of
 * type {@code <M>} see {@link ChildTreeStoreBinding}.
 * 
 * @param <M> the type of objects that populate the store
 * @param <T> the type of <code>TreeNode</code> for the store
 */
public class SubTreeStoreBinding<M, T extends TreeStore.TreeNode<M>> implements LoadHandler<M, List<T>> {
  private final TreeStore<M> store;

  /**
   * Creates a {@link LoadEvent} handler for the given {@link TreeStore}.
   * 
   * @param store the store whose events will be handled
   */
  public SubTreeStoreBinding(TreeStore<M> store) {
    this.store = store;
  }

  @Override
  public void onLoad(LoadEvent<M, List<T>> event) {
    M parent = event.getLoadConfig();

    store.replaceSubTree(parent, event.getLoadResult());
  }
}
