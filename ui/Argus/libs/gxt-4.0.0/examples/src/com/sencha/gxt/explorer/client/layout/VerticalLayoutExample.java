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
package com.sencha.gxt.explorer.client.layout;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.app.ui.ExplorerShell.Theme;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

@Detail(
    name = "Vertical Layout",
    category = "Layouts",
    icon = "verticallayout",
    minHeight = VerticalLayoutExample.MIN_HEIGHT,
    minWidth = VerticalLayoutExample.MIN_WIDTH
)
public class VerticalLayoutExample implements IsWidget, EntryPoint {

  protected static final int MIN_HEIGHT = 320;
  protected static final int MIN_WIDTH = 150;

  private ContentPanel panel;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      VerticalLayoutContainer vlc = new VerticalLayoutContainer();
      vlc.add(createLabel("Test Label 1"), new VerticalLayoutData(1, 0.25, new Margins(10)));
      vlc.add(createLabel("Test Label 2"), new VerticalLayoutData(1, 0.5, new Margins(0, 10, 0, 10)));
      vlc.add(createLabel("Test Label 3"), new VerticalLayoutData(1, 0.25, new Margins(10)));

      panel = new ContentPanel();
      panel.setHeading("Vertical Layout");
      panel.add(vlc);
    }

    return panel;
  }

  private Label createLabel(String text) {
    Label label = new Label(text);
    label.getElement().getStyle().setProperty("whiteSpace", "nowrap");
    if (Theme.BLUE.isActive() || Theme.GRAY.isActive()) {
      label.addStyleName(ThemeStyles.get().style().border());
    }
    label.addStyleName("pad-text gray-bg");

    return label;
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinHeight(MIN_HEIGHT)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

}
