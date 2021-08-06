/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktop.client.theme.base.startmenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.theme.blue.client.menu.BlueMenuAppearance;

public class StartToolMenuAppearance extends BlueMenuAppearance {

  public interface StartToolMenuResources extends BlueMenuResources, ClientBundle {

    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.Vertical)
    ImageResource itemOver();

    @Source({"com/sencha/gxt/theme/base/client/menu/Menu.gss", //
        "com/sencha/gxt/theme/blue/client/menu/BlueMenu.gss", //
        "StartToolMenu.gss"})
    StartToolMenuStyle style();

  }

  public interface StartToolMenuStyle extends BlueMenuStyle {

  }

  public interface StartToolMenuTemplate extends XTemplates {

    @XTemplate(source = "StartToolMenu.html")
    SafeHtml template(StartToolMenuStyle style, String ignoreClass);

  }

  private StartToolMenuTemplate startToolMenuTemplate;

  public StartToolMenuAppearance() {
    this(GWT.<StartToolMenuResources> create(StartToolMenuResources.class),
        GWT.<StartToolMenuTemplate> create(StartToolMenuTemplate.class));
  }

  public StartToolMenuAppearance(StartToolMenuResources resources, StartToolMenuTemplate template) {
    super(resources, null);
    startToolMenuTemplate = template;
  }

  public void render(SafeHtmlBuilder result) {
    result.append(startToolMenuTemplate.template((StartToolMenuStyle) style, CommonStyles.get().ignore()));
  }

}
