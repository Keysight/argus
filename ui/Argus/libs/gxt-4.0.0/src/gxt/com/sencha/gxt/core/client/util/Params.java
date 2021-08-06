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
package com.sencha.gxt.core.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sencha.gxt.core.shared.FastMap;

/**
 * Aggregates both a list of values and a map of named values. Allows methods to
 * support both list and maps in a single parameter.
 * <p>
 * Note that only one type of values should be specified.
 * </p>
 */
public class Params {

  private List<Object> values;
  private Map<String, Object> mapValues;

  /**
   * True if the parameters are a list of values.
   * 
   */
  private boolean isList = false;

  /**
   * True if the parameters are a map of key / value pairs.
   * 
   */
  private boolean isMap = false;

  /**
   * Creates a new params instance.
   */
  public Params() {

  }

  /**
   * Creates a new params instance.
   * 
   * @param values the initial values
   */
  public Params(Map<String, Object> values) {
    mapValues = values;
  }

  /**
   * Creates a new params instance.
   * 
   * @param values the initial values
   */
  public Params(Object... values) {
    for (int i = 0; i < values.length; i++) {
      add(values[i]);
    }
  }

  /**
   * Creates a new parameters instance.
   * 
   * @param key the key
   * @param value the value
   */
  public Params(String key, Object value) {
    mapValues = new FastMap<Object>();
    mapValues.put(key, value);
  }

  /**
   * Adds a value.
   * 
   * @param value the value to add
   * @return this
   */
  public Params add(Object value) {
    isList = true;
    if (values == null) values = new ArrayList<Object>();
    values.add(value);
    return this;
  }

  /**
   * Returns the list values.
   * 
   * @return the list values
   */
  public List<Object> getList() {
    return values;
  }

  /**
   * Returns the values as a map.
   * 
   * @return the map of values
   */
  public Map<String, Object> getMap() {
    return mapValues;
  }

  /**
   * Returns true if the parameters are a list.
   * 
   * @return true if a list
   */
  public boolean isList() {
    return isList;
  }

  /**
   * Returns true if the parameters are a map.
   * 
   * @return true if a map
   */
  public boolean isMap() {
    return isMap;
  }

  /**
   * Sets a value.
   * 
   * @param key the key
   * @param value the value
   * @return this
   */
  public Params set(String key, Object value) {
    isMap = true;
    if (value == null) return this;
    if (mapValues == null) {
      mapValues = new FastMap<Object>();
    }
    mapValues.put(key, value);
    return this;
  }

}
