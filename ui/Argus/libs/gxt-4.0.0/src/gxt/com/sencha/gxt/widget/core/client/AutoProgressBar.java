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

import com.sencha.gxt.cell.core.client.AutoProgressBarCell;

/**
 * An auto mode progress bar widget.
 * 
 * <p />
 * You simply call {@link #auto} and let the progress bar run indefinitely, only
 * clearing it once the operation is complete. You can optionally have the
 * progress bar wait for a specific amount of time and then clear itself.
 * Automatic mode is most appropriate for timed operations or asynchronous
 * operations in which you have no need for indicating intermediate progress.
 */
public class AutoProgressBar extends ProgressBar {

  /**
   * Creates a new progress bar with the default automatic progress bar cell.
   */
  public AutoProgressBar() {
    this(new AutoProgressBarCell());
  }

  /**
   * Creates a new progress bar with the specified automatic progress bar cell.
   * 
   * @param cell the automatic progress bar cell
   */
  public AutoProgressBar(AutoProgressBarCell cell) {
    super(cell);
  }

  /**
   * Initiates an auto-updating progress bar using the current duration,
   * increment, and interval.
   */
  public void auto() {
    getCell().auto(createContext(), getElement());
  }

  @Override
  public AutoProgressBarCell getCell() {
    return (AutoProgressBarCell) super.getCell();
  }

  /**
   * Returns the duration.
   * 
   * @return the duration
   */
  public int getDuration() {
    return getCell().getDuration();
  }

  /**
   * Returns the bar's interval value.
   * 
   * @return the interval in milliseconds
   */
  public int getInterval() {
    return getCell().getInterval();
  }

  /**
   * Returns true if the progress bar is currently in a {@link #auto} operation.
   * 
   * @return true if waiting, else false
   */
  public boolean isRunning() {
    return getCell().isRunning();
  }

  /**
   * The length of time in milliseconds that the progress bar should run before
   * resetting itself (defaults to DEFAULT, in which case it will run
   * indefinitely until reset is called).
   * 
   * @param duration the duration in milliseconds
   */
  public void setDuration(int duration) {
    getCell().setDuration(duration);
  }

  /**
   * Sets the length of time in milliseconds between each progress update
   * (defaults to 300 ms).
   * 
   * @param interval the interval to set
   */
  public void setInterval(int interval) {
    getCell().setInterval(interval);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    if (isRunning()) {
      reset();
    }
  }

}
