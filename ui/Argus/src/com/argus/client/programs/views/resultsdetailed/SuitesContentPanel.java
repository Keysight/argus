package com.argus.client.programs.views.resultsdetailed;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.argus.client.Argus;
import com.argus.client.ArgusServiceAsync;
import com.argus.client.util.apps.PLogger;
import com.argus.client.util.events.ResultsDetailedSelectionEvent;
import com.argus.shared.DbResultsSuite;
import com.argus.shared.DbResultsSuiteProperties;
import com.argus.shared.DbResultsTestCase;
import com.sencha.gxt.cell.core.client.PropertyDisplayCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.IntegerPropertyEditor;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class SuitesContentPanel extends ContentPanel {

	private final ArgusServiceAsync ArgusService = GWT.create(com.argus.client.ArgusService.class);

	DbResultsSuiteProperties suiteProps = GWT.create(DbResultsSuiteProperties.class);

	final ListStore<DbResultsSuite> suiteStore = new ListStore<DbResultsSuite>(suiteProps.id());

	//public SuitesContentPanel (final SimpleEventBus ArgusEventBus, String aUser, String aPath, String aRunDate) {
	public SuitesContentPanel (final SimpleEventBus ArgusEventBus, Integer aKEY, TestCasesContentPanel testCasesContentPanel) {

		/***********************************************************
		 * 
		 * Top Suite aggregation area
		 * 		
		 ***********************************************************/
		ListStore<DbResultsTestCase> testCaseStore = testCasesContentPanel.getTCStore();

		ArgusService.getDbResultsSuite(aKEY, new AsyncCallback<List<DbResultsSuite>>() {
			@Override
			public void onFailure(Throwable caught) {
				PLogger.log("ERROR getDbResultsSuite: " +  Objects.toString(caught));
			}
			@Override
			public void onSuccess(List<DbResultsSuite> result) {
				suiteStore.addAll(result);
			}
		});
		//suiteStore.add(new DbResultsSuite("11111", 0,0,0,0,0,0,0,""));
		//suiteStore.add(new DbResultsSuite("22222", 0,0,0,0,0,0,0,""));
		//suiteStore.add(new DbResultsSuite("3333", 0,0,0,0,0,0,0,""));
		
		ColumnConfig<DbResultsSuite, String>  suiteColumnDRS = null;
		suiteColumnDRS                     = new ColumnConfig<DbResultsSuite, String>(suiteProps.suite(),                 400, "Run Location");			

		ColumnConfig<DbResultsSuite, Integer> totalColumnDRS 				     = new ColumnConfig<DbResultsSuite, Integer>(suiteProps.total(), 				80, "TOTAL");
		ColumnConfig<DbResultsSuite, Integer> passColumnDRS 					 = new ColumnConfig<DbResultsSuite, Integer>(suiteProps.pass(), 				80, "PASS");
		ColumnConfig<DbResultsSuite, Integer> failColumnDRS 					 = new ColumnConfig<DbResultsSuite, Integer>(suiteProps.fail(), 				80, "FAIL");
		ColumnConfig<DbResultsSuite, Integer> skipColumnDRS 					 = new ColumnConfig<DbResultsSuite, Integer>(suiteProps.skip(), 				80, "SKIP");
		ColumnConfig<DbResultsSuite, Integer> pass_percentColumnDRS 			 = new ColumnConfig<DbResultsSuite, Integer>(suiteProps.pass_percent(), 		80, "PASS %");
		ColumnConfig<DbResultsSuite, Integer> fail_percentColumnDRS 			 = new ColumnConfig<DbResultsSuite, Integer>(suiteProps.fail_percent(), 	    80, "FAIL %");
		ColumnConfig<DbResultsSuite, Integer> skip_percentColumnDRS 			 = new ColumnConfig<DbResultsSuite, Integer>(suiteProps.skip_percent(), 		80, "SKIP %");
		ColumnConfig<DbResultsSuite, String>  run_durationColumnDRS              = new ColumnConfig<DbResultsSuite, String>(suiteProps.run_duration(),          100, "Run Duration");

		totalColumnDRS.setFixed(true); 	
		passColumnDRS.setFixed(true); 	 	
		failColumnDRS.setFixed(true); 	 	
		skipColumnDRS.setFixed(true); 	 	
		pass_percentColumnDRS.setFixed(true); 	
		fail_percentColumnDRS.setFixed(true); 	
		skip_percentColumnDRS.setFixed(true); 	
		run_durationColumnDRS.setFixed(false); 	

		totalColumnDRS.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		passColumnDRS.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		failColumnDRS.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		skipColumnDRS.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		pass_percentColumnDRS.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		fail_percentColumnDRS.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		skip_percentColumnDRS.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		run_durationColumnDRS.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		final NumberFormat suiteNumberFormat = NumberFormat.getFormat("0");

		failColumnDRS.setCell(new PropertyDisplayCell<Integer>(new IntegerPropertyEditor(suiteNumberFormat)) {
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, Integer value, SafeHtmlBuilder sb) {
				String color = value > 0 ? "red" : "green";
				sb.appendHtmlConstant("<span style='font-weight: bold;color:" + color + "'>");
				super.render(context, value, sb);
				sb.appendHtmlConstant("</span>");
			}
		});
		if (Argus.loggedInUser.getAccountType().contentEquals("shared")) {
			pass_percentColumnDRS.setCell(new PropertyDisplayCell<Integer>(new IntegerPropertyEditor(suiteNumberFormat)) {
				@Override
				public void render(com.google.gwt.cell.client.Cell.Context context, Integer value, SafeHtmlBuilder sb) {
					String color = value < 100 ? "red" : "green";
					sb.appendHtmlConstant("<span style='font-weight: bold;color:" + color + "'>");
					super.render(context, value, sb);
					sb.appendHtmlConstant("</span>");
				}
			});

			fail_percentColumnDRS.setCell(new PropertyDisplayCell<Integer>(new IntegerPropertyEditor(suiteNumberFormat)) {
				@Override
				public void render(com.google.gwt.cell.client.Cell.Context context, Integer value, SafeHtmlBuilder sb) {
					String color = value > 0 ? "red" : "green";
					sb.appendHtmlConstant("<span style='font-weight: bold;color:" + color + "'>");
					super.render(context, value, sb);
					sb.appendHtmlConstant("</span>");
				}
			});

			skip_percentColumnDRS.setCell(new PropertyDisplayCell<Integer>(new IntegerPropertyEditor(suiteNumberFormat)) {
				@Override
				public void render(com.google.gwt.cell.client.Cell.Context context, Integer value, SafeHtmlBuilder sb) {
					String color = value >= 100 ? "red" : "green";
					sb.appendHtmlConstant("<span style='font-weight: bold;color:" + color + "'>");
					super.render(context, value, sb);
					sb.appendHtmlConstant("</span>");
				}
			});
		}

		RowNumberer<DbResultsSuite> suiteRowNumbererColumn = new RowNumberer<DbResultsSuite>(new IdentityValueProvider<DbResultsSuite>());
		suiteRowNumbererColumn.setWidth(30);		

		List<ColumnConfig<DbResultsSuite, ?>> suiteColumnConfig = new ArrayList<ColumnConfig<DbResultsSuite, ?>>();
		suiteColumnConfig.add(suiteRowNumbererColumn);
		suiteColumnConfig.add(suiteColumnDRS);
		suiteColumnConfig.add(totalColumnDRS);
		suiteColumnConfig.add(passColumnDRS);
		suiteColumnConfig.add(failColumnDRS);
		suiteColumnConfig.add(skipColumnDRS);
		suiteColumnConfig.add(pass_percentColumnDRS);
		suiteColumnConfig.add(fail_percentColumnDRS);
		suiteColumnConfig.add(skip_percentColumnDRS);
		suiteColumnConfig.add(run_durationColumnDRS);	

		ColumnModel<DbResultsSuite> suiteCM = new ColumnModel<DbResultsSuite>(suiteColumnConfig);

		final Grid<DbResultsSuite> suiteGrid = new Grid<DbResultsSuite>(suiteStore, suiteCM);
		suiteGrid.setShadow(true);

		suiteGrid.setSelectionModel(new GridSelectionModel<DbResultsSuite>());
		suiteGrid.setColumnResize(true);
		suiteGrid.setColumnReordering(true);
		suiteGrid.getView().setStripeRows(true);
		suiteGrid.getView().setAutoFill(true);
		suiteGrid.getView().setForceFit(true);
		suiteGrid.getView().setTrackMouseOver(true);
		suiteGrid.getView().setColumnLines(true);

		this.setHeaderVisible(false);
		this.setCollapsible(false);
		this.setWidget(suiteGrid);
		suiteGrid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<DbResultsSuite>() {
			@Override
			public void onSelectionChanged(final SelectionChangedEvent<DbResultsSuite> event) {
				GridFilters<DbResultsTestCase> tcFilters = testCasesContentPanel.getTCFilter();
				tcFilters.clearFilters();
				StringFilter<DbResultsTestCase> sFilter = testCasesContentPanel.getSuiteFilter();
				
				final String itemText = Objects.toString(event.getSelection().get(0).getSuite());
				sFilter.setActive(false, false);
				sFilter.setValue(itemText);
				sFilter.setActive(true, false);
				testCasesContentPanel.selectAllItemsAndFilter(ArgusEventBus, aKEY, itemText);
			}
		});
		

	}

	ListStore<DbResultsSuite> getStore() {
		return suiteStore;
	}


}