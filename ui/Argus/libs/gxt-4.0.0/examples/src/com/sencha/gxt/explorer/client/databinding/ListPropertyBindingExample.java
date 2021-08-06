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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.examples.resources.client.model.Kid;
import com.sencha.gxt.examples.resources.client.model.Person;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;

@Detail(
    name="List Property Binding",
    category="Data Binding",
    icon = "listproperty",
    classes = {
        PersonEditor.class,
        Person.class,
        Kid.class
    },
    minHeight = ListPropertyBindingExample.MIN_HEIGHT,
    minWidth = ListPropertyBindingExample.MIN_WIDTH
)
public class ListPropertyBindingExample implements EntryPoint, IsWidget {

  interface Driver extends SimpleBeanEditorDriver<Person, PersonEditor> {
  }

  protected static final int MIN_HEIGHT = 445;
  protected static final int MIN_WIDTH = 350;

  private Driver driver = GWT.create(Driver.class);
  private ContentPanel panel;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      PersonEditor personEditor = new PersonEditor(driver);

      Person person = new Person("John Doe", "ACME Widget Co.", "Widgets", "Anytown, USA", 43460d);
      List<Kid> kids = new ArrayList<Kid>();
      kids.add(new Kid("Noah", 4, new DateWrapper(2011, 1, 1).asDate()));
      kids.add(new Kid("Emma", 2, new DateWrapper(2013, 1, 1).asDate()));
      kids.add(new Kid("Liam", 1, new DateWrapper(2014, 1, 1).asDate()));
      person.setKids(kids);
      driver.edit(person);

      panel = new ContentPanel();
      panel.setHeading("List Property Binding");
      panel.add(personEditor.asWidget(), new MarginData(10));
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
