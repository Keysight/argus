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
package com.sencha.gxt.data.client.writer;

import java.util.Collection;

import com.google.gwt.http.client.URL;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.AutoBeanVisitor;
import com.sencha.gxt.data.shared.writer.AutoBeanWriter;

/**
 * Encodes an object into a format that can be used for a GET query string or a
 * POST url-encoded body.
 * 
 * @param <M> the starting data format for the model to be inputed
 */
public class UrlEncodingWriter<M> extends AutoBeanWriter<M, String> {

  /**
   * A URL Encoding Appender provides support for appending a field and value to
   * a URL encoded value. Instances of this type can be passed to
   * {@link #appendModel}.
   */
  public interface UrlEncodingAppender {

    /**
     * Append the given field and value to an encapsulated URL encoded value,
     * creating or updating it as necessary.
     * 
     * @param field the field to append
     * @param value the value to append
     * @return the URL encoding appender
     */
    UrlEncodingAppender append(String field, String value);
  }

  /**
   * Creates a new URL encoding writing that can format an object into a GET
   * query string or POST url-encoded body.
   * 
   * @param factory an auto bean factory that can decode objects of type M
   * @param clazz the class to write
   */
  public UrlEncodingWriter(AutoBeanFactory factory, Class<M> clazz) {
    super(factory, clazz);
  }

  /**
   * Appends all of the relevant pieces of data to the url data.
   * 
   * @param model the model
   * @param appender the url encoding appender
   */
  public void appendModel(M model, final UrlEncodingAppender appender) {
    AutoBean<M> autoBean = getAutoBean(model);

    autoBean.accept(new AutoBeanVisitor() {
      @Override
      public boolean visitCollectionProperty(String propertyName, AutoBean<Collection<?>> value,
          CollectionPropertyContext ctx) {
        if (value != null) {
          for (Object obj : value.as()) {
            AutoBean<?> subBean = AutoBeanUtils.getAutoBean(obj);
            subBean.accept(this);
          }
        }
        return false;
      }

      @Override
      public boolean visitValueProperty(String propertyName, Object value, PropertyContext ctx) {
        if (value != null) {
          appender.append(propertyName, value.toString());
        }
        return false;
      }
    });
  }

  @Override
  public String write(M model) {
    final StringBuilder sb = new StringBuilder();

    UrlEncodingAppender a = new UrlEncodingAppender() {
      boolean first = true;

      @Override
      public UrlEncodingAppender append(String field, String value) {
        if (!first) {
          sb.append("&");
        }
        first = false;
        sb.append(URL.encodeQueryString(field)).append("=").append(URL.encodeQueryString(value));
        return this;
      }
    };

    appendModel(model, a);

    return sb.toString();
  }

}
