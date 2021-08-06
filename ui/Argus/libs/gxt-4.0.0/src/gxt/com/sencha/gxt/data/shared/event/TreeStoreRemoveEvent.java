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
package com.sencha.gxt.data.shared.event;

import java.util.Collections;
import java.util.List;

/**
 * Indicates that an element that was visible has been removed from the Store.
 * Note that non-visible elements do not get events, as most display widgets are
 * not concerned with data which has been filtered out.
 * 
 * @param <M> the model type
 */
public class TreeStoreRemoveEvent<M> extends StoreRemoveEvent<M> {

  private M parent;
  private List<M> children;

  /**
   * Creates a new tree store remove event.
   * 
   * @param index the index of the removed item in parent
   * @param item the removed item
   * @param parent the parent of the removed item
   * @param children the children of the removed item
   */
  public TreeStoreRemoveEvent(int index, M item, M parent, List<M> children) {
    super(index, item);
    this.parent = parent;
    this.children = Collections.unmodifiableList(children);
  }

  /**
   * Returns the children of the removed item.
   * 
   * @return the children of the removed item
   */
  public List<M> getChildren() {
    return children;
  }

  /**
   * Returns the parent of the removed item.
   * 
   * @return the parent of the removed item
   */
  public M getParent() {
    return parent;
  }

}