package com.argus.client.util.apps;

import java.util.Date;
import java.util.Objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.argus.client.ArgusServiceAsync;
import com.argus.client.images.DesktopImages;
import com.argus.client.util.events.LogStateEvent;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class PLogger extends Window {
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final ArgusServiceAsync ArgusService = GWT.create(com.argus.client.ArgusService.class);

	 private static Status status;
	 private static TextArea textArea;
	 private static SimpleEventBus ArgusEventBus;
	
	public PLogger(final SimpleEventBus aArgusEventBus) {	
//	public PLogger() {		

		textArea = new TextArea();
		ToolBar toolBar = new ToolBar();
		ArgusEventBus = aArgusEventBus;
		//toolBar.addStyleName(ThemeStyles.getStyle().borderTop()); 3.1.0 MIGRATION
		
		status = new Status();
		
		status.setText("idle");
	    status.setWidth(150);
	    status.setHeight(15);
	    toolBar.setHeight(15);
	    toolBar.add(status);
	    toolBar.add(new FillToolItem());

		VerticalLayoutContainer LogsVLC = new VerticalLayoutContainer();
		
		LogsVLC.add(textArea, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));
		LogsVLC.add(toolBar, new VerticalLayoutData(1, 20, new Margins(0, 0, 0, 0)));
        this.setDraggable(false);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setMinimizable(true);
		this.setHeading("Logs");
		this.getHeader().setIcon(DesktopImages.INSTANCE.log_16());
		this.setPosition(500, 600);
		this.setSize("900px", "300px");
		this.add(LogsVLC);
	}
	
	
	public void activate (String actionName) {
		status.setText(actionName);
		this.show();
	}
	
	public static String logs = "";
	
	public static void log (String message) {
		Date date = new Date();
		
		logs = logs.concat(DateTimeFormat.getFormat("H:mm:ss.S").format(date) + " " + message + "\n");
		ArgusEventBus.fireEvent(new LogStateEvent());
		//Argus.plogger.log("fireEvent ------> Started");
		textArea.setText(Objects.toString(logs));
		status.setText("idle");
	}

}
