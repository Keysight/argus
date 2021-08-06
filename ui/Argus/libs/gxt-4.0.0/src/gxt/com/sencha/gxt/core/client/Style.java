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
package com.sencha.gxt.core.client;

import com.sencha.gxt.core.client.resources.CommonStyles;

/**
 * Defines GXT public constants and enumerations.
 */
public class Style {

  /**
   * Direction enumeration.
   */
  public enum Direction {
    UP, DOWN, LEFT, RIGHT
  }
  
  public enum Side {
    LEFT, RIGHT, TOP, BOTTOM
  }

  public enum Anchor {
    TOP(false, false, true, false), 
    TOP_RIGHT(false, true, true, false),
    RIGHT(false, true, false, false),
    BOTTOM_RIGHT(false, true, false, true),
    BOTTOM(false, false, false, true),
    BOTTOM_LEFT(true, false, false, true),
    LEFT(true, false, false, false),
    TOP_LEFT(true, false, true, false),
    CENTER(false, false, false, false);

    private final boolean isLeft;
    private final boolean isRight;
    private final boolean isTop;
    private final boolean isBottom;
    private Anchor(boolean l, boolean r, boolean t, boolean b) {
      assert l == false || r == false;
      assert t == false || b == false;
      isLeft = l;
      isRight = r;
      isTop = t;
      isBottom = b;
    }

    public boolean isBottom() {
      return isBottom;
    }
    public boolean isLeft() {
      return isLeft;
    }
    public boolean isRight() {
      return isRight;
    }
    public boolean isTop() {
      return isTop;
    }
  }

  public static class AnchorAlignment {

    private Anchor align;
    private Anchor targetAlign;
    private boolean constrainViewport;
    
    public AnchorAlignment(Anchor align) {
      this(align, Anchor.BOTTOM_LEFT);
    }

    public AnchorAlignment(Anchor align, Anchor targetAlign) {
      this(align, targetAlign, true);
    }

    public AnchorAlignment(Anchor align, Anchor targetAlign, boolean constrainViewport) {
      this.align = align;
      this.targetAlign = targetAlign;
      this.constrainViewport = constrainViewport;
    }

    public Anchor getAlign() {
      return align;
    }

    public Anchor getTargetAlign() {
      return targetAlign;
    }

    public boolean isConstrainViewport() {
      return constrainViewport;
    }

  }

  /**
   * HideMode enumeration.
   */
  public enum HideMode {
    OFFSETS(CommonStyles.get().hideOffsets()), VISIBILITY(CommonStyles.get().hideVisibility()), DISPLAY(
        CommonStyles.get().hideDisplay());

    private final String value;

    private HideMode(String value) {
      this.value = value;
    }

    public String value() {
      return value;
    }
  }

  /**
   * Horizontal alignment enumeration.
   */
  public enum HorizontalAlignment {
    LEFT, CENTER, RIGHT
  }

  /**
   * Vertical alignment enumerations.
   */
  public enum VerticalAlignment {
    TOP, MIDDLE, BOTTOM
  }

  /**
   * Layout out regions.
   */
  public enum LayoutRegion {
    NORTH, EAST, SOUTH, WEST, CENTER
  }

  /**
   * Orientation enumeration.
   */
  public enum Orientation {
    VERTICAL, HORIZONTAL
  }

  /**
   * Scroll direction enumeration.
   */
  public enum ScrollDir {
    VERTICAL, HORIZONTAL
  }

  /**
   * Scroll direction enumeration.
   */
  public enum ScrollDirection {
    LEFT, TOP
  }

  /**
   * Selection mode enumeration.
   */
  public enum SelectionMode {
    SINGLE, SIMPLE, MULTI
  }

  /**
   * Indicates that a default value should be used (value is -1).
   */
  public static final int DEFAULT = -1;

  /**
   * Constant for marking a string as undefined rather than null.
   */
  public static final String UNDEFINED = "undefined";
}
