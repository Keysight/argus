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
package com.sencha.gxt.data.shared.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.data.shared.event.StoreAddEvent.HasStoreAddHandlers;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.data.shared.event.StoreClearEvent.HasStoreClearHandler;
import com.sencha.gxt.data.shared.event.StoreClearEvent.StoreClearHandler;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.HasStoreDataChangeHandlers;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.HasStoreFilterHandlers;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.HasStoreRecordChangeHandlers;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent.HasStoreRemoveHandler;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent.StoreRemoveHandler;
import com.sencha.gxt.data.shared.event.StoreSortEvent.HasStoreSortHandler;
import com.sencha.gxt.data.shared.event.StoreSortEvent.StoreSortHandler;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent.HasStoreUpdateHandlers;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent.StoreUpdateHandler;

/**
 * Handler interface for common store events.
 * 
 * @param <M> the type of data contained in the store
 */
public interface StoreHandlers<M> extends StoreAddHandler<M>, StoreRemoveHandler<M>, StoreFilterHandler<M>,
    StoreClearHandler<M>, StoreUpdateHandler<M>, StoreDataChangeHandler<M>, StoreRecordChangeHandler<M>,
    StoreSortHandler<M> {

  /**
   * A class that implements this interface is a public source of
   * common store events.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface HasStoreHandlers<M> extends HasStoreAddHandlers<M>, HasStoreRemoveHandler<M>,
      HasStoreUpdateHandlers<M>, HasStoreRecordChangeHandlers<M>, HasStoreFilterHandlers<M>, HasStoreClearHandler<M>,
      HasStoreDataChangeHandlers<M>, HasStoreSortHandler<M> {

    /**
     * Adds a common store event handler.
     * 
     * @param handlers the handlers
     * @return the registration for the event
     */
    HandlerRegistration addStoreHandlers(StoreHandlers<M> handlers);
  }
}