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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.shared.event.CancellableEvent;
import com.sencha.gxt.state.client.SaveStateEvent.SaveStateHandler;

/**
 * Fires after state has been saved.
 */
public class SaveStateEvent<S, O> extends GwtEvent<SaveStateHandler<S,O>> implements CancellableEvent{

  /**
   * Handler class for {@link SaveStateEvent} events.
   */
  public interface SaveStateHandler<S, O> extends EventHandler {

    /**
     * Called after state has been saved.
     */
    void onSaveState(SaveStateEvent<S, O> event);
  }

  public interface HasSaveStateHandlers<S, O> {

    /**
     * Adds a {@link SaveStateHandler} handler for {@link SaveStateEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addSaveStateHandler(SaveStateHandler<S, O> handler);
  }

  /**
   * Handler type.
   */
  private static Type<SaveStateHandler<?, ?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<SaveStateHandler<?, ?>> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<SaveStateHandler<?, ?>>());
  }
  
  private S state;
  private O target;
  private boolean cancelled;
  
  public SaveStateEvent(S state, O target) {
    this.state = state;
    this.target = target;
  }
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<SaveStateHandler<S, O>> getAssociatedType() {
    return (Type) TYPE;
  }
  
  /**
   * Returns the state.
   * 
   * @return the state
   */
  public S getState() {
    return state;
  }

  /**
   * Returns the target object.
   * 
   * @return the target
   */
  public O getTarget() {
    return target;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancel) {
    this.cancelled = cancel;
  }

  @Override
  protected void dispatch(SaveStateHandler<S, O> handler) {
    handler.onSaveState(this);
  }

}
