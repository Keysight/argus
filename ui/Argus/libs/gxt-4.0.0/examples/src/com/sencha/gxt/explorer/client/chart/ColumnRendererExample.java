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
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.chart.series.SeriesHighlighter;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.chart.series.SeriesRenderer;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.DrawFx;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Data;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.fx.client.easing.BounceOut;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(
    name = "Column Renderer Chart",
    category = "Charts",
    icon = "columnrendererchart",
    classes = Data.class,
    minHeight = ColumnRendererExample.MIN_HEIGHT,
    minWidth = ColumnRendererExample.MIN_WIDTH
)
public class ColumnRendererExample implements IsWidget, EntryPoint {

  public interface DataPropertyAccess extends PropertyAccess<Data> {
    ValueProvider<Data, Double> data1();

    ValueProvider<Data, String> name();

    @Path("id")
    ModelKeyProvider<Data> nameKey();
  }

  protected static final int MIN_HEIGHT = 480;
  protected static final int MIN_WIDTH = 640;

  private static final DataPropertyAccess dataAccess = GWT.create(DataPropertyAccess.class);

  private ContentPanel panel;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      final ListStore<Data> store = new ListStore<Data>(dataAccess.nameKey());
      store.addAll(TestData.getData(5, 0, 100));

      PathSprite grid = new PathSprite();
      grid.setStroke(RGB.GRAY);
      
      TextSprite title = new TextSprite("Number of Hits");
      title.setFontSize(18);
      title.setFill(RGB.GRAY);
      
      PathSprite axisPath = new PathSprite();
      axisPath.setStroke(RGB.GRAY);
      
      TextSprite axisText = new TextSprite();
      axisText.setFill(RGB.GRAY);
      axisText.setTextBaseline(TextBaseline.MIDDLE);
      
      NumericAxis<Data> axis = new NumericAxis<Data>();
      axis.setPosition(Position.LEFT);
      axis.addField(dataAccess.data1());
      axis.setGridDefaultConfig(grid);
      axis.setTitleConfig(title);
      axis.setDisplayGrid(true);
      axis.setAxisConfig(axisPath);
      axis.setLabelConfig(axisText);
      axis.setMinimum(0);
      axis.setMaximum(100);

      title = new TextSprite("Month of the Year");
      title.setFontSize(18);
      title.setFill(RGB.GRAY);

      axisText = axisText.copy();
      axisText.setTextAnchor(TextAnchor.MIDDLE);

      CategoryAxis<Data, String> catAxis = new CategoryAxis<Data, String>();
      catAxis.setPosition(Position.BOTTOM);
      catAxis.setField(dataAccess.name());
      catAxis.setTitleConfig(title);
      catAxis.setAxisConfig(axisPath);
      catAxis.setLabelConfig(axisText);

      final Color[] colors = {
          new RGB(212, 40, 40),
          new RGB(180, 216, 42),
          new RGB(43, 221, 115),
          new RGB(45, 117, 226),
          new RGB(187, 45, 222)
      };

      TextSprite labelText = new TextSprite();
      labelText.setFill(RGB.BLACK);
      labelText.setFontSize(18);
      labelText.setTextAnchor(TextAnchor.MIDDLE);

      SeriesLabelConfig<Data> labelConfig = new SeriesLabelConfig<Data>();
      labelConfig.setSpriteConfig(labelText);

      final BarSeries<Data> column = new BarSeries<Data>();
      column.setYAxisPosition(Position.LEFT);
      column.addYField(dataAccess.data1());
      column.setLabelConfig(labelConfig);
      // Vertical or horizontal column view
      column.setColumn(true);
      column.setHighlighting(true);
      column.setRenderer(new SeriesRenderer<Data>() {
        @Override
        public void spriteRenderer(Sprite sprite, int index, ListStore<Data> store) {
          sprite.setFill(colors[index % colors.length]);
          sprite.redraw();
        }
      });
      column.setHighlighter(new SeriesHighlighter() {
        @Override
        public void highlight(Sprite sprite) {
          sprite.setStroke(new RGB(85, 85, 204));
          DrawFx.createStrokeWidthAnimator(sprite, 3).run(250);
        }

        @Override
        public void unHighlight(Sprite sprite) {
          sprite.setStroke(Color.NONE);
          DrawFx.createStrokeWidthAnimator(sprite, 0).run(250);
        }
      });

      final Chart<Data> chart = new Chart<Data>();
      chart.setAnimationDuration(750);
      chart.setAnimationEasing(new BounceOut());
      chart.setShadowChart(false);
      chart.setStore(store);
      chart.addAxis(axis);
      chart.addAxis(catAxis);
      chart.addSeries(column);
      chart.setDefaultInsets(30);

      TextButton regenerate = new TextButton("Reload Data");
      regenerate.addSelectHandler(new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          store.clear();
          store.addAll(TestData.getData(5, 0, 100));
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
      panel.setHeading("Column Renderer Chart");
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

}
