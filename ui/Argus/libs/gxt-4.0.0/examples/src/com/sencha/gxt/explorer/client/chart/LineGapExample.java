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
package com.sencha.gxt.explorer.client.chart;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.examples.resources.client.model.GapData;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(
    name = "Line Gap Chart",
    category = "Charts",
    icon = "linegapchart",
    classes = GapData.class,
    minHeight = LineGapExample.MIN_HEIGHT,
    minWidth = LineGapExample.MIN_WIDTH
)
public class LineGapExample implements IsWidget, EntryPoint {

  public interface GapDataPropertyAccess extends PropertyAccess<GapData> {
    ValueProvider<GapData, Double> gapless();

    ValueProvider<GapData, Double> gapped();

    ValueProvider<GapData, String> name();

    @Path("name")
    ModelKeyProvider<GapData> nameKey();
  }

  protected static final int MIN_HEIGHT = 480;
  protected static final int MIN_WIDTH = 640;

  private static final GapDataPropertyAccess GapDataAccess = GWT.create(GapDataPropertyAccess.class);
  private static final String[] monthsFull = new String[] {
      "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November",
      "December"};

  private ContentPanel panel;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      final ListStore<GapData> store = new ListStore<GapData>(GapDataAccess.nameKey());
      setupStore(store);
      
      TextSprite title = new TextSprite("Number of Hits");
      title.setFontSize(18);

      PathSprite odd = new PathSprite();
      odd.setOpacity(1);
      odd.setFill(new Color("#ddd"));
      odd.setStroke(new Color("#bbb"));
      odd.setStrokeWidth(0.5);

      NumericAxis<GapData> axis = new NumericAxis<GapData>();
      axis.setPosition(Position.LEFT);
      axis.addField(GapDataAccess.gapless());
      axis.addField(GapDataAccess.gapped());
      axis.setTitleConfig(title);
      axis.setMinorTickSteps(1);
      axis.setDisplayGrid(true);
      axis.setGridOddConfig(odd);
      axis.setMinimum(0);
      axis.setMaximum(100);
      
      title = new TextSprite("Month of the Year");
      title.setFontSize(18);

      CategoryAxis<GapData, String> catAxis = new CategoryAxis<GapData, String>();
      catAxis.setPosition(Position.BOTTOM);
      catAxis.setField(GapDataAccess.name());
      catAxis.setTitleConfig(title);
      catAxis.setLabelProvider(new LabelProvider<String>() {
        @Override
        public String getLabel(String item) {
          return item.substring(0, 3);
        }
      });
      
      Sprite marker = Primitives.square(0, 0, 6);
      marker.setFill(new RGB(194, 0, 36));

      final LineSeries<GapData> series = new LineSeries<GapData>();
      series.setLegendTitle("Gapless");
      series.setYAxisPosition(Position.LEFT);
      series.setYField(GapDataAccess.gapless());
      series.setStroke(new RGB(194, 0, 36));
      series.setShowMarkers(true);
      series.setMarkerConfig(marker);
      series.setHighlighting(true);
    
      marker = Primitives.circle(0, 0, 6);
      marker.setFill(new RGB(240, 165, 10));
    
      final LineSeries<GapData> series2 = new LineSeries<GapData>();
      series2.setLegendTitle("Gapped");
      series2.setGapless(false);
      series2.setYAxisPosition(Position.LEFT);
      series2.setYField(GapDataAccess.gapped());
      series2.setStroke(new RGB(240, 165, 10));
      series2.setShowMarkers(true);
      series2.setSmooth(true);
      series2.setMarkerConfig(marker);
      series2.setHighlighting(true);

      final Legend<GapData> legend = new Legend<GapData>();
      legend.setItemHighlighting(true);
      legend.setItemHiding(true);
      legend.getBorderConfig().setStrokeWidth(0);
      
      final Chart<GapData> chart = new Chart<GapData>();
      chart.setStore(store);
      chart.setShadowChart(false);
      chart.addAxis(axis);
      chart.addAxis(catAxis);
      chart.addSeries(series);
      chart.addSeries(series2);
      chart.setLegend(legend);
      chart.setDefaultInsets(30);

      TextButton regenerate = new TextButton("Reload Data");
      regenerate.addSelectHandler(new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          setupStore(store);
          chart.redrawChart();
        }
      });
      
      ToggleButton animation = new ToggleButton("Animate");
      animation.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
          chart.setAnimated(event.getValue());
        }
      });
      animation.setValue(true, true);
      
      ToggleButton shadow = new ToggleButton("Shadow");
      shadow.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
          chart.setShadowChart(event.getValue());
          chart.redrawChart();
        }
      });
      shadow.setValue(false);

      ToolBar toolBar = new ToolBar();
      toolBar.add(regenerate);
      toolBar.add(animation);
      toolBar.add(shadow);

      VerticalLayoutContainer layout = new VerticalLayoutContainer();
      layout.add(toolBar, new VerticalLayoutData(1, -1));
      layout.add(chart, new VerticalLayoutData(1, 1));
      
      panel = new ContentPanel();
      panel.setHeading("Line Gap Chart");
      panel.add(layout);
    }

    return panel;
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinHeight(MIN_HEIGHT)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

  public void setupStore(ListStore<GapData> store) {
    store.clear();
    for (int i = 0; i < 12; i++) {
      if (i % 4 != 0) {
        store.add(new GapData(monthsFull[i % 12], Math.random() * 100, Math.random() * 100));
      } else {
        store.add(new GapData(monthsFull[i % 12], Double.NaN, Double.NaN));
      }
    }
  }

}
