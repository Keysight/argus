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
package com.sencha.gxt.state.client;

import java.util.Date;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Cookies;

/**
 * The default <code>Provider</code> implementation which saves state via
 * cookies.
 */
public class CookieProvider extends Provider {

  private Date expires;
  private String path, domain;
  private boolean secure;

  /**
   * Creates a new cookie provider
   * 
   * @param path The path for which the cookie is active (defaults to root '/'
   *          which makes it active for all pages in the site)
   * @param expires the cookie expiration date (defaults to 7 days from now)
   * @param domain The domain to save the cookie for. Note that you cannot
   *          specify a different domain than your page is on, but you can
   *          specify a sub-domain.
   * @param secure <code>true</code> if the site is using SSL
   */
  public CookieProvider(String path, Date expires, String domain, boolean secure) {
    this.path = path == null ? "/" : path;
    this.secure = secure;
    this.domain = domain;
    if (expires == null) {
      expires = new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 7)); //7-
                                                                            // days
    }
    this.expires = expires;
  }

  @Override
  public void clear(String name) {
    Cookies.removeCookie(name);
  }

  @Override
  public void getValue(String name, Callback<String, Throwable> callback) {
    callback.onSuccess(Cookies.getCookie(name));
  }

  @Override
  public void setValue(String name, String value) {
    Cookies.setCookie(name, value, expires, domain, path, secure);
  }

  public void set(String name, String value, Date expires) {
    Cookies.setCookie(name, value, expires, domain, path, secure);
  }

}
