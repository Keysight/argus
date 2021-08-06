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
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.LayoutRegion;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.app.ui.ExplorerShell.Theme;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

@Detail(
    name = "Border Layout (UiBinder)",
    category = "Layouts",
    icon = "borderlayoutuibinder",
    files = "BorderLayoutUiBinderExample.ui.xml",
    minHeight = BorderLayoutUiBinderExample.MIN_HEIGHT,
    minWidth = BorderLayoutUiBinderExample.MIN_WIDTH
)
public class BorderLayoutUiBinderExample implements IsWidget, EntryPoint {

  protected static final int MIN_HEIGHT = 480;
  protected static final int MIN_WIDTH = 720;

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  interface MyUiBinder extends UiBinder<Component, BorderLayoutUiBinderExample> {
  }

  @UiField(provided = true)
  final Boolean borders = Theme.TRITON.isActive() ? false : true;
  @UiField(provided = true)
  final Integer margins = Theme.TRITON.isActive() ? 10 : Theme.NEPTUNE.isActive() ? 8 : 5;

  @UiField
  BorderLayoutContainer con;

  private Widget widget;

  @Override
  public Widget asWidget() {
    if (widget == null) {
      widget = uiBinder.createAndBindUi(this);
    }

    return widget;
  }

  @UiFactory
  public FlexTable createFlexTable() {
    FlexTable table = new FlexTable();
    table.getElement().getStyle().setProperty("margin", "10px");
    table.setCellSpacing(8);
    table.setCellPadding(4);

    for (int i = 0; i < LayoutRegion.values().length; i++) {
      final LayoutRegion r = LayoutRegion.values()[i];
      if (r == LayoutRegion.CENTER) {
        continue;
      }

      SelectHandler handler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          TextButton btn = (TextButton) event.getSource();
          String txt = btn.getText();
          if (txt.equals("Expand")) {
            con.expand(r);
          } else if (txt.equals("Collapse")) {
            con.collapse(r);
          } else if (txt.equals("Show")) {
            con.show(r);
          } else {
            con.hide(r);
          }
        }
      };

      table.setHTML(i, 0, "<div style='font-size: 12px; width: 100px'>" + r.name() + ":</span>");
      table.setWidget(i, 1, new TextButton("Expand", handler));
      table.setWidget(i, 2, new TextButton("Collapse", handler));
      table.setWidget(i, 3, new TextButton("Show", handler));
      table.setWidget(i, 4, new TextButton("Hide", handler));
    }

    return table;
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinHeight(MIN_HEIGHT)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

}
