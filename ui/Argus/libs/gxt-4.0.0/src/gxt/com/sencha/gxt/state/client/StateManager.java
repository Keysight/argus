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

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sencha.gxt.core.client.GXT;

/**
 * This is the global state manager. In order for this class to be useful, it must be initialized with a provider when
 * your application initializes. By default, GXT initializes the StateManager with a CookieProvider. The provider can be
 * replaced as needed.
 * 
 * The Provider is treated as an asynchronous String persistence mechanism, as to be compatible with RPC,
 * RequestFactory, Cookies, and HTML5 storage. The StateManager then has tools to map these strings to and from
 * bean-like interfaces, using AutoBeans.
 */
public abstract class StateManager {

  private static final StateManager instance = GWT.create(StateManager.class);

  /**
   * Returns the singleton instance.
   * 
   * @return the state manager
   */
  public static StateManager get() {
    return instance;
  }

  private Provider provider;

  protected StateManager() {
    provider = new CookieProvider("/", null, null, GXT.isSecure());
  }

  private SimpleEventBus eventBus;

  /**
   * Clears the state bean.
   * 
   * @param name the bean name
   */
  public void clear(String name) {
    provider.clear(name);
  }

  /**
   * Returns a state bean. In order to support server side calls, the method returns its value asynchronously via a
   * callback.
   * 
   * @param <T> the state bean type
   * @param name the bean name
   * @param stateBeanType the state bean class
   * @param callback the callback
   */
  public <T> void get(String name, final Class<T> stateBeanType, final Callback<T, Throwable> callback) {
    provider.getValue(name, new Callback<String, Throwable>() {
      @Override
      public void onFailure(Throwable reason) {
        callback.onFailure(reason);
      }

      @Override
      public void onSuccess(String result) {
        if (result == null || result.length() == 0) {
          callback.onSuccess(getDefaultStateInstance(stateBeanType));
        } else {
          AutoBean<T> autoBean = AutoBeanCodex.decode(getStateBeanFactory(), stateBeanType, result);
          callback.onSuccess(autoBean.as());
        }
      }
    });
  }

  /**
   * Returns the default state instance.
   * 
   * @param <S> the state type
   * @param stateType the state class
   * @return the state bean
   */
  public <S> S getDefaultStateInstance(Class<S> stateType) {
    AutoBean<S> autoBean = getStateBeanFactory().create(stateType);
    assert autoBean != null : "Type " + stateType + " is not registered as a type that can be used for state";

    return autoBean.as();
  }

  /**
   * Returns the manager's state provider.
   * 
   * @return the provider
   */
  public Provider getProvider() {
    return provider;
  }

  /**
   * Sets a state bean.
   * 
   * @param <T> the state bean type
   * @param name the bean name
   * @param stateBean the state bean
   */
  public <T> void set(String name, T stateBean) {
    AutoBean<T> wrappedBean = AutoBeanUtils.<T, T> getAutoBean(stateBean);
    String value = AutoBeanCodex.encode(wrappedBean).getPayload();

    // TODO consider comparing the value with the default instance, and calling
    // clear instead

    provider.setValue(name, value);
  }

  /**
   * Sets the manager's state provider.
   * 
   * @param stateProvider the provider
   */
  public void setProvider(Provider stateProvider) {
    provider = stateProvider;

    // TODO cancel outgoing save/loads? do we expect this to change on the fly?

    provider.bind(this);
  }

  /**
   * Return the state bean factory responsible for creating and decoding the state beans.
   * 
   * @return the state bean factory
   */
  protected abstract AutoBeanFactory getStateBeanFactory();

  SimpleEventBus ensureHandlers() {
    return eventBus == null ? eventBus = new SimpleEventBus() : eventBus;
  }
}
