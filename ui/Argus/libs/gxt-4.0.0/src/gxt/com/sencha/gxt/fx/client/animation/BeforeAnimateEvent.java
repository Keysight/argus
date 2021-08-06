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
package com.sencha.gxt.fx.client.animation;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.shared.event.CancellableEvent;
import com.sencha.gxt.fx.client.animation.BeforeAnimateEvent.BeforeAnimateHandler;

/**
 * Represent the before animate event.
 */
public class BeforeAnimateEvent extends GwtEvent<BeforeAnimateHandler> implements CancellableEvent {

  /**
   * Handler type.
   */
  private static Type<BeforeAnimateHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeAnimateHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<BeforeAnimateHandler>();
    }
    return TYPE;
  }

  private boolean cancelled;

  public BeforeAnimateEvent() {
  }

  @Override
  public Type<BeforeAnimateHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  protected void dispatch(BeforeAnimateHandler handler) {
    handler.onBeforeAnimate(this);
  }
  
  /**
   * Handler for {@link BeforeAnimateEvent} events.
   */
  public interface BeforeAnimateHandler extends EventHandler {

    /**
     * Called before the animation is started. Listeners can cancel the action by
     * calling {@link BeforeAnimateEvent#setCancelled(boolean)}.
     * 
     * @param event the {@link BeforeAnimateEvent} that was fired
     */
    void onBeforeAnimate(BeforeAnimateEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeAnimateEvent} events.
   */
  public interface HasBeforeAnimateHandlers {

    /**
     * Adds a {@link BeforeAnimateHandler} handler for {@link BeforeAnimateEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addBeforeAnimateHandler(BeforeAnimateHandler handler);

  }

}
