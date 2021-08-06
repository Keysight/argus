/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktop.client.widget;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class AutoVerticalLayoutContainer extends VerticalLayoutContainer {
  @Override
  public void add(Widget child) {
    super.add(child);
    doLayout();
  }
}