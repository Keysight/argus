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
package com.sencha.gxt.explorer.client.misc;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;

@Detail(
    name = "Draggable",
    category = "Miscellaneous",
    icon = "draggable",
    minHeight = DraggableExample.MIN_HEIGHT,
    minWidth = DraggableExample.MIN_WIDTH
)
public class DraggableExample implements IsWidget, EntryPoint {

  protected static final int MIN_HEIGHT = -1;
  protected static final int MIN_WIDTH = 480;

  private HorizontalLayoutContainer panel;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      ContentPanel cp1 = new ContentPanel() {
        @Override
        protected void onAfterFirstAttach() {
          super.onAfterFirstAttach();

          Draggable draggable = new Draggable(this);
          draggable.setContainer(getParent().getParent());
        }
      };
      cp1.setBodyStyleName("pad-text");
      cp1.setHeading("Draggable — Proxy Drag");
      cp1.add(new Label(TestData.DUMMY_TEXT_SHORT));

      ContentPanel cp2 = new ContentPanel() {
        @Override
        protected void onAfterFirstAttach() {
          super.onAfterFirstAttach();

          Draggable draggable = new Draggable(this, getHeader());
          draggable.setContainer(getParent().getParent());
          draggable.setUseProxy(false);
        }
      };
      cp2.setBodyStyleName("pad-text");
      cp2.setHeading("Draggable — Direct Drag");
      cp2.add(new Label("Drags can only be started from the header."));

      ContentPanel cp3 = new ContentPanel() {
        @Override
        protected void onAfterFirstAttach() {
          super.onAfterFirstAttach();

          Draggable draggable = new Draggable(this, getHeader());
          draggable.setContainer(getParent().getParent());
          draggable.setConstrainHorizontal(true);
        }
      };
      cp3.setBodyStyleName("pad-text");
      cp3.setHeading("Draggable — Constrain");
      cp3.add(new Label("Can only be dragged vertically."));

      panel = new HorizontalLayoutContainer();
      panel.add(cp1, new HorizontalLayoutData(0.33, -1, new Margins(10)));
      panel.add(cp2, new HorizontalLayoutData(0.33, -1, new Margins(10)));
      panel.add(cp3, new HorizontalLayoutData(0.33, -1, new Margins(10)));
    }

    return panel;
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinHeight(MIN_HEIGHT)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

}
