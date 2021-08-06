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
import com.sencha.gxt.data.shared.event.StoreClearEvent.StoreClearHandler;

/**
 * Indicates that the data in the Store has been cleared, and that corresponding
 * views should be cleared as well.
 * 
 * 
 * @param <M> the model type
 */
public final class StoreClearEvent<M> extends StoreEvent<M, StoreClearHandler<M>> {

  /**
   * A class that implements this interface is a public source of
   * {@link StoreClearEvent}.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface HasStoreClearHandler<M> extends HasHandlers {

    /**
     * Adds a {@link StoreClearEvent} handler.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addStoreClearHandler(StoreClearHandler<M> handler);
  }

  /**
   * Handler interface for {@link StoreClearEvent}.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface StoreClearHandler<M> extends EventHandler {

    /**
     * Called when {@link StoreClearEvent} is fired.
     * 
     * @param event the {@link StoreClearEvent} that was fired
     */
    void onClear(StoreClearEvent<M> event);
  }

  private static GwtEvent.Type<StoreClearHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static GwtEvent.Type<StoreClearHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<StoreClearHandler<?>>();
    }
    return TYPE;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public GwtEvent.Type<StoreClearHandler<M>> getAssociatedType() {
    return (GwtEvent.Type) getType();
  }

  @Override
  protected void dispatch(StoreClearHandler<M> handler) {
    handler.onClear(this);
  }
}