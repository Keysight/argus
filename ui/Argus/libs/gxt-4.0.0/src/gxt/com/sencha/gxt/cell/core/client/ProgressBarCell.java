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

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.dom.XElement;

public class ProgressBarCell extends ResizeCell<Double> {

  public static interface ProgressBarAppearance {

    void render(SafeHtmlBuilder sb, Double value, ProgressBarAppearanceOptions options);

  }

  public static class ProgressBarAppearanceOptions {

    private String progressText;
    private int width;

    public ProgressBarAppearanceOptions(String progressText, int width) {
      this.progressText = progressText;
      this.width = width;
    }

    public String getProgressText() {
      return progressText;
    }

    public int getWidth() {
      return width;
    }

    public void setProgressText(String progressText) {
      this.progressText = progressText;
    }

    public void setWidth(int width) {
      this.width = width;
    }

  }

  private final ProgressBarAppearance appearance;
  private int increment = 10;

  private String progressText;

  public ProgressBarCell() {
    this(GWT.<ProgressBarAppearance> create(ProgressBarAppearance.class));
  }

  public ProgressBarCell(ProgressBarAppearance appearance) {
    this.appearance = appearance;
    this.width = 300;
  }

  /**
   * Returns the progress bar appearance.
   * 
   * @return the appearance
   */
  public ProgressBarAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the bar's increment value.
   * 
   * @return the increment the increment
   */
  public int getIncrement() {
    return increment;
  }

  /**
   * Returns the progress text.
   * 
   * @return the progress text
   */
  public String getProgressText() {
    return progressText;
  }

  @Override
  public void render(Context context, Double value, SafeHtmlBuilder sb) {
    appearance.render(sb, value, new ProgressBarAppearanceOptions(progressText, getWidth()));
  }

  /**
   * Resets the progress bar value to 0 and text to empty string.
   */
  public void reset(Context context, XElement parent) {
    progressText = null;
    setValue(context, parent, 0d);
  }

  /**
   * The number of progress update segments to display within the progress bar
   * (defaults to 10). If the bar reaches the end and is still updating, it will
   * automatically wrap back to the beginning.
   * 
   * @param increment the new increment
   */
  public void setIncrement(int increment) {
    this.increment = increment;
  }

  /**
   * Sets the progress text to be displayed in the progress bar. If the text
   * contains '{0}' it will be replaced with the current progress bar value (0 -
   * 100).
   * 
   * @param text the progress text
   */
  public void setProgressText(String text) {
    this.progressText = text;
  }

  /**
   * Updates the progress bar value, and optionally its text. If the text
   * argument is not specified, any existing text value will be unchanged. To
   * blank out existing text, pass "". Note that even if the progress bar value
   * exceeds 1, it will never automatically reset -- you are responsible for
   * determining when the progress is complete and calling {@link #reset} to
   * clear and/or hide the control.
   * 
   * @param value a value between 0 and 1 (e.g., .5, defaults to 0)
   * @param text the string to display in the progress text element or null
   */
  public void updateProgress(Context context, XElement parent, double value, String text) {
    if (text != null) {
      this.progressText = text;
    }
    value = Math.min(Math.max(value, 0), 1);
    setValue(context, parent, value);
  }

}
