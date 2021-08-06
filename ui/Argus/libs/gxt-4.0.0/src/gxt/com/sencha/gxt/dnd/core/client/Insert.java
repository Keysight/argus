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
package com.sencha.gxt.dnd.core.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.widget.core.client.Component;

/**
 * A custom widget used to show insert locations with drop targets.
 */
public class Insert extends Component {

  public static class DefaultInsertAppearance implements InsertAppearance {

    public interface InsertResources extends ClientBundle {

      ImageResource left();

      @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
      ImageResource mid();

      ImageResource right();

      @Source("Insert.gss")
      InsertStyle style();

    }

    public interface InsertStyle extends CssResource {

      String bar();

      String left();

      String mid();

      String right();

    }

    public interface Template extends XTemplates {
      @XTemplate(source = "Insert.html")
      SafeHtml render(InsertStyle style);
    }

    private InsertStyle style;
    private Template template;

    public DefaultInsertAppearance() {
      this((InsertResources) GWT.create(InsertResources.class));
    }

    public DefaultInsertAppearance(InsertResources resources) {
      this.style = resources.style();
      this.style.ensureInjected();

      this.template = GWT.create(Template.class);
    }

    @Override
    public void render(SafeHtmlBuilder sb) {
      sb.append(template.render(style));
    }

  }

  public interface InsertAppearance {
    void render(SafeHtmlBuilder sb);
  }

  private final InsertAppearance appearance;
  private static final Insert instance = GWT.create(Insert.class);

  public static Insert get() {
    return instance;
  }

  protected Insert() {
    this(GWT.<InsertAppearance>create(InsertAppearance.class));
  }

  protected Insert(InsertAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    appearance.render(sb);

    setElement((Element) XDOM.create(sb.toSafeHtml()));

    setShadow(false);
    hide();
  }

  public InsertAppearance getAppearance() {
    return appearance;
  }

  public void show(Element c) {
    c.insertBefore(getElement(), null);
    show();
  }

  @Override
  protected void onHide() {
    super.onHide();
    getElement().removeFromParent();
  }

  @Override
  protected void onShow() {
    super.onShow();
    if (!getElement().isConnected()) {
      Document.get().getBody().insertBefore(getElement(), null);
    }
  }

}
