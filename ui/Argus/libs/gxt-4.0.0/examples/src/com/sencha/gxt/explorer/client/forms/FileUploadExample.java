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
package com.sencha.gxt.explorer.client.forms;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Stock;
import com.sencha.gxt.examples.resources.client.model.StockProperties;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

@Detail(
    name = "File Upload",
    category = "Forms",
    icon = "fileupload",
    classes = {
        Stock.class,
        StockProperties.class
    },
    maxHeight = FileUploadExample.MAX_HEIGHT,
    maxWidth = FileUploadExample.MAX_WIDHT,
    minHeight = FileUploadExample.MIN_HEIGHT,
    minWidth = FileUploadExample.MIN_WIDTH
)
public class FileUploadExample implements IsWidget, EntryPoint {

  protected static final int MAX_HEIGHT = 220;
  protected static final int MAX_WIDHT = 500;
  protected static final int MIN_HEIGHT = 220;
  protected static final int MIN_WIDTH = 350;

  private FramedPanel panel;
  private FormPanel form;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      TextField firstName = new TextField();
      firstName.setAllowBlank(false);

      final FileUploadField file = new FileUploadField();
      file.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          Info.display("File Changed", "You selected " + file.getValue());
        }
      });
      file.setName("uploadedfile");
      file.setAllowBlank(false);

      StockProperties properties = GWT.create(StockProperties.class);

      ListStore<Stock> store = new ListStore<Stock>(properties.key());
      store.addAll(TestData.getStocks());

      ComboBox<Stock> combo = new ComboBox<Stock>(store, properties.nameLabel());
      combo.setTriggerAction(TriggerAction.ALL);

      TextButton resetButton = new TextButton("Reset");
      resetButton.addSelectHandler(new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          form.reset();
          file.reset();
        }
      });

      VerticalLayoutContainer vlc = new VerticalLayoutContainer();
      vlc.add(new FieldLabel(firstName, "Name"), new VerticalLayoutData(1, -1));
      vlc.add(new FieldLabel(file, "File"), new VerticalLayoutData(1, -1));
      vlc.add(new FieldLabel(combo, "Company"), new VerticalLayoutData(1, -1));

      form = new FormPanel();
      form.setAction("myurl");
      form.setEncoding(Encoding.MULTIPART);
      form.setMethod(Method.POST);
      form.add(vlc, new MarginData(10));

      TextButton submitButton = new TextButton("Submit");
      submitButton.addSelectHandler(new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          if (!form.isValid()) {
            return;
          }
          // normally would submit the form but for example no server set up to
          // handle the post
          // panel.submit();
          MessageBox box = new MessageBox("File Upload Example", "Your file was uploaded.");
          box.setIcon(MessageBox.ICONS.info());
          box.show();
        }
      });

      panel = new FramedPanel();
      panel.setHeading("File Upload");
      panel.setButtonAlign(BoxLayoutPack.CENTER);
      panel.add(form);
      panel.addButton(resetButton);
      panel.addButton(submitButton);
    }

    return panel;
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMaxHeight(MAX_HEIGHT)
        .setMaxWidth(MAX_WIDHT)
        .setMinHeight(MIN_HEIGHT)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

}
