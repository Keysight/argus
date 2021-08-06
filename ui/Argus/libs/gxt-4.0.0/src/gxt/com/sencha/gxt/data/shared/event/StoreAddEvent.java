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
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;

/**
 * Indicates that an element has been added to the Store, and is visible under
 * the current filter settings.
 * 
 * @param <M> the model type
 */
public class StoreAddEvent<M> extends StoreEvent<M, StoreAddHandler<M>> {

  /**
   * A class that implements this interface is a public source of
   * {@link StoreAddEvent}.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface HasStoreAddHandlers<M> extends HasHandlers {

    /**
     * Adds a {@link StoreAddEvent} handler.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addStoreAddHandler(StoreAddHandler<M> handler);
  }

  /**
   * Handler interface for {@link StoreAddEvent}.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface StoreAddHandler<M> extends EventHandler {
    
    /**
     * Called when {@link StoreAddEvent} is fired.
     * 
     * @param event the {@link StoreAddEvent} that was fired
     */
    void onAdd(StoreAddEvent<M> event);
  }

  private static GwtEvent.Type<StoreAddHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static GwtEvent.Type<StoreAddHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<StoreAddHandler<?>>();
    }
    return TYPE;
  }

  private final List<M> items;

  private final int index;

  /**
   * Creates a new store add event.
   * 
   * @param index the insert index
   * @param items the items that were added
   */
  public StoreAddEvent(int index, List<M> items) {
    assert items.size() != 0;
    this.index = index;
    this.items = Collections.unmodifiableList(items);
  }

  /**
   * Creates a new store add event.
   * 
   * @param index the insert index
   * @param item the item that was added
   */
  public StoreAddEvent(int index, M item) {
    this.index = index;
    items = Collections.singletonList(item);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<StoreAddHandler<M>> getAssociatedType() {
    return (GwtEvent.Type) getType();
  }

  /**
   * Returns the insert index for this store add event.
   * 
   * @return the insert index
   */
  public int getIndex() {
    return index;
  }

  /**
   * Returns the items that were inserted for this store add event.
   * 
   * @return the list of items that were inserted
   */
  public List<M> getItems() {
    return items;
  }

  @Override
  protected void dispatch(StoreAddHandler<M> handler) {
    handler.onAdd(this);
  }
}