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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.app.ui.ExplorerShell.Theme;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;

@Detail(
    name = "Button Aligning",
    category = "Button",
    icon = "buttonaligning",
    minWidth = ButtonAlignExample.MIN_WIDTH
)
public class ButtonAlignExample implements IsWidget, EntryPoint {

  protected static final int MIN_WIDTH = 300;

  private VerticalLayoutContainer panel;

  public Widget asWidget() {
    if (panel == null) {
      SelectHandler selectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          Info.display("Click", ((TextButton) event.getSource()).getText() + " clicked");
        }
      };

      ContentPanel panelStart = Theme.BLUE.isActive() || Theme.GRAY.isActive() ? new FramedPanel() : new ContentPanel();
      // Align buttons to the start or left in ltr
      panelStart.setButtonAlign(BoxLayoutPack.START); // Left
      panelStart.setHeading("Button Aligning — " + BoxLayoutPack.START);
      panelStart.addButton(new TextButton("Button 1", selectHandler));
      panelStart.addButton(new TextButton("Button 2", selectHandler));
      panelStart.addButton(new TextButton("Button 3", selectHandler));

      ContentPanel panelCenter = Theme.BLUE.isActive() || Theme.GRAY.isActive() ? new FramedPanel() : new ContentPanel();
      // Align buttons to the center
      panelCenter.setButtonAlign(BoxLayoutPack.CENTER); // Center
      panelCenter.setHeading("Button Aligning — " + BoxLayoutPack.CENTER);
      panelCenter.addButton(new TextButton("Button 1", selectHandler));
      panelCenter.addButton(new TextButton("Button 2", selectHandler));
      panelCenter.addButton(new TextButton("Button 3", selectHandler));

      ContentPanel panelEnd = Theme.BLUE.isActive() || Theme.GRAY.isActive() ? new FramedPanel() : new ContentPanel();
      // Align buttons to the end or right in ltr
      panelEnd.setButtonAlign(BoxLayoutPack.END); // Right
      panelEnd.setHeading("Button Aligning — " + BoxLayoutPack.END);
      panelEnd.addButton(new TextButton("Button 1", selectHandler));
      panelEnd.addButton(new TextButton("Button 2", selectHandler));
      panelEnd.addButton(new TextButton("Button 3", selectHandler));

      panel = new VerticalLayoutContainer();
      panel.add(panelStart, new VerticalLayoutData(1, 0.33333));
      panel.add(new HTML(), new VerticalLayoutData(1, -1, new Margins(20, 0, 0, 0)));
      panel.add(panelCenter, new VerticalLayoutData(1, 0.33333));
      panel.add(new HTML(), new VerticalLayoutData(1, -1, new Margins(20, 0, 0, 0)));
      panel.add(panelEnd, new VerticalLayoutData(1, 0.33333));
    }

    return panel;
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

}
