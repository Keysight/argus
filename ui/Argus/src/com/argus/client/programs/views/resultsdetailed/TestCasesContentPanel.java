package com.argus.client.programs.views.resultsdetailed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.argus.client.Argus;
import com.argus.client.ArgusServiceAsync;
import com.argus.client.images.DesktopImages;
import com.argus.client.programs.textfile.ViewTestCaseLogAndScript;
import com.argus.client.programs.views.resultsdetailed.images.ResultsDetailedImages;
import com.argus.client.util.apps.PLogger;
import com.argus.client.util.events.ResultsDetailedSelectionEvent;
import com.argus.shared.DbResultsTestCase;
import com.argus.shared.DbResultsTestCaseProperties;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.LiveGridViewUpdateEvent;
import com.sencha.gxt.widget.core.client.event.LiveGridViewUpdateEvent.LiveGridViewUpdateHandler;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent.RowDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.LiveGridView;
import com.sencha.gxt.widget.core.client.grid.filters.DateFilter;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.info.Info;
public class TestCasesContentPanel extends ContentPanel {

	private final ArgusServiceAsync ArgusService = GWT.create(com.argus.client.ArgusService.class);

	DbResultsTestCaseProperties testCaseProps = GWT.create(DbResultsTestCaseProperties.class);
	final ListStore<DbResultsTestCase> testCaseStore = new ListStore<DbResultsTestCase>(testCaseProps.id());

	final Grid<DbResultsTestCase> testCaseGrid;

	private GridFilters<DbResultsTestCase> testCasesFilters;
	final StringFilter<DbResultsTestCase> test_pass_failFilter;
	final StringFilter<DbResultsTestCase> suiteFilter;
	StringFilter<DbResultsTestCase> card_snFilter;

	private List<Integer> selectedKEYs = new ArrayList<Integer>();
	private List<String> selectedPaths = new ArrayList<String>();

	public TestCasesContentPanel (final SimpleEventBus ArgusEventBus, Integer aRunKEY, String aCard_sn) {
		this(ArgusEventBus, aRunKEY);
		if (aCard_sn != null) {
			card_snFilter.setActive(false,false);
			card_snFilter.setValue(aCard_sn);
			card_snFilter.setActive(true,false);
		}
	}

	public TestCasesContentPanel (final SimpleEventBus ArgusEventBus, Integer aKEY) {

		RpcProxy<FilterPagingLoadConfig, PagingLoadResult<DbResultsTestCase>> rpcProxy = new RpcProxy<FilterPagingLoadConfig, PagingLoadResult<DbResultsTestCase>>() {
			@Override
			public void load(FilterPagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<DbResultsTestCase>> callback) {
				ArgusService.getTestCasePage(aKEY, loadConfig, callback);
			}
		};


		/***********************************************************
		 * 
		 * Bottom Test Cases detailed area
		 * 		
		 ***********************************************************/
		ColumnConfig<DbResultsTestCase, Integer>  testCaseRowNumbererColumn      = new ColumnConfig<DbResultsTestCase, Integer>(testCaseProps.index(),                      40, "");
		ColumnConfig<DbResultsTestCase, String>  test_nameColumn                 = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_name(),                   100, "Test Case");
		ColumnConfig<DbResultsTestCase, String>  test_pass_failColumn            = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_result(),               55, "Result");
		ColumnConfig<DbResultsTestCase, String>  test_errorColumn                = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_error(),                  150, "Message");
		ColumnConfig<DbResultsTestCase, String>  download_logColumn              = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.setup_sn(),                      40, "D");
		//ColumnConfig<DbResultsTestCase, String>  bug_idColumn                    = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.bug_id(),                       60, "BUG Id");
		//ColumnConfig<DbResultsTestCase, String>  bug_stateColumn                 = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.bug_state(),                    60, "Bug State");
		ColumnConfig<DbResultsTestCase, String>  test_durationColumn             = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_duration(),                70, "Run Time");
		ColumnConfig<DbResultsTestCase, String>  ports_typeColumn                = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.ports_type(),                  100, "Port Type");
		ColumnConfig<DbResultsTestCase, String>  chassis_typeColumn              = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.setup_type(),                100, "Chassis Type");
		ColumnConfig<DbResultsTestCase, String>  card_snColumn              	 = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.setup_sn(),                      50, "SN");
		ColumnConfig<DbResultsTestCase, String>  chassis_addressColumn           = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.setup_address(),              70, "Chassis");
		ColumnConfig<DbResultsTestCase, String>  chassis_portsColumn             = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.setup_ports(),               100, "Ports");
		ColumnConfig<DbResultsTestCase, String>  test_client_addressColumn       = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_client_address(),          80, "Client IP");
		ColumnConfig<DbResultsTestCase, String>  test_client_portColumn          = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_client_port(),             50, "Port");
		ColumnConfig<DbResultsTestCase, String>  test_topologyColumn             = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_topology(),               100, "Topology");
		ColumnConfig<DbResultsTestCase, String>  test_hash_codeColumn            = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_hash_code(),              140, "MD5");
		ColumnConfig<DbResultsTestCase, String>  suiteColumn                     = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.suite(),                       100, "Suite");
		ColumnConfig<DbResultsTestCase, Date>    test_start_timeColumn           = new ColumnConfig<DbResultsTestCase, Date>(testCaseProps.test_start_time(),               100, "Start Time");
		ColumnConfig<DbResultsTestCase, String>  ixtracker_tc_idColumn           = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.tc_management(),             100, "TC Id");
		final ColumnConfig<DbResultsTestCase, String>  test_aggregated_areaColumn      = new ColumnConfig<DbResultsTestCase, String>(testCaseProps.test_aggregated_area(),  100, "Regression Island (Area)");

		//MG: be careful this is GWT date Cell not GXT date cell on a GXT column
		Cell<Date> cell = new DateCell(DateTimeFormat.getFormat("yyyy/MM/dd HH:mm:ss"));
		test_start_timeColumn.setCell(cell);

		testCaseRowNumbererColumn.setFixed(true);
		test_nameColumn.setFixed(false);           
		test_pass_failColumn.setFixed(true);      
		test_errorColumn.setFixed(false);          
		download_logColumn.setFixed(true);        
		//bug_idColumn.setFixed(false);              
		//bug_stateColumn.setFixed(false);           
		test_durationColumn.setFixed(true);       
		ports_typeColumn.setFixed(false);          
		chassis_typeColumn.setFixed(false);        
		card_snColumn.setFixed(true);             
		chassis_addressColumn.setFixed(false);     
		chassis_portsColumn.setFixed(false);       
		test_client_addressColumn.setFixed(true); 
		test_client_portColumn.setFixed(true);    
		test_topologyColumn.setFixed(true);       
		test_hash_codeColumn.setFixed(false);      
		suiteColumn.setFixed(false);               
		test_start_timeColumn.setFixed(false);     
		ixtracker_tc_idColumn.setFixed(false);   

		test_nameColumn.setHidden(false);
		test_pass_failColumn.setHidden(false);
		test_errorColumn.setHidden(false);
		//bug_idColumn.setHidden(true);
		//bug_stateColumn.setHidden(true);
		download_logColumn.setHidden(false);
		test_durationColumn.setHidden(false);
		ports_typeColumn.setHidden(true);
		chassis_typeColumn.setHidden(true);
		card_snColumn.setHidden(false);
		chassis_addressColumn.setHidden(true);
		chassis_portsColumn.setHidden(true);
		test_client_addressColumn.setHidden(false);
		test_client_portColumn.setHidden(false);
		test_topologyColumn.setHidden(false);
		test_hash_codeColumn.setHidden(true);
		suiteColumn.setHidden(false);
		test_start_timeColumn.setHidden(true);
		ixtracker_tc_idColumn.setHidden(true);
		test_aggregated_areaColumn.setHidden(true);

		testCaseRowNumbererColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		test_pass_failColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		test_errorColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		download_logColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		test_durationColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		ports_typeColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		chassis_typeColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		card_snColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		chassis_addressColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		chassis_portsColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		test_client_addressColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		test_client_portColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		test_topologyColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		test_hash_codeColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		suiteColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		test_start_timeColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		ixtracker_tc_idColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		test_aggregated_areaColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);




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

		//RowNumberer<DbResultsTestCase> testCaseRowNumbererColumn = new RowNumberer<DbResultsTestCase>(identity);
		//testCaseRowNumbererColumn.setWidth(30);

		final CheckBoxSelectionModel<DbResultsTestCase> testCaseCBSM = new CheckBoxSelectionModel<DbResultsTestCase>(identity);

		testCaseCBSM.setSelectionMode(SelectionMode.SIMPLE);

		List<ColumnConfig<DbResultsTestCase, ?>> testCaseColumnConfig = new ArrayList<ColumnConfig<DbResultsTestCase, ?>>();
		testCaseColumnConfig.add(testCaseRowNumbererColumn);
		testCaseColumnConfig.add(testCaseCBSM.getColumn());
		testCaseColumnConfig.add(test_nameColumn);
		testCaseColumnConfig.add(test_pass_failColumn);
		testCaseColumnConfig.add(test_errorColumn);
		testCaseColumnConfig.add(download_logColumn);
		//testCaseColumnConfig.add(bug_idColumn);
		//testCaseColumnConfig.add(bug_stateColumn);
		testCaseColumnConfig.add(test_durationColumn);
		testCaseColumnConfig.add(ports_typeColumn);
		testCaseColumnConfig.add(chassis_typeColumn);
		testCaseColumnConfig.add(card_snColumn);
		testCaseColumnConfig.add(chassis_addressColumn);
		testCaseColumnConfig.add(chassis_portsColumn);
		testCaseColumnConfig.add(test_client_addressColumn);
		testCaseColumnConfig.add(test_client_portColumn);
		testCaseColumnConfig.add(test_topologyColumn);
		testCaseColumnConfig.add(test_hash_codeColumn);
		testCaseColumnConfig.add(suiteColumn);
		testCaseColumnConfig.add(test_start_timeColumn);
		testCaseColumnConfig.add(ixtracker_tc_idColumn);
		testCaseColumnConfig.add(test_aggregated_areaColumn);

		StringFilter<DbResultsTestCase>           test_nameFilter               = new StringFilter<DbResultsTestCase>(testCaseProps.test_name());
		test_pass_failFilter          											= new StringFilter<DbResultsTestCase>(testCaseProps.test_result());
		StringFilter<DbResultsTestCase>           test_errorFilter              = new StringFilter<DbResultsTestCase>(testCaseProps.test_error());
		//StringFilter<DbResultsTestCase>           bug_idFilter                  = new StringFilter<DbResultsTestCase>(testCaseProps.bug_id());
		//StringFilter<DbResultsTestCase>           bug_stateFilter               = new StringFilter<DbResultsTestCase>(testCaseProps.bug_state());
		StringFilter<DbResultsTestCase>           test_durationFilter           = new StringFilter<DbResultsTestCase>(testCaseProps.test_duration());
		StringFilter<DbResultsTestCase>           ports_typeFilter              = new StringFilter<DbResultsTestCase>(testCaseProps.ports_type());
		StringFilter<DbResultsTestCase>           chassis_typeFilter            = new StringFilter<DbResultsTestCase>(testCaseProps.setup_type());
		card_snFilter                 											= new StringFilter<DbResultsTestCase>(testCaseProps.setup_sn());		
		StringFilter<DbResultsTestCase>           chassis_addressFilter         = new StringFilter<DbResultsTestCase>(testCaseProps.setup_address());
		StringFilter<DbResultsTestCase>           chassis_portsFilter           = new StringFilter<DbResultsTestCase>(testCaseProps.setup_ports());
		StringFilter<DbResultsTestCase>           test_client_addressFilter     = new StringFilter<DbResultsTestCase>(testCaseProps.test_client_address());
		StringFilter<DbResultsTestCase>           test_client_portFilter        = new StringFilter<DbResultsTestCase>(testCaseProps.test_client_port());
		StringFilter<DbResultsTestCase>           test_topologyFilter           = new StringFilter<DbResultsTestCase>(testCaseProps.test_topology());
		StringFilter<DbResultsTestCase>           test_hash_codeFilter          = new StringFilter<DbResultsTestCase>(testCaseProps.test_hash_code());
		suiteFilter                   											= new StringFilter<DbResultsTestCase>(testCaseProps.suite());
		DateFilter<DbResultsTestCase>             test_start_timeFilter         = new DateFilter<DbResultsTestCase>(testCaseProps.test_start_time());
		StringFilter<DbResultsTestCase>           ixtracker_tc_idFilter         = new StringFilter<DbResultsTestCase>(testCaseProps.tc_management());

		ColumnModel<DbResultsTestCase> testCaseCM = new ColumnModel<DbResultsTestCase>(testCaseColumnConfig);

		final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<DbResultsTestCase>> gridLoader = new PagingLoader<FilterPagingLoadConfig, PagingLoadResult<DbResultsTestCase>>(rpcProxy);
		gridLoader.useLoadConfig(new FilterPagingLoadConfigBean());
		gridLoader.setRemoteSort(true);
		gridLoader.addLoadHandler(new LoadResultListStoreBinding<FilterPagingLoadConfig, DbResultsTestCase, PagingLoadResult<DbResultsTestCase>>(testCaseStore));

		final LiveGridView<DbResultsTestCase> liveView = new LiveGridView<DbResultsTestCase>();
		liveView.setAutoExpandColumn(test_nameColumn);
		liveView.addLiveGridViewUpdateHandler(new LiveGridViewUpdateHandler() {
			@Override
			public void onUpdate(LiveGridViewUpdateEvent event) {

				for (DbResultsTestCase item: testCaseStore.getAll()) {
					if (selectedKEYs.contains(item.getKEY())) {
						testCaseCBSM.select(item, true);
					}
				}
				// size of the view, how many rows are in view (page size can change and is based on size of the view)
				//int pageSize = event.getRowCount();

				// index of the top row in view view
				//int viewIndexStart = event.getViewIndex();

				// index of the bottom row in the view
				//int viewIndexEnd = viewIndexStart + pageSize;

				// total length of the result set, provided by the loader
				//int totalCount = event.getTotalCount();

				// calculated page index, based on page size
				//int page = viewIndexStart / pageSize + 1;

				//PLogger.log("PS>" + pageSize + " VIS>" + viewIndexStart + " VIE>" + viewIndexEnd + " TC>" + totalCount + " P>" + page);

			}
		});


		testCaseGrid = new Grid<DbResultsTestCase>(testCaseStore, testCaseCM) {
			@Override
			protected void onAfterFirstAttach() {
				super.onAfterFirstAttach();
				// After the Grid has been attached to DOM load the data
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						gridLoader.load(0, liveView.getCacheSize());
					}
				});
			}
		};

		testCaseGrid.setLoader(gridLoader);
		testCaseGrid.setView(liveView);

		//		testCaseGrid.addBodyScrollHandler(new BodyScrollHandler() {
		//
		//			@Override
		//			public void onBodyScroll(BodyScrollEvent event) {
		//				for (DbResultsTestCase item: testCaseStore.getAll()) {
		//					if (selectedKEYs.contains(item.getKEY())) {
		//						testCaseCBSM.select(item, true);
		//					}
		//				}
		//				
		//			}});


		//		final GroupingView<DbResultsTestCase> groupedview = new GroupingView<DbResultsTestCase>();
		//		groupedview.setForceFit(true);
		//		groupedview.setStripeRows(true);
		//		groupedview.setAutoFill(true);
		//		groupedview.setShowGroupedColumn(true);
		//		groupedview.setForceFit(true);
		//		groupedview.setTrackMouseOver(true);
		//		groupedview.setColumnLines(true);
		//		testCaseGrid.setView(groupedview);


		testCasesFilters = new GridFilters<DbResultsTestCase>(gridLoader);
		testCasesFilters.initPlugin(testCaseGrid);
		testCasesFilters.addFilter(test_nameFilter);
		testCasesFilters.addFilter(test_pass_failFilter);
		testCasesFilters.addFilter(test_errorFilter);
		//testCasesFilters.addFilter(bug_idFilter);
		//testCasesFilters.addFilter(bug_stateFilter);
		testCasesFilters.addFilter(test_durationFilter);
		testCasesFilters.addFilter(ports_typeFilter);
		testCasesFilters.addFilter(chassis_typeFilter);
		testCasesFilters.addFilter(card_snFilter);
		testCasesFilters.addFilter(chassis_addressFilter);
		testCasesFilters.addFilter(chassis_portsFilter);
		testCasesFilters.addFilter(test_client_addressFilter);
		testCasesFilters.addFilter(test_client_portFilter);
		testCasesFilters.addFilter(test_topologyFilter);
		testCasesFilters.addFilter(test_hash_codeFilter);
		testCasesFilters.addFilter(suiteFilter);
		testCasesFilters.addFilter(test_start_timeFilter);
		testCasesFilters.addFilter(ixtracker_tc_idFilter);


		SafeStyles btnPaddingStyle = SafeStylesUtils.fromTrustedString("padding: 1px 3px 0;");
		download_logColumn.setColumnTextClassName(CommonStyles.get().inlineBlock());
		download_logColumn.setColumnTextStyle(btnPaddingStyle);

		ButtonCell<String> download_logButton = new ButtonCell<String>();
		download_logButton.setIcon(ResultsDetailedImages.INSTANCE.download_16());

		download_logButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Context c = event.getContext();
				int row = c.getIndex();
				DbResultsTestCase testCaseItem = testCaseStore.get(row);

				ArgusService.getTestLogDownloadPath(testCaseItem.getKEY(), new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						PLogger.log("ERROR getTestLogDownloadPath" + Objects.toString(caught));
					}
					public void onSuccess(final String link) {
						com.google.gwt.user.client.Window.open(link, "_argus_ui_download", "");
					}
				});			
			}
		});

		download_logColumn.setCell(download_logButton);





		MenuItem hideRun = new MenuItem();  
		hideRun.setText("Hide this run");
		hideRun.setIcon(DesktopImages.INSTANCE.delete());
		hideRun.addSelectionHandler(new SelectionHandler<Item>() {  
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				// TODO implement the row hide
				Integer runKey = testCaseGrid.getSelectionModel().getSelectedItem().getRun_key();
				PLogger.log("hide" + Objects.toString(runKey));
				PLogger.log("Debug" + "Implement me");
				//				ArgusService.hideRegressionRun(run_date,
				//						new AsyncCallback<String>() {
				//					public void onFailure(Throwable caught) {
				//						PLogger.log("Error",Objects.toString(caught));
				//						Argus.log("ERR",Objects.toString(caught));
				//					}
				//					public void onSuccess(String result) {
				//						PLogger.log("Info",result);
				//					}
				//				});				

			}  
		}); 

		Menu viewContextMenu = new Menu();  

		MenuItem allUsersMenuItem = new MenuItem();
		allUsersMenuItem.setText("Show TC History on All Users");
		allUsersMenuItem.setIcon(DesktopImages.INSTANCE.user_group());
		allUsersMenuItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				DbResultsTestCase test = testCaseGrid.getSelectionModel().getSelectedItem();
				String path = test.getTest_location();
				Argus.desktop.activate(new ViewTestCaseHistory(ArgusEventBus, null, path));
			}
		});

		viewContextMenu.add(allUsersMenuItem);

		MenuItem curentUsersMenuItem = new MenuItem();
		curentUsersMenuItem.setText("Show TC History on Curent User");
		curentUsersMenuItem.setIcon(DesktopImages.INSTANCE.user());
		curentUsersMenuItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				DbResultsTestCase test = testCaseGrid.getSelectionModel().getSelectedItem();
				String path = test.getTest_location();
				Argus.desktop.activate(new ViewTestCaseHistory(ArgusEventBus, Argus.loggedInUser.getRunOwner(), path));
			}
		});

		viewContextMenu.add(curentUsersMenuItem);

		testCaseGrid.getView().setAutoFill(true);
		testCaseGrid.getView().setColumnLines(true);
		testCaseGrid.getView().setForceFit(true);
		testCaseGrid.getView().setStripeRows(true);
		testCaseGrid.getView().setTrackMouseOver(true);
		testCaseGrid.setColumnReordering(true);
		testCaseGrid.setColumnResize(true);
		testCaseGrid.setContextMenu(viewContextMenu);
		testCaseGrid.setLoadMask(true);
		testCaseGrid.setSelectionModel(testCaseCBSM);
		testCaseGrid.setShadow(false);

		//		testCaseCBSM.addSelectionChangedHandler(new SelectionChangedHandler<DbResultsTestCase>() {
		//
		//			@Override
		//			public void onSelectionChanged(SelectionChangedEvent<DbResultsTestCase> event) {
		//				event.
		//				for (DbResultsTestCase item : event.getSelection()) {
		//					if (selectedKEYs.contains(item.getKEY())) {
		//						Info.display("onSelectionChanged", "remove");
		//						selectedKEYs.remove(item.getKEY());
		//					} else {
		//						Info.display("onSelectionChanged", "add");
		//						selectedKEYs.add(item.getKEY());
		//					}
		//				}
		//			}
		//		});

		//testCaseCBSM.

		testCaseCBSM.addSelectionHandler(new SelectionHandler<DbResultsTestCase>() {
			@Override
			public void onSelection(SelectionEvent<DbResultsTestCase> event) {
				if (testCaseCBSM.isSelectAllChecked()) {
					selectAllItemsAndFilter(ArgusEventBus, aKEY, null);
				} else if (!selectedKEYs.contains(event.getSelectedItem().getKEY())) {
					selectedKEYs.add(event.getSelectedItem().getKEY());
					selectedPaths.add(event.getSelectedItem().getTest_location());
					ArgusEventBus.fireEvent(new ResultsDetailedSelectionEvent(selectedKEYs.size()));
				}

				//Info.display("onSelectionChanged", "add " + selectedKEYs.size());
			}
		});


		testCaseGrid.addRowDoubleClickHandler(new RowDoubleClickHandler() {
			@Override
			public void onRowDoubleClick(RowDoubleClickEvent event) {
				final DbResultsTestCase testCaseItem = testCaseGrid.getSelectionModel().getSelectedItem();
				testCaseGrid.getSelectionModel().deselectAll();
				selectedKEYs.clear();
				selectedPaths.clear();
				ArgusEventBus.fireEvent(new ResultsDetailedSelectionEvent(selectedKEYs.size()));
				Argus.desktop.activate(new ViewTestCaseLogAndScript(ArgusEventBus, testCaseItem));
			}
		});

		this.setHeaderVisible(false);
		this.setCollapsible(false);
		this.setWidget(testCaseGrid);	

	}

	public Grid<DbResultsTestCase> getTCGrid() {
		return testCaseGrid;
	}

	public StringFilter<DbResultsTestCase> getTest_pass_failFilter() {
		return test_pass_failFilter;
	}
	public StringFilter<DbResultsTestCase> getSuiteFilter() {
		return suiteFilter;
	}

	
	public ListStore<DbResultsTestCase> getTCStore() {
		return testCaseStore;
	}

	public GridFilters<DbResultsTestCase> getTCFilter() {
		return testCasesFilters;
	}

	public List<Integer> getSelectedKEYs() {
		return selectedKEYs;
	}

	public void setSelectedKEYs(List<Integer> selectedKEYs) {
		this.selectedKEYs = selectedKEYs;
	}

	public List<String> getSelectedPaths() {
		return selectedPaths;
	}

	public void setSelectedPaths(List<String> selectedPaths) {
		this.selectedPaths = selectedPaths;
	}

	public void selectAllItemsAndFilter(final SimpleEventBus ArgusEventBus, Integer aKEY, String filter) {
		PLogger.log("selectAllItemsAndFilter: " + Objects.toString(filter));
		ArgusService.getTestCasePage(aKEY, new AsyncCallback<List<DbResultsTestCase>>() {
			@Override
			public void onFailure(Throwable caught) {
				PLogger.log("ERROR getTestCasePage: " + Objects.toString(caught));
				selectedKEYs.clear();
				selectedPaths.clear();
			}
			@Override
			public void onSuccess(List<DbResultsTestCase> result) { 
				selectedKEYs.clear();
				selectedPaths.clear();
				for (DbResultsTestCase item: result) {
					if (filter == null || item.getTest_result().equals(filter)) { 
						selectedKEYs.add(item.getKEY());
						selectedPaths.add(item.getTest_location());
					}
				}
				ArgusEventBus.fireEvent(new ResultsDetailedSelectionEvent(selectedKEYs.size()));
			}
		});
	}


}