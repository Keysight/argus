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
package com.sencha.gxt.dnd.core.client;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.shared.event.CancellableEvent;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent.DndDragEnterHandler;
import com.sencha.gxt.fx.client.DragMoveEvent;
import com.sencha.gxt.fx.client.Draggable;

/**
 * Fires when a drag enters a drop target.
 */
public class DndDragEnterEvent extends GwtEvent<DndDragEnterHandler> implements CancellableEvent {

  /**
   * Handler type.
   */
  private static Type<DndDragEnterHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<DndDragEnterHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<DndDragEnterHandler>();
    }
    return TYPE;
  }

  private boolean cancelled;
  private Widget target;
  private NativeEvent nativeEvent;
  private DragSource dragSource;
  private DragMoveEvent dragMoveEvent;
  private StatusProxy statusProxy;

  public DndDragEnterEvent(Widget target, DragSource dragSource, DragMoveEvent event,
      StatusProxy status) {
    this.target = target;
    this.dragSource = dragSource;
    this.dragMoveEvent = event;
    this.statusProxy = status;
  }

  @Override
  public Type<DndDragEnterHandler> getAssociatedType() {
    return TYPE;
  }

  public DragMoveEvent getDragEnterEvent() {
    return dragMoveEvent;
  }

  public DragSource getDragSource() {
    return dragSource;
  }

  /**
   * Returns the native event.
   * 
   * @return the event
   */
  public NativeEvent getNativeEvent() {
    return nativeEvent;
  }

  public Draggable getSource() {
    return (Draggable) super.getSource();
  }

  public StatusProxy getStatusProxy() {
    return statusProxy;
  }

  public Widget getTarget() {
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
  protected void dispatch(DndDragEnterHandler handler) {
    handler.onDragEnter(this);
  }
  
  /**
   * Handler for {@link DndDragEnterEvent} events.
   */
  public interface DndDragEnterHandler extends EventHandler {

    /**
     * Called when a dragged item enters a drop target.
     * 
     * @param event the {@link DndDragEnterEvent} that was fired
     */
    void onDragEnter(DndDragEnterEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link DndDragEnterEvent} events.
   */
  public interface HasDndDragEnterHandlers {

    /**
     * Adds a {@link DndDragEnterHandler} handler for {@link DndDragEnterEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addDragEnterHandler(DndDragEnterHandler handler);

  }

}
