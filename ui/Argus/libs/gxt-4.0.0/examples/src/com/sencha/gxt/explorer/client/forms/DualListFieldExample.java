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

import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.State;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;

@Detail(
    name = "Dual List Field",
    category = "Forms",
    icon = "duallistfield",
    classes = State.class,
    minHeight = DualListFieldExample.MIN_HEIGHT,
    minWidth = DualListFieldExample.MIN_WIDTH
)
public class DualListFieldExample implements IsWidget, EntryPoint {

  interface StateProperties extends PropertyAccess<State> {
    ModelKeyProvider<State> abbr();

    LabelProvider<State> name();

    @Path("name")
    ValueProvider<State, String> nameProp();
  }

  protected static final int MIN_HEIGHT = 340;
  protected static final int MIN_WIDTH = 420;

  private FramedPanel panel;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      StateProperties props = GWT.create(StateProperties.class);
      
      ListStore<State> states = new ListStore<State>(props.abbr());
      states.addAll(TestData.getStates());
      
      ListStore<State> toStates = new ListStore<State>(props.abbr());
      
      TextField firstName = new TextField();
      firstName.setAllowBlank(false);

      final DualListField<State, String> field = new DualListField<State, String>(states, toStates, props.nameProp(),
          new TextCell());
      field.addValidator(new EmptyValidator<List<State>>());
      field.setEnableDnd(true);
      field.setMode(Mode.INSERT);

      TextField email = new TextField();
      email.setAllowBlank(false);

      VerticalLayoutContainer vlc = new VerticalLayoutContainer();
      vlc.add(new FieldLabel(firstName, "Name"), new VerticalLayoutData(1, -1));
      vlc.add(new FieldLabel(field, "States"), new VerticalLayoutData(1, 1));
      vlc.add(new FieldLabel(email, "Email"), new VerticalLayoutData(1, -1));

      panel = new FramedPanel();
      panel.setHeading("Dual List Field");
      panel.add(vlc, new MarginData(10));
      panel.addButton(new TextButton("Cancel"));
      panel.addButton(new TextButton("Save"));
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
