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
package com.sencha.gxt.state.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.state.client.GridStateHandler.GridState;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent.ColumnWidthChangeHandler;
import com.sencha.gxt.widget.core.client.event.SortChangeEvent;
import com.sencha.gxt.widget.core.client.event.SortChangeEvent.SortChangeHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnHiddenChangeEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnHiddenChangeEvent.ColumnHiddenChangeHandler;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * State handler for grids
 * 
 * @param <M> the grid model type
 */
public class GridStateHandler<M> extends ComponentStateHandler<GridState, Grid<M>> {

  public interface GridSortState {
    SortDir getSortDir();

    String getSortField();

    void setSortDir(SortDir dir);

    void setSortField(String sortField);
  }

  public interface GridState {
    Set<String> getHidden();

    SortDir getSortDir();

    String getSortField();

    Map<String, Integer> getWidths();

    void setHidden(Set<String> hidden);

    void setSortDir(SortDir sortDir);

    void setSortField(String field);

    void setWidths(Map<String, Integer> widths);
  }

  private class Handler implements ColumnHiddenChangeHandler, ColumnWidthChangeHandler, SortChangeHandler {

    @Override
    public void onColumnHiddenChange(ColumnHiddenChangeEvent event) {
      handleColumnHiddenChange(event);
    }

    @Override
    public void onColumnWidthChange(ColumnWidthChangeEvent event) {
      handleColumnWidthChange(event);
    }

    @Override
    public void onSortChange(SortChangeEvent event) {
      handleSortChange(event);
    }

  }

  /**
   * Creates a new grid state handler instance.
   * 
   * @param stateType the state type
   * @param component the grid
   * @param key the state key
   */
  public GridStateHandler(Class<GridState> stateType, Grid<M> component, String key) {
    super(stateType, component, key);

    init(component);
  }

  /**
   * Creates a nbew grid state handler instance.
   * 
   * @param component the grid
   */
  public GridStateHandler(Grid<M> component) {
    super(GridState.class, component);

    init(component);
  }

  /**
   * Creates a new state handler instance.
   * 
   * @param component the grid
   * @param key the state key
   */
  public GridStateHandler(Grid<M> component, String key) {
    super(GridState.class, component, key);

    init(component);
  }

  @Override
  public void applyState() {
    if (getObject().isStateful()) {
      GridState state = getState();
      Set<String> hidden = state.getHidden();
      if (hidden != null) {
        for (String path : hidden) {
          ColumnConfig<M, ?> column = getObject().getColumnModel().findColumnConfig(path);
          if (column != null) {
            column.setHidden(true);
          }
        }
      }

      Map<String, Integer> widths = state.getWidths();
      if (widths != null) {
        for (String path : widths.keySet()) {
          ColumnConfig<M, ?> column = getObject().getColumnModel().findColumnConfig(path);
          if (column != null) {
            column.setWidth(widths.get(path));
          }
        }
      }

      if (state.getSortField() != null) {
        ColumnConfig<M, ?> column = getObject().getColumnModel().findColumnConfig(
                state.getSortField());
        if (column != null) {
          getObject().getStore().clearSortInfo();
          getObject().getStore().addSortInfo(createStoreSortInfo(getObject().getStore(), column, state.getSortDir()));
        }
      }
    }
  }

  /**
   * @see com.sencha.gxt.widget.core.client.grid.GridView#createStoreSortInfo(com.sencha.gxt.widget.core.client.grid.ColumnConfig, com.sencha.gxt.data.shared.SortDir)
   */
  @SuppressWarnings("unchecked")
  protected <V> StoreSortInfo<M> createStoreSortInfo(ListStore<M> ds, ColumnConfig<M, V> column, SortDir sortDir) {
    if (column.getComparator() == null) {
      // These casts can fail, but that implies that the data model has changed, in which case app should deal with invalid state
      @SuppressWarnings("rawtypes")
      ValueProvider<M, Comparable> vp = (ValueProvider) column.getValueProvider();
      return new StoreSortInfo<M>(ds.wrapRecordValueProvider(vp), sortDir);
    } else {
      return new StoreSortInfo<M>(ds.wrapRecordValueProvider(column.getValueProvider()), column.getComparator(), sortDir);
    }
  }

  protected void handleColumnHiddenChange(ColumnHiddenChangeEvent event) {
    if (getObject().isStateful()) {
      GridState state = getState();
      Set<String> hidden = state.getHidden();
      if (hidden == null) {
        hidden = new HashSet<String>();
        state.setHidden(hidden);
      }

      if (event.getColumnConfig().isHidden()) {
        hidden.add(event.getColumnConfig().getPath());
      } else {
        hidden.remove(event.getColumnConfig().getPath());
      }

      saveState();
    }
  }

  protected void handleColumnWidthChange(ColumnWidthChangeEvent event) {
    if (getObject().isStateful()) {
      GridState state = getState();
      Map<String, Integer> widths = state.getWidths();
      if (widths == null) {
        widths = new HashMap<String, Integer>();
        state.setWidths(widths);
      }
      widths.put(event.getColumnConfig().getPath(), event.getColumnConfig().getWidth());

      saveState();
    }
  }

  protected void handleSortChange(SortChangeEvent event) {
    if (getObject().isStateful()) {
      GridState state = getState();

      state.setSortField(event.getSortInfo().getSortField());
      state.setSortDir(event.getSortInfo().getSortDir());

      saveState();
    }
  }

  protected void init(Grid<M> component) {
    Handler h = new Handler();
    component.addSortChangeHandler(h);
    component.getColumnModel().addColumnHiddenChangeHandler(h);
    component.getColumnModel().addColumnWidthChangeHandler(h);
  }

}
