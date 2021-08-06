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
package com.sencha.gxt.explorer.client.treegrid;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ToStringValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.examples.resources.client.model.BaseDto;
import com.sencha.gxt.examples.resources.client.model.FolderDto;
import com.sencha.gxt.examples.resources.client.model.MusicDto;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

@Detail(
    name = "Basic Tree Grid",
    category = "Tree Grid",
    icon = "basictreegrid",
    classes = {
        BaseDto.class,
        FolderDto.class,
        MusicDto.class
    },
    maxHeight = BasicTreeGridExample.MAX_HEIGHT,
    maxWidth = BasicTreeGridExample.MAX_WIDTH,
    minHeight = BasicTreeGridExample.MIN_HEIGHT,
    minWidth = BasicTreeGridExample.MIN_WIDTH
)
public class BasicTreeGridExample implements IsWidget, EntryPoint {

  class KeyProvider implements ModelKeyProvider<BaseDto> {
    @Override
    public String getKey(BaseDto item) {
      return (item instanceof FolderDto ? "f-" : "m-") + item.getId().toString();
    }
  }

  protected static final int MAX_HEIGHT = 600;
  protected static final int MAX_WIDTH = 800;
  protected static final int MIN_HEIGHT = 320;
  protected static final int MIN_WIDTH = 480;

  private ContentPanel panel;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      panel = new ContentPanel();
      panel.setHeading("Basic Tree Grid");

      VerticalLayoutContainer v = new VerticalLayoutContainer();
      panel.add(v);

      TreeStore<BaseDto> store = new TreeStore<BaseDto>(new KeyProvider());

      FolderDto root = TestData.getMusicRootFolder();
      for (BaseDto base : root.getChildren()) {
        store.add(base);
        if (base instanceof FolderDto) {
          processFolder(store, (FolderDto) base);
        }
      }

      ColumnConfig<BaseDto, String> cc1 = new ColumnConfig<BaseDto, String>(new ToStringValueProvider<BaseDto>("name"));
      cc1.setHeader("Name");

      ColumnConfig<BaseDto, String> cc2 = new ColumnConfig<BaseDto, String>(new ValueProvider<BaseDto, String>() {
        @Override
        public String getValue(BaseDto object) {
          return object instanceof MusicDto ? ((MusicDto) object).getAuthor() : "";
        }

        @Override
        public void setValue(BaseDto object, String value) {
          if (object instanceof MusicDto) {
            ((MusicDto) object).setAuthor(value);
          }
        }

        @Override
        public String getPath() {
          return "author";
        }
      });
      cc2.setHeader("Author");

      ColumnConfig<BaseDto, String> cc3 = new ColumnConfig<BaseDto, String>(new ValueProvider<BaseDto, String>() {
        @Override
        public String getValue(BaseDto object) {
          return object instanceof MusicDto ? ((MusicDto) object).getGenre() : "";
        }

        @Override
        public void setValue(BaseDto object, String value) {
          if (object instanceof MusicDto) {
            ((MusicDto) object).setGenre(value);
          }
        }

        @Override
        public String getPath() {
          return "genre";
        }
      });
      cc3.setHeader("Genre");
      cc3.setCell(new TextCell());
      List<ColumnConfig<BaseDto, ?>> l = new ArrayList<ColumnConfig<BaseDto, ?>>();
      l.add(cc1);
      l.add(cc2);
      l.add(cc3);
      ColumnModel<BaseDto> cm = new ColumnModel<BaseDto>(l);

      final TreeGrid<BaseDto> tree = new TreeGrid<BaseDto>(store, cm, cc1);
      tree.getStyle().setLeafIcon(ExampleImages.INSTANCE.music());
      tree.getView().setAutoExpandColumn(cc1);
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

      v.add(buttonBar, new VerticalLayoutData(1, -1));
      v.add(tree, new VerticalLayoutData(1, 1));
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

  private void processFolder(TreeStore<BaseDto> store, FolderDto folder) {
    for (BaseDto child : folder.getChildren()) {
      store.add(folder, child);
      if (child instanceof FolderDto) {
        processFolder(store, (FolderDto) child);
      }
    }
  }
}
