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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.theme.blue.client.menu.BlueMenuAppearance;

public class StartMainMenuAppearance extends BlueMenuAppearance {

  public interface StartMainMenuResources extends BlueMenuResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/menu/Menu.gss", //
        "com/sencha/gxt/theme/blue/client/menu/BlueMenu.gss", //
        "StartMainMenu.gss"})
    StartMainMenuStyle style();

  }

  /**
   * Implementation note: In order to prevent these styles from conflicting with
   * base class styles, define a new interface, even if it is empty. Each
   * distinct style return type will return a wholly separate collection of
   * values. See:
   * http://code.google.com/webtoolkit/doc/latest/DevGuideClientBundle.html
   */
  public interface StartMainMenuStyle extends BlueMenuStyle {
    // Styles referenced by StartMainMenu.html go here
  }

  /**
   * Implementation note: the appearance specific template must extend
   * XTemplates and not an intermediate interface that extends XTemplates in
   * order for the template generation to work properly. Then pass a null for
   * the template value to the base class constructor and override render, and
   * any other place where the template is referenced in the base class.
   */
  public interface StartMainMenuTemplate extends XTemplates {

    @XTemplate(source = "StartMainMenu.html")
    SafeHtml template(StartMainMenuStyle style, String ignoreClass);

  }

  private StartMainMenuTemplate startMainMenuTemplate;

  public StartMainMenuAppearance() {
    this(GWT.<StartMainMenuResources> create(StartMainMenuResources.class),
        GWT.<StartMainMenuTemplate> create(StartMainMenuTemplate.class));
  }

  public StartMainMenuAppearance(StartMainMenuResources resources, StartMainMenuTemplate template) {
    super(resources, null);
    startMainMenuTemplate = template;
  }

  public void render(SafeHtmlBuilder result) {
    result.append(startMainMenuTemplate.template((StartMainMenuStyle) style, CommonStyles.get().ignore()));
  }

}
