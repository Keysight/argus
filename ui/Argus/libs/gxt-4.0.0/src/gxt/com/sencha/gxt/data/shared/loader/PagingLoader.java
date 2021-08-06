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

/**
 * A <code>ListLoader</code> subclass which adds support for paged data (offset,
 * limit, and total count).
 * 
 * @param <C> the type of data for the input object
 * @param <D> the type of data to be returned by the loader
 */
public class PagingLoader<C extends PagingLoadConfig, D extends PagingLoadResult<?>> extends ListLoader<C, D> {

  private int offset = 0;
  private int limit = 50;
  private int totalCount;

  /**
   * Creates a new paging loader instance.
   * 
   * @param proxy the data proxy
   */
  public PagingLoader(DataProxy<C, D> proxy) {
    super(proxy);
  }

  /**
   * Creates a new paging loader instance.
   * 
   * @param proxy the data proxy
   * @param reader the data reader
   */
  public <T> PagingLoader(DataProxy<C, T> proxy, DataReader<D, T> reader) {
    super(proxy, reader);
  }

  /**
   * Returns the current limit.
   * 
   * @return the current limit
   */
  public int getLimit() {
    return limit;
  }

  /**
   * Returns the offset of the first record.
   * 
   * @return the current offset
   */
  public int getOffset() {
    return offset;
  }

  /**
   * Returns the total number of models in the dataset as returned by the
   * server.
   * 
   * @return the number of models as passed from the server
   */
  public int getTotalCount() {
    return totalCount;
  }

  /**
   * Loads the data using the specified configuration.
   * 
   * @param offset the offset of the first record to return
   * @param limit the page size
   */
  public void load(int offset, int limit) {
    this.offset = offset;
    this.limit = limit;
    load();
  }

  /**
   * Sets the limit size.
   * 
   * @param limit the limit
   */
  public void setLimit(int limit) {
    this.limit = limit;
  }

  /**
   * Sets the offset.
   * 
   * @param offset the offset
   */
  public void setOffset(int offset) {
    this.offset = offset;
  }

  /**
   * Use the specified LoadConfig for all load calls,
   * {@link #setReuseLoadConfig(boolean)} will be set to true.
   */
  @Override
  public void useLoadConfig(C loadConfig) {
    super.useLoadConfig(loadConfig);

    offset = loadConfig.getOffset();
    limit = loadConfig.getLimit();
  }

  @SuppressWarnings("unchecked")
  @Override
  protected C newLoadConfig() {
    return (C) new PagingLoadConfigBean();
  }

  /**
   * Called when the remote data has been received.
   * <p/>
   * Any subclass that overrides <code>onLoadSuccess</code> should call
   * <code>super.onLoadSuccess</code> with the result to make sure the loader
   * and the result have consistent data.
   */
  @Override
  protected void onLoadSuccess(C loadConfig, D result) {
    totalCount = result.getTotalLength();
    offset = result.getOffset();
    fireEvent(new LoadEvent<C, D>(loadConfig, result));
  }

  @Override
  protected C prepareLoadConfig(C config) {
    config = super.prepareLoadConfig(config);
    config.setOffset(offset);
    config.setLimit(limit);
    return config;
  }

}
