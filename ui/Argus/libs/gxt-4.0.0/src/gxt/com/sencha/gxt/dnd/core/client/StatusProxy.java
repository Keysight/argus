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
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.widget.core.client.Component;

/**
 * A custom widget used to display the status of the drag operation and
 * information about the data being dragged. The widget is displayed next to the
 * cursor as the user drags data.
 */
public class StatusProxy extends Component {

  public interface StatusProxyAppearance {

    void render(SafeHtmlBuilder builder);

    void setStatus(Element parent, boolean allowed);

    void setStatus(Element parent, ImageResource icon);

    void update(Element parent, SafeHtml html);

  }

  private static final StatusProxy instance = GWT.create(StatusProxy.class);

  /**
   * Returns the singleton instance.
   * 
   * @return the status proxy
   */
  public static StatusProxy get() {
    return instance;
  }

  private boolean status;
  private final StatusProxyAppearance appearance;

  protected StatusProxy() {
    this(GWT.<StatusProxyAppearance> create(StatusProxyAppearance.class));
  }

  protected StatusProxy(StatusProxyAppearance appearance) {
    this.appearance = appearance;
    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    appearance.render(builder);
    setElement((Element) XDOM.create(builder.toSafeHtml()));
    setStatus(false);

    setShadow(true);
  }

  public StatusProxyAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns true if the drop is allowed.
   * 
   * @return the status
   */
  public boolean getStatus() {
    return status;
  }

  /**
   * Updates the proxy's visual element to indicate the status of whether or not
   * drop is allowed over the current target element.
   * 
   * @param allowed true for the standard ok icon, false for standard no icon
   */
  public void setStatus(boolean allowed) {
    appearance.setStatus(getElement(), allowed);
    this.status = allowed;
  }

  /**
   * Updates the proxy's visual element to indicate the status of whether or not
   * drop is allowed over the current target element.
   * 
   * @param allowed drop is allowed
   * @param icon icon to display
   */
  public void setStatus(boolean allowed, ImageResource icon) {
    appearance.setStatus(getElement(), icon);
    this.status = allowed;
  }

  /**
   * Updates the contents of the ghost element.
   * 
   * @param html the html that will replace the current contents of the ghost
   *          element
   */
  public void update(SafeHtml html) {
    appearance.update(getElement(), html);
  }

}
