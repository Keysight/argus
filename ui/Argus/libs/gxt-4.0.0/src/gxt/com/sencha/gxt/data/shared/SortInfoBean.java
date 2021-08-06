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
package com.sencha.gxt.data.shared;

import java.io.Serializable;

import com.sencha.gxt.core.client.ValueProvider;

/**
 * Aggregates sort field and sort direction.
 */
public class SortInfoBean implements Serializable, SortInfo {

  private String sortField;
  private SortDir sortDir = SortDir.ASC;

  /**
   * Creates a new sort field instance.
   */
  public SortInfoBean() {

  }

  /**
   * Creates a new sort info instance.
   * 
   * @param field the sort field
   * @param sortDir the sort direction
   */
  public SortInfoBean(String field, SortDir sortDir) {
    this.sortField = field;
    this.sortDir = sortDir;
  }

  /**
   * Creates a new sort info instance.
   * 
   * @param valueProvider the value provider for the sort field
   * @param sortDir the sort direction
   */
  public SortInfoBean(ValueProvider<?, ?> valueProvider, SortDir sortDir) {
    this.sortField = valueProvider.getPath();
    this.sortDir = sortDir;
  }

  /**
   * Returns the sort field.
   * 
   * @return the sort field
   */
  @Override
  public String getSortField() {
    return sortField;
  }

  /**
   * Sets the sort field.
   * 
   * @param sortField the sort field
   */
  public void setSortField(String sortField) {
    this.sortField = sortField;
  }

  /**
   * Returns the sort direction.
   * 
   * @return the sort direction
   */
  @Override
  public SortDir getSortDir() {
    return sortDir;
  }

  /**
   * Sets the sort direction.
   * 
   * @param sortDir the sort direction
   */
  public void setSortDir(SortDir sortDir) {
    this.sortDir = sortDir;
  }

}
