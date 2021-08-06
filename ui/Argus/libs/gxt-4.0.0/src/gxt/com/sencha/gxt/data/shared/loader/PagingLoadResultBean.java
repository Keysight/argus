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

/**
 * A {@link ListLoadResultBean} that adds support for paging properties as
 * described by {@link PagingLoadResult}.
 * 
 * @param <Data> the type of data for this list load result
 */
public class PagingLoadResultBean<Data> extends ListLoadResultBean<Data> implements PagingLoadResult<Data> {

  private int totalLength;
  private int offset;

  /**
   * Creates an empty paging load result bean.
   */
  public PagingLoadResultBean() {

  }

  /**
   * Creates a new paging list load result.
   * 
   * @param list the data
   * @param totalLength the total length
   * @param offset the paging offset
   */
  public PagingLoadResultBean(List<Data> list, int totalLength, int offset) {
    super(list);
    this.totalLength = totalLength;
    this.offset = offset;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public int getTotalLength() {
    return totalLength;
  }

  @Override
  public void setOffset(int offset) {
    this.offset = offset;
  }

  @Override
  public void setTotalLength(int totalLength) {
    this.totalLength = totalLength;
  }

}
