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
package com.sencha.gxt.explorer.client.grid;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Plant;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public abstract class AbstractGridEditingExample implements IsWidget {

  // just to show the converter feature
  enum Light {
    MOSTLYSHADY("Mostly Shady"), MOSTLYSUNNY("Mostly Sunny"), SHADE("Shade"), SUNNY("Sunny"), SUNORSHADE("Sun or Shade");
    static Light parseString(String object) throws ParseException {
      if (Light.MOSTLYSUNNY.toString().equals(object)) {
        return Light.MOSTLYSUNNY;
      } else if (Light.SUNORSHADE.toString().equals(object)) {
        return Light.SUNORSHADE;
      } else if (Light.MOSTLYSHADY.toString().equals(object)) {
        return Light.MOSTLYSHADY;
      } else if (Light.SHADE.toString().equals(object)) {
        return Light.SHADE;
      } else if (Light.SUNNY.toString().equals(object)) {
        return Light.SUNNY;
      } else {
        throw new ParseException(object.toString() + " could not be parsed", 0);
      }
    }

    private String text;

    Light(String text) {
      this.text = text;
    }

    @Override
    public String toString() {
      return text;
    }
  }

  interface PlaceProperties extends PropertyAccess<Plant> {
    ValueProvider<Plant, Date> available();

    @Path("id")
    ModelKeyProvider<Plant> key();

    ValueProvider<Plant, String> light();

    ValueProvider<Plant, String> name();

    ValueProvider<Plant, Boolean> indoor();

    ValueProvider<Plant, Double> price();
  }

  private static final PlaceProperties properties = GWT.create(PlaceProperties.class);

  protected Grid<Plant> grid;

  protected ContentPanel panel;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      ColumnConfig<Plant, String> nameColumn = new ColumnConfig<Plant, String>(properties.name(), 220, "Name");
      ColumnConfig<Plant, String> lightColumn = new ColumnConfig<Plant, String>(properties.light(), 120, "Light");
      ColumnConfig<Plant, Date> dateColumn = new ColumnConfig<Plant, Date>(properties.available(), 95, "Date");
      ColumnConfig<Plant, Boolean> indoorColumn = new ColumnConfig<Plant, Boolean>(properties.indoor(), 65, "Indoor");
      ColumnConfig<Plant, Double> priceColumn = new ColumnConfig<Plant, Double>(properties.price(), 75, "Price");

      dateColumn.setCell(new DateCell(DateTimeFormat.getFormat("yyyy MMM dd")));
      indoorColumn.setCell(new SimpleSafeHtmlCell<Boolean>(new AbstractSafeHtmlRenderer<Boolean>() {
        @Override
        public SafeHtml render(Boolean object) {
          return SafeHtmlUtils.fromTrustedString(object ? "True" : "False");
        }
      }));

      priceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      priceColumn.setCell(new SimpleSafeHtmlCell<Double>(new AbstractSafeHtmlRenderer<Double>() {
        @Override
        public SafeHtml render(Double object) {
          return SafeHtmlUtils.fromString(NumberFormat.getCurrencyFormat().format(object));
        }
      }));

      List<ColumnConfig<Plant, ?>> l = new ArrayList<ColumnConfig<Plant, ?>>();
      l.add(nameColumn);
      l.add(lightColumn);
      l.add(priceColumn);
      l.add(dateColumn);
      l.add(indoorColumn);

      ColumnModel<Plant> columns = new ColumnModel<Plant>(l);

      final ListStore<Plant> store = new ListStore<Plant>(properties.key());
      store.addAll(TestData.getPlants());

      grid = new Grid<Plant>(store, columns);
      grid.getView().setAutoExpandColumn(nameColumn);

      // EDITING //
      SimpleComboBox<Light> lightCombo = new SimpleComboBox<Light>(new StringLabelProvider<Light>());
      lightCombo.setClearValueOnParseError(false);
      lightCombo.setPropertyEditor(new PropertyEditor<Light>() {
        @Override
        public Light parse(CharSequence text) throws ParseException {
          return Light.parseString(text.toString());
        }

        @Override
        public String render(Light object) {
          return object == null ? Light.SUNNY.toString() : object.toString();
        }
      });
      lightCombo.setTriggerAction(TriggerAction.ALL);
      lightCombo.add(Light.SUNNY);
      lightCombo.add(Light.MOSTLYSUNNY);
      lightCombo.add(Light.SUNORSHADE);
      lightCombo.add(Light.MOSTLYSHADY);
      lightCombo.add(Light.SHADE);

      Converter<String, Light> lightConverter = new Converter<String, Light>() {
        @Override
        public String convertFieldValue(Light object) {
          return object == null ? "" : object.toString();
        }

        @Override
        public Light convertModelValue(String object) {
          try {
            return Light.parseString(object);
          } catch (ParseException e) {
            return null;
          }
        }
      };

      DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
      DateField dateField = new DateField(new DateTimePropertyEditor(dateFormat));
      dateField.setClearValueOnParseError(false);

      final GridEditing<Plant> editing = createGridEditing(grid);
      editing.addEditor(nameColumn, new TextField());
      editing.addEditor(lightColumn, lightConverter, lightCombo);
      editing.addEditor(dateColumn, dateField);
      editing.addEditor(indoorColumn, new CheckBox());
      // column 5 is not editable

      // EDITING //
      customizeGrid(grid);

      TextButton addButton = new TextButton("Add Plant");
      addButton.addSelectHandler(new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          Plant plant = new Plant();
          plant.setName("New Plant 1");
          plant.setLight("Mostly Shady");
          plant.setPrice(0);
          plant.setAvailable(new DateWrapper().clearTime().asDate());
          plant.setIndoor(false);

          editing.cancelEditing();
          store.add(0, plant);

          int row = store.indexOf(plant);
          editing.startEditing(new GridCell(row, 0));
        }
      });

      ToolBar toolBar = new ToolBar();
      toolBar.add(addButton);

      VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
      verticalLayoutContainer.add(toolBar, new VerticalLayoutData(1, -1));
      verticalLayoutContainer.add(grid, new VerticalLayoutData(1, 1));

      panel = new ContentPanel();
      panel.setHeading("Abstract Grid Editing");
      panel.add(verticalLayoutContainer);

      panel.setButtonAlign(BoxLayoutPack.CENTER);
      panel.addButton(new TextButton("Reset", new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          store.rejectChanges();
        }
      }));

      panel.addButton(new TextButton("Save", new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          store.commitChanges();
        }
      }));
    }

    return panel;
  }

  /**
   * Abstract method to allow example subclass to build the specific editing details
   */
  protected abstract GridEditing<Plant> createGridEditing(Grid<Plant> grid);

  /**
   * Additional modifications can be made to the grid in the subclass with this method
   */
  protected void customizeGrid(Grid<Plant> grid) { }

}
