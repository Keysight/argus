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
package com.sencha.gxt.data.shared.writer;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * An AutoBeanWriter that writes an auto bean into Json.
 * 
 * @param <M> the starting data format for the model to be inputed
 */
public class JsonWriter<M> extends AutoBeanWriter<M, String> {

  /**
   * Creates a new JSON writer for auto beans.
   * 
   * @param factory the auto bean factory
   * @param clazz the target class
   */
  public JsonWriter(AutoBeanFactory factory, Class<M> clazz) {
    super(factory, clazz);
  }

  public String write(M model) {
    if (model == null) {
      return "null";
    }
    AutoBean<M> autobean = getAutoBean(model);
    if (autobean == null) {
      throw new RuntimeException(
          "Could not serialize "
              + model.getClass()
              + " using Autobeans, it appears to not be backed by an autobean. You may need to implement your own DataWriter.");
    }
    return AutoBeanCodex.encode(autobean).getPayload();
  }
}
