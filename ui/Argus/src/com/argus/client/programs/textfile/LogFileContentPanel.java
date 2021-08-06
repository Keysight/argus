package com.argus.client.programs.textfile;

import java.util.Objects;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.argus.client.ArgusServiceAsync;
import com.argus.client.util.apps.PLogger;
import com.argus.shared.OsFileContent;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

public class LogFileContentPanel extends ContentPanel {
	private final ArgusServiceAsync ArgusService = GWT.create(com.argus.client.ArgusService.class);

	AceEditor testCaseLogAceEditor;
	Timer getLiveLogTimer;

	public LogFileContentPanel (final SimpleEventBus ArgusEventBus, final String aLogFilePath, final Boolean aFollow) {

		testCaseLogAceEditor = new AceEditor();

		testCaseLogAceEditor.startEditor();
		//pyTharVLC.forceLayout();
		testCaseLogAceEditor.setMode(AceEditorMode.ASCIIDOC);
		testCaseLogAceEditor.setTheme(AceEditorTheme.ECLIPSE);

		final VerticalLayoutContainer pyTharVLC = new VerticalLayoutContainer();
		pyTharVLC.setScrollMode(ScrollMode.AUTO);
		pyTharVLC.add(testCaseLogAceEditor, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));

		getLiveLogTimer = new Timer() {
			public void run() {
				refresh(aLogFilePath, aFollow);
			}
		};

		this.addBeforeShowHandler(new BeforeShowHandler(){
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				if (aFollow) {
					refresh(aLogFilePath, aFollow);
					getLiveLogTimer.scheduleRepeating(5000);
				} else {
					refresh(aLogFilePath, aFollow);
				}
			}
		});

		this.addHideHandler(new HideHandler() {
			@Override
			public void onHide(HideEvent event) {
				getLiveLogTimer.cancel();
			}
		});

		this.setCollapsible(false);
		this.setHeaderVisible(false);
		this.setBodyBorder(false);
		this.setBorders(false);
		this.setWidget(pyTharVLC);

	}

	public void refresh(final String aLogFilePath, final Boolean aFollow) {
		final String fFileName    = aLogFilePath.substring(aLogFilePath.lastIndexOf("/")+1, aLogFilePath.length());
		final String fType        = fFileName.substring(fFileName.lastIndexOf("."));

		if (fType.equals(".zip")) {
			ArgusService.getTestLogFromZipFile(aLogFilePath, new AsyncCallback<OsFileContent>() {
				@Override
				public void onFailure(Throwable caught) {
					PLogger.log("ERROR getTestLogFromZipFile: " +  Objects.toString(caught));
					getLiveLogTimer.cancel();
				}
				@Override
				public void onSuccess(OsFileContent result) {
					updateText(result, aFollow);
				}
			});
		} else {
			ArgusService.readFileFromServer(aLogFilePath, new AsyncCallback<OsFileContent>() {
				public void onFailure(Throwable caught) {
					PLogger.log("ERROR: readFileFromServer" +  Objects.toString(caught));
					getLiveLogTimer.cancel();
				}
				public void onSuccess(OsFileContent result) {
					updateText(result, aFollow);
				}
			});
		}

	}


	public void  updateText(OsFileContent result, final Boolean aFollow) {
		if (result.getContent().contains("java.io.FileNotFoundException")) {
			PLogger.log("java.io.FileNotFoundException" +  result.getContent());
			getLiveLogTimer.cancel();
		} else {
			if (result.getType().equals("html")) {
				//HTMLPanel htmlPanel = new HTMLPanel(test_log.substring(5));
				//pyTharVLC.add(htmlPanel, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));	
			} else {
				if (testCaseLogAceEditor.getText() != result.getContent()) {
					testCaseLogAceEditor.setText(result.getContent());
					if (aFollow) {
						testCaseLogAceEditor.moveCursorFileEnd();
					}
				}
			}

		}
	}

	//	void updateLogFile() {
	//		ArgusService.getTestLog(fKEY, new AsyncCallback<String>() {
	//			public void onFailure(Throwable caught) {
	//				PLogger.log("ERROR getTestLog",Objects.toString(caught));
	//			}
	//			public void onSuccess(final String test_log) {
	//				if (fTestState.equals("EXEC")) {
	//					pyTharVLC.add(testCaseLogAceEditor, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));
	//
	//					getLiveLogTimer.scheduleRepeating(2000);
	//				} else {
	//					if (test_log.startsWith("html:")) {
	//						HTMLPanel htmlPanel = new HTMLPanel(test_log.substring(5));
	//						pyTharVLC.add(htmlPanel, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));
	//					} else {
	//						pyTharVLC.add(testCaseLogAceEditor, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));
	//						testCaseLogAceEditor.startEditor();
	//						pyTharVLC.forceLayout();
	//						testCaseLogAceEditor.setMode(AceEditorMode.ASCIIDOC);
	//						testCaseLogAceEditor.setTheme(AceEditorTheme.ECLIPSE);
	//					testCaseLogAceEditor.setText(test_log);
	//					}
	//				}
	//			}
	//		});
	//
	//	}


}
