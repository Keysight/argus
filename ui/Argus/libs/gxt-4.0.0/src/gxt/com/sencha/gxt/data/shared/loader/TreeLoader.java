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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Default implementation of the <code>TreeLoader</code> interface.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * <dd>{@link BeforeLoadEvent}</b></dd>
 * <dd>{@link LoadEvent}</b></dd>
 * <dd>{@link LoadExceptionEvent}</b></dd>
 * </dl>
 * 
 * 
 * @param <M> the model data type
 */
public class TreeLoader<M> extends Loader<M, List<M>> {

  private Set<M> children = new HashSet<M>();

  /**
   * Creates a new tree loader instance.
   * 
   * @param proxy the data reader
   */
  public TreeLoader(DataProxy<M, List<M>> proxy) {
    super(proxy);
  }

  /**
   * Creates a new tree loader instance.
   * 
   * @param proxy the data proxy
   * @param reader the data reader
   */
  public <T> TreeLoader(DataProxy<M, T> proxy, DataReader<List<M>, T> reader) {
    super(proxy, reader);
  }

  /**
   * Returns true if the model has children. This method allows tree based
   * components to determine if the expand icon should be displayed next to a
   * node.
   * 
   * @param parent the parent model
   * @return true if the model has children, otherwise false
   */
  public boolean hasChildren(M parent) {
    return false;
  }

  /**
   * Initiates a load request for the parent's children.
   * 
   * @param parent the parent
   * @return true if the load was requested
   */
  public boolean loadChildren(M parent) {
    if (children.contains(parent)) {
      return false;
    }
    children.add(parent);
    return load(parent);
  }

  @Override
  protected void onLoadFailure(M loadConfig, Throwable t) {
    children.remove(loadConfig);
    fireEvent(new LoadExceptionEvent<M>(loadConfig, t));
  }

  @Override
  protected void onLoadSuccess(M loadConfig, List<M> result) {
    children.remove(loadConfig);
    fireEvent(new LoadEvent<M, List<M>>(loadConfig, result));
  }

}
