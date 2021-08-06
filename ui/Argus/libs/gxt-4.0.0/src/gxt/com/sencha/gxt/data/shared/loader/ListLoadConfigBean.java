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
import java.util.List;

import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;

/**
 * Default <code>ListLoadConfig</code> implementation.
 * 
 * @see ListLoadConfig
 */
@SuppressWarnings("serial")
public class ListLoadConfigBean implements ListLoadConfig {

  private List<SortInfoBean> sortInfo = new ArrayList<SortInfoBean>();

  /**
   * Create a new load config instance.
   */
  public ListLoadConfigBean() {

  }

  /**
   * Create a new load config instance.
   */
  public ListLoadConfigBean(SortInfoBean info) {
    getSortInfo().add(info);
  }

  /**
   * Creates a new load config instance.
   * 
   * @param info the sort information
   */
  public ListLoadConfigBean(List<SortInfo> info) {
    setSortInfo(info);
  }

  @Override
  public List<SortInfoBean> getSortInfo() {
    return sortInfo;
  }

  @Override
  public void setSortInfo(List<? extends SortInfo> info) {
    sortInfo.clear();
    for (SortInfo i : info) {
      if (i instanceof SortInfoBean) {
        sortInfo.add((SortInfoBean) i);
      } else {
        sortInfo.add(new SortInfoBean(i.getSortField(), i.getSortDir()));
      }
    }
  }


}
