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

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * Abstract base class for Readers that turn the incoming data into
 * {@link AutoBean}s.
 * 
 * @param <M> Expected model type by the {@link Loader} that uses this
 *          {@link DataReader}
 * @param <B> Intermediate type before getting to M, allowing
 *          {@link #createReturnData(Object, Object)} to further modify the data
 * @param <D> Incoming data type from the {@link DataProxy}
 */
public abstract class AbstractAutoBeanReader<M, B, D> implements DataReader<M, D> {
  private final AutoBeanFactory factory;
  private final Class<B> rootType;

  /**
   * Creates a new loader.
   * 
   * @param factory AutoBeanFactory instance capable of building all of the
   *          required classes
   * @param rootBeanType AutoBean based type to represent the base data in the
   *          Splittable
   */
  public AbstractAutoBeanReader(AutoBeanFactory factory, Class<B> rootBeanType) {
    this.factory = factory;
    this.rootType = rootBeanType;
  }

  @Override
  public M read(Object loadConfig, D data) {
    Splittable s = readSplittable(loadConfig, data);

    AutoBean<B> result = AutoBeanCodex.decode(factory, rootType, s);
    return createReturnData(loadConfig, result.as());
  }

  /**
   * Responsible for the object being returned by the reader. Default
   * implementation casts right to the return type, but can be overridden to
   * provide a more specialized implementation.
   * 
   * @param loadConfig the load config
   * @param records the list of models
   * @return the data to be returned by the reader
   */
  @SuppressWarnings("unchecked")
  protected M createReturnData(Object loadConfig, B records) {
    return (M) records;
  }

  /**
   * Implemented by subclasses to provide a {@link Splittable} based on the
   * incoming data.
   * 
   * @param loadConfig the load config
   * @param data the data to read
   * @return the splittable instance
   */
  protected abstract Splittable readSplittable(Object loadConfig, D data);
}
