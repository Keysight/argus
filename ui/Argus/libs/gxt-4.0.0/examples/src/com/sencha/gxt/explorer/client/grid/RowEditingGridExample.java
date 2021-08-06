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
package com.sencha.gxt.explorer.client.grid;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.examples.resources.client.model.Plant;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

@Detail(
    name = "Row Editable Grid",
    category = "Grid",
    icon = "roweditablegrid",
    classes = {
        AbstractGridEditingExample.class,
        Plant.class
    },
    maxHeight = RowEditingGridExample.MAX_HEIGHT,
    maxWidth = RowEditingGridExample.MAX_WIDTH,
    minHeight = RowEditingGridExample.MIN_HEIGHT,
    minWidth = RowEditingGridExample.MIN_WIDTH
)
public class RowEditingGridExample extends AbstractGridEditingExample implements EntryPoint {

  public static final int MAX_HEIGHT = 600;
  public static final int MAX_WIDTH = 800;
  public static final int MIN_HEIGHT = 320;
  public static final int MIN_WIDTH = 480;
  private Widget widget;

  public interface PriceTemplate extends XTemplates {
    @XTemplate("<div style='text-align:right;padding:3px'>{value:currency}</div>")
    SafeHtml render(double value);
  }

  @Override
  public Widget asWidget() {
    if (widget == null) {
      widget = super.asWidget();
      customize();
    }

    return widget;
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMaxHeight(MAX_HEIGHT)
        .setMaxWidth(MAX_WIDTH)
        .setMinHeight(MIN_HEIGHT)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

  protected void customize() {
    panel.setHeading("Row Editable Grid");
  }

  @Override
  protected GridEditing<Plant> createGridEditing(Grid<Plant> editableGrid) {
    GridRowEditing<Plant> rowEditing = new GridRowEditing<Plant>(editableGrid);
    ColumnConfig<Plant, Double> price = editableGrid.getColumnModel().getColumn(2);
    rowEditing.addRenderer(price, new AbstractSafeHtmlRenderer<Double>() {
      PriceTemplate template = GWT.create(PriceTemplate.class);
      @Override
      public SafeHtml render(Double object) {
        return template.render(object);
      }
    });

    return rowEditing;
  }

}