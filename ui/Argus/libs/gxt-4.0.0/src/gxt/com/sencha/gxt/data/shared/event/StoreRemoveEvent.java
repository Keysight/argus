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
import com.google.gwt.event.shared.HasHandlers;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent.StoreRemoveHandler;

/**
 * Indicates that an element that was visible has been removed from the Store.
 * Note that non-visible elements do not get events, as most display widgets are
 * not concerned with data which has been filtered out.
 * 
 * @param <M> the model type
 */
public class StoreRemoveEvent<M> extends StoreEvent<M, StoreRemoveHandler<M>> {

  /**
   * A class that implements this interface is a public source of
   * {@link StoreRemoveEvent}.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface HasStoreRemoveHandler<M> extends HasHandlers {

    /**
     * Adds a {@link StoreRemoveEvent} handler.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addStoreRemoveHandler(StoreRemoveHandler<M> handler);
  }

  /**
   * Handler interface for {@link StoreRemoveEvent}.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface StoreRemoveHandler<M> extends EventHandler {

    /**
     * Called when {@link StoreRemoveEvent} is fired.
     * 
     * @param event the {@link StoreRemoveEvent} that was fired
     */
    void onRemove(StoreRemoveEvent<M> event);
  }

  private static GwtEvent.Type<StoreRemoveHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static GwtEvent.Type<StoreRemoveHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<StoreRemoveHandler<?>>();
    }
    return TYPE;
  }

  private final int index;

  private final M item;

  /**
   * Creates a new store remove event.
   * 
   * @param index the remove index
   * @param item the item that was removed
   */
  public StoreRemoveEvent(int index, M item) {
    this.index = index;
    this.item = item;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public GwtEvent.Type<StoreRemoveHandler<M>> getAssociatedType() {
    return (GwtEvent.Type) getType();
  }

  /**
   * Returns the index of the removed item.
   * 
   * @return the index of the removed item
   */
  public int getIndex() {
    return index;
  }

  /**
   * Returns the removed item.
   * 
   * @return the removed item
   */
  public M getItem() {
    return item;
  }

  @Override
  protected void dispatch(StoreRemoveHandler<M> handler) {
    handler.onRemove(this);
  }
}