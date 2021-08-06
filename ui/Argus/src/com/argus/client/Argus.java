package com.argus.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.argus.client.programs.users.UserDataAvailableEvent;
import com.argus.client.programs.users.UserLogin;
import com.argus.client.programs.views.resultshistory.ViewResultsAggregated;
import com.argus.client.util.apps.PLogger;
import com.argus.shared.DbUserData;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.desktop.client.widget.Desktop;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Argus implements EntryPoint {
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	private final static ArgusServiceAsync ArgusService = GWT.create(ArgusService.class);
	private final static SimpleEventBus ArgusEventBus = GWT.create(SimpleEventBus.class);
		
	Storage localStore = null;
	public static List<DbUserData> userDataList = new ArrayList<DbUserData>();
	
	public final static Desktop desktop = new Desktop();
	public static DbUserData loggedInUser = new DbUserData();
	
	
	public static PLogger plogger = new PLogger(ArgusEventBus);
	
	public void onModuleLoad() {
		RootPanel.get().add(desktop);
		RootPanel.get().addStyleName("x-desktop");
		
		localStore = Storage.getLocalStorageIfSupported();
		desktop.activate(plogger);
		
		ArgusService.loadprops(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				PLogger.log("ERROR loadprops: " +  Objects.toString(caught));
			}
			@Override
			public void onSuccess(String result) {
				PLogger.log("loadprops: " +  Objects.toString(result));
			}
		});
		
		
		
		
		ArgusService.getUsers(new AsyncCallback<List<DbUserData>>() {
			@Override
			public void onFailure(Throwable caught) {
				PLogger.log("ERROR getUsers: " +  Objects.toString(caught));
			}
			@Override
			public void onSuccess(List<DbUserData> result) {
				PLogger.log("getUsers" + "Argus.java");
				userDataList.addAll(result);
				if (localStore == null) {
					PLogger.log("getUsers: " + "HTML5 local store is not supported");
					UserLogin userLogin = new UserLogin(ArgusEventBus, userDataList);
					userLogin.show();
				} else {
					String savedUser = null;
					savedUser = localStore.getItem("lasKnownUserLoggedIn");
					if (savedUser == null) {
						UserLogin userLogin = new UserLogin(ArgusEventBus, userDataList);
						userLogin.show();
					} else {
						boolean didIloggedIn = false;
						for(Iterator<DbUserData> it = userDataList.iterator(); it.hasNext();){
							DbUserData theUser = it.next();
							if (theUser.getUserName().equals(savedUser)) {
								PLogger.log("Auto Login:" +  theUser.getUserName());
								Argus.loggedInUser = theUser;
								Argus.desktop.setStartMenuHeading(theUser.getUserName());
								didIloggedIn = true;
								Argus.updateUserDataAndOpenDefaultWindows();
							}
						};
						if(!didIloggedIn) {
							UserLogin userLogin = new UserLogin(ArgusEventBus, userDataList);
							userLogin.show();
						}
					}

				}

			}
		});
	}
	
	public static void updateUserDataAndOpenDefaultWindows () {
		ArgusService.updateUserData(Argus.loggedInUser, new AsyncCallback<DbUserData>() {
			public void onFailure(Throwable caught) {
				PLogger.log("ERROR updateUserData: " +  Objects.toString(caught));
			}

			public void onSuccess(DbUserData result) {
				Argus.loggedInUser = result;
				openDefaultWindows();
				ArgusEventBus.fireEvent(new UserDataAvailableEvent());
			}
		});
	}
	
	
	
	
	public static void openDefaultWindows () {
		desktop.activate(new ViewResultsAggregated(ArgusEventBus, loggedInUser, null));
	}
	
}
