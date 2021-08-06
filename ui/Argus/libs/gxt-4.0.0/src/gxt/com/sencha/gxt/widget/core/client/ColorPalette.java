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

import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.cell.CellComponent;

/**
 * A cell component that displays a palette of colors and allows the user to
 * select one.
 * <p/>
 * To be notified when the user selects a color, add a value change handler. See
 * {@link CellComponent#addValueChangeHandler(ValueChangeHandler)} for more
 * information.
 * <p/>
 * To set the currently selected color, see
 * {@link CellComponent#setValue(Object)}.
 * <p/>
 * To get the currently selected color, see {@link CellComponent#getValue()}.
 */
public class ColorPalette extends CellComponent<String> implements HasSelectionHandlers<String> {

  /**
   * Creates a new color palette with default colors.
   */
  public ColorPalette() {
    this(new ColorPaletteCell());
  }

  /**
   * Creates a new color palette {@link CellComponent} with the specified color
   * palette {@link Cell}.
   * 
   * @param cell the color palette appearance
   */
  public ColorPalette(final ColorPaletteCell cell) {
    super(cell);
  }

  /**
   * Creates a new color palette with the specified colors.
   * 
   * @param colors the colors, each consisting of a six digit hex value in
   *          RRGGBB format
   * @param labels the color names, in the same order as <code>colors</code>
   */
  public ColorPalette(String[] colors, String[] labels) {
    this(new ColorPaletteCell(colors, labels));
  }

  @Override
  public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

}
