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
package com.sencha.gxt.data.client.loader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.web.bindery.autobean.gwt.client.impl.JsoSplittable;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.data.shared.loader.AbstractAutoBeanReader;
import com.sencha.gxt.data.shared.loader.DataReader;
import com.sencha.gxt.data.shared.loader.Loader;

/**
 * Simple {@link DataReader} implementation to turn JSOs into AutoBeans
 * automatically.
 * 
 * @param <M> Expected model type by the {@link Loader} that uses this
 *          {@link DataReader}
 * @param <Base> Intermediate type before getting to M, allowing
 *          {@link #createReturnData(Object, Object)} to further modify the data
 */
public class JsoReader<M, Base> extends AbstractAutoBeanReader<M, Base, JavaScriptObject> {

  /**
   * Creates a new JSO reader that can turn a JSO into an AutoBean.
   * 
   * @param factory an auto bean factory capable of encoding objects of type M
   * @param rootBeanType AutoBean based type to represent the base data
   */
  public JsoReader(AutoBeanFactory factory, Class<Base> rootBeanType) {
    super(factory, rootBeanType);
  }

  @Override
  protected Splittable readSplittable(Object loadConfig, JavaScriptObject data) {
    if (GWT.isScript()) {
      return data.<JsoSplittable> cast();
    } else {
      JSONObject json = new JSONObject(data);
      return StringQuoter.split(json.toString());
    }
  }

}
