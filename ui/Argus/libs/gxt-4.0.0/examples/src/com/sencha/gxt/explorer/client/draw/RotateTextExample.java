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
package com.sencha.gxt.explorer.client.draw;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.draw.DrawComponent;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Slider;

@Detail(
    name = "Rotate Text",
    category = "Draw",
    icon = "rotatetext",
    minHeight = RotateTextExample.MIN_HEIGHT,
    minWidth = RotateTextExample.MIN_WIDTH
)
public class RotateTextExample implements IsWidget, EntryPoint {

  protected static final int MIN_HEIGHT = 300;
  protected static final int MIN_WIDTH = 400;

  private ContentPanel panel;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      TextSprite text1 = new TextSprite("With GXT 3.0 Drawing");
      text1.setFont("Arial");
      text1.setFontSize(18);
      text1.setRotation(45);
      text1.setTranslation(20, 20);

      TextSprite text2 = new TextSprite("Creating rotated text");
      text2.setFont("Arial");
      text2.setFontSize(18);
      text2.setRotation(90);
      text2.setTranslation(200, 20);

      final TextSprite rotate = new TextSprite("Is a snap!");
      rotate.setFont("Arial");
      rotate.setFontSize(18);
      rotate.setRotation(45);
      rotate.setTranslation(300, 100);

      DrawComponent draw = new DrawComponent(400, 400);
      draw.addSprite(text1);
      draw.addSprite(text2);
      draw.addSprite(rotate);

      Slider slider = new Slider();
      slider.setIncrement(1);
      slider.setMinValue(0);
      slider.setMaxValue(360);
      slider.addValueChangeHandler(new ValueChangeHandler<Integer>() {
        @Override
        public void onValueChange(ValueChangeEvent<Integer> event) {
          rotate.setRotation(event.getValue());
          rotate.redraw();
        }
      });

      VerticalPanel verticalPanel = new VerticalPanel();
      verticalPanel.setSpacing(10);
      verticalPanel.add(slider);
      verticalPanel.add(draw);

      panel = new ContentPanel();
      panel.setHeading("Rotate Text");
      panel.add(verticalPanel);
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
