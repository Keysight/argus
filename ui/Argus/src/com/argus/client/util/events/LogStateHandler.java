package com.argus.client.util.events;

import com.google.gwt.event.shared.EventHandler;

public interface LogStateHandler extends EventHandler {
	void onStateChange (LogStateEvent event);
}