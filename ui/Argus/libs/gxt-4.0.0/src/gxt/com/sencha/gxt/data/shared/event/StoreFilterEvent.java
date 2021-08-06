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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.StoreFilterHandler;

/**
 * Indicates that the Store has had its filtering properties changed, resulting
 * in changes in what data is visible.
 * 
 * @param <M> The type of data in the Store
 */
public final class StoreFilterEvent<M> extends StoreEvent<M, StoreFilterHandler<M>> {

  /**
   * A class that implements this interface is a public source of
   * {@link StoreFilterEvent}.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface HasStoreFilterHandlers<M> extends EventHandler {

    /**
     * Adds a {@link StoreFilterEvent} handler.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addStoreFilterHandler(StoreFilterHandler<M> handler);
  }

  /**
   * Handler interface for {@link StoreFilterEvent}.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface StoreFilterHandler<M> extends EventHandler {

    /**
     * Called when {@link StoreFilterEvent} is fired.
     * 
     * @param event the {@link StoreFilterEvent} that was fired
     */
    void onFilter(StoreFilterEvent<M> event);
  }

  private static GwtEvent.Type<StoreFilterHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<StoreFilterHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<StoreFilterHandler<?>>();
    }
    return TYPE;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public GwtEvent.Type<StoreFilterHandler<M>> getAssociatedType() {
    return (GwtEvent.Type) getType();
  }

  @Override
  protected void dispatch(StoreFilterHandler<M> handler) {
    handler.onFilter(this);
  }
}