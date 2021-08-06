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
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent.AfterAnimateHandler;

/**
 * Represent the after animate event.
 */
public class AfterAnimateEvent extends GwtEvent<AfterAnimateHandler> {

  /**
   * Handler type.
   */
  private static Type<AfterAnimateHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<AfterAnimateHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<AfterAnimateHandler>();
    }
    return TYPE;
  }

  public AfterAnimateEvent() {
  }

  @Override
  public Type<AfterAnimateHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(AfterAnimateHandler handler) {
    handler.onAfterAnimate(this);
  }
  
  /**
   * Handler for {@link AfterAnimateEvent} events.
   */
  public interface AfterAnimateHandler extends EventHandler {

    /**
     * Called when the animation is complete.
     * 
     * @param event the {@link AfterAnimateEvent} that was fired
     */
    void onAfterAnimate(AfterAnimateEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link AfterAnimateEvent} events.
   */
  public interface HasAfterAnimateHandlers {

    /**
     * Adds a {@link AfterAnimateHandler} handler for {@link AfterAnimateEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addAfterAnimateHandler(AfterAnimateHandler handler);

  }

}
