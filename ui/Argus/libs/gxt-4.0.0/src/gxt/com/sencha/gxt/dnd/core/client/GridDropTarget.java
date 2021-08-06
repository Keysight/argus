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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DND.Operation;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * Enables a {@link Grid} to act as the target of a drag and drop operation.
 * <p/>
 * Use {@link #setFeedback(com.sencha.gxt.dnd.core.client.DND.Feedback)} to specify whether to allow inserting
 * items between rows, appending items to the end, or both (defaults to
 * {@link Feedback#BOTH}).
 * <p/>
 * Use {@link #setOperation(com.sencha.gxt.dnd.core.client.DND.Operation)} to specify whether to move items or copy
 * them (defaults to {@link Operation#MOVE}).
 *
 * @param <M> the model type
 */
public class GridDropTarget<M> extends DropTarget {

  protected M activeItem;
  protected Grid<M> grid;
  protected int insertIndex;
  boolean before;

  /**
   * Creates a drop target for the specified grid.
   * 
   * @param grid the grid to enable as a drop target
   */
  public GridDropTarget(Grid<M> grid) {
    super(grid);
    this.grid = grid;
  }

  /**
   * Returns the grid associated with this drop target.
   * 
   * @return the grid associated with this drop target
   */
  public Grid<M> getGrid() {
    return grid;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void onDragDrop(DndDropEvent e) {
    super.onDragDrop(e);
    Object data = e.getData();
    List<M> models = (List<M>) prepareDropData(data, true);
    if (models.size() > 0) {
      if (feedback == Feedback.APPEND) {
        grid.getStore().addAll(models);
      } else {
        grid.getStore().addAll(insertIndex, models);
      }
    }
    insertIndex = -1;
    activeItem = null;
  }

  @Override
  protected void onDragEnter(DndDragEnterEvent e) {
    super.onDragEnter(e);
    e.setCancelled(false);
    e.getStatusProxy().setStatus(true);
  }

  @Override
  protected void onDragMove(DndDragMoveEvent event) {
    Element target = getElementFromEvent(event.getDragMoveEvent().getNativeEvent());

    if (Element.is(target) && !grid.getView().getScroller().isOrHasChild(Element.as(target))) {
      event.setCancelled(true);
      event.getStatusProxy().setStatus(false);
      return;
    }

    event.setCancelled(false);
    event.getStatusProxy().setStatus(true);
  }

  @Override
  protected void showFeedback(DndDragMoveEvent event) {
    event.getStatusProxy().setStatus(true);
    Element target = getElementFromEvent(event.getDragMoveEvent().getNativeEvent());

    if (feedback == Feedback.INSERT || feedback == Feedback.BOTH) {
      Element row = grid.getView().findRow(Element.as(target)).cast();

      if (row == null && grid.getStore().size() > 0) {
        row = grid.getView().getRow(grid.getStore().size() - 1).cast();
      }

      if (row != null) {
        int height = row.getOffsetHeight();
        int mid = height / 2;
        mid += row.getAbsoluteTop();
        int y = event.getDragMoveEvent().getNativeEvent().<XEvent>cast().getXY().getY();
        before = y < mid;
        int idx = grid.getView().findRowIndex(row);

        activeItem = grid.getStore().get(idx);
        insertIndex = adjustIndex(event, idx);

        showInsert(event, row);
      } else {
        insertIndex = 0;
      }
    }
  }

  private int adjustIndex(DndDragMoveEvent event, int index) {
    Object data = event.getData();
    int i = index;
    @SuppressWarnings("unchecked")
    List<M> models = (List<M>) prepareDropData(data, true);
    for (M m : models) {
      int idx = grid.getStore().indexOf(m);
      if (idx > -1 && (before ? idx < index : idx <= index)) {
        i--;
      }
    }
    return before ? i : i + 1;
  }

  private void showInsert(DndDragMoveEvent event, Element row) {
    Insert insert = Insert.get();
    insert.show(row);
    Rectangle rect = XElement.as(row).getBounds();
    int y = !before ? (rect.getY() + rect.getHeight() - 4) : rect.getY() - 2;
    insert.getElement().setBounds(rect.getX(), y, rect.getWidth(), 6);
  }

}
