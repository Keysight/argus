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

import java.io.Serializable;

import com.sencha.gxt.core.client.ValueProvider;

/**
 * Default implementation of the {@link FilterConfig} interface. Provides
 * a convenience method to set field and type in one operation.
 */
public class FilterConfigBean implements FilterConfig, Serializable {

  private String field;
  private String comparison;
  private String type;
  private String value;

  @Override
  public String getComparison() {
    return comparison;
  }

  @Override
  public String getField() {
    return field;
  }

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public void setComparison(String comparison) {
    this.comparison = comparison;
  }

  @Override
  public void setField(String field) {
    this.field = field;
  }

  /**
   * Convenience method to set both field name and type in one operation.
   * 
   * @param valueProvider the value provider. The value provider's path supplies
   *          the field name.
   * @param type the field type. The class name supplies the field type.
   */
  public <V> void setFieldAndType(ValueProvider<?, V> valueProvider, Class<? extends V> type) {
    setField(valueProvider.getPath());
    setType(type.getName());
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public void setValue(String value) {
    this.value = value;
  }

}
