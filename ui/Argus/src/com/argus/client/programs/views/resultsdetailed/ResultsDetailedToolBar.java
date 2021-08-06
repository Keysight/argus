package com.argus.client.programs.views.resultsdetailed;
import java.util.Objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.argus.client.Argus;
import com.argus.client.ArgusServiceAsync;
import com.argus.client.images.DesktopImages;
import com.argus.client.util.apps.PLogger;
import com.argus.client.util.events.ResultsDetailedSelectionEvent;
import com.argus.client.util.events.ResultsDetailedSelectionHandler;
import com.argus.shared.DbResultsTestCase;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.SplitButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.IntegerPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class ResultsDetailedToolBar extends ToolBar {

	private final ArgusServiceAsync ArgusService = GWT.create(com.argus.client.ArgusService.class);

	final SpinnerField<Integer> iterationsNumberField = new SpinnerField<Integer>(new IntegerPropertyEditor());

	public ResultsDetailedToolBar (final SimpleEventBus ArgusEventBus, Integer aRunKEY, TestCasesContentPanel testCasesContentPanel) {

		Grid<DbResultsTestCase> testCaseGrid = testCasesContentPanel.getTCGrid();
		ListStore<DbResultsTestCase> testCaseStore = testCasesContentPanel.getTCStore();
		GridFilters<DbResultsTestCase> testCasesFilters = testCasesContentPanel.getTCFilter();
		StringFilter<DbResultsTestCase> test_pass_failFilter = testCasesContentPanel.getTest_pass_failFilter();

		/***********************************************************
		 * 
		 * Middle ToolBar area
		 * 		
		 ***********************************************************/

		ButtonBar buttonBar = new ButtonBar();

		SeparatorToolItem separatorToolItem_1 = new SeparatorToolItem();
		buttonBar.add(separatorToolItem_1);

		TextButton allStatesButton = new SplitButton("Select All Fail");
		//allStatesButton.setIconStyle("");
		allStatesButton.setMenu(createMenu());
		buttonBar.add(allStatesButton);

		allStatesButton.getMenu().addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				MenuItem item = (MenuItem) event.getSelectedItem();
				final String itemText = Objects.toString(item.getText().toUpperCase());
				test_pass_failFilter.setActive(false, false);
				test_pass_failFilter.setValue(itemText);
				test_pass_failFilter.setActive(true, false);
				testCasesContentPanel.selectAllItemsAndFilter(ArgusEventBus, aRunKEY, itemText);
			}
		});

		allStatesButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				test_pass_failFilter.setActive(false, false);
				test_pass_failFilter.setValue("FAIL");
				test_pass_failFilter.setActive(true, false);
				testCasesContentPanel.selectAllItemsAndFilter(ArgusEventBus, aRunKEY, "FAIL");
			}
		});



		TextButton btnResetFilters = new TextButton("Reset All");
		buttonBar.add(btnResetFilters);
		btnResetFilters.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				testCasesFilters.clearFilters();
				testCaseGrid.getSelectionModel().deselectAll();
				testCaseStore.removeFilters();
				testCaseStore.setEnableFilters(false);
				testCasesContentPanel.getSelectedKEYs().clear();
				testCasesContentPanel.getSelectedPaths().clear();
			}
		});


		FillToolItem fillToolItem = new FillToolItem();
		buttonBar.add(fillToolItem);
		fillToolItem.setWidth("68px");


		SeparatorToolItem separatorToolItem = new SeparatorToolItem();
		buttonBar.add(separatorToolItem);

		//LabelToolItem lbltltmIterations = new LabelToolItem("Iterations");
		//buttonBar.add(lbltltmIterations);


		final SpinnerField<Integer> iterationsNumberField = new SpinnerField<Integer>(new IntegerPropertyEditor());
		iterationsNumberField.setValue(1);
		iterationsNumberField.setIncrement(1);
		iterationsNumberField.setMinValue(1);
		iterationsNumberField.setMaxValue(999);
		iterationsNumberField.setAllowNegative(false);
		iterationsNumberField.setAllowBlank(false);
		iterationsNumberField.getPropertyEditor().setFormat(NumberFormat.getFormat("0"));
		iterationsNumberField.setWidth("59px");

		FieldLabel iterationsNumberLabel = new FieldLabel(iterationsNumberField, "Iteration(s)");
		iterationsNumberLabel.setLabelPad(0);
		iterationsNumberLabel.setLabelSeparator(":");
		iterationsNumberLabel.setLabelWidth(60);

		buttonBar.add(iterationsNumberLabel);


		this.add(buttonBar);
		buttonBar.setWidth("503px");

		FillToolItem fillToolItem_1 = new FillToolItem();
		this.add(fillToolItem_1);
		fillToolItem_1.setWidth("250px");

		// create the button that triggers the action of exporting the XLSX file
		//TextButton exportExcelButton = new TextButton("Export to XLSX",DesktopImages.INSTANCE.XLSX_20());
		TextButton exportFileButton = new TextButton("Export to..");
		exportFileButton.setMenu(createMenuExport());
		exportFileButton.setIcon(DesktopImages.INSTANCE.XLSX_20());
		this.add(exportFileButton);

		exportFileButton.getMenu().addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				MenuItem item = (MenuItem) event.getSelectedItem();
				final String itemText = Objects.toString(item.getText().toUpperCase());
				Integer aRunKEY = testCaseGrid.getStore().get(0).getRun_key();
				ArgusService.exportTestCaseHistory(itemText.toLowerCase(), aRunKEY, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						PLogger.log(Objects.toString(caught));
						Info.display("Error",Objects.toString(caught));
						//Argus.log("ERR",Objects.toString(caught));
					}
					public void onSuccess(String result) {
						Info.display("File Downloaded:",result);
						PLogger.log(result);
						// TODO - BUG - this logging generates in google chrome an RPC error must be fixed.
						com.google.gwt.user.client.Window.Location.assign(result);
						//com.google.gwt.user.client.Window.open("https://confluence.it.keysight.com/display/PYT", "Argus-wiki", "");
					}
				});

			}
		});	




	}

	public String getIterations() {
		return Objects.toString(iterationsNumberField.getValue());
	}

	private Menu createMenuExport() {
		Menu menu = new Menu();
		MenuItem xlsx = new MenuItem("XLSX");
		MenuItem csv = new MenuItem("CSV");
		MenuItem zip = new MenuItem("ZIP");
		xlsx.setIcon(DesktopImages.INSTANCE.xls_16());
		csv.setIcon(DesktopImages.INSTANCE.csv_16());
		zip.setIcon(DesktopImages.INSTANCE.zip_16());
		menu.add(xlsx);
		menu.add(csv);
		menu.add(zip);
		return menu;
	}

	private Menu createMenu() {
		Menu menu = new Menu();
		menu.add(new MenuItem("Fail"));
		menu.add(new MenuItem("Pass"));
		menu.add(new MenuItem("Skip"));
		menu.add(new MenuItem("Exec"));
		return menu;
	}


}

