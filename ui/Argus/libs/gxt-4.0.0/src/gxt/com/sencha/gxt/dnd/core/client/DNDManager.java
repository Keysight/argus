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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.event.XEvent;

class DNDManager {

  private static DNDManager manager;

  static DNDManager get() {
    if (manager == null) {
      manager = new DNDManager();
    }
    return manager;
  }

  private DropTarget currentTarget;
  private List<DropTarget> dropTargets = new ArrayList<DropTarget>();

  protected DropTarget getTarget(DragSource dragSource, Element dropTargetElement) {
    if (dropTargetElement == null) {
      return null;
    }
    DropTarget target = null;
    for (DropTarget t : dropTargets) {
      if (t.isEnabled()
          && Util.equalWithNull(t.getGroup(), dragSource.getGroup())
          && t.dropWidget.getElement().isOrHasChild(dropTargetElement)
          && (target == null || (target.dropWidget.getElement().isOrHasChild(t.dropWidget.getElement())))) {
        target = t;
      }
    }
    return target;
  }

  /**
   * Obtain potential top-most target element associated with provided event.
   *
   * For touch devices, this method will attempt to find element from available dropTargets.
   * This is due to touch events "getEventTarget" always returning the element you started
   * the gesture on, regardless if you moved outside of the region of said element.  If the
   * event coordinates do not match up with any dropTarget elements, then null will be returned.
   *
   * @param event
   * @return
   */
  XElement getDropTargetElement(NativeEvent event) {
    if (GXT.isTouch()) {
      for (DropTarget dropTarget : dropTargets) {
        XElement dropTargetElement = dropTarget.getElementFromEvent(event);
        if (dropTargetElement != null) {
          return dropTargetElement;
        }
      }
      return null;
    }

    return event.getEventTarget().cast();
  }

  List<DropTarget> getDropTargets() {
    return dropTargets;
  }

  void handleDragCancelled(DragSource source, DndDragCancelEvent event) {
    source.onDragCancelled(event);
    source.ensureHandlers().fireEventFromSource(event, source);
    if (currentTarget != null) {
      currentTarget.onDragCancelled(event);
      currentTarget = null;
    }
  }

  void handleDragEnd(DragSource source, DndDropEvent event) {
    if (currentTarget != null) {
      event.setDropTarget(currentTarget);
      event.setOperation(currentTarget.getOperation());
    }

    if (currentTarget != null && event.getStatusProxy().getStatus()) {
      source.onDragDrop(event);
      source.ensureHandlers().fireEventFromSource(event, source);

      currentTarget.handleDrop(event);
      currentTarget.ensureHandlers().fireEventFromSource(event, currentTarget);
    } else {
      source.onDragFail(event);
      source.ensureHandlers().fireEventFromSource(event, source);

      if (currentTarget != null) {
        currentTarget.onDragFail(event);
      }
    }

    currentTarget = null;
    Insert.get().hide();
  }

  void handleDragMove(DragSource source, DndDragMoveEvent event) {
    XElement dropTargetElement = getDropTargetElement(event.getDragMoveEvent().getNativeEvent());

    DropTarget target = getTarget(source, dropTargetElement);

    // no target with current
    if (target == null) {
      if (currentTarget != null) {
        currentTarget.handleDragLeave(event);
        currentTarget = null;
      }
      return;
    }

    // match move
    if (target == currentTarget) {
      event.setCancelled(true);
      event.setDropTarget(currentTarget);
      currentTarget.onDragMove(event);
      currentTarget.ensureHandlers().fireEventFromSource(event, currentTarget);

      if (event.isCancelled()) {
        Insert.get().hide();
      } else {
        currentTarget.showFeedback(event);
      }
      return;
    }

    if (target != currentTarget) {
      if (currentTarget != null) {
        currentTarget.handleDragLeave(event);
        currentTarget = null;
      }

      currentTarget = target;
    }

    if (!currentTarget.isAllowSelfAsSource() && source.getWidget() == currentTarget.getWidget()) {
      currentTarget = null;
      return;
    }

    // entering
    event.setCancelled(true);
    event.setDropTarget(currentTarget);
    currentTarget.handleDragEnter(event);

    if (event.isCancelled()) {
      Insert.get().hide();
      currentTarget = null;
    } else {
      currentTarget.showFeedback(event);
    }
  }

  void handleDragStart(DragSource source, DndDragStartEvent event) {
    source.onDragStart(event);
    source.ensureHandlers().fireEventFromSource(event, source);

    if (event.getData() == null || event.isCancelled()) {
      event.setCancelled(true);
      event.getDragStartEvent().setCancelled(true);
      return;
    }

    source.setData(event.getData());
    source.statusProxy.setStatus(false);
  }

  void registerDropTarget(DropTarget target) {
    dropTargets.add(target);
  }

  void unregisterDropTarget(DropTarget target) {
    dropTargets.remove(target);
  }

}
