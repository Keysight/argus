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

import com.google.gwt.core.client.Callback;
import com.google.gwt.storage.client.Storage;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.writer.DataWriter;

/**
 * A {@link DataProxy} that retrieves data from HTML5 browser based storage.
 * 
 * @param <C> the type of load configuration
 */
public class StorageReadProxy<C> implements DataProxy<C, String> {
  private DataWriter<C, String> writer;
  private final Storage storage;

  /**
   * Creates a storage read proxy that retrieves data from HTML5 browser based
   * storage.
   * 
   * @param session true to use session storage, false to use local storage
   */
  public StorageReadProxy(boolean session) {
    this(session ? Storage.getSessionStorageIfSupported() : Storage.getLocalStorageIfSupported());
  }

  /**
   * Creates a storage read proxy that retrieves data from the given HTML5
   * browser based storage.
   * 
   * @param storage the browser based storage
   */
  public StorageReadProxy(Storage storage) {
    assert storage != null : "Storage may not be null";
    this.storage = storage;
  }
  
  public DataWriter<C, String> getWriter() {
    return writer;
  }
  
  public void setWriter(DataWriter<C, String> writer) {
    this.writer = writer;
  }

  @Override
  public void load(C loadConfig, Callback<String, Throwable> callback) {
    String key = getKeyFromConfig(loadConfig);
    callback.onSuccess(storage.getItem(key));
  }

  /**
   * Save a value in the HTML5 browser based storage.
   * 
   * @param saveConfig provides the source of the storage key (see
   *          {@link #getKeyFromConfig(Object)}).
   * @param data the value to save, associated with <code>saveConfig</code>
   */
  public void save(C saveConfig, String data) {
    String key = getKeyFromConfig(saveConfig);
    storage.setItem(key, data);
  }

  /**
   * Generates a storage key from the given load config. By default, this is the
   * value returned by the load config's <code>toString</code> method.
   * 
   * @param loadConfig the load config
   * @return the key that associates load config with its storage
   */
  protected String getKeyFromConfig(C loadConfig) {
    if (writer == null) {
      return loadConfig.toString();
    } else {
      return writer.write(loadConfig);
    }
  }

}
