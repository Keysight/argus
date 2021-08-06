package com.argus.client.util.events;

import com.google.gwt.event.shared.GwtEvent;

public class LogStateEvent extends GwtEvent<LogStateHandler>{

	public static Type<LogStateHandler> TYPE = new Type<LogStateHandler>();
	
	public LogStateEvent() {
		
	}

	@Override
	public Type<LogStateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogStateHandler handler) {
		handler.onStateChange(this);
	}

}
