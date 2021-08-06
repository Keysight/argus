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

import java.util.Set;

import com.sencha.gxt.core.client.dom.XElement;

/**
 * <code>AbstractEventCell</code> subclass which implements
 * <code>ResizableCell</code>.
 * 
 * @param <C> the cell data type
 */
public abstract class ResizeCell<C> extends AbstractEventCell<C> implements ResizableCell {

  protected int width = -1;
  protected int height = -1;

  public ResizeCell(Set<String> consumedEvents) {
    super(consumedEvents);
  }

  public ResizeCell(String... consumedEvents) {
    super(consumedEvents);
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public void setWidth(int width) {
    this.width = width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
  }
  
  @Override
  public boolean redrawOnResize() {
    return true;
  }

  @Override
  public void setSize(XElement parent, int width, int height) {
    setWidth(width);
    setHeight(height);
  }
}
