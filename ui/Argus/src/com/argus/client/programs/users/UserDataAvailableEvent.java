package com.argus.client.programs.users;

import com.google.gwt.event.shared.GwtEvent;

public class UserDataAvailableEvent extends GwtEvent<UserDataAvailableHandler>{

	public static Type<UserDataAvailableHandler> TYPE = new Type<UserDataAvailableHandler>();
	
	public UserDataAvailableEvent() {
	}

	@Override
	public Type<UserDataAvailableHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UserDataAvailableHandler handler) {
		handler.onDataAvailable(this);
	}

}
