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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.AutoScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent.StoreRemoveHandler;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DND.Operation;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

/**
 * Enables a {@link Tree} to act as the target of a drag and drop operation.
 * <p/>
 * Use {@link #setFeedback(com.sencha.gxt.dnd.core.client.DND.Feedback)} to specify whether to allow inserting
 * items between items, appending items to the end, or both (defaults to
 * {@link Feedback#BOTH}).
 * <p/>
 * Use {@link #setOperation(com.sencha.gxt.dnd.core.client.DND.Operation)} to specify whether to move items or copy
 * them (defaults to {@link Operation#MOVE}).
 *
 * @param <M> the model type
 */
public class TreeDropTarget<M> extends DropTarget {

  /**
   * The tree drop target resources.
   */
  public interface TreeDropTargetResources extends ClientBundle {
    /**
     * Returns an icon to indicate an insert at the current item.
     * 
     * @return an icon to indicate an insert at the current item
     */
    ImageResource dropInsert();

    /**
     * Returns an icon to indicate an insert above the current item.
     * 
     * @return an icon to indicate an insert above the current item
     */
    ImageResource dropInsertAbove();

    /**
     * Returns an icon to indicate an insert below the current item.
     * 
     * @return an icon to indicate an insert below the current item
     */
    ImageResource dropInsertBelow();
  }

  protected TreeNode<M> activeItem, appendItem;
  protected int status;
  protected TreeDropTargetResources resources = GWT.create(TreeDropTargetResources.class);

  private boolean allowDropOnLeaf = false;
  private boolean autoExpand = true, autoScroll = true;
  private int autoExpandDelay = 800;
  private boolean restoreTrackMouse;
  private boolean addChildren;
  private AutoScrollSupport scrollSupport;

  /**
   * Creates a drop target for the specified tree.
   * 
   * @param tree the tree to enable as a drop target
   */
  public TreeDropTarget(Tree<M, ?> tree) {
    super(tree);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Tree<M, ?> getWidget() {
    return (Tree<M, ?>) super.getWidget();
  }

  /**
   * Returns true if children are being added when inserting into the TreeStore.
   * 
   * @return the add children state
   */
  public boolean isAddChildren() {
    return addChildren;
  }

  /**
   * Returns whether drops are allowed on leaf nodes.
   * 
   * @return true of drops on leafs are allowed
   */
  public boolean isAllowDropOnLeaf() {
    return allowDropOnLeaf;
  }

  /**
   * Returns true if auto expand is enabled (defaults to true).
   * 
   * @return the auto expand state
   */
  public boolean isAutoExpand() {
    return autoExpand;
  }

  /**
   * Returns true if auto scroll is enabled (defaults to true).
   * 
   * @return true if auto scroll enabled
   */
  public boolean isAutoScroll() {
    return autoScroll;
  }

  /**
   * True to add children when inserting models into the TreeStore (defaults to
   * false).
   * 
   * @param addChildren true to add children
   */
  public void setAddChildren(boolean addChildren) {
    this.addChildren = addChildren;
  }

  /**
   * True to allow drops on leaf nodes (defaults to false).
   * 
   * @param allowDropOnLeaf true to enable drops on leaf nodes
   */
  public void setAllowDropOnLeaf(boolean allowDropOnLeaf) {
    this.allowDropOnLeaf = allowDropOnLeaf;
  }

  /**
   * True to automatically expand the active tree item when the user hovers over
   * a collapsed item (defaults to true). Use {@link #setAutoExpandDelay(int)}
   * to set the delay.
   * 
   * @param autoExpand true to auto expand
   */
  public void setAutoExpand(boolean autoExpand) {
    this.autoExpand = autoExpand;
  }

  /**
   * Sets the delay used to auto expand items (defaults to 800).
   * 
   * @param autoExpandDelay the delay in milliseconds
   */
  public void setAutoExpandDelay(int autoExpandDelay) {
    this.autoExpandDelay = autoExpandDelay;
  }

  /**
   * True to automatically scroll the tree when the user hovers over the top and
   * bottom of the tree grid (defaults to true).
   * 
   * @see ScrollSupport
   * 
   * @param autoScroll true to enable auto scroll
   */
  public void setAutoScroll(boolean autoScroll) {
    this.autoScroll = autoScroll;
  }

  protected void appendModel(M p, List<?> items, int index) {
    if (items.size() == 0) return;
    if (items.get(0) instanceof TreeStore.TreeNode) {
      @SuppressWarnings("unchecked")
      List<TreeStore.TreeNode<M>> nodes = (List<TreeStore.TreeNode<M>>) items;
      if (p == null) {
        getWidget().getStore().addSubTree(index, nodes);
      } else {
        getWidget().getStore().addSubTree(p, index, nodes);
      }
    } else {
      @SuppressWarnings("unchecked")
      List<M> models = (List<M>) items;
      if (p == null) {
        getWidget().getStore().insert(index, models);
      } else {
        getWidget().getStore().insert(p, index, models);
      }
    }
  }

  protected void bind(TreeGrid<M> tree) {
    // tree.addListener(Events.Unregister, new Listener<TreeGridEvent>() {
    // public void handleEvent(TreeGridEvent be) {
    // if (activeItem == be.getTreeNode()) {
    // activeItem = null;
    // }
    // }
    // });
    tree.getTreeStore().addStoreRemoveHandler(new StoreRemoveHandler<M>() {
      @Override
      public void onRemove(StoreRemoveEvent<M> event) {
        if (activeItem.getModel() == event.getItem()) {
          activeItem = null;
        }
      }
    });
  }

  protected void clearStyle(TreeNode<M> node) {
    getWidget().getView().onDropChange(node, false);
    Insert.get().hide();
  }

  protected void handleAppend(DndDragMoveEvent event, final TreeNode<M> item) {
    // clear any active append item
    if (activeItem != null && activeItem != item) {
      clearStyle(activeItem);
    }
    status = -1;

    Insert.get().hide();
    event.getStatusProxy().setStatus(true);
    if (activeItem != null) {
      clearStyle(activeItem);
    }

    if (item != null && item != appendItem && autoExpand && !item.isExpanded()) {
      Timer t = new Timer() {
        @Override
        public void run() {
          if (item == appendItem) {
            getWidget().setExpanded(item.getModel(), true);
          }
        }
      };
      t.schedule(autoExpandDelay);
    }
    appendItem = item;
    activeItem = item;
    if (activeItem != null) {
      // TODO this might not get the right element
      getWidget().getView().onDropChange(activeItem, true);
    }
  }

  protected void handleAppendDrop(DndDropEvent event, TreeNode<M> item) {
    // TODO not M, but TreeStore.TreeNode<M>
    List<?> models = (List<?>) event.getData();
    if (models.size() > 0) {
      M p = null;
      if (item != null) {
        p = item.getModel();
        appendModel(p, models, getWidget().getStore().getChildCount(item.getModel()));
      } else {
        appendModel(p, models, getWidget().getStore().getRootItems().size());
      }

    }
  }

  protected void handleInsert(DndDragMoveEvent event, final TreeNode<M> item) {
    Element e = getWidget().getView().getElementContainer(item);

    int height = e.getOffsetHeight();
    int mid = height / 2;
    int top = e.getAbsoluteTop();
    mid += top;
    int y = event.getDragMoveEvent().getNativeEvent().<XEvent>cast().getXY().getY();
    boolean before = y < mid;

    boolean leaf = getWidget().isLeaf(item.getModel());

    if ((!leaf || allowDropOnLeaf) && (feedback == Feedback.BOTH || feedback == Feedback.APPEND)
        && ((before && y > top + 4) || (!before && y < top + height - 4))) {
      handleAppend(event, item);
      return;
    }

    if ((!leaf) && ((before && y > top + 4) || (!before && y < top + height - 4))) {
      clearStyle(item);
      return;
    }

    // clear any active append item
    if (activeItem != null && activeItem != item) {
      clearStyle(activeItem);
    }

    appendItem = null;

    status = before ? 0 : 1;

    if (activeItem != null) {
      clearStyle(activeItem);
    }

    activeItem = item;

    if (activeItem != null) {
      TreeStore<M> store = getWidget().getStore();

      int idx = -1;

      M p = store.getParent(activeItem.getModel());
      if (p != null) {
        idx = store.getChildren(p).indexOf(activeItem.getModel());
      } else {
        idx = store.getRootItems().indexOf(activeItem.getModel());
      }

      ImageResource status = resources.dropInsert();
      if (before && idx == 0) {
        status = resources.dropInsertAbove();
      } else if (idx > 1 && !before && p != null && idx == store.getChildCount(p) - 1) {
        status = resources.dropInsertBelow();
      }

      event.getStatusProxy().setStatus(true, status);

      if (before) {
        showInsert(event, e, true);
      } else {
        showInsert(event, e, false);
      }
    }
  }

  protected void handleInsertDrop(DndDropEvent event, TreeNode<M> item, int index) {
    List<?> droppedItems = (List<?>) event.getData();
    if (droppedItems.size() > 0) {
      int idx = getWidget().getStore().indexOf(item.getModel());
      idx = status == 0 ? idx : idx + 1;
      M p = getWidget().getStore().getParent(item.getModel());
      appendModel(p, droppedItems, idx);
    }
  }

  @Override
  protected void onDragCancelled(DndDragCancelEvent event) {
    super.onDragCancelled(event);
    if (autoScroll) {
      scrollSupport.stop();
    }
  }

  @Override
  protected void onDragDrop(DndDropEvent event) {
    super.onDragDrop(event);

    if (activeItem != null && status == -1) {
      clearStyle(activeItem);
      if (event.getData() != null) {
        handleAppendDrop(event, activeItem);
      }
    } else if (activeItem != null && status != -1) {
      if (event.getData() != null) {
        handleInsertDrop(event, activeItem, status);
      }
    } else if (activeItem == null && status == -1) {
      if (event.getData() != null) {
        handleAppendDrop(event, activeItem);
      }
    } else {
      // event.setCancelled(true);
    }
    getWidget().setTrackMouseOver(restoreTrackMouse);
    status = -1;
    activeItem = null;
    appendItem = null;

    if (autoScroll) {
      scrollSupport.stop();
    }
  }

  @Override
  protected void onDragEnter(DndDragEnterEvent event) {
    super.onDragEnter(event);
    event.getStatusProxy().setStatus(false);
    restoreTrackMouse = getWidget().isTrackMouseOver();
    getWidget().setTrackMouseOver(false);
    if (autoScroll) {
      if (scrollSupport == null) {
        scrollSupport = new AutoScrollSupport(getWidget().getElement());
      } else if (scrollSupport.getScrollElement() == null) {
        scrollSupport.setScrollElement(getWidget().getElement());
      }
      scrollSupport.start();
    }
  }

  @Override
  protected void onDragFail(DndDropEvent event) {
    super.onDragFail(event);
    if (autoScroll) {
      scrollSupport.stop();
    }
  }

  @Override
  protected void onDragLeave(DndDragLeaveEvent event) {
    super.onDragLeave(event);
    if (activeItem != null) {
      clearStyle(activeItem);
      activeItem = null;
    }
    getWidget().setTrackMouseOver(restoreTrackMouse);
    if (autoScroll) {
      scrollSupport.stop();
    }
  }

  @Override
  protected void onDragMove(DndDragMoveEvent event) {
    event.setCancelled(false);
  }

  @Override
  protected void showFeedback(DndDragMoveEvent event) {
    Element target = getElementFromEvent(event.getDragMoveEvent().getNativeEvent());

    // TODO this might not get the right element
    final TreeNode<M> item = getWidget().findNode(Element.as(target));

    if (item == null && activeItem != null) {
      clearStyle(activeItem);
    }

    if (item != null && event.getDropTarget().getWidget() == event.getDragSource().getWidget()) {
      @SuppressWarnings("unchecked")
      Tree<M, ?> source = (Tree<M, ?>) event.getDragSource().getWidget();
      List<M> list = source.getSelectionModel().getSelection();
      M overModel = item.getModel();
      for (int i = 0; i < list.size(); i++) {
        M sel = list.get(i);
        if (overModel == sel) {
          Insert.get().hide();
          event.getStatusProxy().setStatus(false);
          return;
        }
        List<M> children = getWidget().getStore().getAllChildren(sel);
        if (children.contains(item.getModel())) {
          Insert.get().hide();
          event.getStatusProxy().setStatus(false);
          return;
        }
      }
    }

    boolean append = feedback == Feedback.APPEND || feedback == Feedback.BOTH;
    boolean insert = feedback == Feedback.INSERT || feedback == Feedback.BOTH;

    if (item == null) {
      handleAppend(event, item);
    } else if (insert) {
      handleInsert(event, item);
    } else if ((!getWidget().isLeaf(item.getModel()) || allowDropOnLeaf) && append) {
      handleAppend(event, item);
    } else {
      if (activeItem != null) {
        clearStyle(activeItem);
      }
      status = -1;
      activeItem = null;
      appendItem = null;

      Insert.get().hide();
      event.getStatusProxy().setStatus(false);
    }
  }

  private void showInsert(DndDragMoveEvent event, Element elem, boolean before) {
    Insert insert = Insert.get();

    insert.show(elem);
    Rectangle rect = elem.<XElement> cast().getBounds();

    int y = before ? rect.getY() - 2 : (rect.getY() + rect.getHeight() - 4);

    // dont call setBounds though component as it expects widget to be attached
    insert.getElement().setBounds(rect.getX(), y, rect.getWidth(), 6);
  }
}
