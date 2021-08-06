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
package com.sencha.gxt.cell.core.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.sencha.gxt.core.client.gestures.CellGestureAdapter;
import com.sencha.gxt.core.client.gestures.GestureRecognizer;
import com.sencha.gxt.core.client.gestures.PointerEvents;
import com.sencha.gxt.core.client.gestures.PointerEventsSupport;
import com.sencha.gxt.core.shared.event.CancellableEvent;
import com.sencha.gxt.widget.core.client.cell.HandlerManagerContext;

public abstract class AbstractEventInputCell<T, V> extends AbstractInputCell<T, V> implements HasHandlers {

  private HandlerManager handlerManager;
  private boolean disableEvents;
  private List<CellGestureAdapter<? extends GestureRecognizer, T>> cellGestureAdapters;
  private Set<String> consumedEvents;

  public AbstractEventInputCell(Set<String> consumedEvents) {
    super(consumedEvents);
    this.consumedEvents = new HashSet<String>(super.getConsumedEvents());
  }

  public AbstractEventInputCell(String... consumedEvents) {
    super(consumedEvents);
    this.consumedEvents = new HashSet<String>(super.getConsumedEvents());
  }

  public void addCellGestureAdapter(CellGestureAdapter<? extends GestureRecognizer, T> cellGestureAdapter) {
    if (cellGestureAdapters == null) {
      cellGestureAdapters = new ArrayList<CellGestureAdapter<? extends GestureRecognizer, T>>();
      if (PointerEventsSupport.impl.isSupported()) {
        consumedEvents.add(PointerEvents.POINTERDOWN.getEventName());
        consumedEvents.add(PointerEvents.POINTERMOVE.getEventName());
        consumedEvents.add(PointerEvents.POINTERUP.getEventName());
        consumedEvents.add(PointerEvents.POINTERCANCEL.getEventName());
      }
      consumedEvents.add(BrowserEvents.TOUCHSTART);
      consumedEvents.add(BrowserEvents.TOUCHMOVE);
      consumedEvents.add(BrowserEvents.TOUCHCANCEL);
      consumedEvents.add(BrowserEvents.TOUCHEND);
    }
    cellGestureAdapter.setDelegate(this);
    cellGestureAdapters.add(cellGestureAdapter);
  }

  public void removeCellGestureAdapter(CellGestureAdapter<? extends GestureRecognizer, T> cellGestureAdapter) {
    if (cellGestureAdapter != null) {
      cellGestureAdapters.remove(cellGestureAdapter);
    }
  }

  public CellGestureAdapter<? extends GestureRecognizer, T> getCellGestureAdapter(int index) {
    if (cellGestureAdapters != null) {
      return cellGestureAdapters.get(index);
    }
    return null;
  }

  public int getCellGestureAdapterCount() {
    return cellGestureAdapters == null ? 0 : cellGestureAdapters.size();
  }

  @Override
  public Set<String> getConsumedEvents() {
    return Collections.unmodifiableSet(consumedEvents);
  }

  @Override
  public void onBrowserEvent(Context context, Element parent, T value, NativeEvent event, ValueUpdater<T> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);
    String eventType = event.getType();
    if (BrowserEvents.TOUCHSTART.equals(eventType) ||
        BrowserEvents.TOUCHMOVE.equals(eventType) ||
        BrowserEvents.TOUCHCANCEL.equals(eventType) ||
        BrowserEvents.TOUCHEND.equals(eventType)) {
      onTouch(context, parent, value, event, valueUpdater);
    }
  }

  /**
   * Adds this handler to the widget.
   *
   * @param <H> the type of handler to add
   * @param type the event type
   * @param handler the handler
   * @return {@link HandlerRegistration} used to remove the handler
   */
  public final <H extends EventHandler> HandlerRegistration addHandler(final H handler, GwtEvent.Type<H> type) {
    return ensureHandlers().addHandler(type, handler);
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
    if (handlerManager != null) {
      handlerManager.fireEvent(event);
    }
  }

  public boolean isDisableEvents() {
    return disableEvents;
  }

  public void setDisableEvents(boolean disableEvents) {
    this.disableEvents = disableEvents;
  }

  /**
   * Creates the {@link HandlerManager} used by this Widget. You can override
   * this method to create a custom {@link HandlerManager}.
   *
   * @return the {@link HandlerManager} you want to use
   */
  protected HandlerManager createHandlerManager() {
    return new HandlerManager(this);
  }

  protected boolean fireCancellableEvent(Context context, GwtEvent<?> event) {
    if (disableEvents) return true;
    fireEvent(context, event);
    if (event instanceof CancellableEvent) {
      return !((CancellableEvent) event).isCancelled();
    }
    return true;
  }

  protected boolean fireCancellableEvent(GwtEvent<?> event) {
    if (disableEvents) return true;
    fireEvent(event);
    if (event instanceof CancellableEvent) {
      return !((CancellableEvent) event).isCancelled();
    }
    return true;
  }

  protected void fireEvent(Context context, GwtEvent<?> event) {
    if (context instanceof HandlerManagerContext) {
      ((HandlerManagerContext)context).getHandlerManager().fireEvent(event);
    } else {
      fireEvent(event);
    }
  }

  protected void onTouch(Context context, Element parent, T value, NativeEvent event, ValueUpdater<T> valueUpdater) {
    for (int i = 0, len = getCellGestureAdapterCount(); i < len; i++) {
      getCellGestureAdapter(i).handle(context, parent, value, event, valueUpdater);
    }
  }

  /**
   * Ensures the existence of the handler manager.
   *
   * @return the handler manager
   * */
  HandlerManager ensureHandlers() {
    return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
  }

  private native void setConsumedEventsInternal(Set<String> events) /*-{
      this.@com.google.gwt.cell.client.AbstractCell::consumedEvents = events;
  }-*/;
}
