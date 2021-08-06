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

import com.sencha.gxt.cell.core.client.ProgressBarCell;
import com.sencha.gxt.widget.core.client.cell.CellComponent;

/**
 * An manual mode updateable progress bar widget.
 * 
 * <p />
 * You are responsible for showing, updating (via {@link #updateProgress}) and
 * clearing the progress bar as needed from your own code. This method is most
 * appropriate when you want to show progress throughout an operation that has
 * predictable points of interest at which you can update the control.
 */
public class ProgressBar extends CellComponent<Double> {

  /**
   * Creates a new progress bar with the default progress bar cell.
   */
  public ProgressBar() {
    this(new ProgressBarCell());
  }

  /**
   * Creates a new progress bar with the specified progress bar cell.
   * 
   * @param cell the progress bar cell
   */
  public ProgressBar(ProgressBarCell cell) {
    super(cell);
    setWidth(300);
  }

  /**
   * Returns the bar's increment value.
   * 
   * @return the increment the increment
   */
  public int getIncrement() {
    return getCell().getIncrement();
  }

  /**
   * Resets the progress bar value to 0 and text to empty string.
   */
  public void reset() {
    setValue(0d, false, false);
    getCell().setProgressText("");
    getCell().reset(createContext(), getElement());
  }

  /**
   * The number of progress update segments to display within the progress bar
   * (defaults to 10). If the bar reaches the end and is still updating, it will
   * automatically wrap back to the beginning.
   * 
   * @param increment the new increment
   */
  public void setIncrement(int increment) {
    getCell().setIncrement(increment);
  }

  /**
   * Updates the progress bar value, and optionally its text. Any instances of
   * {0} in the given text will be substituted with the progress bar's value (0
   * - 100).If the text argument is not specified, any existing text value will
   * be unchanged. To blank out existing text, pass "". Note that even if the
   * progress bar value exceeds 1, it will never automatically reset -- you are
   * responsible for determining when the progress is complete and calling
   * {@link #reset} to clear and/or hide the control.
   * 
   * @param value A value between 0 and 1 (e.g., .5, defaults to 0)
   * @param text the string to display in the progress text element or null
   */
  public void updateProgress(double value, String text) {
    getCell().setProgressText(text);
    setValue(value, true, true);
  }

  @Override
  public ProgressBarCell getCell() {
    return (ProgressBarCell)super.getCell();
  }

  /**
   * Updates the progress bar text.
   * 
   * @param text the text to display
   */
  public void updateText(String text) {
    getCell().setProgressText(text);
    redraw(true);
  }

}
