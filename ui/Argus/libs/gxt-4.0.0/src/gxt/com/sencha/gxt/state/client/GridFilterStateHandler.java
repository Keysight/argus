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

import java.util.ArrayList;
import java.util.List;

import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.data.shared.loader.Loader;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.state.client.GridFilterStateHandler.GridFilterState;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;

public class GridFilterStateHandler<M> extends ComponentStateHandler<GridFilterState, Grid<M>> {

  public interface GridFilterState {
    List<FilterConfig> getFilters();

    void setFilters(List<FilterConfig> filters);
  }

  private class Handler implements StoreFilterHandler<M>, LoadHandler<FilterPagingLoadConfig, PagingLoadResult<M>> {
    @Override
    public void onFilter(StoreFilterEvent<M> event) {
      GridFilterStateHandler.this.onFilter(event);
    }

    @Override
    public void onLoad(LoadEvent<FilterPagingLoadConfig, PagingLoadResult<M>> event) {
      GridFilterStateHandler.this.onLoad(event);
    }

  }

  protected Grid<M> grid;
  protected GridFilters<M> filters;
  protected Loader<FilterPagingLoadConfig, ?> loader;

  public GridFilterStateHandler(Grid<M> grid, GridFilters<M> filters) {
    super(GridFilterState.class, grid);

    if (!filters.isLocal()) {
      assert filters.getLoader() != null : "Loader must not be null with remote filtering";
    }

    this.grid = grid;
    this.filters = filters;
    this.loader = filters.getLoader();

    init(grid, filters);
  }

  @Override
  public void applyState() {
    if (getObject().isStateful()) {
      GridFilterState state = getState();
      List<FilterConfig> stateConfigs = state.getFilters();
      if (stateConfigs == null) return;

      List<ColumnConfig<M, ?>> columns = grid.getColumnModel().getColumns();
      for (int i = 0; i < columns.size(); i++) {
        ColumnConfig<M, ?> col = grid.getColumnModel().getColumn(i);
        Filter<M, ?> filter = filters.getFilter(col.getPath());
        if (filter != null) {
          List<FilterConfig> configs = findConfigs(col.getPath(), stateConfigs);
          if (configs == null) {
            configs = new ArrayList<FilterConfig>();
          }
          filter.setFilterConfig(configs);
        }
      }
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void init(Grid<M> component, GridFilters<M> filters) {
    Handler handler = new Handler();
    if (filters.isLocal()) {
      component.getStore().addStoreFilterHandler(handler);
    } else {
      loader.addLoadHandler((LoadHandler) handler);
    }
  }

  protected void onFilter(StoreFilterEvent<M> event) {
    if (!getObject().isStateful()) {
      return;
    }
    saveFilters();
  }

  protected void onLoad(LoadEvent<FilterPagingLoadConfig, PagingLoadResult<M>> event) {
    if (!getObject().isStateful()) {
      return;
    }
    saveFilters();
  }

  protected void saveFilters() {
    GridFilterState state = getState();
    List<FilterConfig> stateConfigs = state.getFilters();
    if (stateConfigs == null) {
      stateConfigs = new ArrayList<FilterConfig>();
      state.setFilters(stateConfigs);
    }

    stateConfigs.clear();

    List<ColumnConfig<M, ?>> columns = grid.getColumnModel().getColumns();
    for (int i = 0; i < columns.size(); i++) {
      if (grid.getColumnModel().isHidden(i)) {
        continue;
      }
      ColumnConfig<M, ?> col = grid.getColumnModel().getColumn(i);
      Filter<M, ?> filter = filters.getFilter(col.getPath());
      if (filter != null && filter.isActive()) {
        List<FilterConfig> configs = filter.getFilterConfig();
        if (configs == null || configs.size() == 0) {
          continue;
        }
        for (int j = 0; j < configs.size(); j++) {
          FilterConfig actual = configs.get(j);

          FilterConfig stateConfig = StateManager.get().getDefaultStateInstance(FilterConfig.class);
          stateConfig.setField(col.getPath());
          stateConfig.setType(actual.getType());
          stateConfig.setComparison(actual.getComparison());
          stateConfig.setValue(actual.getValue());

          stateConfigs.add(stateConfig);
        }

      }
    }

    saveState();
  }

  private List<FilterConfig> findConfigs(String path, List<FilterConfig> configs) {
    List<FilterConfig> matches = new ArrayList<FilterConfig>();
    for (int i = 0; i < configs.size(); i++) {
      FilterConfig config = configs.get(i);
      if (path != null && path.equals(config.getField())) {
        matches.add(config);
      }
    }
    return matches;
  }
}
