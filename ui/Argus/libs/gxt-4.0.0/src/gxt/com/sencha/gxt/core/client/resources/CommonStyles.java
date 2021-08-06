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
package com.sencha.gxt.core.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * Non-theme related common styles.
 */
public class CommonStyles {

  public interface CommonStylesAppearance {
    Styles styles();
  }

  public static class CommonStylesDefaultAppearance implements CommonStylesAppearance {
    
    public interface CommonDefaultResources extends ClientBundle {
      ImageResource shim();

      @Source("CommonStyles.gss")
      CommonDefaultStyles styles();

    }

    public interface CommonDefaultStyles extends Styles {
      @ClassName("x-clear")
      @Override
      String clear();
    }

    private final CommonDefaultResources bundle;
    private final Styles styles;
    
    public CommonStylesDefaultAppearance() {
      bundle = GWT.create(CommonDefaultResources.class);
      styles = bundle.styles();
    }
    @Override
    public Styles styles() {
      return styles;
    }
  }

  public interface Styles extends CssResource {

    String clear();

    String columnResize();

    String columnRowResize();

    String floatLeft();

    String floatRight();

    String hideDisplay();

    String hideOffsets();

    String hideVisibility();

    String ignore();

    String inlineBlock();

    String nodrag();

    String noFocusOutline();

    String nowrap();

    String positionable();

    String repaint();

    String shim();

    String unselectable();

    String unselectableSingle();

  }

  private final CommonStylesAppearance appearance;

  private static final CommonStyles instance = GWT.create(CommonStyles.class);

  /**
   * Returns the singleton instance.
   * 
   * @return the common styles
   */
  public static Styles get() {
    return instance.appearance.styles();
  }

  private CommonStyles() {
    this.appearance = GWT.create(CommonStylesAppearance.class);

    StyleInjectorHelper.ensureInjected(this.appearance.styles(), true);
  }

}
