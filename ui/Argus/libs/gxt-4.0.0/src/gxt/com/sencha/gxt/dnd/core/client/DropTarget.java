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

import java.util.List;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.shared.event.CancellableEvent;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DND.Operation;
import com.sencha.gxt.dnd.core.client.DndDragCancelEvent.DndDragCancelHandler;
import com.sencha.gxt.dnd.core.client.DndDragCancelEvent.HasDndDragCancelHandlers;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent.DndDragEnterHandler;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent.HasDndDragEnterHandlers;
import com.sencha.gxt.dnd.core.client.DndDragLeaveEvent.DndDragLeaveHandler;
import com.sencha.gxt.dnd.core.client.DndDragLeaveEvent.HasDndDragLeaveHandlers;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent.DndDragMoveHandler;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent.HasDndDragMoveHandlers;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent.HasDndDropHandlers;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.XEvent;

/**
 * Enables a component to act as the target of a drag and drop operation (i.e. a
 * user can drop data on the component).
 * <p />
 * While the cursor is over a target, the target is responsible for determining
 * if the drop is valid and showing any visual indicators for the drop. The
 * {@link StatusProxy} object should be used to specify if the drop is valid,
 * and can also be used to change the values of the proxy object displayed by
 * the cursor.
 */
public class DropTarget implements HasDndDragEnterHandlers, HasDndDragLeaveHandlers, HasDndDragCancelHandlers,
    HasDndDragMoveHandlers, HasDndDropHandlers {

  protected Widget dropWidget;
  protected Feedback feedback;
  protected Operation operation;
  protected String overStyle;

  private boolean allowSelfAsSource;
  private SimpleEventBus eventBus;
  private HandlerRegistration dropWidgetRegistration;
  private AttachEvent.Handler dropWidgetHandler = new Handler() {
    @Override
    public void onAttachOrDetach(AttachEvent event) {
      if (event.isAttached()) {
        onDropWidgetAttach();
      } else {
        onDropWidgetDetach();
      }
    }
  };

  private boolean enabled = true;
  private String group = "";

  /**
   * Creates a new drop dropWidget.
   * 
   * @param dropWidget the dropWidget widget
   */
  public DropTarget(Widget dropWidget) {
    this.dropWidget = dropWidget;
    this.operation = Operation.MOVE;
    this.feedback = Feedback.APPEND;

    dropWidgetRegistration = this.dropWidget.addAttachHandler(dropWidgetHandler);

    if (this.dropWidget.isAttached()) {
      onDropWidgetAttach();
    }
  }

  @Override
  public HandlerRegistration addDragCancelHandler(DndDragCancelHandler handler) {
    return ensureHandlers().addHandler(DndDragCancelEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDragEnterHandler(DndDragEnterHandler handler) {
    return ensureHandlers().addHandler(DndDragEnterEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDragLeaveHandler(DndDragLeaveHandler handler) {
    return ensureHandlers().addHandler(DndDragLeaveEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDragMoveHandler(DndDragMoveHandler handler) {
    return ensureHandlers().addHandler(DndDragMoveEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDropHandler(DndDropHandler handler) {
    return ensureHandlers().addHandler(DndDropEvent.getType(), handler);
  }

  /**
   * Disables the drag source.
   */
  public void disable() {
    enabled = false;
  }

  /**
   * Enables the drag source.
   */
  public void enable() {
    enabled = true;
  }

  /**
   * Returns the target's feedback setting.
   * 
   * @return the feedback
   */
  public Feedback getFeedback() {
    return feedback;
  }

  /**
   * Returns the target's group name.
   * 
   * @return the group name
   */
  public String getGroup() {
    return group;
  }

  /**
   * Returns the target's operation.
   * 
   * @return the operation
   */
  public Operation getOperation() {
    return operation;
  }

  /**
   * Returns the target's over style.
   * 
   * @return the over style
   */
  public String getOverStyle() {
    return overStyle;
  }

  /**
   * Returns the target's widget.
   * 
   * @return the widget
   */
  public Widget getWidget() {
    return dropWidget;
  }

  /**
   * Returns true if internal drops are allowed.
   * 
   * @return true for internal drops
   */
  public boolean isAllowSelfAsSource() {
    return allowSelfAsSource;
  }

  /**
   * Returns true if the drag source is enabled.
   * 
   * @return true for enabled
   */
  public boolean isEnabled() {
    return enabled && (dropWidget instanceof Component ? ((Component) dropWidget).isEnabled() : true);
  }

  /**
   * Unregisters the target as a drop target.
   */
  public void release() {
    dropWidgetRegistration.removeHandler();

    if (dropWidget.isAttached()) {
      onDropWidgetDetach();
    }
  }

  /**
   * Sets whether internal drops are allowed (defaults to false).
   * 
   * @param allowSelfAsSource true to allow internal drops
   */
  public void setAllowSelfAsSource(boolean allowSelfAsSource) {
    this.allowSelfAsSource = allowSelfAsSource;
  }

  /**
   * Sets the target's feedback. Feedback determines the type of visual
   * indicators a drop target supports. Subclasses will determine range of valid
   * values.
   * 
   * @param feedback the feedback
   */
  public void setFeedback(Feedback feedback) {
    this.feedback = feedback;
  }

  /**
   * Sets the drag group. If specified, only drag sources with the same group
   * value are allowed.
   * 
   * @param group the group name
   */
  public void setGroup(String group) {
    this.group = group;
  }

  /**
   * Sets the operation for the drop target which specifies if data should be
   * moved or copied when dropped. Drag sources use this value to determine if
   * the target data should be removed from the source widget.
   * 
   * @param operation the operation
   */
  public void setOperation(Operation operation) {
    this.operation = operation;
  }

  /**
   * Sets the style name to be applied when the cursor is over the target
   * (defaults to null).
   * 
   * @param overStyle the over style
   */
  public void setOverStyle(String overStyle) {
    this.overStyle = overStyle;
  }

  /**
   * Obtain potential top-most target element associated with provided event.
   *
   * For touch devices, this method will attempt to find element from contained Widget.
   * This is due to touch events "getEventTarget" always returning the element you started
   * the gesture on, regardless if you moved outside of the region of said element.  If the
   * event coordinates do not match up with any dropTarget elements, then null will be returned.
   *
   * @param event
   * @return
   */
  protected XElement getElementFromEvent(NativeEvent event) {
    if (GXT.isTouch()) {
      Point eventXY = event.<XEvent>cast().getXY();
      XElement dropTargetElement = getWidget().getElement().cast();
      return elementFromPoint(dropTargetElement, eventXY.getX(), eventXY.getY());
    }
    return event.getEventTarget().cast();
  }

  protected void onDropWidgetAttach() {
    DNDManager.get().registerDropTarget(this);
  }

  protected void onDropWidgetDetach() {
    DNDManager.get().unregisterDropTarget(this);
  }

  /**
   * Called if the user cancels the drag operations while the mouse is over the
   * target.
   * 
   * @param event the drag cancel event
   */
  protected void onDragCancelled(DndDragCancelEvent event) {
    Insert.get().hide();
  }

  /**
   * Called when the user releases the mouse over the target widget.
   * 
   * @param event the drop event
   */
  protected void onDragDrop(DndDropEvent event) {
  }

  /**
   * Called when the cursor first enters the bounds of the drop target.
   * Subclasses or listeners can change the status of status proxy via the
   * passed event.
   * 
   * @param event the drag enter event
   */
  protected void onDragEnter(DndDragEnterEvent event) {
  }

  /**
   * Called when a drop fails.
   * 
   * @param event the drop event
   */
  protected void onDragFail(DndDropEvent event) {
  }

  /**
   * Called when the cursor leaves the target.
   * 
   * @param event the drag leave event
   */
  protected void onDragLeave(DndDragLeaveEvent event) {
  }

  /**
   * Called when the cursor is moved within the target widget. Subclasses or
   * listeners can change the status of status proxy via the passed event. If
   * either a subclass or listener sets
   * {@link CancellableEvent#setCancelled(boolean)} to true,
   * {@link #showFeedback(DndDragMoveEvent)} will be called.
   * 
   * @param event the dd event
   */
  protected void onDragMove(DndDragMoveEvent event) {
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  protected List<Object> prepareDropData(Object data, boolean convertTreeStoreModel) {
    if (data instanceof List) {
      return (List) data;
    }
    // TODO why is this commented out or remove
    // List<ModelData> models = new ArrayList<ModelData>();
    // if (data instanceof ModelData) {
    // if (convertTreeStoreModel && data instanceof TreeStoreModel) {
    // models.add(((TreeStoreModel) data).getModel());
    // } else {
    // models.add((ModelData) data);
    // }
    // } else if (data instanceof List) {
    // for (Object obj : (List) data) {
    // if (obj instanceof ModelData) {
    // if (convertTreeStoreModel && obj instanceof TreeStoreModel) {
    // models.add(((TreeStoreModel) obj).getModel());
    // } else {
    // models.add((ModelData) obj);
    // }
    // }
    // }
    // }
    return null;// models;
  }

  /**
   * Called as the mouse is moved over the target widget. The default
   * implementation does nothing.
   * 
   * @param event the dd event
   */
  protected void showFeedback(DndDragMoveEvent event) {
  }

  SimpleEventBus ensureHandlers() {
    return eventBus == null ? eventBus = new SimpleEventBus() : eventBus;
  }

  boolean handleDragEnter(DndDragMoveEvent event) {
    DndDragEnterEvent e = new DndDragEnterEvent(dropWidget, event.getDragSource(), event.getDragMoveEvent(),
        event.getStatusProxy());
    event.setCancelled(false);
    event.getStatusProxy().setStatus(true);
    onDragEnter(e);

    ensureHandlers().fireEventFromSource(e, this);

    if (e.isCancelled()) {
      event.getStatusProxy().setStatus(false);
      return false;
    }
    if (overStyle != null) {
      dropWidget.addStyleName(overStyle);
    }
    return true;
  }

  void handleDragLeave(DndDragMoveEvent event) {
    if (overStyle != null) {
      dropWidget.removeStyleName(overStyle);
    }

    event.getStatusProxy().setStatus(false);
    Insert.get().hide();

    DndDragLeaveEvent e = new DndDragLeaveEvent(dropWidget, event.getDragSource(), event.getDragMoveEvent(),
    event.getStatusProxy());

    onDragLeave(e);
    ensureHandlers().fireEventFromSource(e, this);
  }

  void handleDragMove(DndDragMoveEvent event) {
    showFeedback(event);
    onDragMove(event);
  }

  void handleDrop(DndDropEvent event) {
    Insert.get().hide();
    if (overStyle != null) {
      dropWidget.removeStyleName(overStyle);
    }
    onDragDrop(event);
  }


  private XElement elementFromPoint(XElement element, int x, int y) {
    if (!element.getBounds().contains(x, y)) {
      return null;
    }
    if (!element.hasChildNodes()) {
      return element;
    }

    NodeList<Node> childNodes = element.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node node = childNodes.getItem(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        XElement child = node.cast();
        if (child.getBounds().contains(x, y)) {
          return elementFromPoint(child, x, y);
        }
      }
    }

    // children are not within bounds, return this
    return element;
  }
}
