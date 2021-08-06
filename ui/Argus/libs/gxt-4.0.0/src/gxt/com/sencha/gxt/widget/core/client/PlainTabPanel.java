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
package com.sencha.gxt.widget.core.client;

import com.google.gwt.core.client.GWT;

/**
 * A {@link TabPanel} with a plain tab bar, with no background behind each tab.
 */
public class PlainTabPanel extends TabPanel {

  /**
   * An appearance applicable to a {@link PlainTabPanel}.
   *
   * This interface exists so that the appropriate appearance can be substituted
   * through deferred binding depending on the theme in use. For example, a
   * blue-colored theme could substitute a blue {@code PlainTabPanel}
   * appearance. In functionality, a {@link PlainTabPanelAppearance} is
   * equivalent to a {@link TabPanel.TabPanelAppearance}.
   */
  public interface PlainTabPanelAppearance extends TabPanelAppearance {
  }

  public interface PlainTabPanelBottomAppearance extends PlainTabPanelAppearance {

  }

  /**
   * Creates a plain tab panel with the default appearance.
   */
  public PlainTabPanel() {
    super(GWT.<PlainTabPanelAppearance> create(PlainTabPanelAppearance.class));
  }

  /**
   * Creates a plain tab panel with the specified appearance.
   * 
   * @param appearance the appearance of the plain tab panel
   */
  public PlainTabPanel(PlainTabPanelAppearance appearance) {
    super(appearance);
  }

}
