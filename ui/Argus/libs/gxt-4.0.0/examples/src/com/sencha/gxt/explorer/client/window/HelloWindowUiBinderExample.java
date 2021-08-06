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
package com.sencha.gxt.explorer.client.window;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

@Detail(
    name = "Hello World (UiBinder)",
    category = "Windows",
    icon = "helloworlduibinder",
    files = "HelloWindowUiBinderExample.ui.xml",
    minWidth = HelloWindowUiBinderExample.MIN_WIDTH,
    preferredHeight = HelloWindowUiBinderExample.PREFERRED_HEIGHT
)
public class HelloWindowUiBinderExample implements IsWidget, EntryPoint {

  interface MyUiBinder extends UiBinder<Widget, HelloWindowUiBinderExample> {
  }

  protected static final int MIN_WIDTH = 88;
  protected static final int PREFERRED_HEIGHT = -1;

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  Window window;

  private ButtonBar buttonBar;

  @Override
  public Widget asWidget() {
    if (buttonBar == null) {
      uiBinder.createAndBindUi(this);

      TextButton buttonHelloWorld = new TextButton("Hello World");
      buttonHelloWorld.addSelectHandler(new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          // show the window
          window.show();

          // constrain the window to the viewport (for small mobile screen sizes)
          Rectangle bounds = window.getElement().getBounds();
          Rectangle adjusted = window.getElement().adjustForConstraints(bounds);
          if (adjusted.getWidth() != bounds.getWidth() || adjusted.getHeight() != bounds.getHeight()) {
            window.setPixelSize(adjusted.getWidth(), adjusted.getHeight());
          }
        }
      });

      window.setData("open", buttonHelloWorld);

      buttonBar = new ButtonBar();
      buttonBar.add(buttonHelloWorld);
    }

    return buttonBar;
  }

  @UiHandler("window")
  public void onHide(HideEvent event) {
    TextButton open = window.getData("open");
    open.focus();
  }

  @UiHandler("closeButton")
  public void onCloseButtonClicked(SelectEvent event) {
    window.hide();
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinWidth(MIN_WIDTH)
        .setPreferredHeight(PREFERRED_HEIGHT)
        .doStandalone();
  }

}