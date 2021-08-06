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
 * A ContentPanel that internally uses a @link {@link FramedPanelAppearance}.
 */
public class FramedPanel extends ContentPanel {
  /**
   * The appearance of a framed content panel. A frame content panel is a
   * content panel with a frame.
   */
  public interface FramedPanelAppearance extends ContentPanelAppearance {
  }

  /**
   * Creates a framed content panel with the default appearance.
   */
  public FramedPanel() {
    this(GWT.<FramedPanelAppearance> create(FramedPanelAppearance.class));
  }

  /**
   * Creates a framed content panel with the specified appearance.
   * 
   * @param appearance the appearance of the framed content panel
   */
  public FramedPanel(FramedPanelAppearance appearance) {
    super(appearance);
  }

}
