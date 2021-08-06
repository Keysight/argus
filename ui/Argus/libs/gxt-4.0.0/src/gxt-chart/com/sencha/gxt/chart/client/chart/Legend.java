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
package com.sencha.gxt.chart.client.chart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.event.LegendHandler;
import com.sencha.gxt.chart.client.chart.event.LegendHandler.HasLegendHandlers;
import com.sencha.gxt.chart.client.chart.event.LegendItemOutEvent;
import com.sencha.gxt.chart.client.chart.event.LegendItemOutEvent.HasLegendItemOutHandlers;
import com.sencha.gxt.chart.client.chart.event.LegendItemOutEvent.LegendItemOutHandler;
import com.sencha.gxt.chart.client.chart.event.LegendItemOverEvent;
import com.sencha.gxt.chart.client.chart.event.LegendItemOverEvent.HasLegendItemOverHandlers;
import com.sencha.gxt.chart.client.chart.event.LegendItemOverEvent.LegendItemOverHandler;
import com.sencha.gxt.chart.client.chart.event.LegendItemUpEvent;
import com.sencha.gxt.chart.client.chart.event.LegendItemUpEvent.HasLegendItemUpHandlers;
import com.sencha.gxt.chart.client.chart.event.LegendItemUpEvent.LegendItemUpHandler;
import com.sencha.gxt.chart.client.chart.event.LegendSelectionEvent;
import com.sencha.gxt.chart.client.chart.event.LegendSelectionEvent.HasLegendSelectionHandlers;
import com.sencha.gxt.chart.client.chart.event.LegendSelectionEvent.LegendSelectionHandler;
import com.sencha.gxt.chart.client.chart.series.Series;
import com.sencha.gxt.chart.client.chart.series.SeriesRenderer;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.util.PrecisePoint;
import com.sencha.gxt.core.client.util.PreciseRectangle;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.widget.core.client.tips.ToolTip;

/**
 * Defines a legend for a chart's series. The legend class displays a list of
 * legend items each of them related with a series being rendered.
 * 
 * @param <M> the data type of the legend
 */
public class Legend<M> implements HasLegendHandlers, HasLegendSelectionHandlers, HasLegendItemOutHandlers,
    HasLegendItemOverHandlers, HasLegendItemUpHandlers {

  protected ToolTip toolTip;
  protected LegendToolTipConfig<M> toolTipConfig;
  private Position position = Position.BOTTOM;
  private boolean visible = true;
  private boolean created = false;
  private double padding = 5;
  private double itemSpacing = 10;
  private double width = 0;
  private double height = 0;
  private double x;
  private double y;
  private double origX;
  private double origY;
  private Chart<M> chart;
  private List<String> currentLegendTitles;
  private ArrayList<LegendItem<M>> items = new ArrayList<LegendItem<M>>();
  private RectangleSprite border;
  private RectangleSprite borderConfig;
  private HandlerManager handlerManager;
  private boolean itemHighlighting = false;
  private boolean itemHiding = false;
  private SeriesRenderer<M> markerRenderer;
  private SeriesRenderer<M> lineRenderer;
  private SeriesRenderer<M> labelRenderer;
  private int lastLegendItem = -1;
  private int legendInset = 10;

  public Legend() {
    borderConfig = new RectangleSprite();
    borderConfig.setStroke(RGB.BLACK);
    borderConfig.setStrokeWidth(1);
    borderConfig.setFill(Color.NONE);
  }

  @Override
  public HandlerRegistration addLegendHandler(LegendHandler handler) {
    GroupingHandlerRegistration reg = new GroupingHandlerRegistration();
    reg.add(ensureHandlers().addHandler(LegendSelectionEvent.getType(), handler));
    reg.add(ensureHandlers().addHandler(LegendItemOutEvent.getType(), handler));
    reg.add(ensureHandlers().addHandler(LegendItemOverEvent.getType(), handler));
    reg.add(ensureHandlers().addHandler(LegendItemUpEvent.getType(), handler));
    return reg;
  }

  @Override
  public HandlerRegistration addLegendItemOutHandler(LegendItemOutHandler handler) {
    return ensureHandlers().addHandler(LegendItemOutEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addLegendItemOverHandler(LegendItemOverHandler handler) {
    return ensureHandlers().addHandler(LegendItemOverEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addLegendItemUpHandler(LegendItemUpHandler handler) {
    return ensureHandlers().addHandler(LegendItemUpEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addLegendSelectionHandler(LegendSelectionHandler handler) {
    return ensureHandlers().addHandler(LegendSelectionEvent.getType(), handler);
  }

  /**
   * Removes all the sprites of the legend from the surface.
   */
  public void clear() {
    while (items.size() > 0) {
      items.remove(items.size() - 1).clear();
    }
    if (border != null) {
      border.remove();
      border = null;
    }
  }

  /**
   * Create all the sprites for the legend.
   */
  public void create() {
    if (border == null && borderConfig != null) {
      border = borderConfig.copy();
      chart.addSprite(border);
      border.redraw();
    }
    createItems();
    if (!created && isDisplayed()) {
      created = true;
    }
  }

  /**
   * Calculates and returns the bounding box of the legend.
   * 
   * @return the bounding box of the legend.
   */
  public PreciseRectangle getBBox() {
    final double strokeWidth;
    if (border != null && !Double.isNaN(border.getStrokeWidth())) {
      strokeWidth = border.getStrokeWidth();
    } else {
      strokeWidth = 1;
    }
    return new PreciseRectangle(Math.round(x) - strokeWidth / 2.0, Math.round(y) - strokeWidth / 2.0, width, height);
  }

  /**
   * Returns the border configuration.
   * 
   * @return the border configuration
   */
  public RectangleSprite getBorderConfig() {
    return borderConfig;
  }

  /**
   * Returns the chart that the legend is attached.
   * 
   * @return the chart that the legend is attached
   */
  public Chart<M> getChart() {
    return chart;
  }

  /**
   * Returns the current list of titles used in the legend.
   * 
   * @return the current list of titles used in the legend
   */
  public List<String> getCurrentLegendTitles() {
    return Collections.unmodifiableList(currentLegendTitles);
  }

  /**
   * Returns the {@link LegendItem} at the given point. Returns null if no item
   * at that point.
   * 
   * @param point the point of the item
   * @return the legend item
   */
  public int getItemFromPoint(PrecisePoint point) {
    for (int i = 0; i < items.size(); i++) {
      LegendItem<M> item = items.get(i);
      if (item.getBBox().contains(point)) {
        return i;
      }
    }
    return -1;
  }

  public List<LegendItem<M>> getItems() {
    return Collections.unmodifiableList(items);
  }

  /**
   * Returns the series renderer for label sprites on the legend.
   * 
   * @return the series renderer for label sprites on the legend
   */
  public SeriesRenderer<M> getLabelRenderer() {
    return labelRenderer;
  }

  /**
   * Returns the inset margin used by the legend.
   * 
   * @return the inset margin used by the legend
   */
  public int getLegendInset() {
    return legendInset;
  }

  /**
   * Returns the series renderer for line sprites on the legend.
   * 
   * @return the series renderer for line sprites on the legend
   */
  public SeriesRenderer<M> getLineRenderer() {
    return lineRenderer;
  }

  /**
   * Returns the series renderer for marker sprites on the legend.
   * 
   * @return the series renderer for marker sprites on the legend
   */
  public SeriesRenderer<M> getMarkerRenderer() {
    return markerRenderer;
  }

  /**
   * Returns the padding between legend items.
   * 
   * @return the padding between legend items
   */
  public double getPadding() {
    return padding;
  }

  /**
   * Returns the position of the legend.
   * 
   * @return the position of the legend
   */
  public Position getPosition() {
    return position;
  }

  /**
   * Returns the currently generated tooltip for legend.
   * 
   * @return the currently generated tooltip for legend
   */
  public ToolTip getToolTip() {
    return toolTip;
  }

  /**
   * Returns the tool tip configuration of the legend.
   * 
   * @return the tool tip configuration of the legend
   */
  public LegendToolTipConfig<M> getToolTipConfig() {
    return toolTipConfig;
  }

  /**
   * Returns true if legend item hiding is enabled.
   * 
   * @return true if legend item hiding is enabled
   */
  public boolean isItemHiding() {
    return itemHiding;
  }

  /**
   * Returns true if legend item highlighting is enabled.
   * 
   * @return true if legend item highlighting is enabled
   */
  public boolean isItemHighlighting() {
    return itemHighlighting;
  }

  /**
   * Removes the components tooltip (if one exists).
   */
  public void removeToolTip() {
    if (toolTip != null) {
      toolTip.initTarget(null);
      toolTip = null;
      toolTipConfig = null;
    }
  }

  /**
   * Sets the border configuration.
   * 
   * @param borderConfig the border configuration
   */
  public void setBorderConfig(RectangleSprite borderConfig) {
    this.borderConfig = borderConfig;
  }

  /**
   * Sets the chart that the legend is attached.
   * 
   * @param chart the chart that the legend is attached
   */
  public void setChart(Chart<M> chart) {
    this.chart = chart;
  }

  /**
   * Sets whether or not the legend uses item highlighting.
   * 
   * @param itemHiding true if the legend uses item highlighting
   */
  public void setItemHiding(boolean itemHiding) {
    this.itemHiding = itemHiding;
  }

  /**
   * Sets whether or not the legend uses item highlighting.
   * 
   * @param itemHighlighting ture if the legend uses item highlighting
   */
  public void setItemHighlighting(boolean itemHighlighting) {
    this.itemHighlighting = itemHighlighting;
  }

  /**
   * Sets the series renderer for label sprites on the legend.
   * 
   * @param labelRenderer the series renderer for label sprites on the legend
   */
  public void setLabelRenderer(SeriesRenderer<M> labelRenderer) {
    this.labelRenderer = labelRenderer;
  }

  /**
   * Sets the inset margin used by the legend.
   * 
   * @param legendInset the inset margin used by the legend
   */
  public void setLegendInset(int legendInset) {
    this.legendInset = legendInset;
  }

  /**
   * Sets the series renderer for line sprites on the legend.
   * 
   * @param lineRenderer the series renderer for line sprites on the legend
   */
  public void setLineRenderer(SeriesRenderer<M> lineRenderer) {
    this.lineRenderer = lineRenderer;
  }

  /**
   * Sets the series renderer for marker sprites on the legend.
   * 
   * @param markerRenderer the series renderer for marker sprites on the legend
   */
  public void setMarkerRenderer(SeriesRenderer<M> markerRenderer) {
    this.markerRenderer = markerRenderer;
  }

  /**
   * Sets the padding between legend items.
   * 
   * @param padding the padding between legend items
   */
  public void setPadding(double padding) {
    this.padding = padding;
  }

  /**
   * Sets the position of the legend.
   * 
   * @param position the position of the legend
   */
  public void setPosition(Position position) {
    assert position != null;
    this.position = position;
  }

  /**
   * Sets the tooltip configuration.
   * 
   * @param config the tooltip configuration
   */
  public void setToolTipConfig(LegendToolTipConfig<M> config) {
    this.toolTipConfig = config;
    if (config != null) {
      if (toolTip == null) {
        toolTip = new ToolTip(null, config);
      } else {
        toolTip.update(config);
      }
    } else if (config == null) {
      removeToolTip();
    }
  }

  /**
   * Adjusts the position of the legend to fit in its chart.
   */
  public void updatePosition() {
    PreciseRectangle bbox = chart.getBBox();
    double chartX = bbox.getX() + legendInset;
    double chartY = bbox.getY() + legendInset;
    double chartWidth = bbox.getWidth() - (legendInset * 2.0);
    double chartHeight = bbox.getHeight() - (legendInset * 2.0);
    if (isDisplayed()) {
      // Find the position based on the dimensions
      if (position == Position.LEFT) {
        x = legendInset;
        y = Math.floor(chartY + chartHeight / 2.0 - height / 2.0);
      } else if (position == Position.RIGHT) {
        x = Math.floor(chart.getSurface().getWidth() - width) - legendInset;
        y = Math.floor(chartY + chartHeight / 2.0 - height / 2.0);
      } else if (position == Position.TOP) {
        x = Math.floor(chartX + chartWidth / 2.0 - width / 2.0);
        y = legendInset;
      } else if (position == Position.BOTTOM) {
        x = Math.floor(chartX + chartWidth / 2.0 - width / 2.0);
        y = Math.floor(chart.getSurface().getHeight() - height) - legendInset;
      } else {
        x = Math.floor(origX) + legendInset;
        y = Math.floor(origY) + legendInset;
      }

      // Update the position of each item
      for (int i = 0; i < items.size(); i++) {
        items.get(i).updatePosition(x, y);
      }
      // Update the position of the containing box
      if (border != null) {
        PreciseRectangle rect = getBBox();
        border.setX(rect.getX());
        border.setY(rect.getY());
        border.setWidth(rect.getWidth());
        border.setHeight(rect.getHeight());
        border.redraw();
      }
    }
  }

  protected HandlerManager ensureHandlers() {
    if (handlerManager == null) {
      handlerManager = new HandlerManager(this);
    }
    return handlerManager;
  }

  protected void onMouseDown(PrecisePoint point, Event event) {
    if (itemHiding || handlerManager != null) {
      int index = getItemFromPoint(point);
      if (index > -1) {
        LegendItem<M> item = items.get(index);
        item.onMouseDown();
        ensureHandlers().fireEvent(new LegendSelectionEvent(index, item, event));
      }
    }
  }

  protected int onMouseMove(PrecisePoint point, Event event) {
    if (itemHighlighting || toolTip != null || handlerManager != null) {
      int item = getItemFromPoint(point);
      if (lastLegendItem != -1 && lastLegendItem != item) {
        onMouseOut(lastLegendItem, event);
      }
      if (item != -1) {
        LegendItem<M> sprite = items.get(item);
        if (toolTip != null) {
          if (toolTipConfig.getCustomLabelProvider() != null) {
            toolTipConfig.setBody(toolTipConfig.getCustomLabelProvider().getLabel(sprite));
          } else if (sprite.size() > 0) {
            toolTipConfig.setBody(((TextSprite) sprite.get(0)).getText());
          }
          toolTip.update(toolTipConfig);
          toolTip.showAt(event.getClientX() + toolTipConfig.getMouseOffsetX(),
              event.getClientY() + toolTipConfig.getMouseOffsetY());
        }
        LegendItem<M> legendItem = items.get(item);
        legendItem.onMouseMove();
        ensureHandlers().fireEvent(new LegendItemOverEvent(item, legendItem, event));
      }
      lastLegendItem = item;
      return item;
    } else {
      lastLegendItem = -1;
      return -1;
    }
  }

  protected void onMouseOut(int index, Event event) {
    if (lastLegendItem != -1 && (itemHighlighting || handlerManager != null) && index < items.size()) {
      LegendItem<M> item = items.get(index);
      item.onMouseOut();
      ensureHandlers().fireEvent(new LegendItemOutEvent(index, item, event));
      lastLegendItem = -1;
    }
  }

  protected void onMouseUp(PrecisePoint point, Event event) {
    if (itemHiding || handlerManager != null) {
      int index = getItemFromPoint(point);
      if (index > -1) {
        LegendItem<M> item = items.get(index);
        ensureHandlers().fireEvent(new LegendItemUpEvent(index, item, event));
      }
    }
  }

  /**
   * Create the series markers and labels.
   */
  private void createItems() {
    double spacing = 0;
    double totalHeight = 0;
    double totalWidth = 0;
    double maxHeight = 0;
    double maxWidth = 0;
    double spacingOffset = 2;

    // remove all legend items
    while (items.size() > 0) {
      items.remove(0).clear();
    }

    // Create all the item labels, collecting their dimensions and positioning
    // each one
    // properly in relation to the previous item
    for (int i = 0; i < chart.getSeries().size(); i++) {
      Series<M> series = chart.getSeries(i);
      if (series.isShownInLegend()) {
        currentLegendTitles = series.getLegendTitles();
        for (int j = 0; j < currentLegendTitles.size(); j++) {
          LegendItem<M> item = new LegendItem<M>(this, series, j);
          items.add(item);
          PreciseRectangle bbox = item.getBBox();

          // always measure from x=0, since not all markers go all the way to
          // the left
          if (i + j == 0) {
            if (position.isVertical()) {
              spacing = padding + bbox.getHeight() / 2.0;
            } else {
              spacing = padding;
            }
          } else {
            if (position.isVertical()) {
              spacing = itemSpacing / 2.0;
            } else {
              spacing = itemSpacing;
            }
          }
          // Set the item's position relative to the legend box
          if (position.isVertical()) {
            item.setX(Math.floor(padding));
            item.setY(Math.floor(totalHeight + spacing));
          } else {
            item.setX(Math.floor(totalWidth + padding));
            item.setY(Math.floor(padding + bbox.getHeight() / 2.0));
          }

          // Collect cumulative dimensions
          totalWidth += bbox.getWidth() + spacing;
          totalHeight += bbox.getHeight() + spacing;
          maxWidth = Math.max(maxWidth, bbox.getWidth());
          maxHeight = Math.max(maxHeight, bbox.getHeight());
        }
      }
    }

    // Store the collected dimensions for later
    if (position.isVertical()) {
      width = Math.floor(maxWidth + padding * 2.0);
      if (items.size() == 1) {
        spacingOffset = 1;
      }
      height = Math.floor((totalHeight - spacingOffset * spacing) + (padding * 2.0));
    } else {
      width = Math.floor(totalWidth + padding * 2.0);
      height = Math.floor(maxHeight + padding * 2.0);
    }
  }

  /**
   * Returns whether or not the legend is diplayed.
   * 
   * @return true if the legend is diplayed
   */
  private boolean isDisplayed() {
    if (visible) {
      for (Series<M> series : chart.getSeries()) {
        if (series.isShownInLegend()) {
          return true;
        }
      }
      return false;
    } else {
      return false;
    }
  }

}
