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
import com.sencha.gxt.state.client.RestoreStateEvent.RestoreStateHandler;

/**
 * Fires after state has been restored.
 */
public class RestoreStateEvent<S, O> extends GwtEvent<RestoreStateHandler<S, O>> {

  public interface HasRestoreStateHandlers<S, O> {

    /**
     * Adds a {@link RestoreStateHandler} handler for {@link RestoreStateEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addRestoreStateHandler(RestoreStateHandler<S, O> handler);
  }

  /**
   * Handler class for {@link RestoreStateEvent} events.
   */
  public interface RestoreStateHandler<S, O> extends EventHandler {

    /**
     * Called after state has been restored.
     */
    void onRestoreState(RestoreStateEvent<S, O> event);
  }

  /**
   * Handler type.
   */
  private static Type<RestoreStateHandler<?, ?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<RestoreStateHandler<?, ?>> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<RestoreStateHandler<?, ?>>());
  }
  
  private S state;
  private O target;
  
  public RestoreStateEvent(S state, O target) {
    this.state = state;
    this.target = target;
  }
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<RestoreStateHandler<S, O>> getAssociatedType() {
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
  protected void dispatch(RestoreStateHandler<S, O> handler) {
    handler.onRestoreState(this);
  }

}
