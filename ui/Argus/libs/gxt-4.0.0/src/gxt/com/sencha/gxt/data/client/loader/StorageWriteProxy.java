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
import com.sencha.gxt.data.client.loader.StorageWriteProxy.Entry;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.writer.DataWriter;

/**
 * Proxy to push key/value string pairs into local html5 browser storage. Both
 * key and value must be populated prior to
 * 
 */
public class StorageWriteProxy<K, V> implements DataProxy<Entry<K, V>, Void> {

  /**
   * Defines a key / value pair.
   */
  public static class Entry<K, V> {
    private final K key;
    private final V value;

    public Entry(K key, V value) {
      this.key = key;
      this.value = value;
    }

    public K getKey() {
      return key;
    }

    public V getValue() {
      return value;
    }
  }

  private final Storage storage;
  private DataWriter<K, String> keyWriter;
  private DataWriter<V, String> valueWriter;

  /**
   * Creates a storage write proxy that saves a key and value to HTML5 browser
   * based storage.
   * 
   * @param session true to use session storage, false to use local storage
   */
  public StorageWriteProxy(boolean session) {
    this(session ? Storage.getSessionStorageIfSupported() : Storage.getLocalStorageIfSupported());
  }

  /**
   * Creates a storage write proxy that saves a key and value to the given HTML5
   * browser based storage.
   * 
   * @param storage the browser based storage
   */
  public StorageWriteProxy(Storage storage) {
    assert storage != null : "Storage may not be null";
    this.storage = storage;
  }

  /**
   * Returns the current {@link DataWriter} used for converting key instances
   * into Strings
   * 
   * @return the current data writer that converts key instances into strings
   */
  public DataWriter<K, String> getKeyWriter() {
    return keyWriter;
  }

  /**
   * Returns the current {@link DataWriter} used for converting value instances
   * into Strings
   * 
   * @return the current data writer that converts value instances into strings
   */
  public DataWriter<V, String> getValueWriter() {
    return valueWriter;
  }

  @Override
  public void load(Entry<K, V> data, Callback<Void, Throwable> callback) {
    storage.setItem(getEncodedKey(data.getKey()), getEncodedValue(data.getValue()));
    callback.onSuccess(null);
  }

  /**
   * Sets a writer to use for converting key objects into a string to use when
   * storing values.
   * 
   * @param keyWriter
   */
  public void setKeyWriter(DataWriter<K, String> keyWriter) {
    this.keyWriter = keyWriter;
  }

  /**
   * Sets a writer to use for converting value objects into a string to be
   * stored.
   * 
   * @param valueWriter
   */
  public void setValueWriter(DataWriter<V, String> valueWriter) {
    this.valueWriter = valueWriter;
  }

  protected String getEncodedKey(K key) {
    if (keyWriter == null) {
      return key.toString();
    } else {
      return keyWriter.write(key);
    }
  }

  protected String getEncodedValue(V value) {
    if (valueWriter == null) {
      return value.toString();
    } else {
      return valueWriter.write(value);
    }
  }
}
