package com.argus.client.programs.textfile;

import java.util.Objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.argus.client.ArgusServiceAsync;
import com.argus.client.images.DesktopImages;
import com.argus.client.util.apps.PLogger;
import com.argus.shared.DbResultsTestCase;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.TabPanel.TabPanelAppearance;
import com.sencha.gxt.widget.core.client.TabPanel.TabPanelBottomAppearance;
import com.sencha.gxt.widget.core.client.Window;

public class ViewTestCaseLogAndScript extends Window {


	private final ArgusServiceAsync ArgusService = GWT.create(com.argus.client.ArgusService.class);


	public ViewTestCaseLogAndScript(final SimpleEventBus ArgusEventBus, DbResultsTestCase aTestCaseItem) {


		final TabPanel pyTharTabPanel = new TabPanel(GWT.<TabPanelAppearance> create(TabPanelBottomAppearance.class));

		final Integer fKEY             = aTestCaseItem.getKEY();
		final String  fTest_pass_fail  = aTestCaseItem.getTest_result();
		final String  fTest_location   = aTestCaseItem.getTest_location();
		final String  fScriptName      = aTestCaseItem.getTest_name();
		final String  fFileName        = fTest_location.substring(fTest_location.lastIndexOf("/")+1, fTest_location.length());
		final String  fType            = fFileName.substring(fFileName.lastIndexOf("."));



		/***********************************************************
		 * 
		 * Script log tab
		 * 		
		 ***********************************************************/

		final TabItemConfig logTabItemConfig = new TabItemConfig("Log");
		logTabItemConfig.setIcon(DesktopImages.INSTANCE.logger_16());


		/***********************************************************
		 * 
		 * Script source tab
		 * 		
		 ***********************************************************/

		Boolean readOnly = true; // by default scripts should be edited in perforce and synced
		if (fTest_location.startsWith("/home")) {
			readOnly = false; // allows scripts located in the user home folder to be edited
		}
		final Boolean fReadOnly = readOnly;

		final TabItemConfig sourceTabItemConfig = new TabItemConfig("Source");
		if (fType.equals(".tcl")) {
			sourceTabItemConfig.setIcon(DesktopImages.INSTANCE.tcl());
		} else if (fType.equals(".py")) {
			sourceTabItemConfig.setIcon(DesktopImages.INSTANCE.python());
		} else if (fType.equals(".pl")) {
			sourceTabItemConfig.setIcon(DesktopImages.INSTANCE.perl());
		}  else {
			sourceTabItemConfig.setIcon(DesktopImages.INSTANCE.question_blue());
		}
		
		// TODO: find a better way to set the default tab then to create then in the desired order
		ArgusService.getTestLog(fKEY, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				PLogger.log("ERROR: getTestLog" + Objects.toString(caught));
			}
			public void onSuccess(String testLog) {
				Boolean follow = false; //by default we read the log once and display it
				if (fTest_pass_fail.contentEquals("EXEC")) {
					follow = true; // if test is running we will automatically refresh the log.
				}
				pyTharTabPanel.add(new LogFileContentPanel(ArgusEventBus, testLog, follow), logTabItemConfig);
				pyTharTabPanel.add(new ScriptFileContentPanel(ArgusEventBus, fTest_location, fReadOnly), sourceTabItemConfig);
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
		this.setHeading(aTestCaseItem.getTest_name());
		this.getHeader().setIcon(DesktopImages.INSTANCE.logger_16());
		this.add(pyTharTabPanel);



		//
		//		if (enabledWindow.equals("Log")) {
		//
		//		final Integer fKEY        = aTestCaseItem.getKEY();
		//		final String  fTestState  = aTestCaseItem.getPass_fail();
		//		final String  fScriptPath = aTestCaseItem.getTest_location();
		//		final String  fScriptName = aTestCaseItem.getTest_name();
		//		final String  fFileName   = fScriptPath.substring(fScriptPath.lastIndexOf("/")+1, fScriptPath.length());
		//		final String  fType       = fFileName.substring(fFileName.lastIndexOf("."));
		//
		//		ArgusService.getTestLog(fKEY, new AsyncCallback<String>() {
		//			public void onFailure(Throwable caught) {
		//				PLogger.log("ERROR: getTestLog",Objects.toString(caught));
		//			}
		//			public void onSuccess(String test_log) {
		//				if (fTestState.equals("EXEC")) {
		//					// TODO: live log code
		//					// Argus.regressionResultsTable.addTestLiveLogsTab(test_name, test_log);
		//				} else if (fTestState.equals("SKIP")) {
		//					// TODO: just disable the log tab for skipped tests but show the code
		//				} else if (fTestState.equals("PASS") || fTestState.equals("FAIL")) {
		//					// TODO: write the static log getter
		//					// Argus.regressionResultsTable.addTestLogsTab(test_name, test_log);
		//				} else {
		//					PLogger.log(fTestState, "It's an unrecognized test case state (known states: EXEC,PASS,FAIL,SKIP)");
		//				}
		//			}
		//		});
		//
		//
		//
		//		TabPanel testCaseTabPanel = new TabPanel(GWT.<TabPanelAppearance> create(BlueTabPanelBottomAppearance.class));
		//
		//
		//
		//		/***********************************************************
		//		 *
		//		 * Test Case Script Viewer (and soon editor)
		//		 *
		//		 ***********************************************************/
		//
		//		final AceEditor testCaseScriptAceEditor = new AceEditor();
		//		//testCaseScriptAceEditor.setStyleName("argus-code-and-log-fonts");
		//		//testCaseScriptAceEditor.setStylePrimaryName("argus-code-and-log-fonts");
		//		//testCaseScriptAceEditor.getElement().getStyle().setProperty("font-family", "Inconsolata");
		//		//testCaseScriptAceEditor.redraw();
		//
		//		TabItemConfig testCaseScriptTabItemConfig = new TabItemConfig("Source");
		//
		//		if (fType.equals(".tcl")) {
		//			 testCaseScriptTabItemConfig.setIcon(DesktopImages.INSTANCE.tcl());
		//		} else if (fType.equals(".py")) {
		//			testCaseScriptTabItemConfig.setIcon(DesktopImages.INSTANCE.python());
		//		} else if (fType.equals(".pl")) {
		//			testCaseScriptTabItemConfig.setIcon(DesktopImages.INSTANCE.perl());
		//		}  else {
		//			testCaseScriptTabItemConfig.setIcon(DesktopImages.INSTANCE.question_blue());
		//		}
		//
		//		testCaseTabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
		//			@Override
		//			public void onSelection(SelectionEvent<Widget> event) {
		//				TabPanel panel = (TabPanel) event.getSource();
		//				Widget w = event.getSelectedItem();
		//				TabItemConfig config = panel.getConfig(w);
		//				if (config.getText().contains("Source")) {
		//					//if (testCaseScriptAceEditor.getText() == null) {
		//					ArgusService.readFileFromServer(fScriptPath, new AsyncCallback<String>() {
		//						public void onFailure(Throwable caught) {
		//							PLogger.log("ERROR: readFileFromServer",Objects.toString(caught));
		//						}
		//						public void onSuccess(String result) {
		//							if (result.contains("java.io.FileNotFoundException")) {
		//								PLogger.log("java.io.FileNotFoundException",fScriptPath);
		//							} else {
		//								testCaseScriptAceEditor.setText(result);
		//								//final String html = testCaseScriptAceEditor.getElement().getInnerHTML().replace("<textarea style=\"", "<textarea style=\"font: 13px monospace !important; ");
		//								//testCaseScriptAceEditor.getElement().setInnerHTML(html);
		//							}
		//						}
		//					});
		//					//}
		//				} else if (config.getText().contains("settings.py")) {
		//					// getLiveLogTimer.cancel();
		//				}
		//			}});
		//
		//
		//		/***********************************************************
		//		 *
		//		 * Test Case Script Resource Tree
		//		 *
		//		 ***********************************************************/
		//
		//
		//		final TreeStore<OsFile> utilsTreeStore;
		//		ValueProvider<OsFile, String> utilsTreeValueProvider;
		//		IconProvider<OsFile> utilsTreeIconProvider;
		//		final 	Tree<OsFile, String> utilsTree;
		//
		//		utilsTreeStore = new TreeStore<OsFile>(new KeyProvider());
		//		utilsTreeStore.add(new OsFile(0, "Loading ...", "Loading ..."));
		//
		//		utilsTreeValueProvider = new ValueProvider<OsFile, String>() {
		//			@Override
		//			public String getValue(OsFile object) {
		//				return object.getName();
		//			}
		//
		//			@Override
		//			public void setValue(OsFile object, String value) {
		//			}
		//
		//			@Override
		//			public String getPath() {
		//				return "name";
		//			}
		//		};
		//
		//		utilsTreeIconProvider = new IconProvider<OsFile> () {
		//			@Override
		//			public ImageResource getIcon(OsFile model) {
		//				if (model.getName().contains(".tcl")) {
		//					return DesktopImages.INSTANCE.tcl();
		//				} else if (model.getPath().contains("packages_folder")) {
		//					return DesktopImages.INSTANCE.packages_folder_16();
		//				} else if (model.getPath().contains("package")) {
		//					return DesktopImages.INSTANCE.package_16();
		//				} else if (model.getPath().contains("utils_folder")) {
		//					return DesktopImages.INSTANCE.utils_folder_16();
		//				} else {
		//					return null;
		//				}
		//			}
		//		};
		//
		//		utilsTree = new Tree<OsFile, String>(utilsTreeStore, utilsTreeValueProvider);
		//		utilsTree.setIconProvider(utilsTreeIconProvider);
		//		//utilsTree.setCheckable(true);
		//		//utilsTree.setCheckStyle(CheckCascade.TRI);
		//		//utilsTree.setCheckNodes(CheckNodes.BOTH);
		//		//utilsTree.setPixelSize(300, 600);
		//		utilsTree.setBorders(true);
		//		utilsTree.setTrackMouseOver(true);
		//
		//		ArgusService.getResourceFromScript(fScriptPath,new AsyncCallback<OsFolder>() {
		//			@Override
		//			public void onFailure(Throwable caught) {
		//				PLogger.log("Error",Objects.toString(caught));
		//			}
		//			@Override
		//			public void onSuccess(OsFolder result) {
		//				utilsTreeStore.clear();
		//				for (OsFile base : result.getChildren()) {
		//					utilsTreeStore.add(base);
		//					if (base instanceof OsFolder) {
		//						Argus.processFolder(utilsTreeStore, (OsFolder) base);
		//					}
		//				}
		//				utilsTree.expandAll();
		//			}
		//		});
		//
		//
		//		/***********************************************************
		//		 *
		//		 * Test Case Script Layout
		//		 *
		//		 ***********************************************************/
		//
		//		final BorderLayoutContainer aggregatedBorderLayoutContainer = new BorderLayoutContainer();
		//
		//
		//		MarginData centerData = new MarginData();
		//
		//		BorderLayoutData westData = new BorderLayoutData(250);
		//		westData.setCollapsible(true);
		//		westData.setSplit(true);
		//		westData.setCollapseMini(true);
		//		westData.setMargins(new Margins(0, 5, 0, 5));
		//
		//		ContentPanel scriptUtilsTreeContentPanel = new ContentPanel(); //WEST
		//		scriptUtilsTreeContentPanel.setHeading("Select File");
		//		scriptUtilsTreeContentPanel.add(utilsTree);
		//
		//		ContentPanel testCaseScriptContentPanel = new ContentPanel(); //CENTER
		//		testCaseScriptContentPanel.setHeading(fFileName);
		//		testCaseScriptContentPanel.add(testCaseScriptAceEditor);
		//
		//		aggregatedBorderLayoutContainer.setWestWidget(scriptUtilsTreeContentPanel, westData);
		//		aggregatedBorderLayoutContainer.setCenterWidget(testCaseScriptContentPanel, centerData);
		//
		//		testCaseTabPanel.add(aggregatedBorderLayoutContainer, testCaseScriptTabItemConfig);
		//
		//		this.addShowHandler(new ShowHandler() {
		//			@Override
		//			public void onShow(ShowEvent event) {
		//				testCaseScriptAceEditor.startEditor();
		//				if (fType.equals(".tcl")) {
		//					testCaseScriptAceEditor.setMode(AceEditorMode.TCL);
		//				} else if (fType.equals(".py")) {
		//					testCaseScriptAceEditor.setMode(AceEditorMode.PYTHON);
		//				} else if (fType.equals(".pl")) {
		//					testCaseScriptAceEditor.setMode(AceEditorMode.PERL);
		//				}  else {
		//					testCaseScriptAceEditor.setMode(AceEditorMode.ASCIIDOC);
		//				}
		//
		//				//				switch (fType) {
		//				//				    case "tcl": testCaseScriptAceEditor.setMode(AceEditorMode.TCL);
		//				//				    case "py" : testCaseScriptAceEditor.setMode(AceEditorMode.PYTHON);
		//				//				    case "pl" : testCaseScriptAceEditor.setMode(AceEditorMode.PERL);
		//				//				}
		//				testCaseScriptAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		//			}
		//		});
		//
		//		/***********************************************************
		//		 *
		//		 * The Window/Program area
		//		 *
		//		 ***********************************************************/
		//
		//		this.setClosable(true);
		//		this.setMaximizable(true);
		//		this.setMinimizable(true);
		//		this.setHeading(fScriptName);
		//		this.getHeader().setIcon(DesktopImages.INSTANCE.logger_16());
		//		this.add(testCaseTabPanel);
		//
		//		} else {
		//
		//			final String  fScriptPath = aTestCaseItem.getTest_location();
		//			final String  fScriptName = aTestCaseItem.getTest_name();
		//			final String  fFileName   = fScriptPath.substring(fScriptPath.lastIndexOf("/")+1, fScriptPath.length());
		//			final String  fType       = fFileName.substring(fFileName.lastIndexOf("."));
		//
		//			TabPanel testCaseTabPanel = new TabPanel(GWT.<TabPanelAppearance> create(BlueTabPanelBottomAppearance.class));
		//
		//
		//
		//			/***********************************************************
		//			 *
		//			 * Test Case Script Viewer (and soon editor)
		//			 *
		//			 ***********************************************************/
		//
		//			final AceEditor testCaseScriptAceEditor = new AceEditor();
		//			//testCaseScriptAceEditor.setStyleName("argus-code-and-log-fonts");
		//			//testCaseScriptAceEditor.setStylePrimaryName("argus-code-and-log-fonts");
		//			//testCaseScriptAceEditor.getElement().getStyle().setProperty("font-family", "Inconsolata");
		//			//testCaseScriptAceEditor.redraw();
		//
		//			TabItemConfig testCaseScriptTabItemConfig = new TabItemConfig("Source");
		//
		//			if (fType.equals(".tcl")) {
		//				 testCaseScriptTabItemConfig.setIcon(DesktopImages.INSTANCE.tcl());
		//			} else if (fType.equals(".py")) {
		//				testCaseScriptTabItemConfig.setIcon(DesktopImages.INSTANCE.python());
		//			} else if (fType.equals(".pl")) {
		//				testCaseScriptTabItemConfig.setIcon(DesktopImages.INSTANCE.perl());
		//			}  else {
		//				testCaseScriptTabItemConfig.setIcon(DesktopImages.INSTANCE.question_blue());
		//			}
		//
		//			testCaseTabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
		//				@Override
		//				public void onSelection(SelectionEvent<Widget> event) {
		//					TabPanel panel = (TabPanel) event.getSource();
		//					Widget w = event.getSelectedItem();
		//					TabItemConfig config = panel.getConfig(w);
		//					if (config.getText().contains("Source")) {
		//						//if (testCaseScriptAceEditor.getText() == null) {
		//						ArgusService.readFileFromServer(fScriptPath, new AsyncCallback<String>() {
		//							public void onFailure(Throwable caught) {
		//								PLogger.log("ERROR: readFileFromServer",Objects.toString(caught));
		//							}
		//							public void onSuccess(String result) {
		//								if (result.contains("java.io.FileNotFoundException")) {
		//									PLogger.log("java.io.FileNotFoundException",fScriptPath);
		//								} else {
		//									testCaseScriptAceEditor.setText(result);
		//									//final String html = testCaseScriptAceEditor.getElement().getInnerHTML().replace("<textarea style=\"", "<textarea style=\"font: 13px monospace !important; ");
		//									//testCaseScriptAceEditor.getElement().setInnerHTML(html);
		//								}
		//							}
		//						});
		//						//}
		//					} else if (config.getText().contains("settings.py")) {
		//						// getLiveLogTimer.cancel();
		//					}
		//				}});
		//
		//
		//			/***********************************************************
		//			 *
		//			 * Test Case Script Resource Tree
		//			 *
		//			 ***********************************************************/
		//
		//
		//			final TreeStore<OsFile> utilsTreeStore;
		//			ValueProvider<OsFile, String> utilsTreeValueProvider;
		//			IconProvider<OsFile> utilsTreeIconProvider;
		//			final 	Tree<OsFile, String> utilsTree;
		//
		//			utilsTreeStore = new TreeStore<OsFile>(new KeyProvider());
		//			utilsTreeStore.add(new OsFile(0, "Loading ...", "Loading ..."));
		//
		//			utilsTreeValueProvider = new ValueProvider<OsFile, String>() {
		//				@Override
		//				public String getValue(OsFile object) {
		//					return object.getName();
		//				}
		//
		//				@Override
		//				public void setValue(OsFile object, String value) {
		//				}
		//
		//				@Override
		//				public String getPath() {
		//					return "name";
		//				}
		//			};
		//
		//			utilsTreeIconProvider = new IconProvider<OsFile> () {
		//				@Override
		//				public ImageResource getIcon(OsFile model) {
		//					if (model.getName().contains(".tcl")) {
		//						return DesktopImages.INSTANCE.tcl();
		//					} else if (model.getPath().contains("packages_folder")) {
		//						return DesktopImages.INSTANCE.packages_folder_16();
		//					} else if (model.getPath().contains("package")) {
		//						return DesktopImages.INSTANCE.package_16();
		//					} else if (model.getPath().contains("utils_folder")) {
		//						return DesktopImages.INSTANCE.utils_folder_16();
		//					} else {
		//						return null;
		//					}
		//				}
		//			};
		//
		//			utilsTree = new Tree<OsFile, String>(utilsTreeStore, utilsTreeValueProvider);
		//			utilsTree.setIconProvider(utilsTreeIconProvider);
		//			//utilsTree.setCheckable(true);
		//			//utilsTree.setCheckStyle(CheckCascade.TRI);
		//			//utilsTree.setCheckNodes(CheckNodes.BOTH);
		//			//utilsTree.setPixelSize(300, 600);
		//			utilsTree.setBorders(true);
		//			utilsTree.setTrackMouseOver(true);
		//
		//			ArgusService.getResourceFromScript(fScriptPath,new AsyncCallback<OsFolder>() {
		//				@Override
		//				public void onFailure(Throwable caught) {
		//					PLogger.log("Error",Objects.toString(caught));
		//				}
		//				@Override
		//				public void onSuccess(OsFolder result) {
		//					utilsTreeStore.clear();
		//					for (OsFile base : result.getChildren()) {
		//						utilsTreeStore.add(base);
		//						if (base instanceof OsFolder) {
		//							Argus.processFolder(utilsTreeStore, (OsFolder) base);
		//						}
		//					}
		//					utilsTree.expandAll();
		//				}
		//			});
		//
		//
		//			/***********************************************************
		//			 *
		//			 * Test Case Script Layout
		//			 *
		//			 ***********************************************************/
		//
		//			final BorderLayoutContainer aggregatedBorderLayoutContainer = new BorderLayoutContainer();
		//
		//
		//			MarginData centerData = new MarginData();
		//
		//			BorderLayoutData westData = new BorderLayoutData(250);
		//			westData.setCollapsible(true);
		//			westData.setSplit(true);
		//			westData.setCollapseMini(true);
		//			westData.setMargins(new Margins(0, 5, 0, 5));
		//
		//			ContentPanel scriptUtilsTreeContentPanel = new ContentPanel(); //WEST
		//			scriptUtilsTreeContentPanel.setHeading("Select File");
		//			scriptUtilsTreeContentPanel.add(utilsTree);
		//
		//			ContentPanel testCaseScriptContentPanel = new ContentPanel(); //CENTER
		//			testCaseScriptContentPanel.setHeading(fFileName);
		//			testCaseScriptContentPanel.add(testCaseScriptAceEditor);
		//
		//			aggregatedBorderLayoutContainer.setWestWidget(scriptUtilsTreeContentPanel, westData);
		//			aggregatedBorderLayoutContainer.setCenterWidget(testCaseScriptContentPanel, centerData);
		//
		//			testCaseTabPanel.add(aggregatedBorderLayoutContainer, testCaseScriptTabItemConfig);
		//
		//			this.addShowHandler(new ShowHandler() {
		//				@Override
		//				public void onShow(ShowEvent event) {
		//					testCaseScriptAceEditor.startEditor();
		//					if (fType.equals(".tcl")) {
		//						testCaseScriptAceEditor.setMode(AceEditorMode.TCL);
		//					} else if (fType.equals(".py")) {
		//						testCaseScriptAceEditor.setMode(AceEditorMode.PYTHON);
		//					} else if (fType.equals(".pl")) {
		//						testCaseScriptAceEditor.setMode(AceEditorMode.PERL);
		//					}  else {
		//						testCaseScriptAceEditor.setMode(AceEditorMode.ASCIIDOC);
		//					}
		//
		//					//				switch (fType) {
		//					//				    case "tcl": testCaseScriptAceEditor.setMode(AceEditorMode.TCL);
		//					//				    case "py" : testCaseScriptAceEditor.setMode(AceEditorMode.PYTHON);
		//					//				    case "pl" : testCaseScriptAceEditor.setMode(AceEditorMode.PERL);
		//					//				}
		//					testCaseScriptAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		//				}
		//			});
		//
		//			/***********************************************************
		//			 *
		//			 * The Window/Program area
		//			 *
		//			 ***********************************************************/
		//
		//			this.setClosable(true);
		//			this.setMaximizable(true);
		//			this.setMinimizable(true);
		//			this.setHeading(fScriptName);
		//			this.getHeader().setIcon(DesktopImages.INSTANCE.logger_16());
		//			this.add(testCaseTabPanel);
		//
		//		}



	}

}
