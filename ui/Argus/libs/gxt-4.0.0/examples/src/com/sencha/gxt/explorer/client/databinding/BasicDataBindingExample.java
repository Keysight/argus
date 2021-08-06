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
package com.sencha.gxt.explorer.client.databinding;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Stock;
import com.sencha.gxt.examples.resources.client.model.StockProperties;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CssFloatLayoutContainer;
import com.sencha.gxt.widget.core.client.container.CssFloatLayoutContainer.CssFloatData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DoubleField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MinNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

@Detail(
    name = "Basic Data Binding",
    category = "Data Binding",
    icon = "basicbinding",
    files = "BasicDataBindingExample.html",
    classes = {
        Stock.class,
        StockProperties.class
    },
    minHeight = BasicDataBindingExample.MIN_WIDTH,
    minWidth = BasicDataBindingExample.MIN_HEIGHT
)
public class BasicDataBindingExample implements IsWidget, EntryPoint, Editor<Stock> {

  interface StockDriver extends SimpleBeanEditorDriver<Stock, BasicDataBindingExample> {
  }

  interface StockTemplate extends XTemplates {
    @XTemplate(source = "BasicDataBindingExample.html")
    SafeHtml drawStock(Stock stock);
  }

  protected static final int MIN_WIDTH = 350;
  protected static final int MIN_HEIGHT = 475;

  private ContentPanel panel;
  private Stock stock;
  private HTML display;
  private ComboBox<Stock> nameCombo;

  // editor fields
  TextField symbol;
  TextField name;
  DoubleField last;
  DoubleField change;
  DateField lastTrans;

  private ListStore<Stock> stockStore;
  private StockDriver driver = GWT.create(StockDriver.class);
  private StockProperties props;

  public Widget asWidget() {
    if (panel == null) {
      props = GWT.create(StockProperties.class);

      stockStore = new ListStore<Stock>(props.key());
      stockStore.addAll(TestData.getStocks());

      stock = stockStore.get(0);

      HorizontalLayoutContainer hp = new HorizontalLayoutContainer();
      hp.add(updateDisplay(), new HorizontalLayoutData(200, 1, new Margins(0, 10, 0, 0)));
      hp.add(createEditor(), new HorizontalLayoutData(1, 1));

      panel = new ContentPanel();
      panel.setHeading("Basic Data Binding");
      panel.add(hp);

      driver.initialize(this);
      nameCombo.setValue(stock);
      driver.edit(stock);
    }

    return panel;
  }

  private Widget createEditor() {
    nameCombo = new ComboBox<Stock>(stockStore, props.nameLabel());
    nameCombo.setForceSelection(true);
    nameCombo.setTypeAhead(true);
    nameCombo.setName("company");
    nameCombo.setTriggerAction(TriggerAction.ALL);
    nameCombo.setEditable(false);
    nameCombo.addSelectionHandler(new SelectionHandler<Stock>() {
      @Override
      public void onSelection(SelectionEvent<Stock> event) {
        symbol.clearInvalid();
        change.clearInvalid();
        last.clearInvalid();
        lastTrans.clearInvalid();

        stock = event.getSelectedItem();
        driver.edit(stock);
        updateDisplay();
      }
    });

    name = new TextField();
    name.setAllowBlank(false);

    symbol = new TextField();
    symbol.addValidator(new RegExValidator("^[^a-z]+$", "Only uppercase letters allowed"));
    symbol.setAutoValidate(true);
    symbol.setName("symbol");

    last = new DoubleField();
    last.setName("last");
    last.setFormat(NumberFormat.getFormat("0.00"));
    last.setAllowNegative(false);
    last.addValidator(new MinNumberValidator<Double>(0D));

    change = new DoubleField();
    change.setName("change");
    change.setFormat(NumberFormat.getFormat("0.00"));

    lastTrans = new DateField();
    lastTrans.setName("date");
    lastTrans.setClearValueOnParseError(false);
    lastTrans.setAutoValidate(true);

    final CssFloatLayoutContainer inner = new CssFloatLayoutContainer();
    inner.add(new FieldLabel(name, "Name"), new CssFloatData(1));
    inner.add(new FieldLabel(symbol, "Symbol"), new CssFloatData(1));
    inner.add(new FieldLabel(last, "Last"), new CssFloatData(1));
    inner.add(new FieldLabel(change, "Change"), new CssFloatData(1));
    inner.add(new FieldLabel(lastTrans, "Updated"), new CssFloatData(1));

    CssFloatLayoutContainer outer = new CssFloatLayoutContainer();
    outer.add(new FieldLabel(nameCombo, "Select Company"), new CssFloatData(1));
    outer.add(inner, new CssFloatData(1));

    TextButton reset = new TextButton("Cancel");
    reset.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        FormPanelHelper.reset(inner);
        driver.edit(stock);
      }
    });

    TextButton save = new TextButton("Save");
    save.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        stock = driver.flush();
        if (driver.hasErrors()) {
          new MessageBox("Please correct the errors before saving.").show();
          return;
        }
        updateDisplay();
        stockStore.update(stock);
      }
    });

    FramedPanel panel = new FramedPanel();
    panel.setHeaderVisible(false);
    panel.setBorders(false);
    panel.add(outer, new MarginData(5));
    panel.addButton(reset);
    panel.addButton(save);

    return panel;
  }

  private HTML updateDisplay() {
    if (display == null) {
      display = new HTML();
    }
    StockTemplate template = GWT.create(StockTemplate.class);
    display.setHTML(template.drawStock(stock));
    return display;
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinHeight(MIN_HEIGHT)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

}
