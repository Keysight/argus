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
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.shared.FastSet;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore.TreeNode;
import com.sencha.gxt.dnd.core.client.DND.Operation;
import com.sencha.gxt.dnd.core.client.DND.TreeSource;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 * Enables a {@link Tree} to act as the source of a drag and drop operation.
 * <p/>
 * Use {@link #setTreeSource(com.sencha.gxt.dnd.core.client.DND.TreeSource)} to specify whether leaf nodes, non-leaf
 * nodes or both types of nodes can be dragged (defaults to {@link TreeSource#BOTH}). The drag operation is cancelled
 * if the user attempts to drag a node type that is not permitted.
 * <p/>
 * The drag data consists of a list of items of type {@code <M>}. It is optimized to remove children of parents that are
 * also in the list (i.e. if a parent is the subject of a drag operation then all of its children are implicitly part of
 * the drag operation).
 * 
 * @param <M> the model type
 */
public class TreeDragSource<M> extends DragSource {

  private TreeSource treeSource = TreeSource.BOTH;

  /**
   * Creates a drag source for the specified tree.
   * 
   * @param tree the tree to enable as a drag source
   */
  public TreeDragSource(Tree<M, ?> tree) {
    super(tree);
    setStatusText("{0} items selected");
  }

  /**
   * Returns the tree associated with this drag source.
   * 
   * @return the tree associated with this drag source
   */
  public TreeSource getTreeSource() {
    return treeSource;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Tree<M, ?> getWidget() {
    return (Tree<M, ?>) super.getWidget();
  }

  /**
   * Sets the tree source (defaults to {@link TreeSource#BOTH}).
   * 
   * @param treeSource the tree source
   */
  public void setTreeSource(TreeSource treeSource) {
    this.treeSource = treeSource;
  }

  @Override
  protected void onDragDrop(DndDropEvent event) {
    if (event.getOperation() == Operation.MOVE) {
      @SuppressWarnings("unchecked")
      List<TreeNode<M>> sel = (List<TreeNode<M>>) event.getData();
      for (TreeNode<M> s : sel) {
        getWidget().getStore().remove(s.getData());
      }
      super.data = null;
    }
  }

  @Override
  protected void onDragStart(DndDragStartEvent event) {
    Element startTarget = event.getDragStartEvent().getStartElement().<Element> cast();
    Tree.TreeNode<M> start = getWidget().findNode(startTarget);
    if (start == null || !getWidget().getView().isSelectableTarget(start.getModel(), startTarget)) {
      event.setCancelled(true);
      return;
    }

    List<M> selected = getWidget().getSelectionModel().getSelectedItems();

    if (selected.size() == 0) {
      event.setCancelled(true);
      return;
    }

    List<TreeNode<M>> selectedSubTrees = new ArrayList<TreeNode<M>>();
    if (getTreeSource() == TreeSource.LEAF) {
      for (M item : selected) {
        if (getWidget().isLeaf(item)) {
          selectedSubTrees.add(getWidget().getStore().getSubTree(item));
        } else {
          // forget it, we've got a non-leaf
          event.setCancelled(true);
          return;
        }
      }
    } else {
      // If allowed to drop only nodes or both, then we need to remove items from the list
      // that also have their parents being dragged
      ModelKeyProvider<? super M> kp = getWidget().getStore().getKeyProvider();
      
      // Start by looking for all selected non-leaf items
      FastSet nonLeafKeys = new FastSet();
      for (M item : selected) {
        if (getWidget().isLeaf(item)) {
          // While we're at it, if we're only allowed nodes, cancel if we see a leaf
          if (treeSource == TreeSource.NODE) {
            event.setCancelled(true);
            return;
          }
        } else {
          nonLeafKeys.add(kp.getKey(item));
        }
      }
      
      // Walking backward (so we can remove as we go) through the list of all selected items, for
      // each item, check if it has a parent already in the list.
      // Clearly that parent is a non-leaf, so will be in the set we established in the last loop
      // Note: see TreeGridDragSource it's similar
      for (int i = selected.size() - 1; i >= 0; i--) {
        // TODO consider tracking these parents, and if they are part of another
        // parent, adding them to the keyset
        M parent = selected.get(i);
        while ((parent = getWidget().getStore().getParent(parent)) != null) {
          // If we find that this item's parent is also selected, then we can skip this item
          if (nonLeafKeys.contains(kp.getKey(parent))) {
            selected.remove(i);
            break;
          }
        }
      }
      for (M item : selected) {
        selectedSubTrees.add(getWidget().getStore().getSubTree(item));
      }
    }

    if (selectedSubTrees.size() > 0) {
      event.setData(selectedSubTrees);
    } else {
      event.setCancelled(true);
    }

    if (selected.size() > 0) {
      event.setCancelled(false);

      if (getStatusText() == null) {
        event.getStatusProxy().update(SafeHtmlUtils.fromString(
            DefaultMessages.getMessages().listField_itemsSelected(selected.size())));
      } else {
        event.getStatusProxy().update(SafeHtmlUtils.fromString(Format.substitute(getStatusText(), selected.size())));
      }
    }
  }
}
