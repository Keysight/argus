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
package com.sencha.gxt.explorer.client.grid;

import com.google.gwt.dom.client.Style;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;

/**
 * A {@link Grid} {@link ColumnHeader} that supports word wrap, enabling long column headings to be displayed in
 * multiple rows. Provides options for letting the height of the header default to the height of the tallest heading, or
 * to be manually set to a specific pixel value.
 * <p />
 * In order for a heading to word wrap, the heading must be set to HTML that enables word wrap, e.g.
 * <code>&lt;span style='white-space: normal;'&gt;A very long heading&lt;/span&gt;</code>.
 */
public class WordWrapColumnHeader<M> extends ColumnHeader<M> {

  protected class WordWrapHead extends ColumnHeader<M>.Head {
    @SuppressWarnings({"rawtypes"})
    public WordWrapHead(ColumnConfig column) {
      super(column);
      getElement().getStyle().setFloat(Style.Float.NONE);
    }
  }

  private int fixedHeight = -1;

  /**
   * Constructs a new word wrap column header for the specified grid and column model. To configure the grid to use the
   * new word wrap column header, invoke {@link GridView#setColumnHeader(ColumnHeader)}, e.g.
   * <code>grid.getView().setColumnHeader(new WordWrapColumnHeader&lt;T&gt;(g, cm)</code>.
   * 
   * @param container - the grid that contains this word wrap column header
   * @param cm - the column model that provides the configuration for this column header
   */
  public WordWrapColumnHeader(Grid<M> container, ColumnModel<M> cm) {
    super(container, cm);
  }

  /**
   * Sets the height of the word wrap column header to a specific value. Generally this is not required as the column
   * header sizes itself to the height of the tallest heading. However, there are some use cases where the column header
   * height must be set (e.g. linked grids displayed side-by-side, where the column header height of one grid must match
   * that of the other).
   * 
   * @param newHeight the height of the column header, or -1 to resize the header to the height of it's tallest heading
   *          (i.e. it's "natural" height)
   */
  @Override
  public void setHeight(int newHeight) {
    if (GXT.isIE()) {
      getElement().getOffsetParent().setScrollTop(0);
    }

    // Do not forward to base class... the parent element must remain unsized
    fixedHeight = newHeight;
    if (newHeight == -1) {
      setNaturalHeight();
    } else {
      setFixedHeight(newHeight);
    }
    super.checkHeaderSizeChange();
  }

  @Override
  protected void checkHeaderSizeChange() {
    onHeaderSizeChange();
    super.checkHeaderSizeChange();
  }

  @Override
  @SuppressWarnings("rawtypes")
  protected Head createNewHead(ColumnConfig config) {
    return new WordWrapHead(config);
  }

  private int getContentHeight() {
    int height = 0;
    int columnCount = cm.getColumnCount();
    for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
      if (!cm.isHidden(columnIndex)) {
        Head head = getHead(columnIndex);
        XElement inner = head.getElement();
        height = Math.max(height, inner.getOffsetHeight());
      }
    }
    
    // adjust to allow space for sort icon
    height += 5;
    return height;
  }

  private void onHeaderSizeChange() {
    if (fixedHeight == -1) {
      setNaturalHeight();
    } else {
      setFixedHeight(fixedHeight);
    }
  }

  private void setFixedHeight(int newHeight) {
    int offsetHeight = getOffsetHeight();
    int contentHeight = getContentHeight();
    int deltaHeight = offsetHeight - contentHeight;
    newHeight -= deltaHeight;
    overrideHeaderHeight = newHeight;
    refresh();
  }

  private void setNaturalHeight() {
    overrideHeaderHeight = -1;
    refresh();
  }

}