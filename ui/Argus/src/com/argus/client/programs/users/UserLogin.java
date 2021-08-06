package com.argus.client.programs.users;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.argus.client.ArgusServiceAsync;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.argus.client.Argus;
import com.argus.client.ArgusServiceAsync;
import com.argus.client.programs.users.images.UsersImages;
import com.argus.client.util.apps.PLogger;
import com.argus.shared.DbUserData;
import com.argus.shared.DbUserDataProperties;
import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent.RowClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;

public class UserLogin extends Window {
	private final ArgusServiceAsync ArgusService = GWT.create(com.argus.client.ArgusService.class);

	Storage localStore = null;

	public UserLogin(final SimpleEventBus ArgusEventBus, final List<DbUserData> aUserDataList) {

		final Window me = this;

		/**
		 * design the left side of the login window
		 */

		DbUserDataProperties usersProps = GWT.create(DbUserDataProperties.class);

		final ListStore<DbUserData> usersStore = new ListStore<DbUserData>(usersProps.id());
		usersStore.addAll(aUserDataList);

		ColumnConfig<DbUserData, String> usersColumn = new ColumnConfig<DbUserData, String>(usersProps.UserName(), 210, "Name");
		// IMPORTANT we want the text element (cell parent) to only be as wide as
		// the cell and not fill the cell
		usersColumn.setColumnTextClassName(CommonStyles.get().inlineBlock());
		// nameColumn.setColumnTextStyle(textStyles);

		TextButtonCell button = new TextButtonCell();
		button.setSize(360, 50);
		button.setIcon(UsersImages.INSTANCE.user_male_48());
		usersColumn.setCell(button);

		List<ColumnConfig<DbUserData, ?>> usersColumnConfig = new ArrayList<ColumnConfig<DbUserData, ?>>();
		usersColumnConfig.add(usersColumn);

		ColumnModel<DbUserData> usersCM = new ColumnModel<DbUserData>(usersColumnConfig);

		final Grid<DbUserData> usersGrid = new Grid<DbUserData>(usersStore, usersCM);

		final StringFilter<DbUserData> userFilter = new StringFilter<DbUserData>(usersProps.UserName());

		final GridFilters<DbUserData> usersFilters = new GridFilters<DbUserData>();
		usersFilters.initPlugin(usersGrid);
		usersFilters.setLocal(true);
		usersFilters.addFilter(userFilter);

		button.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				// TODO Auto-generated method stub
				Context c = event.getContext();
				int row = c.getIndex();
				DbUserData theUser = usersStore.get(row);
				usersGrid.getSelectionModel().deselectAll();

				PLogger.log("onSelect" +  theUser.getUserName());
				localStore = Storage.getLocalStorageIfSupported();
				if (localStore != null) {
					localStore.setItem("lasKnownUserLoggedIn", theUser.getUserName());
				} else {
					PLogger.log("UserLogin1" +  " HTML5 local store is not supported");
				}
				Argus.loggedInUser = theUser;
				Argus.desktop.setStartMenuHeading(theUser.getUserName());
				me.hide();
				
		
				
				
			}
		});

		usersGrid.setBorders(true);
		usersGrid.getView().setTrackMouseOver(false);
		usersGrid.getView().setColumnLines(false);
		usersGrid.getView().setStripeRows(false);
		usersGrid.getView().setEnableRowBody(false);
		usersGrid.getView().setAutoFill(true);
		usersGrid.getView().setForceFit(true);
		usersGrid.setHideHeaders(true);
		usersGrid.setShadow(false);
		usersGrid.setStyleName("argus-login-grid");

		FieldLabel userGridLabel = new FieldLabel(usersGrid, " ");
		userGridLabel.setLabelAlign(LabelAlign.TOP);
		userGridLabel.setLabelSeparator("");
		userGridLabel.setStyleName("argus-login-grid-label");
		userGridLabel.setHTML("&nbsp; ");

		VerticalLayoutContainer leftVLC = new VerticalLayoutContainer();
		leftVLC.add(userGridLabel, new VerticalLayoutData(1, 1, new Margins(0, 0, 10, 10)));
		/**
		 * design the right side of the login window
		 */

		VerticalLayoutContainer rightVLC = new VerticalLayoutContainer();


		StoreFilterField<DbUserData> userNameFilter = new StoreFilterField<DbUserData>() {
			@Override
			protected boolean doSelect(Store<DbUserData> store, DbUserData parent, DbUserData item, String filter) {
				String name = item.getUserName();
				name = name.toLowerCase();
				if (name.contains(filter.toLowerCase())) {
					return true;
				}
				return false;
			}
		};

		userNameFilter.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13) {
					Integer nrOfFilteredUsers = usersGrid.getStore().getAll().size();
					if (nrOfFilteredUsers == 1) {
						DbUserData theUser = usersStore.get(0);
						usersGrid.getSelectionModel().deselectAll();
						PLogger.log("onKeyDown" +  theUser.getUserName());
						localStore = Storage.getLocalStorageIfSupported();
						if (localStore != null) {
							localStore.setItem("lasKnownUserLoggedIn", theUser.getUserName());
						} else {
							PLogger.log("UserLogin2" +  " HTML5 local store is not supported");
						}
						Argus.loggedInUser = theUser;
						Argus.desktop.setStartMenuHeading(theUser.getUserName());
						me.hide();
					} else if (nrOfFilteredUsers == 0) {
						PLogger.log("Login:" +  "No user selected !");
					} else {
						PLogger.log("Login:" +  "Please select a user !");
					}
				}
			}
		});

		userNameFilter.bind(usersStore);
		userNameFilter.setStyleName("argus-login-filter");
		userNameFilter.setEmptyText("Filter the users ...");

		FieldLabel userNameLabel = new FieldLabel(userNameFilter, "User Name");
		userNameLabel.setStyleName("argus-login-user-name-label");
		userNameLabel.setLabelAlign(LabelAlign.TOP);
		// TODO: NASTY BUG, setPosition is not working, had to hack the style and
		// enforce the position.
		// userNameLabel.setPosition(20, 150);
		// userNameLabel.getElement().setPropertyInt("left", 20);
		// userNameLabel.getElement().setPropertyInt("top", 150);

		rightVLC.add(userNameLabel, new VerticalLayoutData(342, 234, new Margins(0, 0, 0, 0)));

		/**
		 * the horizontal window container for the left and right side
		 */

		HorizontalLayoutContainer windowHLC = new HorizontalLayoutContainer();
		windowHLC.add(leftVLC, new HorizontalLayoutData(400, 546, new Margins(0, 0, 0, 0)));
		windowHLC.add(rightVLC, new HorizontalLayoutData(400, 546, new Margins(0, 0, 0, 0)));

		/**
		 * the window object attributes
		 */

		this.setBodyBorder(false);
		this.setBorders(false);
		this.setMaximizable(false);
		this.setMinimizable(false);
		this.setClosable(false);
		this.setHeaderVisible(false);
		this.setShadow(false);
		this.setDraggable(false);
		this.setResizable(false);
		this.setResize(false);
		this.setModal(true);
		this.setBlinkModal(false);
		this.setPixelSize(800, 550);
		this.setBodyStyleName("argus-login-window-body");
		this.add(windowHLC);

		this.addHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					me.hide();
				}
			}
		}, KeyDownEvent.getType());

		// TODO: fix enter key on the filter

		// TODO: add domain users

		// TODO: prevent the filter from typing if the user list will become empty

		// TODO: better user switching system

		// TODO: save user database for 'run as'

	}



}
