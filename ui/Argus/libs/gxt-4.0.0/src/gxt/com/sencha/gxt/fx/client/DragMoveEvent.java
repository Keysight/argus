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
package com.sencha.gxt.fx.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.shared.event.CancellableEvent;
import com.sencha.gxt.fx.client.DragMoveEvent.DragMoveHandler;

/**
 * Fires when a draggable item is moved.
 */
public class DragMoveEvent extends GwtEvent<DragMoveHandler> implements CancellableEvent {

  /**
   * Handler type.
   */
  private static Type<DragMoveHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<DragMoveHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<DragMoveHandler>();
    }
    return TYPE;
  }

  private int x;
  private int y;
  private Element startElement;
  private boolean cancelled;
  private Widget target;
  private NativeEvent nativeEvent;

  public DragMoveEvent(Widget target, Element startElement, int x, int y, Event event) {
    this.target = target;
    this.startElement = startElement;
    this.x = x;
    this.y = y;
    this.nativeEvent = event;
  }

  @Override
  public Type<DragMoveHandler> getAssociatedType() {
    return TYPE;
  }

  /**
   * Returns the native event.
   * 
   * @return the native event
   */
  public NativeEvent getNativeEvent() {
    return nativeEvent;
  }

  public Draggable getSource() {
    return (Draggable) super.getSource();
  }

  public Element getStartElement() {
    return startElement;
  }

  public Widget getTarget() {
    return target;
  }

  /**
   * Returns the widget's page coordinates.
   * 
   * @return the x-coordinate value
   */
  public int getX() {
    return x;
  }

  /**
   * Returns the widget's page coordinates.
   * 
   * @return the y-coordinate value
   */
  public int getY() {
    return y;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancel) {
    this.cancelled = cancel;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  @Override
  protected void dispatch(DragMoveHandler handler) {
    handler.onDragMove(this);
  }
  
  /**
   * Handler for {@link DragMoveEvent} events.
   */
  public interface DragMoveHandler extends EventHandler {

    void onDragMove(DragMoveEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link DragMoveEvent} events.
   */
  public interface HasDragMoveHandlers {

    /**
     * Adds a {@link DragMoveHandler} handler for {@link DragMoveEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addDragMoveHandler(DragMoveHandler handler);

  }

}
