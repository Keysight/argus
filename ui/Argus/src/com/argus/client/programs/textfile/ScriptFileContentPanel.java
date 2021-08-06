package com.argus.client.programs.textfile;

import com.argus.client.ArgusServiceAsync;
import com.argus.client.images.DesktopImages;
import com.argus.client.util.apps.PLogger;
import com.argus.shared.OsFileContent;

import java.util.Objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;


public class ScriptFileContentPanel extends ContentPanel {
	private final ArgusServiceAsync ArgusService = GWT.create(com.argus.client.ArgusService.class);

	AceEditor testCaseScriptAceEditor;

	public ScriptFileContentPanel (final SimpleEventBus ArgusEventBus, final String aScriptFilePath, final Boolean aReadOnly) {

		final String  fType = aScriptFilePath.substring(aScriptFilePath.lastIndexOf("."));

		/***********************************************************
		 * 
		 * ToolBar area
		 * 		
		 ***********************************************************/

		ToolBar scriptFileToolBar = new ToolBar();
		TextButton btnReload = new TextButton("Reload");
		btnReload.setIcon(DesktopImages.INSTANCE.reload());
		scriptFileToolBar.add(btnReload);

		btnReload.addSelectHandler(new SelectHandler(){
			@Override
			public void onSelect(SelectEvent event) {
				refresh(aScriptFilePath);
			}
		});
        
        
        
		TextButton btnSave = new TextButton("Save");
		if (aReadOnly) {
			btnSave.disable();
		} else {
			btnSave.enable();
		}

		btnSave.setIcon(DesktopImages.INSTANCE.save_as20());
		scriptFileToolBar.add(btnSave);

		btnSave.addSelectHandler(new SelectHandler(){
			@Override
			public void onSelect(SelectEvent event) {
				save(aScriptFilePath);
			}
		});




		/***********************************************************
		 * 
		 * Editor area
		 * 		
		 ***********************************************************/
		testCaseScriptAceEditor = new AceEditor();
		testCaseScriptAceEditor.startEditor();
		testCaseScriptAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		if (fType.equals(".tcl")) {
			testCaseScriptAceEditor.setMode(AceEditorMode.TCL);
		} else if (fType.equals(".py")) {
			testCaseScriptAceEditor.setMode(AceEditorMode.PYTHON);
		} else if (fType.equals(".pl")) {
			testCaseScriptAceEditor.setMode(AceEditorMode.PERL);
		} else if (fType.equals(".rb")) {
			testCaseScriptAceEditor.setMode(AceEditorMode.RUBY);
		}  else {
			testCaseScriptAceEditor.setMode(AceEditorMode.ASCIIDOC);
		}

		this.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				refresh(aScriptFilePath);
			}
		});


		VerticalLayoutContainer scriptFileVLC = new VerticalLayoutContainer();
		scriptFileVLC.add(scriptFileToolBar, new VerticalLayoutData(1, 30, new Margins(0, 0, 0, 0)));
		scriptFileVLC.add(testCaseScriptAceEditor, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));

		this.setCollapsible(false);
		this.setHeaderVisible(false);
		this.setBodyBorder(false);
		this.setBorders(false);
		this.setWidget(scriptFileVLC);

	}


	public void refresh(final String aScriptFilePath) {
		ArgusService.readFileFromServer(aScriptFilePath, new AsyncCallback<OsFileContent>() {
			public void onFailure(Throwable caught) {
				PLogger.log("ERROR: readFileFromServer" + Objects.toString(caught));
			}
			public void onSuccess(OsFileContent result) {
				if (result.getContent().contains("java.io.FileNotFoundException")) {
					PLogger.log("java.io.FileNotFoundException" +  aScriptFilePath);
				} else {
					testCaseScriptAceEditor.setText(result.getContent());
				}
			}
		});
	}

	public void save(String aScriptFilePath) {
		ArgusService.writeFileToServer(aScriptFilePath, testCaseScriptAceEditor.getText(), new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				PLogger.log("Error" + Objects.toString(caught));
				//Argus.log("ERR",Objects.toString(caught));
			}
			public void onSuccess(String result) {
				PLogger.log("Config Saved" + result);
			}
		});
	}

	public void updateText(String searchFor, String replaceWith) {
		PLogger.log("updateText: " + searchFor + " > " + replaceWith);
		testCaseScriptAceEditor.setText(testCaseScriptAceEditor.getText().replaceAll(searchFor, replaceWith));
	}
}


