package com.argus.client.programs.users;

import com.google.gwt.event.shared.EventHandler;

public interface UserDataAvailableHandler extends EventHandler {
	void onDataAvailable (UserDataAvailableEvent event);

}
