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
package com.sencha.gxt.explorer.client.tree;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.examples.resources.client.model.BaseDto;
import com.sencha.gxt.examples.resources.client.model.FolderDto;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;

@Detail(
    name = "Cell Action Tree",
    category = "Tree",
    icon = "cellactiontree",
    classes = {
        BaseDto.class,
        FolderDto.class
    },
    minHeight = ActionTreeExample.MIN_HEIGHT,
    minWidth = ActionTreeExample.MIN_WIDTH
)
public class ActionTreeExample implements IsWidget, EntryPoint {

  class KeyProvider implements ModelKeyProvider<BaseDto> {
    @Override
    public String getKey(BaseDto item) {
      return (item instanceof FolderDto ? "f-" : "m-") + item.getId().toString();
    }
  }

  protected static final int MIN_HEIGHT = 240;
  protected static final int MIN_WIDTH = 320;

  private ContentPanel panel;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      TreeStore<BaseDto> store = new TreeStore<BaseDto>(new KeyProvider());

      FolderDto root = TestData.getMusicRootFolder();
      for (BaseDto base : root.getChildren()) {
        store.add(base);
        if (base instanceof FolderDto) {
          processFolder(store, (FolderDto) base);
        }
      }

      SimpleSafeHtmlCell<String> cell = new SimpleSafeHtmlCell<String>(SimpleSafeHtmlRenderer.getInstance(), "click", "touchend") {
        @Override
        public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event,
            ValueUpdater<String> valueUpdater) {
          super.onBrowserEvent(context, parent, value, event, valueUpdater);
          if ("touchend".equals(event.getType()) || "click".equals(event.getType())) {
            Info.display("Selected", "You selected \"" + value + "\"!");
          }
        }
      };

      final Tree<BaseDto, String> tree = new Tree<BaseDto, String>(store, new ValueProvider<BaseDto, String>() {
        @Override
        public String getValue(BaseDto object) {
          return object.getName();
        }

        @Override
        public void setValue(BaseDto object, String value) {
        }

        @Override
        public String getPath() {
          return "name";
        }
      });
      tree.setCell(cell);
      tree.setWidth(300);
      tree.getStyle().setLeafIcon(ExampleImages.INSTANCE.music());

      ToolBar buttonBar = new ToolBar();
      buttonBar.add(new TextButton("Expand All", new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          tree.expandAll();
        }
      }));
      buttonBar.add(new TextButton("Collapse All", new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          tree.collapseAll();
        }
      }));

      VerticalLayoutContainer vlc = new VerticalLayoutContainer();
      vlc.add(buttonBar, new VerticalLayoutData(1, -1));
      vlc.add(tree, new VerticalLayoutData(1, 1));

      panel = new ContentPanel();
      panel.setHeading("Cell Action Tree");
      panel.add(vlc);
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

  private void processFolder(TreeStore<BaseDto> store, FolderDto folder) {
    for (BaseDto child : folder.getChildren()) {
      store.add(folder, child);
      if (child instanceof FolderDto) {
        processFolder(store, (FolderDto) child);
      }
    }
  }

}
