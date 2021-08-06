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
package com.sencha.gxt.explorer.client.toolbar;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

@Detail(
    name = "Overflow Tool Bar (UiBinder)",
    category = "Tool Bar & Menu",
    icon = "overflowtoolbaruibinder",
    files = "OverflowToolBarUiBinderExample.ui.xml",
    minWidth = OverflowToolBarUiBinderExample.MIN_WIDTH,
    preferredHeight = OverflowToolBarUiBinderExample.PREFERRED_HEIGHT
)
public class OverflowToolBarUiBinderExample implements IsWidget, EntryPoint {

  interface MyUiBinder extends UiBinder<Widget, OverflowToolBarUiBinderExample> {
  }

  protected static final int MIN_WIDTH = 120;
  protected static final int PREFERRED_HEIGHT = -1;

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  Window window;

  private ButtonBar buttonBar;

  @Override
  public Widget asWidget() {
    if (buttonBar == null) {
      uiBinder.createAndBindUi(this);

      TextButton buttonOverflow = new TextButton("Overflow Tool Bar");
      buttonOverflow.addSelectHandler(new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          window.show();
        }
      });

      buttonBar = new ButtonBar();
      buttonBar.add(buttonOverflow);
    }

    return buttonBar;
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinWidth(MIN_WIDTH)
        .setPreferredHeight(PREFERRED_HEIGHT)
        .doStandalone();
  }

}
