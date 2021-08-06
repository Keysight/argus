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
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;

/**
 * Indicates that the items in the store have been replaced.
 * 
 * @param <M> the model type
 */
public final class StoreDataChangeEvent<M> extends StoreEvent<M, StoreDataChangeHandler<M>> {

  /**
   * A class that implements this interface is a public source of
   * {@link StoreDataChangeEvent}.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface HasStoreDataChangeHandlers<M> extends HasHandlers {

    /**
     * Adds a {@link StoreDataChangeEvent} handler.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addStoreDataChangeHandler(StoreDataChangeHandler<M> handler);
  }

  /**
   * Handler interface for {@link StoreDataChangeEvent}.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface StoreDataChangeHandler<M> extends EventHandler {

    /**
     * Called when {@link StoreDataChangeEvent} is fired.
     * 
     * @param event the {@link StoreDataChangeEvent} that was fired
     */
    void onDataChange(StoreDataChangeEvent<M> event);
  }

  private static GwtEvent.Type<StoreDataChangeHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static GwtEvent.Type<StoreDataChangeHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<StoreDataChangeHandler<?>>();
    }
    return TYPE;
  }

  private M parent;

  /**
   * Creates a new store data change event.
   */
  public StoreDataChangeEvent() {

  }

  /**
   * Creates a new store data change event.
   * 
   * @param parent the parent of the changed data
   */
  public StoreDataChangeEvent(M parent) {
    this.parent = parent;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<StoreDataChangeHandler<M>> getAssociatedType() {
    return (GwtEvent.Type) getType();
  }

  /**
   * Returns the parent model who's children were just loaded. Parent will
   * always be null with ListStore.
   * 
   * @return the parent or null if ListStore
   */
  public M getParent() {
    return parent;
  }

  @Override
  protected void dispatch(StoreDataChangeHandler<M> handler) {
    handler.onDataChange(this);
  }
}