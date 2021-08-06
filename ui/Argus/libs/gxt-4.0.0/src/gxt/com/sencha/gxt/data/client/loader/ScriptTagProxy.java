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
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.writer.DataWriter;

/**
 * A {@link DataProxy} that reads a data from a URL which may be in a
 * domain other than the originating domain of the running page.
 * 
 * <p />
 * Note that if you are retrieving data from a page that is in a domain that is
 * NOT the same as the originating domain of the running page, you must use this
 * class, rather than HttpProxy.
 * 
 * <p />
 * When using a load config object that implements <code>LoadConfig</code> or
 * <code>ModelData</code>, all properties and property values will be sent as
 * request parameters in the load request.
 * 
 * 
 * @see HttpProxy
 */
public class ScriptTagProxy<C> implements DataProxy<C, JavaScriptObject> {

  private String url;

  private DataWriter<C, String> writer;

  /**
   * Creates a script tag proxy that reads data from a URL that may be in a
   * domain other than the originating domain of the running page.
   * 
   * @param url the URL representing the data to retrieve
   */
  public ScriptTagProxy(String url) {
    this.url = url;
  }

  /**
   * Returns the data writer for this proxy. The data writer is responsible for
   * encoding the load config.
   * 
   * @return the data writer
   */
  public DataWriter<C, String> getWriter() {
    return writer;
  }

  public void load(C loadConfig, final Callback<JavaScriptObject, Throwable> callback) {
    String prepend = url.indexOf("?") != -1 ? "&" : "?";
    String u = url + prepend + generateUrl(loadConfig);

    JsonpRequestBuilder b = new JsonpRequestBuilder();
    b.requestObject(u, new AsyncCallback<JavaScriptObject>() {
      @Override
      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }

      @Override
      public void onSuccess(JavaScriptObject result) {
        callback.onSuccess(result);
      }
    });
  }

  /**
   * Sets the proxy's url.
   * 
   * @param url the url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Sets the data writer for this proxy. The data writer is responsible for
   * encoding the load config.
   * 
   * @param writer the data writer
   */
  public void setWriter(DataWriter<C, String> writer) {
    this.writer = writer;
  }

  /**
   * Encodes the load config into a format that can be used for a GET query
   * string or a POST url-encoded body. Use {@link #setWriter} to set the data
   * writer responsible for encoding the load config. If it is not set the load
   * config is assumed to require no further encoding and its value is retrieved
   * using {@link Object#toString()}.
   * 
   * @param loadConfig the load config to encode
   * @return the encoded load config
   */
  protected String generateUrl(C loadConfig) {
    if (writer != null) {
      return writer.write(loadConfig);
    } else {
      if (loadConfig == null) {
        return "";
      }
      return loadConfig.toString();
    }
  }
}
