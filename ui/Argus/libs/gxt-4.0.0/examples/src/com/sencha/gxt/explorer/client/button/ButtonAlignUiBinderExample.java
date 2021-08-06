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
package com.sencha.gxt.explorer.client.button;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.app.ui.ExplorerShell.Theme;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.info.Info;

@Detail(
    name = "Button Aligning (UiBinder)",
    category = "Button",
    icon = "buttonaligninguibinder",
    files = {
        "ButtonAlignUiBinderFramedExample.ui.xml",
        "ButtonAlignUiBinderUnframedExample.ui.xml"
    },
    minWidth = ButtonAlignUiBinderExample.MIN_WIDTH
)
public class ButtonAlignUiBinderExample implements IsWidget, EntryPoint {

  @UiTemplate("ButtonAlignUiBinderFramedExample.ui.xml")
  interface UiBinderFramed extends UiBinder<Widget, ButtonAlignUiBinderExample> {
  }

  @UiTemplate("ButtonAlignUiBinderUnframedExample.ui.xml")
  interface UiBinderUnframed extends UiBinder<Widget, ButtonAlignUiBinderExample> {
  }

  protected static final int MIN_WIDTH = 300;

  private static UiBinder<Widget, ButtonAlignUiBinderExample>  uiBinder;

  private Widget widget;

  public Widget asWidget() {
    if (widget == null) {
      if (Theme.BLUE.isActive() || Theme.GRAY.isActive()) {
        uiBinder = GWT.create(UiBinderFramed.class);
      } else {
        uiBinder = GWT.create(UiBinderUnframed.class);
      }
      widget = uiBinder.createAndBindUi(this);
    }

    return widget;
  }

  @UiHandler({"button1s", "button2s", "button3s", "button1c", "button2c", "button3c", "button1e", "button2e", "button3e"})
  public void onButtonClick(SelectEvent event) {
    Info.display("Click", ((TextButton) event.getSource()).getText() + " clicked");
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

}
