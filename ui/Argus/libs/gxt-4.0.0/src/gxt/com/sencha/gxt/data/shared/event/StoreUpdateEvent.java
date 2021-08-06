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

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.sencha.gxt.data.shared.Store.Record;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent.StoreUpdateHandler;

/**
 * Indicates that changes to one or more {@link Record}s have been committed, so
 * the changes have affected the underlying model.
 * 
 * @param <M> the model type
 */
public final class StoreUpdateEvent<M> extends StoreEvent<M, StoreUpdateHandler<M>> {

  /**
   * A class that implements this interface is a public source of
   * {@link StoreUpdateEvent}.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface HasStoreUpdateHandlers<M> extends HasHandlers {

    /**
     * Adds a {@link StoreUpdateEvent} handler.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addStoreUpdateHandler(StoreUpdateHandler<M> handler);
  }

  /**
   * Handler interface for {@link StoreUpdateEvent}.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface StoreUpdateHandler<M> extends EventHandler {

    /**
     * Called when {@link StoreUpdateEvent} is fired.
     * 
     * @param event the {@link StoreUpdateEvent} that was fired
     */
    void onUpdate(StoreUpdateEvent<M> event);
  }

  private static GwtEvent.Type<StoreUpdateHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static GwtEvent.Type<StoreUpdateHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<StoreUpdateHandler<?>>();
    }
    return TYPE;
  }

  private List<M> items;

  /**
   * Creates a new store update event.
   * 
   * @param items the items that have been updated
   */
  public StoreUpdateEvent(List<M> items) {
    this.items = Collections.unmodifiableList(items);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public GwtEvent.Type<StoreUpdateHandler<M>> getAssociatedType() {
    return (GwtEvent.Type) getType();
  }

  /**
   * Returns the items that were responsible for firing this store update event.
   * 
   * @return the updated items
   */
  public List<M> getItems() {
    return items;
  }

  @Override
  protected void dispatch(StoreUpdateHandler<M> handler) {
    handler.onUpdate(this);
  }
}