package com.argus.client.programs.views.resultshistory;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.argus.client.Argus;
import com.argus.client.ArgusServiceAsync;
import com.argus.client.programs.views.resultsdetailed.ViewResultsDetailed;
import com.argus.client.util.apps.PLogger;
import com.argus.client.util.valueproviders.HashMapValueProvider;
import com.argus.shared.DbResultsAggregated;
import com.argus.shared.DbResultsAggregatedProperties;
import com.argus.shared.ResultsCompare;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.event.SeriesSelectionEvent;
import com.sencha.gxt.chart.client.chart.event.SeriesSelectionEvent.SeriesSelectionHandler;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.chart.series.SeriesHighlighter;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelProvider;
import com.sencha.gxt.chart.client.chart.series.SeriesToolTipConfig;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Item;

public class AggregatedChartContentPanel extends ContentPanel {
	
	private final ArgusServiceAsync ArgusService = GWT.create(com.argus.client.ArgusService.class);

	DbResultsAggregatedProperties aggregatedProps = GWT.create(DbResultsAggregatedProperties.class);
	final ListStore<DbResultsAggregated> chartStore = new ListStore<DbResultsAggregated>(aggregatedProps.id());
	final Chart<DbResultsAggregated> aggregatedChart = new Chart<DbResultsAggregated>();
	
	SimpleEventBus ArgusEventBus;
	
	public AggregatedChartContentPanel (SimpleEventBus argusEventBus) {
		this.ArgusEventBus = argusEventBus;

		/***********************************************************
		 * 
		 * The Chart area
		 * 		
		 ***********************************************************/

		LabelProvider<Number> percentLabelProvider = new LabelProvider<Number>() {
			@Override
			public String getLabel(Number item) {
				int value = item.intValue();
				return value + "%";
			}
		};

		NumericAxis<DbResultsAggregated> axis = new NumericAxis<DbResultsAggregated>();
		axis.setPosition(Position.LEFT);
		axis.addField(aggregatedProps.pass_percent());
		axis.addField(aggregatedProps.fail_percent());
		axis.addField(aggregatedProps.skip_percent());
		axis.setDisplayGrid(true);
		axis.setLabelProvider(percentLabelProvider);


		CategoryAxis<DbResultsAggregated, String> catAxis = new CategoryAxis<DbResultsAggregated, String>();
		catAxis.setPosition(Position.BOTTOM);
		//TODO: make this work for any product
		catAxis.setField(new HashMapValueProvider("SONiC"));
		TextSprite sprite = new TextSprite();
		sprite.setRotation(315);
		catAxis.setLabelPadding(-10);
		catAxis.setLabelConfig(sprite);
		catAxis.setLabelTolerance(50);



		final Legend<DbResultsAggregated> legend = new Legend<DbResultsAggregated>();
		legend.setPosition(Position.RIGHT);
		legend.setItemHiding(true);
		legend.setItemHighlighting(true);


		final BarSeries<DbResultsAggregated> aggregatedBar = new BarSeries<DbResultsAggregated>();
		aggregatedBar.setYAxisPosition(Position.LEFT);
		aggregatedBar.addYField(aggregatedProps.pass_percent());
		aggregatedBar.addYField(aggregatedProps.fail_percent());
		aggregatedBar.addYField(aggregatedProps.skip_percent());
		aggregatedBar.addColor(new RGB(127, 255,   0)); //GREEN
		aggregatedBar.addColor(new RGB(255,   0,   0)); //RED
		aggregatedBar.addColor(new RGB(220, 220, 220)); //GREY
		aggregatedBar.setStacked(true);
		aggregatedBar.setColumn(true);
		SeriesToolTipConfig<DbResultsAggregated> config = new SeriesToolTipConfig<DbResultsAggregated>();
		config.setLabelProvider(new SeriesLabelProvider<DbResultsAggregated>() {
			@Override
			public String getLabel(DbResultsAggregated item, ValueProvider<? super DbResultsAggregated, ? extends Number> valueProvider) {
				return valueProvider.getValue(item).intValue() + "%";
			}
		});
		config.setDismissDelay(2000);
		aggregatedBar.setToolTipConfig(config);
		aggregatedBar.setHighlighting(true);
		
//		aggregatedBar.setHighlighter(new SeriesHighlighter() {
//	        @Override
//	        public void highlight(Sprite sprite) {
//	          if (sprite instanceof RectangleSprite) {
//	            RectangleSprite bar = (RectangleSprite) sprite;
//	            bar.setStroke(new RGB(85, 85, 204));
//	            bar.setStrokeWidth(3);
//	            //bar.setFill(new RGB("#a2b5ca"));
//	            bar.redraw();
//	          }
//	        }
//
//	        @Override
//	        public void unHighlight(Sprite sprite) {
//	          if (sprite instanceof RectangleSprite) {
//	            RectangleSprite bar = (RectangleSprite) sprite;
//	            bar.setStroke(Color.NONE);
//	            bar.setStrokeWidth(0);
//	            //bar.setFill(new RGB(148, 174, 10));
//	            bar.redraw();
//	          }
//	        }
//	      });

		aggregatedBar.addSeriesSelectionHandler(new SeriesSelectionHandler<DbResultsAggregated>() {
	        @Override
	        public void onSeriesSelection(SeriesSelectionEvent<DbResultsAggregated> event) {
	        		String build  = Objects.toString(event.getItem().getValueFromMap("SONiC").split("-")[0]);
	        		PLogger.log("Opening ViewResultsDetailed for: " + build);
					Argus.desktop.activate(new ViewResultsDetailed(ArgusEventBus, event.getItem().getKEY(),build));
				}
			});
	          
	          
		aggregatedChart.setStore(chartStore);
		aggregatedChart.setShadowChart(true);
		aggregatedChart.setAnimated(true);
		aggregatedChart.addSeries(aggregatedBar);
		aggregatedChart.addAxis(axis);
		aggregatedChart.addAxis(catAxis);
		aggregatedChart.setLegend(legend);
		aggregatedChart.getLegend().setPosition(Position.LEFT);
		aggregatedChart.setLayoutData(new VerticalLayoutData(1, 1));
		
		
		
		
		this.add(aggregatedChart);

		this.setHeading("Build over build - Pass, Fail, Skip Chart");
		
	}
		
	public void updateChartData(List<DbResultsAggregated> dbResultsAggregated) {
		PLogger.log("updateChartData: updated");
		chartStore.clear();
		chartStore.addAll(dbResultsAggregated.subList(0, Math.min(25, dbResultsAggregated.size())));
		aggregatedChart.redrawChart();
	}

	public void setEventBus(SimpleEventBus argusEventBus) {
		this.ArgusEventBus = argusEventBus;
	}
	
	
	
}