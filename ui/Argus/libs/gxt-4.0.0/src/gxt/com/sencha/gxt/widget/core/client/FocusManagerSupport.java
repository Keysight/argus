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

/**
 * Component support class for Focus Manager.
 */
public class FocusManagerSupport {

  private boolean ignore;
  private String nextId;
  private String previousId;
  private Component c;

  FocusManagerSupport(Component c) {

  }

  /**
   * Returns the target widget.
   * 
   * @return the target widget
   */
  public Component getComponent() {
    return c;
  }

  /**
   * Returns the next widget id.
   * 
   * @return the next widget id
   */
  public String getNextId() {
    return nextId;
  }

  /**
   * Returns the previous widget id.
   * 
   * @return the previous widget id
   */
  public String getPreviousId() {
    return previousId;
  }

  /**
   * Returns true if the widget will be ignored by the ARIA and FocusManager
   * API.
   * 
   * @return true if widget is being ignored
   */
  public boolean isIgnore() {
    return ignore;
  }

  /**
   * True to mark this widget to be ignored by the ARIA and FocusManager API
   * (defaults to false). Typically set to true for any containers that should
   * not be navigable to.
   * 
   * @param ignore true to ignore
   */
  public void setIgnore(boolean ignore) {
    this.ignore = ignore;
  }

  /**
   * The id of the widget to navigate to when TAB is pressed (defaults to
   * null). When set, the focus manager will override its default behavior to
   * determine the next focusable widget.
   * 
   * @param nextId the next widget id
   */
  public void setNextId(String nextId) {
    this.nextId = nextId;
  }

  /**
   * The id of the widget to navigate to when SHIFT-TAB is pressed (defaults
   * to null). When set, the focus manager will override its default behavior to
   * determine the previous focusable widget.
   * 
   * @param previousId the previous widget id
   */
  public void setPreviousId(String previousId) {
    this.previousId = previousId;
  }
}
