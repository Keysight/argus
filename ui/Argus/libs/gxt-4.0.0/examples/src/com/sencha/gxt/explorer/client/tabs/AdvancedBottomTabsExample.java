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
package com.sencha.gxt.explorer.client.tabs;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel.PlainTabPanelBottomAppearance;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.TabPanel.TabPanelBottomAppearance;

@Detail(
    name = "Bottom Advanced Tabs",
    icon = "advancedbottomtabs",
    category = "Tabs",
    minHeight = AdvancedBottomTabsExample.MIN_HEIGHT,
    minWidth = AdvancedBottomTabsExample.MIN_WIDTH
)
public class AdvancedBottomTabsExample extends AdvancedTabsExample implements EntryPoint {

  protected static final int MIN_HEIGHT = 435;
  protected static final int MIN_WIDTH = 435;

  @Override
  protected TabPanel getTabPanel() {
    return new TabPanel(GWT.<TabPanelBottomAppearance> create(TabPanelBottomAppearance.class));
  }

  @Override
  protected PlainTabPanel getPlainTabPanel() {
    return new PlainTabPanel(GWT.<PlainTabPanelBottomAppearance> create(PlainTabPanelBottomAppearance.class));
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinHeight(MIN_HEIGHT)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

}
