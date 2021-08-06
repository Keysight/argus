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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sencha.gxt.data.shared.SortInfo;

/**
 * A <code>Loader</code> subclass which return a list of data. Adds support for
 * sort field, sort direction, and remote sorting.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * <dd>{@link BeforeLoadEvent}</b></dd>
 * <dd>{@link LoadEvent}</b></dd>
 * <dd>{@link LoadExceptionEvent}</b></dd>
 * </dl>
 * 
 * @param <C> the type of data for the input object
 * @param <D> the type of data to be returned by the loader
 */
public class ListLoader<C extends ListLoadConfig, D extends ListLoadResult<?>> extends Loader<C, D> {
  private boolean remoteSort;
  private final List<SortInfo> sortInfo = new ArrayList<SortInfo>();

  /**
   * Creates a new loader instance with the given proxy. A reader is not
   * specified and will not be passed to the proxy at load time.
   * 
   * @param proxy the data proxy
   */
  public ListLoader(DataProxy<C, D> proxy) {
    super(proxy);
  }

  /**
   * Creates a new loader instance.
   * 
   * @param proxy the data proxy
   * @param reader the data reader
   */
  public <T> ListLoader(DataProxy<C, T> proxy, DataReader<D, T> reader) {
    super(proxy, reader);
  }

  /**
   * Adds a new SortInfo object to the next ListLoadConfig in the indicated
   * position in the list. Can be useful to set the new sort as the first sort
   * to use by setting the index to 0.
   * 
   * @param index the position of the sort info in the list of sort info
   * @param sortInfo the sort info to add
   */
  public void addSortInfo(int index, SortInfo sortInfo) {
    this.sortInfo.add(index, sortInfo);
  }

  /**
   * Adds a new SortInfo object to the next ListLoadConfig to the end of the
   * list.
   * 
   * @param sortInfo the sort info to add
   */
  public void addSortInfo(SortInfo sortInfo) {
    this.sortInfo.add(sortInfo);
  }

  /**
   * Clears all of the currently set sort info objects
   */
  public void clearSortInfo() {
    this.sortInfo.clear();
  }

  /**
   * Gets the current set of SortInfo objects to be sent to the server. This
   * list should not be modified.
   * 
   * This method can be useful to override to modify the objects in the list to
   * a format that can go over the wire, such as for RequestFactory.
   * 
   * @return an immutable collection of the current sort settings
   */
  public List<? extends SortInfo> getSortInfo() {
    return Collections.unmodifiableList(sortInfo);
  }

  /**
   * Returns <code>true</code> if remote sorting is enabled.
   * 
   * @return the remote sort state
   */
  public boolean isRemoteSort() {
    return remoteSort;
  }

  /**
   * Removes the indicated SortInfo object from the list to send to the server,
   * if it was present in the list.
   * 
   * @param lastSort the sort info to remove
   */
  public void removeSortInfo(SortInfo lastSort) {
    sortInfo.remove(lastSort);
  }

  /**
   * Sets the remote sort state (defaults to false). When true, the loader will
   * make a new load request when the data needs to be sorted.
   * 
   * @param remoteSort true for remote sort, false for local sorting
   */
  public void setRemoteSort(boolean remoteSort) {
    this.remoteSort = remoteSort;
  }

  /**
   * Use the specified LoadConfig for all load calls,
   * {@link #setReuseLoadConfig(boolean)} will be set to true.
   */
  public void useLoadConfig(C loadConfig) {
    setReuseLoadConfig(true);
    setLastLoadConfig(loadConfig);

    sortInfo.clear();
    if (loadConfig.getSortInfo() != null) {
      sortInfo.addAll(loadConfig.getSortInfo());
    }
  }

  /**
   * Template method to allow custom BaseLoader subclasses to provide their own
   * implementation of LoadConfig
   */
  @SuppressWarnings("unchecked")
  protected C newLoadConfig() {
    return (C) new ListLoadConfigBean();
  }

  /**
   * Template method to allow custom subclasses to prepare the load config prior
   * to loading data
   */
  protected C prepareLoadConfig(C config) {
    config = super.prepareLoadConfig(config);
    config.setSortInfo(getSortInfo());
    return config;
  }

}
