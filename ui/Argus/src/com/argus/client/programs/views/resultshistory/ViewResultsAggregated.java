/**
 * Port of: ResultsDataPanel.java summary grids only
 */

package com.argus.client.programs.views.resultshistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.argus.client.Argus;
import com.argus.client.ArgusServiceAsync;
import com.argus.client.programs.views.images.ViewsImages;
import com.argus.client.util.apps.PLogger;
import com.argus.client.images.DesktopImages;
import com.argus.shared.DbResultsAggregated;
import com.argus.shared.DbResultsAggregatedProperties;
import com.argus.shared.DbResultsTestCase;
import com.argus.shared.DbResultsTestCaseProperties;
import com.argus.shared.DbUserData;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;



public class ViewResultsAggregated extends Window {

	private final ArgusServiceAsync ArgusService = GWT.create(com.argus.client.ArgusService.class);

	DbResultsAggregatedProperties aggregatedProps = GWT.create(DbResultsAggregatedProperties.class);
	final ListStore<DbResultsAggregated> aggregatedStore = new ListStore<DbResultsAggregated>(aggregatedProps.id());

	final AggregatedChartContentPanel aggregatedChartContentPanel = new AggregatedChartContentPanel(null);

	final Slider historySlider = new Slider(false);

	List<Integer> runsKeys = new ArrayList<Integer>();
	final RunsCompareContentPanel runsCompareContentPanel = new RunsCompareContentPanel(null, runsKeys);

	public ViewResultsAggregated(final SimpleEventBus ArgusEventBus, final DbUserData aUser, final String aPath) {
		aggregatedChartContentPanel.setEventBus(ArgusEventBus);

		RpcProxy<FilterPagingLoadConfig, PagingLoadResult<DbResultsAggregated>> rpcProxy = new RpcProxy<FilterPagingLoadConfig, PagingLoadResult<DbResultsAggregated>>() {
			@Override
			public void load(FilterPagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<DbResultsAggregated>> callback) {
				PLogger.log("getDbResultsAggregated " + aUser.getRunOwner());
				ArgusService.getDbResultsAggregated(aUser, loadConfig, callback);
			}
		};

		/***********************************************************
		 * 
		 * The Grid area
		 * 		
		 ***********************************************************/

		// TODO: add merge row possibility to grid

		// TODO: add undo merge row possibility to grid

		// TODO: add un-hide a run option, or to be able to view hidden ones.

		// TODO: change the grid type to liveView, or something smarter to load faster, or optimize SQL queries, it's loading more rows than necesary initialy

		// TODO: update the context menu icons
		
		
		
		
		
		
		//runsCompareContentPanel.setContextMenu(viewContextMenu);
		

		ToolButton refreshGridToolButton = new ToolButton(ToolButton.REFRESH);
		refreshGridToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				refreshHistory(aUser, historySlider.getValue());
			}
		});



		// ,mid area


		TextButton exportXLSXButton = new TextButton("XLSX");
		//button1.setIcon(Resources.IMAGES.menu_show());


		historySlider.setIncrement(1);
		historySlider.setMinValue(0);
		//historySlider.setWidth(1);

		ArgusService.getDatasetSize(aUser, new AsyncCallback<Integer>() {
			public void onFailure(Throwable caught) {
				PLogger.log("Error" + Objects.toString(caught));
			}
			public void onSuccess(Integer result) {
				PLogger.log("getDatasetSize: " + Integer.toString(result));
				historySlider.setMaxValue(result);
				historySlider.setValue(result);
			}
		});

		historySlider.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				refreshHistory(aUser, event.getValue());
			}
		});

		ToolBar toolBar = new ToolBar();

		toolBar.add(exportXLSXButton);
		FillToolItem fillToolItem = new FillToolItem();
		//fillToolItem.setWidth(1);
		//toolBar.add(fillToolItem);
		toolBar.add(historySlider); 

		// this.addResizeHandler(new ResizeHandler());


		/***********************************************************
		 * 
		 * Border Layout area
		 * 		
		 ***********************************************************/	

		final BorderLayoutContainer aggregatedBorderLayoutContainer = new BorderLayoutContainer();

		BorderLayoutData northData = new BorderLayoutData(300);
		northData.setMargins(new Margins(5));
		northData.setCollapsible(true);
		//northData.setSplit(true);

		MarginData centerData = new MarginData();

		// TODO : this just does not work.
		//aggregatedChartContentPanel.collapse();


		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		vlc.add(toolBar, new VerticalLayoutData(1, 30, new Margins(0)));
		vlc.add(runsCompareContentPanel, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));

		aggregatedBorderLayoutContainer.setNorthWidget(aggregatedChartContentPanel, northData);
		aggregatedBorderLayoutContainer.setCenterWidget(vlc, centerData);

		/***********************************************************
		 * 
		 * The Window/Program area
		 * 		
		 ***********************************************************/	

		this.setClosable(true);
		this.setMaximizable(true);
		this.setMinimizable(true);
		//this.maximize();
		if (aPath == null) {
			this.setHeading("All Runs");
		} else {
			this.setHeading(aPath.substring(aPath.lastIndexOf("/")+1, aPath.length()));
		}
		this.getHeader().setIcon(ViewsImages.INSTANCE.bar_chart_16());
		this.add(aggregatedBorderLayoutContainer);
		this.addTool(refreshGridToolButton);

		refreshHistory(aUser, -1);

	}


	public void refreshHistory(DbUserData aUser, Integer startPoint) {
		PLogger.log("refreshHistory " + aUser.getRunOwner() + " -> " + Integer.toString(startPoint));
		PLogger.log("product names " + aUser.getProductNames().get(0));
		aggregatedStore.clear();
		ArgusService.getDbResultsHistoryAggregated(aUser, startPoint, new AsyncCallback<List<DbResultsAggregated>>() {
			public void onFailure(Throwable caught) {
				PLogger.log("Error" + Objects.toString(caught));
			}
			public void onSuccess(List<DbResultsAggregated> result) {
				PLogger.log("getDbResultsAggregated: " + Integer.toString(result.size()));
				runsKeys.clear();
				for (DbResultsAggregated item : result) {
					PLogger.log(Integer.toString(item.getKEY()));
					runsKeys.add(item.getKEY());
				}
				aggregatedStore.addAll(result);
				aggregatedChartContentPanel.updateChartData(result);
				runsCompareContentPanel.update(runsKeys);
			}
		});
	}

}