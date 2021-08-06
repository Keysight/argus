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
import java.util.logging.Logger;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DND.Operation;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.event.XEvent;

/**
 * Enables a {@link ListView} to act as the target of a drag and drop operation.
 * <p/>
 * Use {@link #setFeedback(com.sencha.gxt.dnd.core.client.DND.Feedback)} to specify whether to allow inserting items
 * between rows, appending items to the end, or both (defaults to {@link Feedback#BOTH}).
 * <p/>
 * Use {@link #setOperation(com.sencha.gxt.dnd.core.client.DND.Operation)} to specify whether to move items or copy
 * them (defaults to {@link Operation#MOVE}).
 *
 * @param <M> the model type
 */
public class ListViewDropTarget<M> extends DropTarget {

  private static final Logger logger = Logger.getLogger(ListViewDropTarget.class.getName());

  protected ListView<M, ?> listView;
  protected M activeItem;
  protected int insertIndex;
  protected boolean before;

  private boolean autoSelect;

  /**
   * Creates a drop target for the specified list view.
   * 
   * @param listView the list view to enable as a drop target
   */
  public ListViewDropTarget(ListView<M, ?> listView) {
    super(listView);
    this.listView = listView;
  }

  /**
   * Returns the list view associated with this drop target.
   * 
   * @return the list view associated with this drop target
   */
  public ListView<M, ?> getListView() {
    return listView;
  }

  /**
   * Returns true if auto select is enabled.
   * 
   * @return the auto select state
   */
  public boolean isAutoSelect() {
    return autoSelect;
  }

  /**
   * True to automatically select any new items created after a drop (defaults to false).
   * 
   * @param autoSelect true to auto select
   */
  public void setAutoSelect(boolean autoSelect) {
    this.autoSelect = autoSelect;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  protected void onDragDrop(DndDropEvent event) {
    super.onDragDrop(event);

    Object data = event.getData();
    List<M> models = (List) prepareDropData(data, true);
    if (models.size() > 0) {
      if (feedback == Feedback.APPEND) {
        listView.getStore().addAll(models);
      } else {
        listView.getStore().addAll(insertIndex, models);
      }
    }
    insertIndex = -1;
    activeItem = null;
  }

  @Override
  protected void onDragEnter(DndDragEnterEvent event) {
    super.onDragEnter(event);
    event.setCancelled(true);
    event.getStatusProxy().setStatus(true);
  }

  @Override
  protected void onDragLeave(DndDragLeaveEvent event) {
    super.onDragLeave(event);
    Insert insert = Insert.get();
    insert.setVisible(false);
  }

  @Override
  protected void onDragMove(DndDragMoveEvent event) {
    XElement target = getElementFromEvent(event.getDragMoveEvent().getNativeEvent());

    if (!listView.getElement().isOrHasChild(target)) {
      event.setCancelled(true);
      event.getStatusProxy().setStatus(false);
    } else {
      event.setCancelled(false);
      event.getStatusProxy().setStatus(true);
    }
  }

  @Override
  protected void showFeedback(DndDragMoveEvent event) {
    event.getStatusProxy().setStatus(true);

    Element target = getElementFromEvent(event.getDragMoveEvent().getNativeEvent());

    if (feedback == Feedback.INSERT) {
      Element row = target != null ? listView.findElement(target) : null;

      if (row == null && listView.getStore().size() > 0) {
        row = listView.getElement(listView.getStore().size() - 1).cast();
      }

      if (row != null) {
        int height = row.getOffsetHeight();
        int mid = height / 2;
        mid += row.getAbsoluteTop();
        int y = event.getDragMoveEvent().getNativeEvent().<XEvent>cast().getXY().getY();
        before = y < mid;
        int idx = listView.findElementIndex(row);

        activeItem = listView.getStore().get(idx);
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
      int idx = listView.getStore().indexOf(m);
      if (idx > -1 && (before ? idx < index : idx <= index)) {
        i--;
      }
    }
    return before ? i : i + 1;
  }

  private void showInsert(DndDragMoveEvent event, Element row) {
    Insert insert = Insert.get();
    insert.show(row.getParentElement());

    Rectangle rect = row.<XElement> cast().getBounds();

    int y = rect.getY() - 2;
    if (!before) {
      y = rect.getY() + rect.getHeight() - 4;
    }

    insert.getElement().makePositionable(true);
    insert.getElement().setBounds(rect.getX(), y, rect.getWidth(), 6);

    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("showInsert: y=" + y + " before=" + before + " rect=" + rect);
    }
  }

}
