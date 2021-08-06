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
package com.sencha.gxt.widget.core.client;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.cell.core.client.SliderCell;
import com.sencha.gxt.cell.core.client.SliderCell.HorizontalSliderAppearance;
import com.sencha.gxt.cell.core.client.SliderCell.VerticalSliderAppearance;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * Lets the user select a value by sliding an indicator within a bounded range.
 */
public class Slider extends Field<Integer> {

  /**
   * Creates a slider with the default slider cell.
   */
  public Slider() {
    super(new SliderCell());
    setAllowTextSelection(false);
    setValue(50);
    redraw();
  }

  /**
   * Creates a slider with the specified orientation.
   * 
   * @param vertical true to create a vertical slider
   */
  public Slider(boolean vertical) {
    super(new SliderCell(vertical ? GWT.<VerticalSliderAppearance> create(VerticalSliderAppearance.class)
        : GWT.<HorizontalSliderAppearance> create(HorizontalSliderAppearance.class)));
    setValue(50);
    redraw();
  }

  /**
   * Creates a slider with the specified slider cell.
   * 
   * @param cell the cell for this slider
   */
  public Slider(SliderCell cell) {
    super(cell);
    setValue(50);
    redraw();
  }

  @Override
  public SliderCell getCell() {
    return (SliderCell) super.getCell();
  }

  /**
   * Returns the increment.
   * 
   * @return the increment
   */
  public int getIncrement() {
    return getCell().getIncrement();
  }

  /**
   * Returns the max value (defaults to 100).
   * 
   * @return the max value
   */
  public int getMaxValue() {
    return getCell().getMaxValue();
  }

  /**
   * Returns the tool tip message.
   * 
   * @return the tool tip message
   */
  public String getMessage() {
    return getCell().getMessage();
  }

  /**
   * Returns the minimum value (defaults to 0).
   * 
   * @return the minimum value
   */
  public int getMinValue() {
    return getCell().getMinValue();
  }
  
  /**
   * Returns true if the tool tip message is shown 
   * 
   * @return the showMessage state
   */
  public boolean isShowMessage() {
    return getCell().isShowMessage();
  }

  /**
   * How many units to change the slider when adjusting by drag and drop. Use
   * this option to enable 'snapping' (default to 10).
   * 
   * @param increment the increment
   */
  public void setIncrement(int increment) {
    getCell().setIncrement(increment);
  }

  /**
   * Sets the max value (defaults to 100).
   * 
   * @param maxValue the max value
   */
  public void setMaxValue(int maxValue) {
    getCell().setMaxValue(maxValue);
  }

  /**
   * Sets the tool tip message (defaults to '{0}'). "{0} will be substituted
   * with the current slider value.
   * 
   * @param message the tool tip message
   */
  public void setMessage(String message) {
    getCell().setMessage(message);
  }

  /**
   * Sets the minimum value (defaults to 0).
   * 
   * @param minValue the minimum value
   */
  public void setMinValue(int minValue) {
    getCell().setMinValue(minValue);
  }
  
  /**
   * Sets if the tool tip message should be displayed (defaults to true,
   * pre-render).
   * 
   * @param showMessage true to show tool tip message
   */
  public void setShowMessage(boolean showMessage) {
    getCell().setShowMessage(showMessage);
  }

  /**
   * Set the tooltip config. This is the tooltip for the message configuration.
   * {@link SliderCell#setShowMessage(boolean)} has to be on to use this.
   *
   * @param toolTipConfig is the tooltip configuration.
   */
  public void setToolTipConfig(ToolTipConfig toolTipConfig) {
    getCell().setToolTipConfig(toolTipConfig);
  }

}
