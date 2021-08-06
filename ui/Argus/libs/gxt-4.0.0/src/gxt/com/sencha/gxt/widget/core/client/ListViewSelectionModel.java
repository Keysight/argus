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
package com.sencha.gxt.widget.core.client;

import java.util.Collections;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.PointerEventsSupport;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.RefreshEvent;
import com.sencha.gxt.widget.core.client.event.RefreshEvent.RefreshHandler;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.selection.AbstractStoreSelectionModel;

/**
 * ListView selection model.
 * 
 * @param <M> the model type
 */
public class ListViewSelectionModel<M> extends AbstractStoreSelectionModel<M> {

  private class Handler implements MouseDownHandler, ClickHandler, RefreshHandler {

    @Override
    public void onClick(ClickEvent event) {
      onMouseClick(event);
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
      if (!PointerEventsSupport.impl.isSupported()) {
        ListViewSelectionModel.this.onMouseDown(event);
      }
    }

    @Override
    public void onRefresh(RefreshEvent event) {
      refresh();
      if (getLastFocused() != null) {
        listView.onHighlightRow(listStore.indexOf(getLastFocused()), true);
      }
    }

  }

  protected boolean enableNavKeys = true;
  protected KeyNav keyNav = new KeyNav() {

    @Override
    public void onDown(NativeEvent e) {
      if (isVertical) {
        onKeyDown(e);
      }
    }

    @Override
    public void onKeyPress(NativeEvent ce) {
      ListViewSelectionModel.this.onKeyPress(ce);
    }

    @Override
    public void onLeft(NativeEvent e) {
      if (!isVertical) {
        onKeyUp(e);
      }
    }

    @Override
    public void onRight(NativeEvent e) {
      if (!isVertical) {
        onKeyDown(e);
      }
    }

    @Override
    public void onUp(NativeEvent e) {
      if (isVertical) {
        onKeyUp(e);
      }
    }

  };

  protected ListStore<M> listStore;
  protected ListView<M, ?> listView;

  /**
   * True to deselect a selected item on click (defaults to {@code true).
   */
  protected boolean deselectOnSimpleClick = true;

  private boolean isVertical = true;
  private Handler handler = new Handler();
  private GroupingHandlerRegistration handlerRegistration = new GroupingHandlerRegistration();

  /**
   * Track the selection index, when the shift combined with and so then this is the starting point of the selection.
   */
  private int indexOnSelectNoShift;

  /**
   * Binds the list view to the selection model.
   *
   * @param listView the list view
   */
  public void bindList(ListView<M, ?> listView) {
    if (this.listView != null) {
      handlerRegistration.removeHandler();
      keyNav.bind(null);
      this.listStore = null;
      bind(null);
    }
    this.listView = listView;
    if (listView != null) {
      if (handlerRegistration == null) {
        handlerRegistration = new GroupingHandlerRegistration();
      }
      handlerRegistration.add(listView.addDomHandler(handler, MouseDownEvent.getType()));
      handlerRegistration.add(listView.addDomHandler(handler, ClickEvent.getType()));
      handlerRegistration.add(listView.addRefreshHandler(handler));
      keyNav.bind(listView);
      bind(listView.getStore());
      this.listStore = listView.getStore();
    }
  }

  /**
   * Returns the currently bound list view.
   *
   * @return the list view
   */
  public ListView<M, ?> getListView() {
    return listView;
  }

  /**
   * Returns {@code true} if up and down arrow keys are used for navigation. Else left and right arrow keys are used.
   *
   * @return the isVertical
   */
  public boolean isVertical() {
    return isVertical;
  }

  /**
   * Sets if up and down arrow keys or left and right arrow keys should be used (defaults to {@code true}).
   *
   * @param isVertical the isVertical to set
   */
  public void setVertical(boolean isVertical) {
    this.isVertical = isVertical;
  }

  protected void onMouseClick(ClickEvent ce) {
    onMouseClick(ce.getNativeEvent());
  }

  protected void onMouseClick(NativeEvent event) {
    XEvent e = event.cast();
    XElement target = e.getEventTargetEl();

    if (isLocked() || isInput(target)) {
      return;
    }

    if (fireSelectionChangeOnClick) {
      fireSelectionChange();
      fireSelectionChangeOnClick = false;
    }

    int index = listView.findElementIndex(target);

    if (index == -1) {
      deselectAll();
      return;
    }
    if (selectionMode == SelectionMode.MULTI) {
      M sel = listStore.get(index);
      if (e.getCtrlOrMetaKey() && isSelected(sel)) {
        // reset the starting location of the click
        indexOnSelectNoShift = index;
        doDeselect(Collections.singletonList(sel), false);
      } else if (e.getCtrlOrMetaKey()) {
        // reset the starting location of the click
        indexOnSelectNoShift = index;
        doSelect(Collections.singletonList(sel), true, false);
        listView.focusItem(index);
      } else if (isSelected(sel) && !e.getShiftKey() && !e.getCtrlOrMetaKey() && selected.size() > 1) {
        doSelect(Collections.singletonList(sel), false, false);
        listView.focusItem(index);
      }
    }
  }

  protected void onMouseDown(MouseDownEvent mde) {
    onMouseDown(mde.getNativeEvent());
  }

  protected void onMouseDown(NativeEvent event) {
    XEvent e = event.<XEvent>cast();
    XElement target = e.getEventTargetEl();
    int selIndex = listView.findElementIndex(target);

    if (selIndex == -1 || isLocked() || isInput(target)) {
      return;
    }

    mouseDown = true;

    if (e.isRightClick()) {
      if (selectionMode != SelectionMode.SINGLE && isSelected(listStore.get(selIndex))) {
        return;
      }
      select(selIndex, false);
      listView.focusItem(selIndex);
    } else {
      M sel = listStore.get(selIndex);
      if (sel == null) {
        return;
      }
      
      boolean isSelected = isSelected(sel);
      boolean isMeta = e.getCtrlOrMetaKey();
      boolean isShift = e.getShiftKey();
      
      switch (selectionMode) {
        case SIMPLE:
          listView.focusItem(selIndex);
          if (!isSelected) {
            select(sel, true);
          } else if (isSelected && deselectOnSimpleClick) {
            deselect(sel);
          }
          break;
          
        case SINGLE:
          if (isMeta && isSelected) {
            deselect(sel);
          } else if (!isSelected) {
            listView.focusItem(selIndex);
            select(sel, false);
          }
          break;
          
        case MULTI:
          if (isMeta) {
            break;
          }
          
          if (isShift && lastSelected != null) {
            int last = listStore.indexOf(lastSelected);
            listView.focusItem(last);

            int start;
            int end;
            // This deals with flipping directions
            if (indexOnSelectNoShift < selIndex) {
              start = indexOnSelectNoShift;
              end = selIndex;
            } else {
              start = selIndex;
              end = indexOnSelectNoShift;
            }
            
            select(start, end, false);
          } else if (!isSelected) {
            // reset the starting location of multi select
            indexOnSelectNoShift = selIndex;

            listView.focusItem(selIndex);
            doSelect(Collections.singletonList(sel), false, false);
          }
          break;
      }
    }

    mouseDown = false;
  }

  protected boolean isInput(Element target) {
    String tag = target.getTagName();
    return "INPUT".equals(tag) || "TEXTAREA".equals(tag);
  }

  protected void onKeyDown(NativeEvent event) {
    XEvent e = event.<XEvent>cast();

    if (!e.getCtrlOrMetaKey() && selected.size() == 0 && getLastFocused() == null) {
      select(0, false);
    } else {
      int idx = listStore.indexOf(getLastFocused());
      if (idx >= 0 && (idx + 1) < listStore.size()) {
        if (e.getCtrlOrMetaKey() || (e.getShiftKey() && isSelected(listStore.get(idx + 1)))) {
          if (!e.getCtrlOrMetaKey()) {
            deselect(idx);
          }

          M lF = listStore.get(idx + 1);
          if (lF != null) {
            setLastFocused(lF);
            listView.focusItem(idx + 1);
          }

        } else {
          if (e.getShiftKey() && lastSelected != getLastFocused()) {
            select(listStore.indexOf(lastSelected), idx + 1, true);
            listView.focusItem(idx + 1);
          } else {
            if (idx + 1 < listStore.size()) {
              select(idx + 1, e.getShiftKey());
              listView.focusItem(idx + 1);
            }
          }
        }
      }
    }

    e.preventDefault();
  }

  protected void onKeyPress(NativeEvent event) {
    XEvent e = event.<XEvent>cast();

    if (lastSelected != null && enableNavKeys) {
      int kc = e.getKeyCode();
      if (kc == KeyCodes.KEY_PAGEUP || kc == KeyCodes.KEY_HOME) {
        e.stopEvent();
        select(0, false);
        listView.focusItem(0);
      } else if (kc == KeyCodes.KEY_PAGEDOWN || kc == KeyCodes.KEY_END) {
        e.stopEvent();
        int idx = listStore.indexOf(listStore.get(listStore.size() - 1));
        select(idx, false);
        listView.focusItem(idx);
      }
    }
    // if space bar is pressed
    if (e.getKeyCode() == 32) {
      if (getLastFocused() != null) {
        if (e.getShiftKey() && lastSelected != null) {
          int last = listStore.indexOf(lastSelected);
          int i = listStore.indexOf(getLastFocused());
          select(last, i, e.getCtrlOrMetaKey());
          listView.focusItem(i);
        } else {
          if (isSelected(getLastFocused())) {
            deselect(getLastFocused());
          } else {
            select(getLastFocused(), true);
            listView.focusItem(listStore.indexOf(getLastFocused()));
          }
        }
      }
    }
  }

  protected void onKeyUp(NativeEvent event) {
    XEvent e = event.<XEvent>cast();
    int idx = listStore.indexOf(getLastFocused());
    if (idx >= 1) {
      if (e.getCtrlOrMetaKey() || (e.getShiftKey() && isSelected(listStore.get(idx - 1)))) {
        if (!e.getCtrlOrMetaKey()) {
          deselect(idx);
        }

        M lF = listStore.get(idx - 1);
        if (lF != null) {
          setLastFocused(lF);
          listView.focusItem(idx - 1);
        }

      } else {

        if (e.getShiftKey() && lastSelected != getLastFocused()) {
          select(listStore.indexOf(lastSelected), idx - 1, true);
          listView.focusItem(idx - 1);
        } else {
          if (idx > 0) {
            select(idx - 1, e.getShiftKey());
            listView.focusItem(idx - 1);
          }
        }
      }
    }

    e.preventDefault();
  }

  @Override
  protected void onLastFocusChanged(M oldFocused, M newFocused) {
    int i;
    if (oldFocused != null) {
      i = listStore.indexOf(oldFocused);
      if (i >= 0) {
        listView.onHighlightRow(i, false);
      }
    }
    if (newFocused != null) {
      i = listStore.indexOf(newFocused);
      if (i >= 0) {
        listView.onHighlightRow(i, true);
      }
    }
  }

  @Override
  protected void onSelectChange(M model, boolean select) {
    listView.onSelectChange(model, select);
  }

  void onRowUpdated(M model) {
    if (isSelected(model)) {
      onSelectChange(model, true);
    }
    if (getLastFocused() == model) {
      setLastFocused(getLastFocused());
    }
  }

}
