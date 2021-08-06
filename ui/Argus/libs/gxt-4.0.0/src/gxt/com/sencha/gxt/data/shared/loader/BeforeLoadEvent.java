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
package com.sencha.gxt.data.shared.loader;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.shared.event.CancellableEvent;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent.BeforeLoadHandler;

/**
 * Event type for loader events.
 */
public class BeforeLoadEvent<C> extends GwtEvent<BeforeLoadHandler<C>> implements CancellableEvent {

  /**
   * Handler class for {@link BeforeLoadEvent} events.
   */
  public interface BeforeLoadHandler<C> extends EventHandler {

    /**
     * Called before a load operation. Handlers can cancel the action by calling
     * {@link BeforeLoadEvent#setCancelled(boolean)}.
     */
    void onBeforeLoad(BeforeLoadEvent<C> event);
  }

  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeLoadEvent} events.
   */
  public interface HasBeforeLoadHandlers<C> {

    /**
     * Adds a {@link BeforeLoadEvent} handler.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addBeforeLoadHandler(BeforeLoadHandler<C> handler);

  }

  /**
   * Handler type.
   */
  private static Type<BeforeLoadHandler<?>> TYPE;
  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeLoadHandler<?>> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<BeforeLoadHandler<?>>());
  }

  private C loadConfig;

  private boolean cancelled;

  /**
   * Creates a before load event.
   * 
   * @param loadConfig the load configuration that will be used to load the data
   */
  public BeforeLoadEvent(C loadConfig) {
    this.loadConfig = loadConfig;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<BeforeLoadHandler<C>> getAssociatedType() {
    return (Type) TYPE;
  }

  /**
   * Returns the load config.
   * 
   * @return the load config
   */
  public C getLoadConfig() {
    return loadConfig;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancel) {
    cancelled = cancel;
  }

  @Override
  protected void dispatch(BeforeLoadHandler<C> handler) {
    handler.onBeforeLoad(this);
  }

}
