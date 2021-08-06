/**
 * Sencha GXT 4.0.0 - Sencha for GWT
 * Copyright (c) 2006-2015, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Open Source License
 * ================================================================================
 * This version of Sencha GXT is licensed under the terms of the Open Source GPL v3
 * license. You may use this license only if you are prepared to distribute and
 * share the source code of your application under the GPL v3 license:
 * http://www.gnu.org/licenses/gpl.html
 *
 * If you are NOT prepared to distribute and share the source code of your
 * application under the GPL v3 license, other commercial and oem licenses
 * are available for an alternate download of Sencha GXT.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package com.sencha.gxt.explorer.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.sencha.gxt.explorer.client.app.mvp.ExplorerPlaceHistoryMapper;
import com.sencha.gxt.explorer.client.app.place.ExamplePlace;
import com.sencha.gxt.explorer.client.app.ui.ExampleDetailView;
import com.sencha.gxt.explorer.client.app.ui.ExampleListView;
import com.sencha.gxt.explorer.client.app.ui.ExplorerShell;
import com.sencha.gxt.explorer.client.app.ui.OverviewExample;
import com.sencha.gxt.explorer.client.model.Example;
import com.sencha.gxt.explorer.client.model.ExampleModel;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class ExplorerApp implements ExampleListView.Presenter, ExampleDetailView.Presenter {

  private static final Logger log = Logger.getLogger(ExplorerApp.class.getName());
  public static final String OVERVIEW = "Overview";
  public static final String OVERVIEW_CATEGORY = "overview category";
  @Inject
  private EventBus eventBus;

  @Inject
  private PlaceController placeController;

  @Inject
  private ActivityMapper activityMapper;

  @Inject
  private ExplorerShell shell;

  @Inject
  private ExampleListView listView;

  @Inject
  private ExampleDetailView detailView;

  @Inject
  private ExampleModel exampleModel;
  
  public void run() {
    init();
  }

  @SuppressWarnings("deprecation")
  private void init() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      public void onUncaughtException(Throwable e) {
        Window.alert("Error: " + e.getMessage());
        log.log(Level.SEVERE, e.getMessage(), e);
        e.printStackTrace();
      }
    });

    // Start ActivityManager for the main widget with our ActivityMapper
    ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
    activityManager.setDisplay(shell.getDisplay());

    // Start PlaceHistoryHandler with our PlaceHistoryMapper
    ExplorerPlaceHistoryMapper historyMapper = GWT.create(ExplorerPlaceHistoryMapper.class);
    final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
    historyHandler.register(placeController, eventBus, new ExamplePlace(OVERVIEW.toLowerCase()));

    listView.setPresenter(this);
    detailView.setPresenter(this);

    eventBus.addHandler(PlaceChangeEvent.TYPE, listView);

    Example example = exampleModel.findExample(OVERVIEW.toLowerCase());
    OverviewExample oExample = (OverviewExample)example.getExample();
    oExample.setPlaceController(placeController);
    oExample.loadData(exampleModel);

    Viewport vp = new Viewport();
    vp.setWidget(shell);
    RootPanel.get().add(vp);

    // Goes to place represented on URL or default place
    historyHandler.handleCurrentHistory();
  }

  @Override
  public void selectExample(Example ex) {
    placeController.goTo(new ExamplePlace(ex.getId()));
  }
}
