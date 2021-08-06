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
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent.ResizeEndHandler;

@Detail(
    name = "Collapse, Drag, Resize",
    category = "Miscellaneous",
    icon = "resizable",
    maxHeight = ResizableExample.MAX_HEIGHT,
    maxWidth = ResizableExample.MAX_WIDTH,
    minHeight = ResizableExample.MIN_HEIGHT,
    minWidth = ResizableExample.MIN_WIDTH
)
public class ResizableExample implements IsWidget, EntryPoint {

  protected static final int MAX_HEIGHT = 600;
  protected static final int MAX_WIDTH = 800;
  protected static final int MIN_HEIGHT = 240;
  protected static final int MIN_WIDTH = 320;

  private ContentPanel panel;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      panel = new ContentPanel() {
        private Draggable draggable;
        private Resizable resizeable;

        @Override
        protected void onAfterFirstAttach() {
          super.onAfterFirstAttach();

          // draggable within the parent container
          draggable = new Draggable(this);
          draggable.setContainer(getParent());

          // resizable in all directions with min/max sizes
          resizeable = new Resizable(this, Dir.E, Dir.N, Dir.NE, Dir.NW, Dir.S, Dir.SE, Dir.SW, Dir.W);
          resizeable.setPreserveRatio(false);
          resizeable.setMaxHeight(MAX_HEIGHT);
          resizeable.setMaxWidth(MAX_WIDTH);
          resizeable.setMinHeight(MIN_HEIGHT);
          resizeable.setMinWidth(MIN_WIDTH);
          resizeable.addResizeEndHandler(new ResizeEndHandler() {
            @Override
            public void onResizeEnd(ResizeEndEvent event) {
              // constrain resize to the parent container
              Rectangle r = getElement().adjustForConstraints(getParent().getElement(), getElement().getBounds());
              setPagePosition(r.getX(), r.getY());
              setPixelSize(r.getWidth(), r.getHeight());
            }
          });
        }

        @Override
        protected void onCollapse() {
          // don't allow resizing while collapsed
          resizeable.setEnabled(false);

          super.onCollapse();
        }

        @Override
        protected void onExpand() {
          super.onExpand();

          // allow resizing while expanded
          resizeable.setEnabled(true);
        }
      };
      panel.setHeading("Collapse, Drag, Resize");
      panel.setCollapsible(true);
      panel.add(new Label("Collapse, drag and resize this box."), new MarginData(20));
    }

    return panel;
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMaxHeight(MAX_HEIGHT)
        .setMaxWidth(MAX_WIDTH)
        .setMinHeight(MIN_HEIGHT)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

}
