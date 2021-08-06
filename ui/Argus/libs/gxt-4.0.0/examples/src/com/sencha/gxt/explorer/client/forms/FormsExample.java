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

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Stock;
import com.sencha.gxt.examples.resources.client.model.StockProperties;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent.ParseErrorHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DoubleSpinnerField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.TimeField;
import com.sencha.gxt.widget.core.client.form.validator.MinDateValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.info.Info;

@Detail(
    name = "Forms Example",
    category = "Forms",
    icon = "formsexample",
    classes = {
        Stock.class,
        StockProperties.class
    },
    minHeight = FormsExample.MIN_HEIGHT,
    minWidth = FormsExample.MIN_WIDTH
)
public class FormsExample implements IsWidget, EntryPoint {

  protected static final int MIN_HEIGHT = 940;
  protected static final int MIN_WIDTH = 360;

  private VerticalLayoutContainer widget;

  public Widget asWidget() {
    if (widget == null) {
      widget = new VerticalLayoutContainer();
      widget.add(createForm1(), new VerticalLayoutData(1, 1, new Margins(0, 0, 10, 0)));
      widget.add(createForm2(), new VerticalLayoutData(1, -1, new Margins(10, 0, 0, 0)));
    }

    return widget;
  }

  private FramedPanel createForm1() {
    TextField firstName = new TextField();
    firstName.setAllowBlank(false);
    firstName.setEmptyText("Enter your name...");
    firstName.addValueChangeHandler(new ValueChangeHandler<String>() {
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        Info.display("Value Changed", "First name changed to " + event.getValue() == null ? "blank" : event.getValue());
      }
    });

    TextField email = new TextField();
    email.setAllowBlank(false);

    PasswordField password = new PasswordField();

    IntegerField age = new IntegerField();
    age.setAllowBlank(false);
    age.addParseErrorHandler(new ParseErrorHandler() {
      @Override
      public void onParseError(ParseErrorEvent event) {
        Info.display("Parse Error", event.getErrorValue() + " could not be parsed as a number");
      }
    });

    StockProperties properties = GWT.create(StockProperties.class);

    ListStore<Stock> store = new ListStore<Stock>(properties.key());
    store.addAll(TestData.getStocks());

    ComboBox<Stock> combo = new ComboBox<Stock>(store, properties.nameLabel());
    combo.setAllowBlank(true);
    combo.setForceSelection(true);
    combo.setTriggerAction(TriggerAction.ALL);
    combo.addValueChangeHandler(new ValueChangeHandler<Stock>() {
      @Override
      public void onValueChange(ValueChangeEvent<Stock> event) {
        Info.display("Selected", "You selected " + event.getValue());
      }
    });

    DateField date = new DateField();
    date.addValidator(new MinDateValidator(new Date()));
    date.addParseErrorHandler(new ParseErrorHandler() {
      @Override
      public void onParseError(ParseErrorEvent event) {
        Info.display("Parse Error", event.getErrorValue() + " could not be parsed as a date");
      }
    });
    date.addValueChangeHandler(new ValueChangeHandler<Date>() {
      @Override
      public void onValueChange(ValueChangeEvent<Date> event) {
        String v = event.getValue() == null ? "nothing"
            : DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(event.getValue());
        Info.display("Selected", "You selected " + v);
      }
    });
    date.setAutoValidate(true);

    TimeField time = new TimeField();
    time.setTriggerAction(TriggerAction.ALL);
    time.setFormat(DateTimeFormat.getFormat("hh:mm a"));
    time.setMinValue(new DateWrapper().clearTime().addHours(8).asDate());
    time.setMaxValue(new DateWrapper().clearTime().addHours(18).addSeconds(1).asDate());
    time.addParseErrorHandler(new ParseErrorHandler() {
      @Override
      public void onParseError(ParseErrorEvent event) {
        Info.display("Parse Error", event.getErrorValue() + " could not be parsed as a valid time");
      }
    });

    Slider slider = new Slider();
    slider.setMinValue(40);
    slider.setMaxValue(90);
    slider.setValue(0);
    slider.setIncrement(5);
    slider.setMessage("{0} inches tall");

    ValueChangeHandler<Boolean> musicHandler = new ValueChangeHandler<Boolean>() {
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        CheckBox check = (CheckBox) event.getSource();
        Info.display("Music Changed", check.getBoxLabel() + " " + (event.getValue() ? "selected" : "not selected"));
      }
    };

    CheckBox check1 = new CheckBox();
    check1.setEnabled(false);
    check1.setBoxLabel("Classical");
    check1.addValueChangeHandler(musicHandler);

    CheckBox check2 = new CheckBox();
    check2.setBoxLabel("Rock");
    check2.addValueChangeHandler(musicHandler);
    check2.setValue(true);

    CheckBox check3 = new CheckBox();
    check3.setBoxLabel("Blues");
    check3.addValueChangeHandler(musicHandler);

    Radio radio = new Radio();
    radio.setBoxLabel("Red");

    Radio radio2 = new Radio();
    radio2.setBoxLabel("Blue");
    radio2.setValue(true);

    // we can set name on radios or use toggle group
    ToggleGroup toggle = new ToggleGroup();
    toggle.add(radio);
    toggle.add(radio2);
    toggle.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>() {
      @Override
      public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
        ToggleGroup group = (ToggleGroup) event.getSource();
        Radio radio = (Radio) group.getValue();
        Info.display("Color Changed", "You selected " + radio.getBoxLabel());
      }
    });

    TextArea description = new TextArea();
    description.setAllowBlank(false);
    description.addValidator(new MinLengthValidator(10));

    final DoubleSpinnerField spinnerField = new DoubleSpinnerField();
    spinnerField.setIncrement(.1d);
    spinnerField.setMinValue(-10d);
    spinnerField.setMaxValue(10d);
    spinnerField.setAllowNegative(true);
    spinnerField.setAllowBlank(false);
    spinnerField.getPropertyEditor().setFormat(NumberFormat.getFormat("0.0"));
    spinnerField.addValueChangeHandler(new ValueChangeHandler<Double>() {
      @Override
      public void onValueChange(ValueChangeEvent<Double> event) {
        Info.display("Duration Changed",
            "Duration changed to " + event.getValue() == null ? "nothing" : event.getValue() + "");
      }
    });
    spinnerField.addSelectionHandler(new SelectionHandler<Double>() {
      @Override
      public void onSelection(SelectionEvent<Double> event) {
        String msg = "nothing";
        if (event.getSelectedItem() != null) {
          msg = spinnerField.getPropertyEditor().render(event.getSelectedItem());
        }

        Info.display("Spin Change", "Current value changed to " + msg);
      }
    });

    FieldLabel spinLabel = new FieldLabel(spinnerField, "Duration(s)");

    HorizontalPanel hp1 = new HorizontalPanel();
    hp1.add(check1);
    hp1.add(check2);
    hp1.add(check3);

    HorizontalPanel hp2 = new HorizontalPanel();
    hp2.add(radio);
    hp2.add(radio2);

    VerticalLayoutContainer vlc = new VerticalLayoutContainer();
    vlc.add(new FieldLabel(firstName, "Name"), new VerticalLayoutData(1, -1));
    vlc.add(new FieldLabel(email, "Email"), new VerticalLayoutData(1, -1));
    vlc.add(new FieldLabel(password, "Password"), new VerticalLayoutData(1, -1));
    vlc.add(new FieldLabel(age, "Age"), new VerticalLayoutData(1, -1));
    vlc.add(new FieldLabel(combo, "Company"), new VerticalLayoutData(1, -1));
    vlc.add(new FieldLabel(date, "Birthday"), new VerticalLayoutData(1, -1));
    vlc.add(new FieldLabel(time, "Time"), new VerticalLayoutData(1, -1));
    vlc.add(new FieldLabel(slider, "Size"), new VerticalLayoutData(1, -1));
    vlc.add(new FieldLabel(hp1, "Music"));
    vlc.add(new FieldLabel(hp2, "Color"));
    vlc.add(new FieldLabel(description, "Description"), new VerticalLayoutData(1, 1));
    vlc.add(spinLabel, new VerticalLayoutData(1, -1));

    FramedPanel panel = new FramedPanel();
    panel.setHeading("Forms Example — Simple");
    panel.add(vlc, new MarginData(15, 15, 0, 15));
    panel.addButton(new TextButton("Save"));
    panel.addButton(new TextButton("Cancel"));

    return panel;
  }

  private FramedPanel createForm2() {
    TextField firstName = new TextField();
    firstName.setAllowBlank(false);

    TextField lastName = new TextField();
    lastName.setAllowBlank(false);

    TextField email = new TextField();
    email.setAllowBlank(false);

    VerticalLayoutContainer vlc = new VerticalLayoutContainer();
    vlc.add(new FieldLabel(firstName, "First Name"), new VerticalLayoutData(1, -1));
    vlc.add(new FieldLabel(lastName, "Last Name"), new VerticalLayoutData(1, -1));
    vlc.add(new FieldLabel(email, "Email"), new VerticalLayoutData(1, -1));

    FieldSet fieldSet = new FieldSet();
    fieldSet.setHeading("User Information");
    fieldSet.setCollapsible(true);
    fieldSet.add(vlc);

    FramedPanel form2 = new FramedPanel();
    form2.setHeading("Forms Example — FieldSet");
    form2.add(fieldSet, new MarginData(15));
    form2.addButton(new TextButton("Save"));
    form2.addButton(new TextButton("Cancel"));

    return form2;
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinHeight(MIN_HEIGHT)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

}
