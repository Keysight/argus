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

import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.cell.CellComponent;

/**
 * Interface for cells that can be sized.
 * 
 * <p />
 * {@link CellComponent} will "size" any cells which implement this interface.
 */
public interface ResizableCell {

  /**
   * Returns the height.
   * 
   * @return the height
   */
  public int getHeight();

  /**
   * Returns the width.
   * 
   * @return the width
   */
  public int getWidth();

  /**
   * Determines if the cell should be redrawn when resized by @link
   * {@link CellComponent}. If true, {@link CellComponent#redraw()} will be
   * called.
   * 
   * @return true to force a redraw
   */
  public boolean redrawOnResize();

  /**
   * Sets the height.
   * 
   * @param height the height in pixels
   */
  public void setHeight(int height);

  /**
   * Sets the cell size.
   * 
   * @param width the width in pixels
   * @param height the height in pixels
   */
  public void setSize(int width, int height);

  /**
   * Sets the size of the cell without requiring a redraw. This method is called
   * by {@link CellComponent} when {@link #redrawOnResize()} returns false.
   * 
   * @param parent the parent element
   * @param width the width
   * @param height the height
   */
  public void setSize(XElement parent, int width, int height);

  /**
   * Sets the width.
   * 
   * @param width the width in pixels
   */
  public void setWidth(int width);
}
