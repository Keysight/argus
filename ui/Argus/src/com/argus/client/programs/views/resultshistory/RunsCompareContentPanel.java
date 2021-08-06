package com.argus.client.programs.views.resultshistory;

import com.sencha.gxt.widget.core.client.ContentPanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.argus.client.Argus;
import com.argus.client.ArgusServiceAsync;
import com.argus.client.images.DesktopImages;
import com.argus.client.util.apps.PLogger;
import com.argus.shared.DbResultsTestCase;
import com.argus.shared.ResultsCompare;
import com.argus.shared.ResultsCompareProperties;
import com.argus.shared.ResultsCompareValueProvider;
import com.argus.client.programs.views.resultsdetailed.ViewTestCaseHistory;
import com.argus.client.programs.textfile.ViewTestCaseLogAndScript;
import com.argus.client.programs.views.resultsdetailed.ViewResultsDetailed;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent.RowDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class RunsCompareContentPanel extends ContentPanel {

	private final ArgusServiceAsync ArgusService = GWT.create(com.argus.client.ArgusService.class);

	ResultsCompareProperties resultsProps = GWT.create(ResultsCompareProperties.class);

	final ListStore<ResultsCompare> compareResultsStore = new ListStore<ResultsCompare>(resultsProps.id());
	final Grid<ResultsCompare> compareGrid;
	
	SimpleEventBus ArgusEventBus;

	public RunsCompareContentPanel(final SimpleEventBus argusEventBus, final List<Integer> runsKeys) {
		this.ArgusEventBus = argusEventBus;

		IdentityValueProvider<ResultsCompare> identity = new IdentityValueProvider<ResultsCompare>();

		RowNumberer<ResultsCompare> compareRowNumbererColumn = new RowNumberer<ResultsCompare>(identity);
		compareRowNumbererColumn.setWidth(30);

		final List<ColumnConfig<ResultsCompare, ?>> compareColumnConfigs = new ArrayList<ColumnConfig<ResultsCompare, ?>>();
		compareColumnConfigs.add(compareRowNumbererColumn);


		ColumnConfig<ResultsCompare, String>  test_case_nameColumn  = new ColumnConfig<ResultsCompare, String>(resultsProps.test_location(),     150,  "Test Case Name");
		test_case_nameColumn.setHidden(false);
		test_case_nameColumn.setSortable(false);
		compareColumnConfigs.add(test_case_nameColumn);

		for (Integer i = 0; i < 25 ; i++) {
			// runsKeys.size()
			// runsKeys.get(i).toString()
			ColumnConfig<ResultsCompare, String>  columnColumn = new ColumnConfig<ResultsCompare, String>(new ResultsCompareValueProvider(i), 69,  "^");
			columnColumn.setHidden(false);
			columnColumn.setSortable(true);
			columnColumn.setCell(new AbstractCell<String>() {
				@Override
				public void render(Context context, String value, SafeHtmlBuilder sb) {
					String color = new String();
					if (value.equals("FAIL")) { color = "red"; }
					if (value.equals("PASS")) { color = "green"; }
					if (value.equals("EXEC")) { color = "blue"; }
					if (value.equals("SKIP")) { color = "grey"; }
					if (value.equals("N/A")) { color = "black"; }
					sb.appendHtmlConstant("<span style='font-weight: bold;color:" + color + "'>" + value + "</span>");
				}
			});

			compareColumnConfigs.add(columnColumn);
		}

		final ColumnModel<ResultsCompare> compareCM = new ColumnModel<ResultsCompare>(compareColumnConfigs);
		compareGrid = new Grid<ResultsCompare>(compareResultsStore, compareCM);

		
		Menu viewContextMenu = new Menu();  

		MenuItem allUsersMenuItem = new MenuItem();
		allUsersMenuItem.setText("Show TC History on All User");
		allUsersMenuItem.setIcon(DesktopImages.INSTANCE.user_group());

		allUsersMenuItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				ResultsCompare test = compareGrid.getSelectionModel().getSelectedItem();
				String path = test.getTest_location();
				Info.display("PATH",path);
				Argus.desktop.activate(new ViewTestCaseHistory(ArgusEventBus, null, path));
			}
		});
		
		
		viewContextMenu.add(allUsersMenuItem);

		MenuItem curentUsersMenuItem = new MenuItem();
		curentUsersMenuItem.setText("Show TC History on Curent User");
		curentUsersMenuItem.setIcon(DesktopImages.INSTANCE.user());
		
		viewContextMenu.add(curentUsersMenuItem);

		curentUsersMenuItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				Argus.desktop.activate(new ViewTestCaseHistory(ArgusEventBus, Argus.loggedInUser.getRunOwner(), compareGrid.getSelectionModel().getSelectedItem().getTest_location()));

			}
		});
		
		
		
		
		compareGrid.setContextMenu(viewContextMenu);
		

		compareGrid.addRowDoubleClickHandler(new RowDoubleClickHandler() {
			@Override
			public void onRowDoubleClick(RowDoubleClickEvent event) {
				Argus.desktop.activate(new ViewTestCaseHistory(ArgusEventBus, Argus.loggedInUser.getRunOwner(), compareGrid.getSelectionModel().getSelectedItem().getTest_location()));
			}
		});
		
		
//		List<String> resultList = new ArrayList<String>();
//		resultList.add("P");
//		resultList.add("f");
//		resultList.add("F");
//		resultList.add("p");
//		
//		List<ResultsCompare> result = new ArrayList<ResultsCompare>();
//		result.add(new ResultsCompare("ABC1", "XYZ7", resultList));
//		result.add(new ResultsCompare("ABC2", "XYZ8", resultList));
//		result.add(new ResultsCompare("ABC3", "XYZ9", resultList));
//		
//		compareResultsStore.addAll(result);
//		compareGrid.getView().refresh(true);


		
		this.setHeaderVisible(false);
		this.add(compareGrid);
	}


	public void setEventBus(SimpleEventBus argusEventBus) {
		this.ArgusEventBus = argusEventBus;
	}

	public void update(List<Integer> runsKeys) {
		PLogger.log("keys V");
		for (Integer key: runsKeys) {
			PLogger.log(Integer.toString(key));
		}
		PLogger.log("keys ^");
		ArgusService.compareRuns(runsKeys, new AsyncCallback<List<ResultsCompare>>() {
			public void onFailure(Throwable caught) {
				Info.display("Error",caught.toString());
				//Argus.log("ERR",caught.toString());
			}

			public void onSuccess(List<ResultsCompare> result) {
				//Info.display("DEBUG: compare", result.get(0).getResultListSize().toString());
				compareResultsStore.clear();
				compareResultsStore.addAll(result);
			}
		});
	}

}
