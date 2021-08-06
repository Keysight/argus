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
package com.sencha.gxt.explorer.client.window;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.examples.resources.client.ExampleStyles;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.examples.resources.client.model.NameImageModel;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.app.ui.ExplorerShell.Theme;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;

@Detail(
    name = "Accordion Window",
    category = "Windows",
    icon = "accordionwindow",
    minWidth = AccordionWindowExample.MIN_WIDTH,
    preferredHeight = AccordionWindowExample.PREFERRED_HEIGHT
)
public class AccordionWindowExample implements IsWidget, EntryPoint {

  protected static final int MIN_WIDTH = 56;
  protected static final int PREFERRED_HEIGHT = -1;

  private ButtonBar buttonBar;

  @Override
  public Widget asWidget() {
    if (buttonBar == null) {
      AccordionLayoutAppearance appearance = GWT.<AccordionLayoutAppearance> create(AccordionLayoutAppearance.class);

      final NameImageModel modelFamily = newItem("Family", null);
      final NameImageModel modelFriends = newItem("Friends", null);

      TreeStore<NameImageModel> store = new TreeStore<NameImageModel>(NameImageModel.KP);
      store.add(modelFamily);
      store.add(modelFamily, newItem("John", "user"));
      store.add(modelFamily, newItem("Olivia", "user-girl"));
      store.add(modelFamily, newItem("Noah", "user-kid"));
      store.add(modelFamily, newItem("Emma", "user-kid"));
      store.add(modelFamily, newItem("Liam", "user-kid"));
      store.add(modelFriends);
      store.add(modelFriends, newItem("Mason", "user"));
      store.add(modelFriends, newItem("Sophia", "user-girl"));
      store.add(modelFriends, newItem("Isabella", "user-girl"));
      store.add(modelFriends, newItem("Jacob", "user"));

      Tree<NameImageModel, String> tree = new Tree<NameImageModel, String>(store,
          new ValueProvider<NameImageModel, String>() {
            @Override
            public String getValue(NameImageModel object) {
              return object.getName();
            }

            @Override
            public void setValue(NameImageModel object, String value) {
            }

            @Override
            public String getPath() {
              return "name";
            }
          }) {
        @Override
        protected void onAfterFirstAttach() {
          super.onAfterFirstAttach();
          
          setExpanded(modelFamily, true);
          setExpanded(modelFriends, true);
        }
      };
      tree.setIconProvider(new IconProvider<NameImageModel>() {
        public ImageResource getIcon(NameImageModel model) {
          if (null == model.getImage()) {
            return null;
          } else if ("user-girl" == model.getImage()) {
            return ExampleImages.INSTANCE.userFemale();
          } else if ("user-kid" == model.getImage()) {
            return ExampleImages.INSTANCE.userKid();
          } else {
            return ExampleImages.INSTANCE.user();
          }
        }
      });

      ContentPanel cp1 = new ContentPanel(appearance);
      cp1.setHeading("Online Users");
      cp1.add(tree);
      if (Theme.BLUE.isActive() || Theme.GRAY.isActive()) {
        cp1.getHeader().addStyleName(ThemeStyles.get().style().borderTop());
      }

      ContentPanel cp2 = new ContentPanel(appearance);
      cp2.setBodyStyleName(ExampleStyles.get().paddedText());
      cp2.setHeading("Settings");
      cp2.add(new Label(TestData.DUMMY_TEXT_SHORT));

      ContentPanel cp3 = new ContentPanel(appearance);
      cp3.setBodyStyleName(ExampleStyles.get().paddedText());
      cp3.setHeading("Stuff");
      cp3.add(new Label(TestData.DUMMY_TEXT_SHORT));

      ContentPanel cp4 = new ContentPanel(appearance);
      cp4.setBodyStyleName(ExampleStyles.get().paddedText());
      cp4.setHeading("More Stuff");
      cp4.add(new Label(TestData.DUMMY_TEXT_SHORT));

      AccordionLayoutContainer accordion = new AccordionLayoutContainer();
      accordion.setExpandMode(ExpandMode.SINGLE_FILL);
      accordion.add(cp1);
      accordion.add(cp2);
      accordion.add(cp3);
      accordion.add(cp4);
      accordion.setActiveWidget(cp1);

      final Window complex = new Window();
      complex.setResizable(false);
      complex.setHeading("Accordion Window");
      complex.setWidth(275);
      complex.setHeight(350);
      complex.add(accordion);

      TextButton buttonOpen = new TextButton("Open");
      buttonOpen.addSelectHandler(new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          complex.show();
        }
      });

      buttonBar = new ButtonBar();
      buttonBar.add(buttonOpen);
    }

    return buttonBar;
  }

  private NameImageModel newItem(String text, String iconStyle) {
    return new NameImageModel(text, iconStyle);
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinWidth(MIN_WIDTH)
        .setPreferredHeight(PREFERRED_HEIGHT)
        .doStandalone();
  }

}
