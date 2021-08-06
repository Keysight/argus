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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.sencha.gxt.data.shared.SortInfo;

/**
 * An abstract request factory proxy that supports processing results using
 * either a RPC style <code>Callback</code> or a <code>RequestFactory</code>
 * <code>Receiver</code>.
 * 
 * @param <C> the type of data used to configure the load from the proxy
 * @param <D> the type of data being returned by the data proxy
 */
public abstract class RequestFactoryProxy<C, D> implements DataProxy<C, D> {
  public final void load(C loadConfig, final Callback<D, Throwable> callback) {
    load(loadConfig, new Receiver<D>() {
      @Override
      public void onFailure(ServerFailure error) {
        callback.onFailure(new RuntimeException(error.getMessage()));
      }

      public void onSuccess(D response) {
        callback.onSuccess(response);
      }
    });
  }

  /**
   * Process results using a <code>Receiver</code>.
   * 
   * @param loadConfig the load config object to be passed to server
   * @param receiver the data callback
   */
  public abstract void load(C loadConfig, Receiver<? super D> receiver);
  
  /**
   * Helper method to translate a list of {@link SortInfo} instances to something that 
   * RequestFactory is able to send over the wire. Must be given the {@code RequestContext} 
   * instance to be used to send the request so the newly created object exists in the request.
   * 
   * @param request the request over which these items will be sent
   * @param original the list of {@code SortInfo} instances to copy
   * @return a list of {@code SortInfo} instances able to be sent over the given request
   */
  protected List<SortInfo> createRequestSortInfo(RequestContext request, List<? extends SortInfo> original) {
    List<SortInfo> sortInfo = new ArrayList<SortInfo>();
    
    for (int i = 0; i < original.size(); i++) {
      SortInfo originalSortInfo = original.get(i);
      SortInfo reqSortInfo = request.create(SortInfo.class);
      reqSortInfo.setSortDir(originalSortInfo.getSortDir());
      reqSortInfo.setSortField(originalSortInfo.getSortField());
      sortInfo.add(reqSortInfo);
    }
    
    return sortInfo;
  }
  
  /**
   * Helper method to translate a list of {@link FilterConfig} instances to something that 
   * RequestFactory is able to send over the wire. Must be given the {@code RequestContext} 
   * instance to be used to send the request so the newly created object exists in the request.
   * 
   * @param request the request over which these items will be sent
   * @param original the list of {@code FilterConfig} instances to copy
   * @return a list of {@code FilterConfig} instances able to be sent over the given request
   */
  protected List<FilterConfig> createRequestFilterConfig(RequestContext request, List<? extends FilterConfig> original) {
    List<FilterConfig> sortInfo = new ArrayList<FilterConfig>();
    
    for (int i = 0; i < original.size(); i++) {
      FilterConfig originalSortInfo = original.get(i);
      FilterConfig reqSortInfo = request.create(FilterConfig.class);
      
      reqSortInfo.setComparison(originalSortInfo.getComparison());
      reqSortInfo.setField(originalSortInfo.getField());
      reqSortInfo.setType(originalSortInfo.getType());
      reqSortInfo.setValue(originalSortInfo.getValue());
      
      sortInfo.add(reqSortInfo);
    }
    
    return sortInfo;
  }
}
