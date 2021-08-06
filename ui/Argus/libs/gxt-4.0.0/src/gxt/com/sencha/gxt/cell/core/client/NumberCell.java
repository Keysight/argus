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

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;

public class NumberCell<N extends Number> extends AbstractCell<N> {

  /**
   * The {@link NumberFormat} used to render the number.
   */
  private final NumberFormat format;

  /**
   * The {@link SafeHtmlRenderer} used to render the formatted number as HTML.
   */
  private final SafeHtmlRenderer<String> renderer;

  /**
   * Construct a new {@link NumberCell} using decimal format and a
   * {@link SimpleSafeHtmlRenderer}.
   */
  public NumberCell() {
    this(NumberFormat.getDecimalFormat(), SimpleSafeHtmlRenderer.getInstance());
  }

  /**
   * Construct a new {@link NumberCell} using the given {@link NumberFormat} and
   * a {@link SimpleSafeHtmlRenderer}.
   *
   * @param format the {@link NumberFormat} used to render the number
   */
  public NumberCell(NumberFormat format) {
    this(format, SimpleSafeHtmlRenderer.getInstance());
  }

  /**
   * Construct a new {@link NumberCell} using decimal format and the given
   * {@link SafeHtmlRenderer}.
   *
   * @param renderer the {@link SafeHtmlRenderer} used to render the formatted
   *          number as HTML
   */
  public NumberCell(SafeHtmlRenderer<String> renderer) {
    this(NumberFormat.getDecimalFormat(), renderer);
  }

  /**
   * Construct a new {@link NumberCell} using the given {@link NumberFormat} and
   * a {@link SafeHtmlRenderer}.
   *
   * @param format the {@link NumberFormat} used to render the number
   * @param renderer the {@link SafeHtmlRenderer} used to render the formatted
   *          number as HTML
   */
  public NumberCell(NumberFormat format, SafeHtmlRenderer<String> renderer) {
    if (format == null) {
      throw new IllegalArgumentException("format == null");
    }
    if (renderer == null) {
      throw new IllegalArgumentException("renderer == null");
    }
    this.format = format;
    this.renderer = renderer;
  }

  @Override
  public void render(Context context, Number value, SafeHtmlBuilder sb) {
    if (value != null) {
      sb.append(renderer.render(format.format(value)));
    }
  }
}

