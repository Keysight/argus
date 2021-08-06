package com.argus.client.programs.views.resultsdetailed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.argus.client.Argus;
import com.argus.client.ArgusServiceAsync;
import com.argus.client.images.DesktopImages;
import com.argus.client.programs.textfile.ViewTestCaseLogAndScript;
import com.argus.client.util.valueproviders.HashMapValueProvider;
import com.argus.shared.DbResultsTestCase;
import com.argus.shared.DbResultsTestCaseProperties;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent.RowDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GroupingView;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.info.Info;

public class ViewTestCaseHistory extends Window {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final ArgusServiceAsync ArgusService = GWT.create(com.argus.client.ArgusService.class);

	DbResultsTestCaseProperties testCaseProps = GWT.create(DbResultsTestCaseProperties.class);

	final ListStore<DbResultsTestCase> testCaseStore = new ListStore<DbResultsTestCase>(testCaseProps.id());


	public ViewTestCaseHistory(final SimpleEventBus ArgusEventBus, final String aUser, String aPath, String snNumber) {

		final String fUser    = aUser;
		final String fPath    = aPath;
		final String fRunDate = null;
		final String fsnNumber = snNumber;


		ColumnConfig<DbResultsTestCase, String>  linux_userColumn                = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.run_owner(),                  100, "Linux User");
		ColumnConfig<DbResultsTestCase, Date>  run_start_dateColumn                  = new ColumnConfig<DbResultsTestCase, Date>(testCaseProps.run_start_date(),                    100, "Run Date");
		//		ColumnConfig<DbResultsTestCase, String>  test_start_timeColumn             = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_start_time(),               100, "Test Run Date");
		ColumnConfig<DbResultsTestCase, String>  test_nameColumn                 = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_name(),                   100, "Test Case");
		ColumnConfig<DbResultsTestCase, String>  test_pass_failColumn            = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_result(),                    60, "Result");
		ColumnConfig<DbResultsTestCase, String>  test_errorColumn                = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_error(),                   60, "Message");
		ColumnConfig<DbResultsTestCase, String>  test_durationColumn             = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_duration(),                70, "Run Time");
		//D		ColumnConfig<DbResultsTestCase, String>  bug_idColumn                    = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.bug_id(),                       60, "BUG Id");
		//D		ColumnConfig<DbResultsTestCase, String>  bug_stateColumn                 = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.bug_state(),                    60, "Bug State");
		//D		ColumnConfig<DbResultsTestCase, String>  run_durationColumn              = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.run_duration(),                 80, "Run Time");
		ColumnConfig<DbResultsTestCase, String>  ports_typeColumn                = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.ports_type(),                  100, "Port Type");
		ColumnConfig<DbResultsTestCase, String>  chassis_typeColumn              = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.setup_type(),                100, "Chassis Type");
		ColumnConfig<DbResultsTestCase, String>  card_snColumn                   = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.setup_sn(),                     100, "Card SN");
		ColumnConfig<DbResultsTestCase, String>  chassis_addressColumn           = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.setup_address(),             100, "Chassis");
		ColumnConfig<DbResultsTestCase, String>  chassis_portsColumn             = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.setup_ports(),               100, "Ports");
		ColumnConfig<DbResultsTestCase, String>  test_client_addressColumn       = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_client_address(),         100, "Tcl Server");
		ColumnConfig<DbResultsTestCase, String>  test_client_portColumn          = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_client_port(),            100, "Tcl Port");
		ColumnConfig<DbResultsTestCase, String>  test_topologyColumn             = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_topology(),               100, "Topology");
		ColumnConfig<DbResultsTestCase, String>  test_hash_codeColumn            = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_hash_code(),                   140, "MD5");
		ColumnConfig<DbResultsTestCase, String>  suiteColumn                     = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.suite(),                       100, "Suite");
		final ColumnConfig<DbResultsTestCase, String>  test_locationColumn       = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_location(),                   450, "Test Case Location");


		if (aUser == null) {
			linux_userColumn.setHidden(false);
		} else  {
			linux_userColumn.setHidden(true);
		}
		//D		run_dateColumn.setHidden(false);
		if (fsnNumber==null) {test_nameColumn.setHidden(true);}
		run_start_dateColumn.setHidden(false);
		test_pass_failColumn.setHidden(false);
		test_errorColumn.setHidden(false);
		test_durationColumn.setHidden(false);
		//D		bug_idColumn.setHidden(true);
		//D		bug_stateColumn.setHidden(true);
		//D		run_durationColumn.setHidden(false);
		ports_typeColumn.setHidden(false);
		chassis_typeColumn.setHidden(true);
		card_snColumn.setHidden(false);
		chassis_addressColumn.setHidden(true);
		chassis_portsColumn.setHidden(true);
		test_client_addressColumn.setHidden(true);
		test_client_portColumn.setHidden(true);
		test_topologyColumn.setHidden(true);
		test_locationColumn.setHidden(true);
		test_hash_codeColumn.setHidden(false);
		suiteColumn.setHidden(true);

		test_pass_failColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		test_durationColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		test_errorColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		//D		run_durationColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		ports_typeColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		chassis_typeColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		card_snColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		chassis_addressColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		chassis_portsColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		test_client_addressColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		test_client_portColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		test_topologyColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		test_locationColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		test_hash_codeColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		suiteColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);


		test_pass_failColumn.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				String color = "black";
				if (value.equals("FAIL")) { color = "red"; }
				if (value.equals("PASS")) { color = "green"; }
				if (value.equals("EXEC")) { color = "blue"; }
				if (value.equals("SKIP")) { color = "grey"; }
				sb.appendHtmlConstant("<span style='font-weight: bold;color:" + color + "'>" + value + "</span>");
			}
		}); 

		//    sb.appendHtmlConstant("<span " + style + " qtitle='Change' qtip='" + v + "'>" + v + "</span>");

		IdentityValueProvider<DbResultsTestCase> identity = new IdentityValueProvider<DbResultsTestCase>();

		RowNumberer<DbResultsTestCase> testCaseRowNumbererColumn = new RowNumberer<DbResultsTestCase>(identity);
		testCaseRowNumbererColumn.setWidth(30);

		List<ColumnConfig<DbResultsTestCase, ?>> testCaseColumnConfig = new ArrayList<ColumnConfig<DbResultsTestCase, ?>>();
		testCaseColumnConfig.add(testCaseRowNumbererColumn);
		testCaseColumnConfig.add(linux_userColumn);
		testCaseColumnConfig.add(run_start_dateColumn);
		//		testCaseColumnConfig.add(test_start_timeColumn);
		testCaseColumnConfig.add(test_nameColumn);
		testCaseColumnConfig.add(test_pass_failColumn);
		testCaseColumnConfig.add(test_durationColumn);
		testCaseColumnConfig.add(test_errorColumn);
		//D		testCaseColumnConfig.add(bug_idColumn);
		//D		testCaseColumnConfig.add(bug_stateColumn);
		//D		testCaseColumnConfig.add(run_durationColumn);
		testCaseColumnConfig.add(ports_typeColumn);
		testCaseColumnConfig.add(chassis_typeColumn);
		testCaseColumnConfig.add(card_snColumn);
		testCaseColumnConfig.add(chassis_addressColumn);
		testCaseColumnConfig.add(chassis_portsColumn);
		testCaseColumnConfig.add(test_client_addressColumn);
		testCaseColumnConfig.add(test_client_portColumn);
		testCaseColumnConfig.add(test_topologyColumn);
		testCaseColumnConfig.add(test_locationColumn);
		testCaseColumnConfig.add(test_hash_codeColumn);
		testCaseColumnConfig.add(suiteColumn);

		//MG: be careful this is GWT date Cell not GXT date cell on a GXT column
		Cell<Date> dateCell = new DateCell(DateTimeFormat.getFormat("yyyy/MM/dd HH:mm"));
		run_start_dateColumn.setCell(dateCell);		
		
		for (String product: Argus.loggedInUser.getProductNames()) {
			ColumnConfig<DbResultsTestCase, String> productColumn = new ColumnConfig<DbResultsTestCase, String>(new HashMapValueProvider(product), 70, product);

			if (product.equals("IxNetwork")) {
				if (Argus.loggedInUser.getAccountType().equals("shared")) {
					productColumn.setHidden(false);
				} else {
					productColumn.setHidden(true);
				}
			}

			if (product.equals("IxOs")) {
				productColumn.setHidden(false);
			}

			testCaseColumnConfig.add(productColumn);
		}


		StringFilter<DbResultsTestCase>           linux_userFilter              = new StringFilter<DbResultsTestCase>(testCaseProps.run_owner());
		//D		StringFilter<DbResultsTestCase>           run_dateFilter                = new StringFilter<DbResultsTestCase>(testCaseProps.run_date());
		//		StringFilter<DbResultsTestCase>           test_start_timeFilter           = new StringFilter<DbResultsTestCase>(testCaseProps.test_start_time());
		StringFilter<DbResultsTestCase>           test_nameFilter               = new StringFilter<DbResultsTestCase>(testCaseProps.test_name());
		//D TODO
		//D		StringFilter<DbResultsTestCase>           ixnetwork_build_numberFilter  = new StringFilter<DbResultsTestCase>(testCaseProps.ixnetwork_build_number());
		//D		StringFilter<DbResultsTestCase>           ixos_build_numberFilter       = new StringFilter<DbResultsTestCase>(testCaseProps.ixos_build_number());
		StringFilter<DbResultsTestCase>           test_pass_failFilter          = new StringFilter<DbResultsTestCase>(testCaseProps.test_result());
		StringFilter<DbResultsTestCase>           test_durationFilter           = new StringFilter<DbResultsTestCase>(testCaseProps.test_duration());
		StringFilter<DbResultsTestCase>           test_errorFilter              = new StringFilter<DbResultsTestCase>(testCaseProps.test_error());
		//D		StringFilter<DbResultsTestCase>           bug_idFilter                  = new StringFilter<DbResultsTestCase>(testCaseProps.bug_id());
		//D		StringFilter<DbResultsTestCase>           bug_stateFilter               = new StringFilter<DbResultsTestCase>(testCaseProps.bug_state());
		//D		StringFilter<DbResultsTestCase>           run_durationFilter            = new StringFilter<DbResultsTestCase>(testCaseProps.run_duration());
		StringFilter<DbResultsTestCase>           ports_typeFilter              = new StringFilter<DbResultsTestCase>(testCaseProps.ports_type());
		StringFilter<DbResultsTestCase>           chassis_typeFilter            = new StringFilter<DbResultsTestCase>(testCaseProps.setup_type());
		StringFilter<DbResultsTestCase>           card_snFilter                 = new StringFilter<DbResultsTestCase>(testCaseProps.setup_sn());		
		StringFilter<DbResultsTestCase>           chassis_addressFilter         = new StringFilter<DbResultsTestCase>(testCaseProps.setup_address());
		StringFilter<DbResultsTestCase>           chassis_portsFilter           = new StringFilter<DbResultsTestCase>(testCaseProps.setup_ports());
		StringFilter<DbResultsTestCase>           test_client_addressFilter     = new StringFilter<DbResultsTestCase>(testCaseProps.test_client_address());
		StringFilter<DbResultsTestCase>           test_client_portFilter        = new StringFilter<DbResultsTestCase>(testCaseProps.test_client_port());
		StringFilter<DbResultsTestCase>           test_topologyFilter           = new StringFilter<DbResultsTestCase>(testCaseProps.test_topology());
		StringFilter<DbResultsTestCase>           test_locationFilter           = new StringFilter<DbResultsTestCase>(testCaseProps.test_location());
		StringFilter<DbResultsTestCase>           test_hash_codeFilter          = new StringFilter<DbResultsTestCase>(testCaseProps.test_hash_code());
		StringFilter<DbResultsTestCase>           suiteFilter                   = new StringFilter<DbResultsTestCase>(testCaseProps.suite());

		final GroupingView<DbResultsTestCase> view = new GroupingView<DbResultsTestCase>();

		ColumnModel<DbResultsTestCase> testCaseCM = new ColumnModel<DbResultsTestCase>(testCaseColumnConfig);
		final Grid<DbResultsTestCase> testCaseGrid = new Grid<DbResultsTestCase>(testCaseStore, testCaseCM);

		final GridFilters<DbResultsTestCase> testCasesFilters = new GridFilters<DbResultsTestCase>();
		testCasesFilters.initPlugin(testCaseGrid);
		testCasesFilters.setLocal(true);
		//D		testCasesFilters.addFilter(linux_userFilter);
		//D		testCasesFilters.addFilter(run_dateFilter);
		//		testCasesFilters.addFilter(test_start_timeFilter);
		testCasesFilters.addFilter(test_nameFilter);
		//D TODO
		//D		testCasesFilters.addFilter(ixnetwork_build_numberFilter);
		//D		testCasesFilters.addFilter(ixos_build_numberFilter);
		testCasesFilters.addFilter(test_pass_failFilter);
		testCasesFilters.addFilter(test_durationFilter);
		testCasesFilters.addFilter(test_errorFilter);
		//D		testCasesFilters.addFilter(bug_idFilter);
		//D		testCasesFilters.addFilter(bug_stateFilter);
		//D		testCasesFilters.addFilter(run_durationFilter);
		testCasesFilters.addFilter(ports_typeFilter);
		testCasesFilters.addFilter(chassis_typeFilter);
		testCasesFilters.addFilter(card_snFilter);
		testCasesFilters.addFilter(chassis_addressFilter);
		testCasesFilters.addFilter(chassis_portsFilter);
		testCasesFilters.addFilter(test_client_addressFilter);
		testCasesFilters.addFilter(test_client_portFilter);
		testCasesFilters.addFilter(test_topologyFilter);
		testCasesFilters.addFilter(test_locationFilter);
		testCasesFilters.addFilter(test_hash_codeFilter);
		testCasesFilters.addFilter(suiteFilter);


		testCaseGrid.setColumnResize(true);
		testCaseGrid.setColumnReordering(true);

		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		view.setStripeRows(true);
		view.setAutoFill(true);
		view.setTrackMouseOver(true);
		view.setColumnLines(true);
		//This work for less recordslike work for loop not for MSD
		//if (fsnNumber!=null) {view.groupBy(ixos_build_numberColumn);} 
		testCaseGrid.setView(view);
		if (fsnNumber!=null) {
			card_snFilter.setValue(fsnNumber);
			card_snFilter.setActive(true,false);
		}

		//Argus.logView.log("Get history from US");
		ArgusService.getTestCaseHistory(Argus.loggedInUser, aUser, aPath, new AsyncCallback<List<DbResultsTestCase>>() {
			public void onFailure(Throwable caught) {
				Info.display("Error", Objects.toString(caught));
			}
			public void onSuccess(List<DbResultsTestCase> result) {
				//Argus.logView.log("Get history from US - results are in");
				testCaseStore.addAll(result);
				//Argus.logView.log("Get history from US - results are added to the store");
				//updateBugsInfo(result);
				//view.groupBy(test_locationColumn);
				//Argus.logView.log("Get history from US - grouping enabled");
			}
		});
		/*
		//Argus.logView.log("Get history from RO");
		ArgusService.getTestCasePageRo(fUser, fPath, fRunDate, new AsyncCallback<List<DbResultsTestCase>>() {
			public void onFailure(Throwable caught) {
				Info.display("Error",Objects.toString(caught));
			}
			public void onSuccess(List<DbResultsTestCase> result) {
				//Argus.logView.log("Get history from RO - results are in");
				//view.groupBy(null);
				//Argus.logView.log("Get history from RO - grouping disabled");
				testCaseStore.addAll(0,result);
				//Argus.logView.log("Get history from RO - results are added to the store");
				view.groupBy(test_locationColumn);
				//Argus.logView.log("Get history from RO - grouping enabled");
			}
		});
		 */
		testCaseGrid.addRowDoubleClickHandler(new RowDoubleClickHandler() {
			@Override
			public void onRowDoubleClick(RowDoubleClickEvent event) {
				final DbResultsTestCase testCaseItem = testCaseGrid.getSelectionModel().getSelectedItem();
				testCaseGrid.getSelectionModel().deselectAll();
				Argus.desktop.activate(new ViewTestCaseLogAndScript(ArgusEventBus, testCaseItem));
			}
		});


		/***********************************************************
		 * 
		 * The Window/Program area
		 * 		
		 ***********************************************************/


		this.setClosable(true);
		this.setMaximizable(true);
		this.setMinimizable(true);
		this.setHeading("Test Case History");
		this.getHeader().setIcon(DesktopImages.INSTANCE.results_detailed_16());
		this.add(testCaseGrid);


	}

	public ViewTestCaseHistory(SimpleEventBus ArgusEventBus, String aUser, String aPath) {
		this(ArgusEventBus, aUser, aPath, null);
	}

	//	private void updateBugsInfo (List<DbResultsTestCase> aTestCaseList) {
	//		for (final DbResultsTestCase testCase : aTestCaseList) {
	//			if (testCase.getTest_pass_fail().equals("FAIL")) {
	//				ArgusService.getBugInfo(testCase.getTest_error(), new AsyncCallback<List<String>>() {
	//					public void onFailure(Throwable caught) {
	//						Info.display("Error",Objects.toString(caught));
	//						//Argus.log("ERR",Objects.toString(caught));
	//					}
	//					public void onSuccess(List<String> result) {
	//						testCase.setBug_id(result.get(0));
	//						testCase.setBug_state(result.get(1));
	//						testCaseStore.update(testCase);
	//					}
	//				});
	//			}
	//		}
	//	}

	//
}