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
package com.sencha.gxt.cell.core.client;

import com.google.gwt.user.client.Timer;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.dom.XElement;

public class AutoProgressBarCell extends ProgressBarCell {

  private Timer timer;
  private boolean running;
  private int i = 0;
  private int duration = Style.DEFAULT;
  private int interval = 300;

  public AutoProgressBarCell() {
    super();
  }
  
  public AutoProgressBarCell(ProgressBarAppearance appearance) {
    super(appearance);
  }
  
  /**
   * Initiates an auto-updating progress bar using the current duration,
   * increment, and interval.
   */
  public void auto(final Context context, final XElement parent) {
    if (timer == null) {
      timer = new Timer() {
        public void run() {
          int inc = getIncrement();
          updateProgress(context, parent, ((((i++ + inc) % inc) + 1) * (100 / inc)) * .01, null);
        }
      };
    }
    timer.scheduleRepeating(getInterval());
    running = true;
  }

  /**
   * Returns the duration.
   * 
   * @return the duration
   */
  public int getDuration() {
    return duration;
  }

  /**
   * Returns the bar's interval value.
   * 
   * @return the interval in milliseconds
   */
  public int getInterval() {
    return interval;
  }

  /**
   * Returns true if the progress bar is currently in a {@link #auto} operation.
   * 
   * @return true if waiting, else false
   */
  public boolean isRunning() {
    return running;
  }

  @Override
  public void reset(Context context, XElement parent) {
    super.reset(context, parent);
    if (timer != null) {
      timer.cancel();
    }
    running = false;
  }

  /**
   * The length of time in milliseconds that the progress bar should run before
   * resetting itself (defaults to DEFAULT, in which case it will run
   * indefinitely until reset is called)
   * 
   * @param duration the duration in milliseconds
   */
  public void setDuration(int duration) {
    this.duration = duration;
  }

  /**
   * Sets the length of time in milliseconds between each progress update
   * (defaults to 300 ms).
   * 
   * @param interval the interval to set
   */
  public void setInterval(int interval) {
    this.interval = interval;
  }
}
